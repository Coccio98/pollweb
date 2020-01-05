/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;


import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Pagliarini Andrea
 */
public interface SurveyResponse {
    
    long getId();

    void setId(long id);
    
    Survey getSurvey();

    void setSurvey(Survey survey);

    LocalDate getSubmissionDate();

    void setSubmissionDate(LocalDate submissionDate);

    List<Answer> getAnswers();

    void setAnswers(List<Answer> answers);
    
    boolean isValid();   
}
