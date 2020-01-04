/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.data.model.Option;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class ChoiceQuestionImpl extends QuestionImpl implements ChoiceQuestion {
    
    public final static short UNCOSTRAINED = 0;
    
    private short maxNumberOfChoices;
    private short minNumberOfChoices;
    private List<Option> options = new ArrayList<>();

    public ChoiceQuestionImpl() {
        this.maxNumberOfChoices = UNCOSTRAINED;
        this.minNumberOfChoices = UNCOSTRAINED;
    }
    
    @Override
    public short getMaxNumberOfChoices() {
        return maxNumberOfChoices;
    }
    
    @Override
    public void setMaxNumberOfChoices(short maxNumberOfChoices) {
        this.maxNumberOfChoices = maxNumberOfChoices;
    }
    
    @Override
    public short getMinNumberOfChoices() {
        return minNumberOfChoices;
    }
    
    @Override
    public void setMinNumberOfChoices(short minNumberOfChoices) {
        this.minNumberOfChoices = minNumberOfChoices;
    }
    
    @Override
    public List<Option> getOptions() {
        return options;
    }
    
    @Override
    public void setOptions(List<Option> choices) {
        this.options = choices;
    }
    
    @Override
    public Option getOption(short position) {
        for (Option o: this.options) {
            if (o.getPosition() == position) {
                return o;
            }
        }
        return null;
    }
    
    @Override
    public void addOption(Option  option) {
        this.options.add(option);
    }
    
    @Override
    public String getQuestionType() {
        return "choice";
    }
}
