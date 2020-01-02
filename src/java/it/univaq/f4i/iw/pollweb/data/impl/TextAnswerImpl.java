/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.TextAnswer;

/**
 *
 * @author andrea
 */
public class TextAnswerImpl extends AnswerImpl implements TextAnswer{
    
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    @Override
    public boolean isValid(){ 
        if(this.getQuestion() instanceof TextQuestionImpl && this.getQuestion()!=null){
            TextQuestionImpl question =(TextQuestionImpl) this.getQuestion();
        
            if((question.getMaxLength()!= question.UNCOSTRAINED) && (
                    answer.length()>question.getMaxLength())){
                return false;       
            }
            if((question.getMinLength()!= question.UNCOSTRAINED) && (
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
