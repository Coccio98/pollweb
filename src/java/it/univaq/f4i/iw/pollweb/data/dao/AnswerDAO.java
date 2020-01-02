/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.pollweb.data.model.Answer;
import it.univaq.f4i.iw.pollweb.data.model.ChoiceAnswer;
import it.univaq.f4i.iw.pollweb.data.model.DateAnswer;
import it.univaq.f4i.iw.pollweb.data.model.NumberAnswer;
import it.univaq.f4i.iw.pollweb.data.model.ShortTextAnswer;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.model.TextAnswer;
import java.util.List;

/**
 *
 * @author andrea
 */
public interface AnswerDAO {
    
    ChoiceAnswer createChoiceAnswer();
    DateAnswer createDateAnswer();
    NumberAnswer createNumberAnswer();
    TextAnswer createTextAnswer();
    ShortTextAnswer createShortTextAnswer();
    List<Answer> findBySurveyResponse(SurveyResponse surveyResponse) throws DataException;
    void saveOrUpdate(Answer answer, long surveyId) throws DataException;
}
