/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.DateAnswer;
import it.univaq.f4i.iw.pollweb.data.model.DateQuestion;
import java.time.LocalDate;

/**
 *
 * @author andrea
 */
public class DateAnswerImpl extends AnswerImpl implements DateAnswer{
    
    private LocalDate answer;
    
    @Override
    public LocalDate getAnswer() {
        return answer;
    }
    
    @Override
    public void setAnswer(LocalDate answer) {
        this.answer = answer;
    }
    
    @Override
    public boolean isValid(){ 
        if(this.getQuestion()!= null && (this.getQuestion() instanceof DateQuestion)){
            DateQuestion question =(DateQuestion) this.getQuestion();
        
            if((question.getMaxDate()!= null) &&
                    answer.isAfter(question.getMaxDate())){
                return false;       
            }
            if((question.getMinDate()!= null) && 
                    answer.isBefore(question.getMinDate())){
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
