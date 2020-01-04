/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.ChoiceAnswer;
import it.univaq.f4i.iw.pollweb.data.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.data.model.Option;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class ChoiceAnswerImpl extends AnswerImpl implements ChoiceAnswer{
    
    private List<Option> answers;
    
    public ChoiceAnswerImpl() {
        this.answers = new ArrayList<>();
    }
    
    @Override
    public List<Option> getOptions() {
        return answers;
    }
    
    @Override
    public void setAnswers(List<Option> answers) {
        this.answers = answers;
    }
    
    @Override
    public boolean contains(Option option){
        for(Option o: this.answers){
            if(o.getText().equals(option.getText())){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isValid(){ 
        if(this.getQuestion() != null && (this.getQuestion() instanceof ChoiceQuestion)){
            ChoiceQuestion question =(ChoiceQuestion) this.getQuestion();
        
            if((question.getMaxNumberOfChoices()!= 0) && (
                    question.getMaxNumberOfChoices() < this.getOptions().size())){
                return false;       
            }
            if((question.getMinNumberOfChoices()!= 0) && (
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
        for(Option o: this.getOptions()){
            s = s + (o.getPosition()+1) + ".  " + o.getText() + ", ";
        }
        return s;
    }
}
