/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.model.Answer;
import it.univaq.f4i.iw.pollweb.data.model.Option;
import it.univaq.f4i.iw.pollweb.data.model.Participant;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.model.User;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author andrea
 */
public class Pollweb_DataLayer extends DataLayer {
    
    public Pollweb_DataLayer(DataSource dataSource) throws SQLException {
        super(dataSource);
    }
    
    @Override
    public void init() throws DataException {
        //registriamo i nostri dao
        //register our daos
        registerDAO(Survey.class, new SurveyDAO_MySQL(this));
        registerDAO(Participant.class, new ParticipantDAO_MySQL(this));
        registerDAO(SurveyResponse.class, new SurveyResponseDAO_MySQL(this));
        registerDAO(Question.class, new QuestionDAO_MySQL(this));
        registerDAO(Answer.class, new AnswerDAO_MySQL(this));
        registerDAO(Option.class, new OptionDAO_MySQL(this));
        registerDAO(User.class, new UserDAO_MySQL(this));
    }
    
    public UserDAO getUserDAO() {
        return (UserDAO) getDAO(User.class);
    }
    
    public SurveyDAO getSurveyDAO() {
        return (SurveyDAO) getDAO(Survey.class);
    }
    
    public SurveyResponseDAO getSurveyResponseDAO() {
        return (SurveyResponseDAO) getDAO(SurveyResponse.class);
    }
    
    public AnswerDAO getAnswerDAO() {
        return (AnswerDAO) getDAO(Answer.class);
    }
    
    public QuestionDAO getQuestionDAO() {
        return (QuestionDAO) getDAO(Question.class);
    }
    
    public OptionDAO getOptionDAO() {
        return (OptionDAO) getDAO(Option.class);
    }
    
    public ParticipantDAO getParticipantDAO() {
        return (ParticipantDAO) getDAO(Participant.class);
    }
}
