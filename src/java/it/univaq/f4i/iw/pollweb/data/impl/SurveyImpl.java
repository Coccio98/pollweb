/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class SurveyImpl implements Survey{
    private long id;
    private String title;
    private String openingText;
    private String closingText;
    private List<Question> questions;
    private User manager;
    private boolean active;
    private List<SurveyResponse> responses;
    
    public SurveyImpl() {
        this.id = 0;
        this.title = "";
        this.openingText = "";
        this.closingText = "";
        this.questions = new ArrayList<>();
        this.active = true;
        this.responses = new ArrayList<>();
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
    public String getTitle() {
        return title;
    }
    
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String getOpeningText() {
        return openingText;
    }
    
    @Override
    public void setOpeningText(String openingText) {
        this.openingText = openingText;
    }
    
    @Override
    public String getClosingText() {
        return closingText;
    }
    
    @Override
    public void setClosingText(String closingText) {
        this.closingText = closingText;
    }
    
    @Override
    public List<Question> getQuestions() {
        return questions;
    }
    
    @Override
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    @Override
    public void addQuestion(Question q) {
        getQuestions().add(q);
    }
    
    @Override
    public User getManager() {
        return manager;
    }
    
    @Override
    public void setManager(User manager) {
        this.manager = manager;
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public boolean isReserved() {
        return false;
    }
    
    @Override
    public List<SurveyResponse> getResponses() {
        return responses;
    }
    
    @Override
    public void setResponses(List<SurveyResponse> responses) {
        this.responses = responses;
    }
}
