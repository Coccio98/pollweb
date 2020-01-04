/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.NumberAnswer;
import it.univaq.f4i.iw.pollweb.data.model.NumberQuestion;

/**
 *
 * @author andrea
 */
public class NumberAnswerImpl extends AnswerImpl implements NumberAnswer {
    
    private float answer;
    
    @Override
    public float getAnswer() {
        return answer;
    }
    
    @Override
    public void setAnswer(float answer) {
        this.answer = answer;
    }
    
    @Override
    public boolean isValid(){ 
        if(this.getQuestion()!= null && (this.getQuestion() instanceof NumberQuestion)){
            NumberQuestion question =(NumberQuestion) this.getQuestion();
        
            if((question.getMaxValue()!= Integer.MIN_VALUE) && (
                    answer > question.getMaxValue())){
                return false;       
            }
            if((question.getMinValue()!= Integer.MIN_VALUE) && (
                    answer < question.getMinValue())){
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getAnswer());
    }
}
