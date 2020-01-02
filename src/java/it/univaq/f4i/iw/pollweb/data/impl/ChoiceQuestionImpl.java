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
public class ChoiceQuestion extends Question {
    
    public final static short UNCOSTRAINED = 0;
    
    private short maxNumberOfChoices;
    private short minNumberOfChoices;
    private List<Option> options = new ArrayList<>();

    public ChoiceQuestion() {
        this.maxNumberOfChoices = UNCOSTRAINED;
        this.minNumberOfChoices = UNCOSTRAINED;
    }
    
    public short getMaxNumberOfChoices() {
        return maxNumberOfChoices;
    }

    public void setMaxNumberOfChoices(short maxNumberOfChoices) {
        this.maxNumberOfChoices = maxNumberOfChoices;
    }
    
    public short getMinNumberOfChoices() {
        return minNumberOfChoices;
    }

    public void setMinNumberOfChoices(short minNumberOfChoices) {
        this.minNumberOfChoices = minNumberOfChoices;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> choices) {
        this.options = choices;
    }
    
    public Option getOption(short position) {
        for (Option o: this.options) {
            if (o.getPosition() == position) {
                return o;
            }
        }
        return null;
    }
    
    public void addOption(Option  option) {
        this.options.add(option);
    }
    
    @Override
    public String getQuestionType() {
        return "choice";
    }
}
