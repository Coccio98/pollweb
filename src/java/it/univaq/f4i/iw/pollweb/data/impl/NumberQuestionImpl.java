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
public class NumberQuestionImpl extends QuestionImpl {
    
    public static final int UNCOSTRAINED = Integer.MIN_VALUE;
    
    private int minValue;
    private int maxValue;

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
    
    @Override
    public String getQuestionType() {
        return "number";
    }
}
