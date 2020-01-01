/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.model;

import java.util.Objects;

/**
 *
 * @author andrea
 */
public abstract class Answer {
    private long id;
    private Question question;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
    
    public abstract boolean isValid();
    
    @Override
    public String toString() {
        return null;
    }
}
