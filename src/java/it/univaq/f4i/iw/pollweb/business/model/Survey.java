/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author andrea
 */
public class Survey {
    private long id;
    private String title;
    private String openingText;
    private String closingText;
    private List<Question> questions;
    private User manager;
    private boolean active;
    private List<SurveyResponse> responses;
    
    public Survey() {
        this.id = 0;
        this.title = "";
        this.openingText = "";
        this.closingText = "";
        this.questions = new ArrayList<>();
        this.active = true;
        this.responses = new ArrayList<>();
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOpeningText() {
        return openingText;
    }

    public void setOpeningText(String openingText) {
        this.openingText = openingText;
    }

    public String getClosingText() {
        return closingText;
    }

    public void setClosingText(String closingText) {
        this.closingText = closingText;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    public void addQuestion(Question q) {
        getQuestions().add(q);
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isReserved() {
        return false;
    }
    
    public List<SurveyResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<SurveyResponse> responses) {
        this.responses = responses;
    }
}
