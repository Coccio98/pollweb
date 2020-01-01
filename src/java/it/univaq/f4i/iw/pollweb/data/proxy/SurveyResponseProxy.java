/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.proxy;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.business.model.Answer;
import it.univaq.f4i.iw.pollweb.business.model.Survey;
import it.univaq.f4i.iw.pollweb.business.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.dao.AnswerDAO;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyDAO;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andrea
 */
public class SurveyResponseProxy extends SurveyResponse {
    private DataLayer dataLayer;
    private boolean dirty;
    private long surveyId;

    public SurveyResponseProxy(DataLayer dataLayer) {
        super.setAnswers(null);
        super.setSurvey(null);
        this.dataLayer = dataLayer;
        this.dirty = false;
        this.surveyId = 0;
    }

    @Override
    public void setAnswers(List<Answer> answers) {
        super.setAnswers(answers);
        setDirty(true);
    }

    @Override
    public List<Answer> getAnswers() {
        if (super.getAnswers() == null) {
            try {
                super.setAnswers(((AnswerDAO) this.dataLayer.getDAO(Answer.class)).findBySurveyResponse(this));
            } catch (DataException ex) {
                Logger.getLogger(SurveyResponseProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getAnswers();
    }

    @Override
    public void setSubmissionDate(LocalDate submissionDate) {
        if (! super.getSubmissionDate().equals(submissionDate)) {
            super.setSubmissionDate(submissionDate);
            setDirty(true);
        }
    }

    @Override
    public void setSurvey(Survey survey) {
        super.setSurvey(survey);
        setDirty(true);
    }

    @Override
    public Survey getSurvey() {
        if (super.getSurvey() == null) {
            try {
                super.setSurvey(((SurveyDAO) this.dataLayer.getDAO(Survey.class)).findById(surveyId));
            } catch (DataException ex) {
                Logger.getLogger(SurveyResponseProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getSurvey();
    }
    
    public DataLayer getDataLayer() {
        return dataLayer;
    }

    public void setDataLayer(DataLayer dataLayer) {
        this.dataLayer = dataLayer;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
        super.setSurvey(null);
    }
}