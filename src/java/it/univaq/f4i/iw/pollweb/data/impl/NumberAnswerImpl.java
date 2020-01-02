/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.NumberAnswer;

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
        if(this.getQuestion()!= null && (this.getQuestion() instanceof NumberQuestionImpl)){
            NumberQuestionImpl question =(NumberQuestionImpl) this.getQuestion();
        
            if((question.getMaxValue()!= question.UNCOSTRAINED) && (
                    answer > question.getMaxValue())){
                return false;       
            }
            if((question.getMinValue()!= question.UNCOSTRAINED) && (
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
