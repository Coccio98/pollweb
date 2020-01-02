/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

/**
 *
 * @author Pagliarini Alberto
 */
public interface NumberAnswer extends Answer{
    
    float getAnswer();
    
    void setAnswer(float answer);
    
    @Override
    boolean isValid();
    
    @Override
    String toString();
    
}
