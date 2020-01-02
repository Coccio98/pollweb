/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.proxy.SurveyResponseProxy;
import java.util.List;

/**
 *
 * @author andrea
 */
public interface SurveyResponseDAO {
    
    SurveyResponseProxy createSurveyResponse();
    SurveyResponse findById(long id) throws DataException;
    List<SurveyResponse> findBySurvey(Survey survey) throws DataException;
    void saveOrUpdate(SurveyResponse sr, long surveyId) throws DataException;
}
