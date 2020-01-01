/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class SurveyResponse {
    private long id;
    private Survey survey;
    private LocalDate submissionDate;
    private List<Answer> answers = new ArrayList<>();
    
    public SurveyResponse() {
        this.submissionDate = LocalDate.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    
    public boolean isValid(){
        if (submissionDate == null) {
            return false;
        }
        for(Answer answer: this.answers){
            if(!answer.isValid()){
                return false;
            }
        }
        return true;
    }
}
