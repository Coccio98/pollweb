/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.TextAnswer;
import it.univaq.f4i.iw.pollweb.data.model.TextQuestion;

/**
 *
 * @author andrea
 */
public class TextAnswerImpl extends AnswerImpl implements TextAnswer{
    
    private String answer;
    
    @Override
    public String getAnswer() {
        return answer;
    }
    
    @Override
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    @Override
    public boolean isValid(){ 
        if(this.getQuestion() instanceof TextQuestion && this.getQuestion()!=null){
            TextQuestion question =(TextQuestion) this.getQuestion();
        
            if((question.getMaxLength()!= 0) && (
                    answer.length()>question.getMaxLength())){
                return false;       
            }
            if((question.getMinLength()!= 0) && (
                    answer.length()<question.getMinLength())){
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.getAnswer();
    }
}
