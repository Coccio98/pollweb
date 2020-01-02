/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.Answer;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import java.util.Objects;

/**
 *
 * @author andrea
 */
public abstract class AnswerImpl implements Answer{
    private long id;
    private Question question;
    
    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public void setId(long id) {
        this.id = id;
    }
    
    @Override
    public Question getQuestion() {
        return question;
    }
    
    @Override
    public void setQuestion(Question question) {
        this.question = question;
    }
    
    @Override
    public abstract boolean isValid();
    
    @Override
    public String toString() {
        return null;
    }
}
