/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.Answer;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class SurveyResponseImpl implements SurveyResponse{
    private long id;
    private Survey survey;
    private LocalDate submissionDate;
    private List<Answer> answers = new ArrayList<>();
    
    public SurveyResponseImpl() {
        this.submissionDate = LocalDate.now();
    }
    
    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public void setId(long id) {
        this.id = id;
    }
    
    @Override
    public Survey getSurvey() {
        return survey;
    }
    
    @Override
    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
    
    @Override
    public LocalDate getSubmissionDate() {
        return submissionDate;
    }
    
    @Override
    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    @Override
    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    
    @Override
    public boolean isValid(){
        if (submissionDate == null) {
            return false;
        }
        for(Answer answer: getAnswers()){
            if(!answer.isValid()){
                return false;
            }
        }
        return true;
    }
}
