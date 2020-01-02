/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class ChoiceAnswer extends Answer {
    
    private List<Option> answers;
    
    public ChoiceAnswer() {
        this.answers = new ArrayList<>();
    }

    public List<Option> getOptions() {
        return answers;
    }

    public void setAnswers(List<Option> answers) {
        this.answers = answers;
    }
    
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
        for(Option o: this.getOptions()){
            s = s + o.getPosition() + ". " + o.getText() + ",";
        }
        return s;
    }
}
