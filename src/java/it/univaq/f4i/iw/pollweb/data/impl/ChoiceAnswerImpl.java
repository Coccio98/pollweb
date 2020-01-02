/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.ChoiceAnswer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class ChoiceAnswerImpl extends AnswerImpl implements ChoiceAnswer{
    
    private List<OptionImpl> answers;
    
    public ChoiceAnswerImpl() {
        this.answers = new ArrayList<>();
    }
    
    @Override
    public List<OptionImpl> getOptions() {
        return answers;
    }
    
    @Override
    public void setAnswers(List<OptionImpl> answers) {
        this.answers = answers;
    }
    
    @Override
    public boolean contains(OptionImpl option){
        for(OptionImpl o: this.answers){
            if(o.getText().equals(option.getText())){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isValid(){ 
        if(this.getQuestion() != null && (this.getQuestion() instanceof ChoiceQuestionImpl)){
            ChoiceQuestionImpl question =(ChoiceQuestionImpl) this.getQuestion();
        
            if((question.getMaxNumberOfChoices()!= question.UNCOSTRAINED) && (
                    question.getMaxNumberOfChoices() < this.getOptions().size())){
                return false;       
            }
            if((question.getMinNumberOfChoices()!= question.UNCOSTRAINED) && (
                    question.getMinNumberOfChoices() > this.getOptions().size())){
                return false;       
            }
        } else {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        String s = "";
        for(OptionImpl o: this.getOptions()){
            s = s + o.getPosition() + ". " + o.getText() + ",";
        }
        return s;
    }
}
