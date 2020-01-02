/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

import it.univaq.f4i.iw.pollweb.data.impl.OptionImpl;
import java.util.List;

/**
 *
 * @author Pagliarini Alberto
 */
public interface ChoiceAnswer extends Answer{
    
    List<OptionImpl> getOptions();

    void setAnswers(List<OptionImpl> answers);
    
    boolean contains(OptionImpl option);
    
    @Override
    boolean isValid();
    
    @Override
    String toString();
}
