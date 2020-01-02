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
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.proxy.SurveyResponseProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author andrea
 */
public class SurveyResponseDAO_MySQL extends DAO implements SurveyResponseDAO {

    private PreparedStatement sResponseById, sResponsesBySurvey;
    private PreparedStatement iResponse;
    
    public SurveyResponseDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void destroy() throws DataException {
        try {
            sResponseById.close();
            sResponsesBySurvey.close();
            iResponse.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        super.destroy();
    }
    
    @Override
    public SurveyResponseProxy createSurveyResponse() {
        return new SurveyResponseProxy(getDataLayer());
    }

    @Override
    public void init() throws DataException {
        try {
            sResponsesBySurvey = connection.prepareStatement("SELECT * FROM survey_responses WHERE id_survey=?");
            sResponseById = connection.prepareStatement("SELECT * FROM survey_responses WHERE id=?");
            iResponse = connection.prepareStatement("INSERT INTO survey_responses (submission_date,id_survey,id_participant) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new DataException("Error initializing Data Layer", ex);
        }
    }

    private SurveyResponse createSurveyResponse(ResultSet rs) throws DataException {
        try {
            SurveyResponseProxy proxy = new SurveyResponseProxy(dataLayer);
            proxy.setId(rs.getLong("id"));
            proxy.setSubmissionDate(rs.getDate("submission_date").toLocalDate());
            proxy.setSurveyId(rs.getLong("id_survey"));
            return proxy;
        } catch (SQLException ex) {
            throw new DataException("Unable to create SurveyResponse from ResultSet", ex);
        }
    }

    @Override
    public SurveyResponse findById(long id) throws DataException {
        ResultSet rs = null;
        try {
            sResponseById.setLong(1, id);
            rs = sResponseById.executeQuery();
            if (rs.next()) {
                return createSurveyResponse(rs);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load survey response by id", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } 
        }
    }
    
    @Override
    public List<SurveyResponse> findBySurvey(Survey survey) throws DataException {
        ResultSet rs = null;
        List<SurveyResponse> responses = new ArrayList<>();
        try {
            sResponsesBySurvey.setLong(1, survey.getId());
            rs = sResponsesBySurvey.executeQuery();
            while (rs.next()) {
                SurveyResponse response = createSurveyResponse(rs);
                responses.add(response);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load survey responses", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return responses;
    }

    @Override
    public void saveOrUpdate(SurveyResponse sr, long surveyId) throws DataException {
        //INSERT
        if (sr.getId() == 0) {
            ResultSet rs = null;
            try {
                iResponse.setDate(1, Date.valueOf(sr.getSubmissionDate()));
                iResponse.setLong(2, surveyId);
                iResponse.setNull(3, java.sql.Types.INTEGER);
                if (iResponse.executeUpdate() == 0) {
                    System.out.println(iResponse.toString());
                } 
                long id = 0;
                rs = iResponse.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getLong(1);
                }
                for (Answer answer: sr.getAnswers()) {
                    ((AnswerDAO) this.dataLayer.getDAO(Answer.class)).saveOrUpdate(answer, id);
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to save SurveyResponse", ex);
            }
        } else {
            
        }
    }
}