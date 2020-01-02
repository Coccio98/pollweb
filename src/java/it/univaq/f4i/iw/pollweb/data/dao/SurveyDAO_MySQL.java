/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.model.ReservedSurvey;
import it.univaq.f4i.iw.pollweb.data.model.User;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.proxy.ReservedSurveyProxy;
import it.univaq.f4i.iw.pollweb.data.proxy.SurveyProxy;
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
public class SurveyDAO_MySQL extends DAO implements SurveyDAO {

    private PreparedStatement sSurveyByID,sSurveyByIDOpen;
    private PreparedStatement sSurveysByType, sSurveysByManager;
    private PreparedStatement iSurvey, uSurvey, dSurvey;
    
    public SurveyDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            this.sSurveyByID = this.connection.prepareStatement("SELECT * FROM surveys WHERE id=?");
            this.sSurveyByIDOpen = this.connection.prepareStatement("SELECT * FROM surveys WHERE id=? AND open=1");
            this.sSurveysByManager = this.connection.prepareStatement("SELECT * FROM surveys "
                    + "WHERE id_manager=?");
            this.sSurveysByType = this.connection.prepareStatement("SELECT * FROM surveys WHERE reserved=? AND open=1");
            this.iSurvey = this.connection.prepareStatement("INSERT INTO surveys"
                    + " (title,opening_text,closing_text,open,reserved,id_manager) "
                    + "VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            this.uSurvey = this.connection.prepareStatement("UPDATE surveys SET "
                    + "title=?,opening_text=?,closing_text=?,open=?,reserved=?"
                    + "WHERE id=?");
            this.dSurvey = this.connection.prepareStatement("DELETE FROM sondaggio WHERE id=?");
        } catch(SQLException ex) {
            throw new DataException("Error initializing Data Layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sSurveyByID.close();
            sSurveysByType.close();
            sSurveysByManager.close();
            iSurvey.close();
            uSurvey.close();
            dSurvey.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        super.destroy();
    }

    @Override
    public SurveyProxy createSurvey() {
        return new SurveyProxy(getDataLayer());
    }
    
    @Override
    public ReservedSurveyProxy createReservedSurvey() {
        return new ReservedSurveyProxy(getDataLayer());
    }
    @Override
    public Survey createSurvey(ResultSet rs) throws DataException {
        try {
            if (! rs.getBoolean("reserved")) {
                SurveyProxy s = createSurvey();
                s.setId(rs.getLong("id"));
                s.setTitle(rs.getString("title"));
                s.setOpeningText(rs.getString("opening_text"));
                s.setClosingText(rs.getString("closing_text"));
                s.setManagerId(rs.getLong("id_manager"));
                s.setActive(rs.getBoolean("open"));
                s.setDirty(false);
                return s;
            } else {
                ReservedSurveyProxy s = createReservedSurvey();
                s.setId(rs.getLong("id"));
                s.setTitle(rs.getString("title"));
                s.setOpeningText(rs.getString("opening_text"));
                s.setClosingText(rs.getString("closing_text"));
                s.setManagerId(rs.getLong("id_manager"));
                s.setActive(rs.getBoolean("open"));
                s.setDirty(false);
                return s;
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to create Survey object from ResultSet", ex);
        }
    }

    @Override
    public Survey findById(long id) throws DataException {
        Survey survey = null;
        ResultSet rs = null;
        try {
            sSurveyByID.setLong(1, id);
            rs = sSurveyByID.executeQuery();
            if (rs.next()) {             
                survey = createSurvey(rs);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load survey by id", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return survey;
    }
    
    @Override
    public Survey findByIdOpen(long id) throws DataException {
        Survey survey = null;
        ResultSet rs = null;
        try {
            sSurveyByIDOpen.setLong(1, id);
            rs = sSurveyByIDOpen.executeQuery();
            if (rs.next()) {             
                survey = createSurvey(rs);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load survey by id", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return survey;
    }

    @Override
    public List<Survey> findByManager(User manager) throws DataException {
        List<Survey> surveys = new ArrayList<>();
        ResultSet rs = null;
        try {
            sSurveysByManager.setLong(1, manager.getId());
            rs = sSurveysByManager.executeQuery();
            while (rs.next()) {
                Survey survey = createSurvey(rs);
                surveys.add(survey);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load surveys by manager", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return surveys;
    }

    @Override
    public List<Survey> findAllNotReserved() throws DataException {
        List<Survey> surveys = new ArrayList<>();
        ResultSet rs = null;
        try {
            sSurveysByType.setBoolean(1, false);
            rs = sSurveysByType.executeQuery();
            while (rs.next()) {
                Survey survey = createSurvey(rs);
                surveys.add(survey);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load surveys by type", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return surveys;
    }

    @Override
    public void saveOrUpdate(Survey survey) throws DataException {
        if(survey.getId() == 0){
            ResultSet rs = null;
            try {
                iSurvey.setString(1, survey.getTitle());
                iSurvey.setString(2, survey.getOpeningText());
                iSurvey.setString(3, survey.getClosingText());
                iSurvey.setString(4, "0");
                if(survey.isReserved()){
                    iSurvey.setString(5, "1");
                } else {
                    iSurvey.setString(5, "0");
                }
                iSurvey.setLong(6, survey.getManager().getId());
                if (iSurvey.executeUpdate() == 0) {
                    System.out.println(iSurvey.toString());
                }
                rs = iSurvey.getGeneratedKeys();
                if (rs.next()) {
                    survey.setId(rs.getLong(1));
                }
            }catch (SQLException ex) {
                throw new DataException("Unable to save Survey", ex);
            }
        }else {
            try {
                uSurvey.setString(1, survey.getTitle());
                uSurvey.setString(2, survey.getOpeningText());
                uSurvey.setString(3, survey.getClosingText());
                if(survey.isActive()){
                    uSurvey.setString(4, "1");
                } else {
                    uSurvey.setString(4, "0");
                }
                if(survey.isReserved()){
                    uSurvey.setString(5, "1");
                } else {
                    uSurvey.setString(5, "0");
                }
                uSurvey.setLong(6, survey.getId());
                uSurvey.executeUpdate();
            }catch (SQLException ex) {
                throw new DataException("Unable to save Survey", ex);
            }
        }
    }
    
    @Override
    public void delete(Survey survey) throws DataException {
        try {
            dSurvey.setLong(1, survey.getId());
            dSurvey.execute();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete survey", ex);
        }
    } 
}
