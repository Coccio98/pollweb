/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

/**
 *
 * @author andrea
 */
public class TextQuestionImpl extends QuestionImpl {
   
    public static final short UNCOSTRAINED = 0;
    
    private int minLength;
    private int maxLength;
    
    public TextQuestionImpl() {
        this.minLength = UNCOSTRAINED;
        this.maxLength = 10000;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
    
    @Override
    public String getQuestionType() {
        return "long text";
    }
}
