/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.ShortTextAnswer;
import it.univaq.f4i.iw.pollweb.data.model.ShortTextQuestion;
import java.util.regex.Pattern;

/**
 *
 * @author andrea
 */
public class ShortTextAnswerImpl extends TextAnswerImpl implements ShortTextAnswer{

    @Override
    public boolean isValid() {
        if(this.getQuestion()!= null && (this.getQuestion() instanceof ShortTextQuestion)){
            ShortTextQuestion question =(ShortTextQuestion) this.getQuestion();
        
            if((question.getMaxLength()!= 0) && (
                    getAnswer().length() > question.getMaxLength())){
                return false;       
            }
            if((question.getMinLength()!= 0) && (
                    getAnswer().length() < question.getMinLength())){
                return false;
            }
            if (!(question.getPattern().equals(".")) &&
                    !(Pattern.matches(question.getPattern(), question.getText()))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
