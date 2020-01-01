/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.business.model.Answer;
import it.univaq.f4i.iw.pollweb.business.model.ChoiceAnswer;
import it.univaq.f4i.iw.pollweb.business.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.business.model.DateAnswer;
import it.univaq.f4i.iw.pollweb.business.model.DateQuestion;
import it.univaq.f4i.iw.pollweb.business.model.NumberAnswer;
import it.univaq.f4i.iw.pollweb.business.model.NumberQuestion;
import it.univaq.f4i.iw.pollweb.business.model.Option;
import it.univaq.f4i.iw.pollweb.business.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.business.model.TextAnswer;
import it.univaq.f4i.iw.pollweb.business.model.TextQuestion;
import it.univaq.f4i.iw.pollweb.business.model.ShortTextAnswer;
import it.univaq.f4i.iw.pollweb.business.model.ShortTextQuestion;
import it.univaq.f4i.iw.pollweb.data.dao.AnswerDAO;
import it.univaq.f4i.iw.pollweb.data.dao.OptionDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andrea
 */
public class AnswerDAOImpl extends DAO implements AnswerDAO {

    private PreparedStatement sAnswersBySurveyResponse, sAnswerById;
    private PreparedStatement iAnswer, uAnswer, dAnswer;
    
    public AnswerDAOImpl(DataLayer d) {
        super(d);
    }

    @Override
    public void destroy() throws DataException {
        try {
            sAnswerById.close();
            sAnswersBySurveyResponse.close();
            iAnswer.close();
            uAnswer.close();
            dAnswer.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void init() throws DataException {
        try {
            sAnswerById = connection.prepareStatement("SELECT a.*, q.* FROM answers AS a INNER JOIN questions AS q WHERE a.id=?");
            sAnswersBySurveyResponse = connection.prepareStatement("SELECT a.*, q.* FROM pollweb.answers AS a JOIN pollweb.questions AS q WHERE a.id_survey_response=? && a.id_question = q.id");
            iAnswer = connection.prepareStatement("INSERT INTO answers (text,id_question,id_survey_response,type) VALUES (?,?,?,?)");
            uAnswer = connection.prepareStatement("UPDATE answers SET text=?,id_question=?,id_survey_response=? WHERE id=?");
            dAnswer = connection.prepareStatement("DELETE FROM answers WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error occurred initializing data layer", ex);
        }
    }
    
    @Override
    public List<Answer> findBySurveyResponse(SurveyResponse surveyResponse) throws DataException {
        ResultSet rs = null;
        List<Answer> answers = new ArrayList<>();
        try {
            sAnswersBySurveyResponse.setLong(1, surveyResponse.getId());
            rs = sAnswersBySurveyResponse.executeQuery();
            while (rs.next()) {
                Answer answer = createAnswer(rs);
                answers.add(answer);
            }   
        } catch (SQLException ex) {
            throw new DataException("Unable to load answers from SurveyResponse");
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return answers;
    }

    @Override
    public void saveOrUpdate(Answer answer, long responseId) throws DataException {
        // INSERT
        if (answer.getId() == 0) {
            try {
                String text = "";
                if (answer instanceof ChoiceAnswer) {
                    iAnswer.setString(4, "choice");
                    ChoiceAnswer ca = (ChoiceAnswer) answer;
                    for (int i = 0; i < ca.getOptions().size(); i++) {
                        Option o = ca.getOptions().get(i);
                        text += o.getPosition() + "-" + o.getText();
                        if (i < ca.getOptions().size() - 1) {
                            text += ";";
                        }
                    }
                } else if (answer instanceof DateAnswer) {
                    iAnswer.setString(4, "date");
                    DateAnswer da = (DateAnswer) answer;
                    text += da.getAnswer().toString();
                } else if (answer instanceof NumberAnswer) {
                    iAnswer.setString(4, "number");
                    NumberAnswer na = (NumberAnswer) answer;
                    text += Float.toString(na.getAnswer());
                } else if (answer instanceof TextAnswer) {
                    if(answer instanceof ShortTextAnswer){
                        iAnswer.setString(4, "short text");
                    }else{                   
                        iAnswer.setString(4, "long text");
                    }
                    TextAnswer ta = (TextAnswer) answer;
                    text += ta.getAnswer();
                }
                iAnswer.setString(1, text);
                iAnswer.setLong(2, answer.getQuestion().getId());
                iAnswer.setLong(3, responseId);
                iAnswer.execute();
            } catch (SQLException ex) {
                throw new DataException("Unable to save Answer", ex);
            }
            
        // UPDATE
        } else {
            
        }
    }

    @Override
    public void delete(Answer answer) throws DataException {
        try {
            dAnswer.setLong(1, answer.getId());
            dAnswer.execute();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete answer", ex);
        }
    }
    
    private Answer createAnswer(ResultSet rs) throws DataException {
        try {
            String answerType = rs.getString("type");
            switch (answerType) {
                case "short text":
                    return createShortTextAnswer(rs);
                case "long text":
                    return createTextAnswer(rs);
                case "date":
                    return createDateAnswer(rs);
                case "number":
                    return createNumberAnswer(rs);
                case "choice":
                    return createChoiceAnswer(rs);
                default:
                    throw new DataException("Unknown answer type");
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to create answer from ResultSet", ex);
        }
    }
    
    private TextAnswer createTextAnswer(ResultSet rs) throws SQLException {
        TextAnswer answer = new TextAnswer();
        answer.setId(rs.getLong("a.id"));
        answer.setAnswer(rs.getString("a.text"));
        TextQuestion question = new TextQuestion();
        question.setId(rs.getLong("q.id"));
        question.setCode(rs.getString("q.code"));
        question.setMandatory(rs.getBoolean("q.mandatory"));
        question.setMaxLength(rs.getInt("q.max_value"));
        question.setMinLength(rs.getInt("q.min_value"));
        question.setNote(rs.getString("q.note"));
        question.setPosition(rs.getShort("q.position"));
        question.setText(rs.getString("q.text"));
        answer.setQuestion(question);
        return answer;
    }
    
    private TextAnswer createShortTextAnswer(ResultSet rs) throws SQLException {
        ShortTextAnswer answer = new ShortTextAnswer();
        answer.setId(rs.getLong("a.id"));
        answer.setAnswer(rs.getString("a.text"));
        ShortTextQuestion question = new ShortTextQuestion();
        question.setId(rs.getLong("q.id"));
        question.setCode(rs.getString("q.code"));
        question.setMandatory(rs.getBoolean("q.mandatory"));
        question.setMaxLength(rs.getInt("q.max_value"));
        question.setMinLength(rs.getInt("q.min_value"));
        question.setNote(rs.getString("q.note"));
        question.setPattern(rs.getString("q.reg_expr"));
        question.setPosition(rs.getShort("q.position"));
        question.setText(rs.getString("q.text"));
        answer.setQuestion(question);
        return answer;
    }
    
    private DateAnswer createDateAnswer(ResultSet rs) throws SQLException {
        DateAnswer answer = new DateAnswer();
        answer.setId(rs.getLong("a.id"));
        answer.setAnswer(rs.getDate("a.text").toLocalDate());
        DateQuestion question = new DateQuestion();
        question.setId(rs.getLong("q.id"));
        question.setCode(rs.getString("q.code"));
        question.setMandatory(rs.getBoolean("q.mandatory"));
        question.setMaxDate(rs.getDate("q.max_date").toLocalDate());
        question.setMinDate(rs.getDate("q.min_date").toLocalDate());
        question.setNote(rs.getString("q.note"));
        question.setPosition(rs.getShort("q.position"));
        question.setText(rs.getString("q.text"));
        answer.setQuestion(question);
        return answer;
    }
    
    private NumberAnswer createNumberAnswer(ResultSet rs) throws SQLException {
        NumberAnswer answer = new NumberAnswer();
        answer.setId(rs.getLong("a.id"));
        answer.setAnswer(rs.getInt("a.text"));
        NumberQuestion question = new NumberQuestion();
        question.setId(rs.getLong("q.id"));
        question.setCode(rs.getString("q.code"));
        question.setMandatory(rs.getBoolean("q.mandatory"));
        question.setMaxValue(rs.getInt("q.max_value"));
        question.setMinValue(rs.getInt("q.min_value"));
        question.setNote(rs.getString("q.note"));
        question.setPosition(rs.getShort("q.position"));
        question.setText(rs.getString("q.text"));
        answer.setQuestion(question);
        return answer;
    }
    
    // TODO refactor and manage option id
    private ChoiceAnswer createChoiceAnswer(ResultSet rs) throws SQLException {
        ChoiceAnswer answer = new ChoiceAnswer();
        answer.setId(rs.getLong("a.id"));
        for (String s: rs.getString("a.text").split(";")) {
            String[] t = s.split("-");
            Option o = new Option();
            o.setPosition(Short.valueOf(t[0]));
            o.setText(t[1]);
            answer.getOptions().add(o);
        }
        ChoiceQuestion question = new ChoiceQuestion();
        question.setId(rs.getLong("q.id"));
        question.setCode(rs.getString("q.code"));
        question.setMandatory(rs.getBoolean("q.mandatory"));
        question.setMaxNumberOfChoices(rs.getShort("q.max_value"));
        question.setNote(rs.getString("q.note"));
        question.setPosition(rs.getShort("q.position"));
        question.setText(rs.getString("q.text"));
        try {
            for (Option o: ((OptionDAO)this.dataLayer.getDAO(Option.class)).findByQuestion(question)) {
                question.addOption(o);
            }
        } catch (DataException ex) {
            Logger.getLogger(AnswerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        answer.setQuestion(question);
        return answer;
    }
}
