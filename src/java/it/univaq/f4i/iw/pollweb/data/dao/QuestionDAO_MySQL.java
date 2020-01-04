/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.data.model.DateQuestion;
import it.univaq.f4i.iw.pollweb.data.model.NumberQuestion;
import it.univaq.f4i.iw.pollweb.data.model.Option;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.TextQuestion;
import it.univaq.f4i.iw.pollweb.data.model.ShortTextQuestion;
import it.univaq.f4i.iw.pollweb.data.proxy.ChoiceQuestionProxy;
import it.univaq.f4i.iw.pollweb.data.proxy.DateQuestionProxy;
import it.univaq.f4i.iw.pollweb.data.proxy.NumberQuestionProxy;
import it.univaq.f4i.iw.pollweb.data.proxy.ShortTextQuestionProxy;
import it.univaq.f4i.iw.pollweb.data.proxy.TextQuestionProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class QuestionDAO_MySQL extends DAO implements QuestionDAO {
    
    private PreparedStatement sQuestionById, sQuestionsBySurvey,sQuestionByAnswer;
    private PreparedStatement iQuestion,uQuestionCreation, uQuestion, dQuestion;

    public QuestionDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            sQuestionById = connection.prepareStatement("SELECT * FROM questions WHERE id=?");
            sQuestionsBySurvey = connection.prepareStatement("SELECT * FROM questions WHERE id_survey=? ORDER BY questions.position");
            sQuestionByAnswer = connection.prepareStatement("SELECT q.* FROM pollweb.answers AS a JOIN pollweb.questions AS q WHERE a.id=? && a.id_question = q.id");
            iQuestion = connection.prepareStatement("INSERT INTO questions (text,note,mandatory,type,min_date,max_date,min_value,max_value,reg_expr,id_survey,position,code) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uQuestionCreation = connection.prepareStatement("UPDATE questions SET min_date=?,max_date=?,min_value=?,max_value=?,reg_expr=? where id=?");
            uQuestion = connection.prepareStatement("UPDATE questions SET text=?,note=?,mandatory=?,type=?,min_date=?,max_date=?,min_value=?,max_value=?,reg_expr=?,position=?,code=? WHERE id=?");
            dQuestion = connection.prepareStatement("DELETE from questions WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing Data Layer", ex);
        } 
    }
    
    @Override
    public void destroy() throws DataException {
        try {
            sQuestionById.close();
            sQuestionsBySurvey.close();
            iQuestion.close();
            uQuestion.close();
            dQuestion.close();
            uQuestionCreation.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        super.destroy();
    }
    
    @Override
    public ChoiceQuestionProxy createChoiceQuestion() {
        return new ChoiceQuestionProxy(getDataLayer());
    }
   
    @Override
    public DateQuestionProxy createDateQuestion() {
        return new DateQuestionProxy(getDataLayer());
    }
  
    @Override
    public NumberQuestionProxy createNumberQuestion() {
        return new NumberQuestionProxy(getDataLayer());
    }
      
    @Override
    public TextQuestionProxy createTextQuestion() {
        return new TextQuestionProxy(getDataLayer());
    }
    
    @Override
    public ShortTextQuestionProxy createShortTextQuestion() {
        return new ShortTextQuestionProxy(getDataLayer());
    }
   
    protected Question createQuestion(ResultSet rs) throws DataException {
        try {
            String questionType = rs.getString("type");
            switch (questionType) {
                case "date":
                    return createDateQuestion(rs);
                case "choice":
                    return createChoicheQuestion(rs);
                case "number":
                    return createNumberQuestion(rs);
                case "short text":
                    return createShortTextQuestion(rs);
                case "long text":
                    return createTextQuestion(rs);
                default:
                    throw new DataException("Unable to create question. Unknown question type");
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to create question object from ResultSet", ex);
        }
    }

    private Question createDateQuestion(ResultSet rs) throws DataException {
        DateQuestion q = createDateQuestion();
        try {
            q.setId(rs.getLong("id"));
            q.setPosition(rs.getShort("position"));
            q.setText(rs.getString("text"));
            q.setNote(rs.getString("note"));
            q.setCode(rs.getString("code"));
            q.setMandatory(rs.getBoolean("mandatory"));
            q.setMinDate(rs.getDate("min_date").toLocalDate());
            q.setMaxDate(rs.getDate("max_date").toLocalDate());
        } catch (SQLException ex) {
            throw new DataException("Unable to create question object from ResultSet", ex);
        }
        return q;
    }

    private Question createChoicheQuestion(ResultSet rs) throws DataException {
        ChoiceQuestion q = createChoiceQuestion();
        try {
            q.setId(rs.getLong("id"));
            q.setPosition(rs.getShort("position"));
            q.setText(rs.getString("text"));
            q.setNote(rs.getString("note"));
            q.setCode(rs.getString("code"));
            q.setMandatory(rs.getBoolean("mandatory"));
            q.setMaxNumberOfChoices((short) rs.getInt("max_value"));
            q.setMinNumberOfChoices((short) rs.getInt("min_value"));
            for (Option option: ((Pollweb_DataLayer) this.dataLayer).getOptionDAO().findByQuestion(q)) {
               q.addOption(option);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to create question object from ResultSet", ex);
        }
        return q;
    }
    
    private Question createNumberQuestion(ResultSet rs) throws DataException {
        NumberQuestion q = createNumberQuestion();
        try {
            q.setId(rs.getLong("id"));
            q.setPosition(rs.getShort("position"));
            q.setText(rs.getString("text"));
            q.setNote(rs.getString("note"));
            q.setCode(rs.getString("code"));
            q.setMandatory(rs.getBoolean("mandatory"));
            q.setMinValue(rs.getInt("min_value"));
            q.setMaxValue(rs.getInt("max_value"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create question object from ResultSet", ex);
        }
        return q;
    }
    
    private Question createTextQuestion(ResultSet rs) throws DataException {
        TextQuestion q = createTextQuestion();
        try {
            q.setId(rs.getLong("id"));
            q.setPosition(rs.getShort("position"));
            q.setText(rs.getString("text"));
            q.setNote(rs.getString("note"));
            q.setCode(rs.getString("code"));
            q.setMandatory(rs.getBoolean("mandatory"));
            q.setMinLength(rs.getInt("min_value"));
            q.setMaxLength(rs.getInt("max_value"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create question object from ResultSet", ex);
        }
        return q;
    }
    
    private Question createShortTextQuestion(ResultSet rs) throws DataException {
        ShortTextQuestion q = createShortTextQuestion();
        try {
            q.setId(rs.getLong("id"));
            q.setPosition(rs.getShort("position"));
            q.setText(rs.getString("text"));
            q.setNote(rs.getString("note"));
            q.setCode(rs.getString("code"));
            q.setMandatory(rs.getBoolean("mandatory"));
            q.setMinLength(rs.getInt("min_value"));
            q.setMaxLength(rs.getInt("max_value"));
            q.setPattern(rs.getString("reg_expr"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create question object from ResultSet", ex);
        }
        return q;
    }

    @Override
    public Question findById(long id) throws DataException {
        ResultSet rs = null;
        try {
            sQuestionById.setLong(1, id);
            rs = sQuestionById.executeQuery();
            if(rs.next()) {
                return createQuestion(rs);
            } else {
                throw new DataException("The question doesn't exist.");
            } 
        } catch (SQLException ex) {
            throw new DataException("Unable to load question by id", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public Question findByAnswer(long id) throws DataException {
        ResultSet rs = null;
        try {
            sQuestionByAnswer.setLong(1, id);
            rs = sQuestionByAnswer.executeQuery();
            if(rs.next()) {
                return createQuestion(rs);
            } else {
                throw new DataException("The question doesn't exist.");
            } 
        } catch (SQLException ex) {
            throw new DataException("Unable to load question by id", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    public List<Question> findBySurvey(Survey survey) throws DataException {
        ResultSet rs = null;
        List<Question> questions = new ArrayList<>();
        try {
            sQuestionsBySurvey.setLong(1, survey.getId());
            rs = sQuestionsBySurvey.executeQuery();
            while (rs.next()) {
                Question question = createQuestion(rs);
                questions.add(question);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load questions of survey", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } 
        return questions;
    }

    @Override
    public void newQuestion(Question question, long surveyId) throws DataException {
        if(question.getId() == 0){
            ResultSet rs = null;
            try {
                // 1    2      3        4     5        6        7        8         9          10     11      12
                //text,note,mandatory,type,min_date,max_date,min_value,max_value,reg_expr,id_survey,position,code
                iQuestion.setString(1, question.getText());
                iQuestion.setString(2, question.getNote());
                if(question.isMandatory()){
                    iQuestion.setString(3, "1");
                }else{
                    iQuestion.setString(3, "0");
                }
                iQuestion.setShort(11, question.getPosition());
                String code = surveyId + question.getQuestionType().charAt(0)+","+question.getId()+","+ question.getPosition();
                iQuestion.setString(12, code);
                iQuestion.setString(4, question.getQuestionType());
                iQuestion.setString(5, "1-1-1");
                iQuestion.setString(6, "1-1-1");
                iQuestion.setInt(7, 0);
                iQuestion.setInt(8, 0);
                iQuestion.setString(9, "");
                iQuestion.setLong(10, surveyId);
                if (iQuestion.executeUpdate() == 0) {
                    System.out.println(iQuestion.toString());
                }
                rs = iQuestion.getGeneratedKeys();
                if (rs.next()) {
                    question.setId(rs.getLong(1));
                }
            }catch (SQLException ex) {
                throw new DataException("Unable to save Question", ex);
            }
        }else {
            try {
                // 1        2           3        4         5      6
                //min_date,max_date,min_value,max_value,reg_expr id
                uQuestionCreation.setString(1, "1-1-1");
                uQuestionCreation.setString(2, "1-1-1");
                uQuestionCreation.setInt(3, 0);
                uQuestionCreation.setInt(4, 0);
                uQuestionCreation.setString(5, ".");
                uQuestionCreation.setLong(6, question.getId());               
                if(question instanceof TextQuestion){
                    if(question instanceof ShortTextQuestion){
                        uQuestionCreation.setString(5,((ShortTextQuestion) question).getPattern());
                    }
                    uQuestionCreation.setInt(3,((TextQuestion) question).getMinLength());  
                    uQuestionCreation.setInt(4,((TextQuestion) question).getMaxLength());
                } else
                if(question instanceof NumberQuestion){ 
                    uQuestionCreation.setInt(3,((NumberQuestion) question).getMinValue());  
                    uQuestionCreation.setInt(4,((NumberQuestion) question).getMaxValue());
                } else
                if(question instanceof DateQuestion){                    
                    uQuestionCreation.setString(1,(((DateQuestion) question).getMinDate()).toString());  
                    uQuestionCreation.setString(2,(((DateQuestion) question).getMaxDate()).toString());
                } else
                if(question instanceof ChoiceQuestion){
                    uQuestionCreation.setInt(3, ((ChoiceQuestion) question).getMinNumberOfChoices());
                    uQuestionCreation.setInt(4,((ChoiceQuestion) question).getMaxNumberOfChoices());
                }
                if (uQuestionCreation.executeUpdate() == 0) {
                    System.out.println(uQuestionCreation.toString());
                }
            }catch (SQLException ex) {
                throw new DataException("Unable to save Question", ex);
            } 
        }
    }
    @Override
    public void update(Question question, long surveyId) throws DataException{
        try {
            //  1     2         3         4         5        6           7             8         9          10       11         12
            //text=?,note=?,mandatory=?,type=?,min_date=?,max_date=?,min_value=?,max_value=?,reg_expr=?,position=?,code=? WHERE id=?
            uQuestion.setString(1, question.getText());
            uQuestion.setString(2, question.getNote());
            if(question.isMandatory()){
                uQuestion.setString(3, "1");
            }else{
                uQuestion.setString(3, "0");
            }
            uQuestion.setString(4, question.getQuestionType());
            uQuestion.setString(5, "1-1-1");
            uQuestion.setString(6, "1-1-1");
            uQuestion.setInt(7,0);  
            uQuestion.setInt(8,0);
            uQuestion.setString(9, ".");
            uQuestion.setShort(10, question.getPosition());
            uQuestion.setLong(12, question.getId());
            String code = Long.toString(surveyId) + question.getQuestionType().charAt(0) +","+question.getId()+"," + question.getPosition();
            uQuestion.setString(11, code);
            if(question instanceof TextQuestion){
                if(question instanceof ShortTextQuestion){
                    uQuestion.setString(9,((ShortTextQuestion) question).getPattern());
                }
                uQuestion.setInt(7,((TextQuestion) question).getMinLength());  
                uQuestion.setInt(8,((TextQuestion) question).getMaxLength());
            } else
            if(question instanceof NumberQuestion){
                uQuestion.setInt(7,((NumberQuestion) question).getMinValue());  
                uQuestion.setInt(8,((NumberQuestion) question).getMaxValue());
            } else
            if(question instanceof DateQuestion){
                uQuestion.setString(5,(((DateQuestion) question).getMinDate()).toString());  
                uQuestion.setString(6,(((DateQuestion) question).getMaxDate()).toString());
            } else
            if(question instanceof ChoiceQuestion){
                uQuestion.setInt(7, ((ChoiceQuestion) question).getMinNumberOfChoices());
                uQuestion.setInt(8,((ChoiceQuestion) question).getMaxNumberOfChoices());
            }
            if (uQuestion.executeUpdate() == 0) {
                System.out.println(uQuestion.toString());
            }
        }catch (SQLException ex) {
            throw new DataException("Unable to save Question", ex);
        } 
    }

    @Override
    public void delete(Survey survey, int del) throws DataException {
        try {
            List<Question> questions = survey.getQuestions();
            for(int i=del+1; i < questions.size(); i++){
                questions.get(i).setPosition((short)(i-1));
                update(questions.get(i),survey.getId());
            }
            if(questions.get(del) instanceof ChoiceQuestion){
                ((Pollweb_DataLayer) this.dataLayer).getOptionDAO().deleteByQuestion(questions.get(del).getId());
            }
            dQuestion.setLong(1, questions.get(del).getId());
            dQuestion.execute();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete the question", ex);
        }
    }
}
