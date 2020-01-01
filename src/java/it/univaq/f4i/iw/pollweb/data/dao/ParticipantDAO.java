/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.pollweb.business.model.Participant;
import it.univaq.f4i.iw.pollweb.business.model.Survey;
import java.util.List;

/**
 *
 * @author andrea
 */
public interface ParticipantDAO {
    
    List<Participant> findBySurvey(Survey Survey) throws DataException;
    List<Participant> findBySurveyAndCompiledStatus(Survey survey, boolean status) throws DataException;
    void saveOrUpdate(Participant participant, long surveyId) throws DataException;
    void delete(Participant participant) throws DataException;
}
