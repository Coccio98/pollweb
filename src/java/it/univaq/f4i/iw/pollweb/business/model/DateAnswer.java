/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.model;

import java.time.LocalDate;

/**
 *
 * @author andrea
 */
public class DateAnswer extends Answer {
    
    private LocalDate answer;

    public LocalDate getAnswer() {
        return answer;
    }

    public void setAnswer(LocalDate answer) {
        this.answer = answer;
    }
    
    @Override
    public boolean isValid(){ 
        if(this.getQuestion()!= null && (this.getQuestion() instanceof DateQuestion)){
            DateQuestion question =(DateQuestion) this.getQuestion();
        
            if((question.getMaxDate()!= question.UNCOSTRAINED) &&
                    answer.isAfter(question.getMaxDate())){
                return false;       
            }
            if((question.getMinDate()!= question.UNCOSTRAINED) && 
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
