/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import java.util.List;

/**
 *
 * @author andrea
 */
public interface QuestionDAO {
    
    Question findById(long id) throws DataException;
    List<Question> findBySurvey(Survey survey) throws DataException;
    Question findByAnswer(long id) throws DataException;
    void newQuestion(Question question, long surveyId) throws DataException;
    void update(Question question, long surveyId) throws DataException;
    void delete(Survey survey, int del) throws DataException;
}
