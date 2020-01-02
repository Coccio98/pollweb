/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

import it.univaq.f4i.iw.pollweb.data.model.Question;

/**
 *
 * @author Pagliarini Alberto
 */
public interface Answer {
    
    long getId();
    
    void setId(long id);

    Question getQuestion();

    void setQuestion(Question question);
    
    public abstract boolean isValid();
    
    @Override
    public String toString();
    
}
