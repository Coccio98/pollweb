/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.TextQuestion;

/**
 *
 * @author andrea
 */
public class TextQuestionImpl extends QuestionImpl implements TextQuestion{
   
    public static final short UNCOSTRAINED = 0;
    
    private int minLength;
    private int maxLength;
    
    public TextQuestionImpl() {
        this.minLength = UNCOSTRAINED;
        this.maxLength = 10000;
    }
    
    @Override
    public int getMinLength() {
        return minLength;
    }
    
    @Override
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }
    
    @Override
    public int getMaxLength() {
        return maxLength;
    }
    
    @Override
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
    
    @Override
    public String getQuestionType() {
        return "long text";
    }
}
