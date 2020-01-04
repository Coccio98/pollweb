/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.NumberQuestion;

/**
 *
 * @author andrea
 */
public class NumberQuestionImpl extends QuestionImpl implements NumberQuestion {
    
    public static final int UNCOSTRAINED = Integer.MIN_VALUE;
    
    private int minValue;
    private int maxValue;
    
    @Override
    public int getMinValue() {
        return minValue;
    }
    
    @Override
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }
    
    @Override
    public int getMaxValue() {
        return maxValue;
    }
    
    @Override
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
    
    @Override
    public String getQuestionType() {
        return "number";
    }
}
