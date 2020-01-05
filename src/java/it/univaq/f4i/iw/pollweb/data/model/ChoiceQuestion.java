/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

import java.util.List;

/**
 *
 * @author Pagliarini Andrea
 */
public interface ChoiceQuestion extends Question{
    
    short getMaxNumberOfChoices();

    public void setMaxNumberOfChoices(short maxNumberOfChoices);
    
    public short getMinNumberOfChoices();

    public void setMinNumberOfChoices(short minNumberOfChoices);

    public List<Option> getOptions();

    public void setOptions(List<Option> choices);
    
    public Option getOption(short position);
    
    public void addOption(Option  option);
    
    @Override
    public String getQuestionType();
    
}
