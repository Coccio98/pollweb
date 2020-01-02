/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.model;

/**
 *
 * @author andrea
 */
public class ShortTextQuestion extends TextQuestion {
    
    public static final String UNCOSTRAINED = ".";
    
    private String pattern;

    public ShortTextQuestion() {
        this.pattern = UNCOSTRAINED;
    }
    
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getQuestionType() {
        return "short text";
    }
}
