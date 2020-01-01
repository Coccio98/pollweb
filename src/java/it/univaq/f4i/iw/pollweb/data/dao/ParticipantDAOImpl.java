/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.business.model.Participant;
import it.univaq.f4i.iw.pollweb.business.model.Survey;
import it.univaq.f4i.iw.pollweb.data.dao.ParticipantDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author andrea
 */
public class ParticipantDAOImpl extends DAO implements ParticipantDAO {

    private PreparedStatement sParticipantsBySurvey, sParticipantsBySurveyAndStatus;
    private PreparedStatement iParticipant, uParticipant, dParticipant;
    
    public ParticipantDAOImpl(DataLayer dl) {
        super(dl);
    }

    @Override
    public void destroy() throws DataException {
        try {
            sParticipantsBySurvey.close();
            sParticipantsBySurveyAndStatus.close();
            iParticipant.close();
            uParticipant.close();
            dParticipant.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        super.destroy();
    } 

    @Override
    public void init() throws DataException {
        try {
            sParticipantsBySurvey = connection.prepareStatement("SELECT * FROM participants WHERE id_survey=?");
            sParticipantsBySurveyAndStatus = connection.prepareStatement("SELECT * FROM participants WHERE id_survey=? AND compiled=?");
            iParticipant = connection.prepareStatement("INSERT INTO participants (name,email,password,id_survey) VALUES (?,?,?,?)");
            uParticipant = connection.prepareStatement("UPDATE participants SET compiled=? WHERE id=?");
            dParticipant = connection.prepareStatement("DELETE FROM participants WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing Data Layer", ex);
        }
    }
    
    

    @Override
    public List<Participant> findBySurvey(Survey survey) throws DataException {
        ResultSet rs = null;
        List<Participant> participants = new ArrayList<>();
        try {
            sParticipantsBySurvey.setLong(1, survey.getId());
            rs = sParticipantsBySurvey.executeQuery();
            while (rs.next()) {
                Participant p = new Participant();
                p.setId(rs.getLong("id"));
                p.setEmail(rs.getString("email"));
                p.setPassword(rs.getString("password"));
                p.setName(rs.getString("name"));
                p.setSubmitted(rs.getBoolean("compiled"));
                participants.add(p);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load participants from survey", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return participants;
    } 

    @Override
    public List<Participant> findBySurveyAndCompiledStatus(Survey survey, boolean status) throws DataException {
        ResultSet rs = null;
        List<Participant> participants = new ArrayList<>();
        try {
            sParticipantsBySurveyAndStatus.setLong(1, survey.getId());
            sParticipantsBySurveyAndStatus.setBoolean(2, status);
            rs = sParticipantsBySurvey.executeQuery();
            while (rs.next()) {
                Participant p = new Participant();
                p.setId(rs.getLong("id"));
                p.setEmail(rs.getString("email"));
                p.setName(rs.getString("name"));
                participants.add(p);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load participants from survey", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return participants;
    }
    
    @Override
    public void saveOrUpdate(Participant participant, long surveyId) throws DataException {
        if (participant.getId()==0){
            try {
                iParticipant.setString(1,participant.getName());
                iParticipant.setString(2,participant.getEmail());
                iParticipant.setString(3,participant.getPassword());
                iParticipant.setLong(4,surveyId);
                iParticipant.executeUpdate();
            }catch (SQLException ex) {
                throw new DataException("Unable to save Participant", ex);
            }
        } else {
            try {
                //if(participant.isSubmitted()){
                    uParticipant.setBoolean(1,participant.isSubmitted());
                    uParticipant.setLong(2,participant.getId());
                    uParticipant.executeUpdate();
               // } else {

                //}
            }catch (SQLException ex) {
                throw new DataException("Unable to save Participant", ex);
            }        
        }
    }

    @Override
    public void delete(Participant participant) throws DataException {
        try {
            dParticipant.setLong(1, participant.getId());
            dParticipant.execute();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete participant", ex);
        }
    }  
}
