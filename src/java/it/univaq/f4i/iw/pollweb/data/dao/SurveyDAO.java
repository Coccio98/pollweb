/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.pollweb.business.model.ReservedSurvey;
import it.univaq.f4i.iw.pollweb.business.model.User;
import it.univaq.f4i.iw.pollweb.business.model.Survey;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author andrea
 */
public interface SurveyDAO {
    
    Survey createSurvey();
    ReservedSurvey createReservedSurvey();
    Survey createSurvey(ResultSet rs) throws DataException;
    Survey findById(long id) throws DataException;
    Survey findByIdOpen(long id) throws DataException;
    List<Survey> findByManager(User manager) throws DataException;
    List<Survey> findAllNotReserved() throws DataException;
    List<Survey> findAll() throws DataException;
    void saveOrUpdate(Survey survey) throws DataException;
    void delete(Survey survey) throws DataException;
}
