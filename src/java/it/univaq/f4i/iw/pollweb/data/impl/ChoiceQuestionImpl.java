/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class ChoiceQuestionImpl extends QuestionImpl {
    
    public final static short UNCOSTRAINED = 0;
    
    private short maxNumberOfChoices;
    private short minNumberOfChoices;
    private List<OptionImpl> options = new ArrayList<>();

    public ChoiceQuestionImpl() {
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

    public List<OptionImpl> getOptions() {
        return options;
    }

    public void setOptions(List<OptionImpl> choices) {
        this.options = choices;
    }
    
    public OptionImpl getOption(short position) {
        for (OptionImpl o: this.options) {
            if (o.getPosition() == position) {
                return o;
            }
        }
        return null;
    }
    
    public void addOption(OptionImpl  option) {
        this.options.add(option);
    }
    
    @Override
    public String getQuestionType() {
        return "choice";
    }
}
