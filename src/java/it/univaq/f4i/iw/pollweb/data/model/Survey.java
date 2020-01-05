/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

import java.util.List;

/**
 *
 * @author Pagliarini Andrea
 */
public interface Survey {
    
    
    public long getId();
    
    
    public void setId(long id);
    
    
    public String getTitle();
    
    
    public void setTitle(String title);
    
    
    public String getOpeningText();
    
    
    public void setOpeningText(String openingText);
    
    
    public String getClosingText();
    
    
    public void setClosingText(String closingText);
    
    
    public List<Question> getQuestions();
    
    
    public void setQuestions(List<Question> questions);
    
    
    public void addQuestion(Question q);
    
    
    public User getManager();
    
    
    public void setManager(User manager);
    
    
    public boolean isActive();
    
    
    public void setActive(boolean active);
    
    
    public boolean isReserved();
    
    
    public List<SurveyResponse> getResponses();
    
    
    public void setResponses(List<SurveyResponse> responses);
}
