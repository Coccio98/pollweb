/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.ShortTextAnswer;
import java.util.regex.Pattern;

/**
 *
 * @author andrea
 */
public class ShortTextAnswerImpl extends TextAnswerImpl implements ShortTextAnswer{

    @Override
    public boolean isValid() {
        if(this.getQuestion()!= null && (this.getQuestion() instanceof ShortTextQuestionImpl)){
            ShortTextQuestionImpl question =(ShortTextQuestionImpl) this.getQuestion();
        
            if((question.getMaxLength()!= TextQuestionImpl.UNCOSTRAINED) && (
                    getAnswer().length() > question.getMaxLength())){
                return false;       
            }
            if((question.getMinLength()!= TextQuestionImpl.UNCOSTRAINED) && (
                    getAnswer().length() < question.getMinLength())){
                return false;
            }
            if (!(question.getPattern().equals(ShortTextQuestionImpl.UNCOSTRAINED)) &&
                    !(Pattern.matches(question.getPattern(), question.getText()))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
