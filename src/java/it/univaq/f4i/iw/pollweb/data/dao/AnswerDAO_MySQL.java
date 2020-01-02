/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.model.Answer;
import it.univaq.f4i.iw.pollweb.data.model.ChoiceAnswer;
import it.univaq.f4i.iw.pollweb.data.model.DateAnswer;
import it.univaq.f4i.iw.pollweb.data.model.NumberAnswer;
import it.univaq.f4i.iw.pollweb.data.impl.OptionImpl;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.model.TextAnswer;
import it.univaq.f4i.iw.pollweb.data.model.ShortTextAnswer;
import it.univaq.f4i.iw.pollweb.data.proxy.ChoiceAnswerProxy;
import it.univaq.f4i.iw.pollweb.data.proxy.DateAnswerProxy;
import it.univaq.f4i.iw.pollweb.data.proxy.NumberAnswerProxy;
import it.univaq.f4i.iw.pollweb.data.proxy.ShortTextAnswerProxy;
import it.univaq.f4i.iw.pollweb.data.proxy.TextAnswerProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author andrea
 */
public class AnswerDAO_MySQL extends DAO implements AnswerDAO {

    private PreparedStatement sAnswersBySurveyResponse, sAnswerById;
    private PreparedStatement iAnswer;
    
    public AnswerDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void destroy() throws DataException {
        try {
            sAnswerById.close();
            sAnswersBySurveyResponse.close();
            iAnswer.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public ChoiceAnswerProxy createChoiceAnswer() {
        return new ChoiceAnswerProxy(getDataLayer());
    }
    
    @Override
    public DateAnswerProxy createDateAnswer() {
        return new DateAnswerProxy(getDataLayer());
    }
    
    @Override
    public NumberAnswerProxy createNumberAnswer() {
        return new NumberAnswerProxy(getDataLayer());
    }
   
    @Override
    public TextAnswerProxy createTextAnswer() {
        return new TextAnswerProxy(getDataLayer());
    }
    
    @Override
    public ShortTextAnswerProxy createShortTextAnswer() {
        return new ShortTextAnswerProxy(getDataLayer());
    }

    @Override
    public void init() throws DataException {
        try {
            sAnswerById = connection.prepareStatement("SELECT a.*, q.* FROM answers AS a INNER JOIN questions AS q WHERE a.id=?");
            sAnswersBySurveyResponse = connection.prepareStatement("SELECT a.*, q.* FROM pollweb.answers AS a JOIN pollweb.questions AS q WHERE a.id_survey_response=? && a.id_question = q.id");
            iAnswer = connection.prepareStatement("INSERT INTO answers (text,id_question,id_survey_response,type) VALUES (?,?,?,?)");
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
                        OptionImpl o = ca.getOptions().get(i);
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
        TextAnswerProxy answer = new TextAnswerProxy(this.dataLayer);
        answer.setId(rs.getLong("a.id"));
        answer.setAnswer(rs.getString("a.text"));
        answer.setDirty(false);
        return answer;
    }
    
    private ShortTextAnswer createShortTextAnswer(ResultSet rs) throws SQLException {
        ShortTextAnswerProxy answer = new ShortTextAnswerProxy(this.dataLayer);
        answer.setId(rs.getLong("a.id"));
        answer.setAnswer(rs.getString("a.text"));
        answer.setDirty(false);
        return answer;
    }
    
    private DateAnswer createDateAnswer(ResultSet rs) throws SQLException {
        DateAnswerProxy answer = new DateAnswerProxy(this.dataLayer);
        answer.setId(rs.getLong("a.id"));
        answer.setAnswer(rs.getDate("a.text").toLocalDate());
        answer.setDirty(false);
        return answer;
    }
    
    private NumberAnswer createNumberAnswer(ResultSet rs) throws SQLException {
        NumberAnswerProxy answer = new NumberAnswerProxy(this.dataLayer);
        answer.setId(rs.getLong("a.id"));
        answer.setAnswer(rs.getInt("a.text"));
        answer.setDirty(false);
        return answer;
    }
    
    // TODO refactor and manage option id
    private ChoiceAnswer createChoiceAnswer(ResultSet rs) throws SQLException {
        ChoiceAnswerProxy answer = new ChoiceAnswerProxy(this.dataLayer);
        answer.setId(rs.getLong("a.id"));
        for (String s: rs.getString("a.text").split(";")) {
            String[] t = s.split("-");
            OptionImpl o = new OptionImpl();
            o.setPosition(Short.valueOf(t[0]));
            o.setText(t[1]);
            answer.getOptions().add(o);
        }
        answer.setDirty(false);
        return answer;
    }
}
