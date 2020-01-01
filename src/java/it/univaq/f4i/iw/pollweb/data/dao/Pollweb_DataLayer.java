/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.pollweb.data.dao.OptionDAOImpl;
import it.univaq.f4i.iw.pollweb.data.dao.AnswerDAOImpl;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.business.model.Answer;
import it.univaq.f4i.iw.pollweb.business.model.Option;
import it.univaq.f4i.iw.pollweb.business.model.Participant;
import it.univaq.f4i.iw.pollweb.business.model.Question;
import it.univaq.f4i.iw.pollweb.business.model.Survey;
import it.univaq.f4i.iw.pollweb.business.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.business.model.User;
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
        registerDAO(Survey.class, new SurveyDAOImpl(this));
        registerDAO(Participant.class, new ParticipantDAOImpl(this));
        registerDAO(SurveyResponse.class, new SurveyResponseDAOImpl(this));
        registerDAO(Question.class, new QuestionDAOImpl(this));
        registerDAO(Answer.class, new AnswerDAOImpl(this));
        registerDAO(Option.class, new OptionDAOImpl(this));
        registerDAO(User.class, new UserDAOImpl(this));
    }
}
