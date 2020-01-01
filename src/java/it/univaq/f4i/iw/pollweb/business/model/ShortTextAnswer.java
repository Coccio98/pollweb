/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.model;

import java.util.regex.Pattern;

/**
 *
 * @author andrea
 */
public class ShortTextAnswer extends TextAnswer {

    @Override
    public boolean isValid() {
        if(this.getQuestion()!= null && (this.getQuestion() instanceof ShortTextQuestion)){
            ShortTextQuestion question =(ShortTextQuestion) this.getQuestion();
        
            if((question.getMaxLength()!= TextQuestion.UNCOSTRAINED) && (
                    getAnswer().length() > question.getMaxLength())){
                return false;       
            }
            if((question.getMinLength()!= TextQuestion.UNCOSTRAINED) && (
                    getAnswer().length() < question.getMinLength())){
                return false;
            }
            if (!(question.getPattern().equals(ShortTextQuestion.UNCOSTRAINED)) &&
                    !(Pattern.matches(question.getPattern(), question.getText()))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
