/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.ShortTextQuestion;

/**
 *
 * @author andrea
 */
public class ShortTextQuestionImpl extends TextQuestionImpl implements ShortTextQuestion{
    
    public static final String UNCOSTRAINED = ".";
    
    private String pattern;

    public ShortTextQuestionImpl() {
        this.pattern = UNCOSTRAINED;
    }
    
    @Override
    public String getPattern() {
        return pattern;
    }
    
    @Override
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getQuestionType() {
        return "short text";
    }
}
