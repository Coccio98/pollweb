/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.model.Participant;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.proxy.ParticipantProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author andrea
 */
public class ParticipantDAO_MySQL extends DAO implements ParticipantDAO {

    private PreparedStatement sParticipantsBySurvey;
    private PreparedStatement iParticipant, uParticipant;
    
    public ParticipantDAO_MySQL(DataLayer dl) {
        super(dl);
    }

    @Override
    public void destroy() throws DataException {
        try {
            sParticipantsBySurvey.close();
            iParticipant.close();
            uParticipant.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        super.destroy();
    } 

    @Override
    public void init() throws DataException {
        try {
            sParticipantsBySurvey = connection.prepareStatement("SELECT * FROM participants WHERE id_survey=?");
            iParticipant = connection.prepareStatement("INSERT INTO participants (name,email,password,id_survey) VALUES (?,?,?,?)");
            uParticipant = connection.prepareStatement("UPDATE participants SET compiled=? WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing Data Layer", ex);
        }
    }
    
    @Override
    public ParticipantProxy createParticipant() {
        return new ParticipantProxy(getDataLayer());
    }

    @Override
    public List<Participant> findBySurvey(Survey survey) throws DataException {
        ResultSet rs = null;
        List<Participant> participants = new ArrayList<>();
        try {
            sParticipantsBySurvey.setLong(1, survey.getId());
            rs = sParticipantsBySurvey.executeQuery();
            while (rs.next()) {
                Participant p = createParticipant();
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
                uParticipant.setBoolean(1,participant.isSubmitted());
                uParticipant.setLong(2,participant.getId());
                uParticipant.executeUpdate();
            }catch (SQLException ex) {
                throw new DataException("Unable to save Participant", ex);
            }        
        }
    }

}
