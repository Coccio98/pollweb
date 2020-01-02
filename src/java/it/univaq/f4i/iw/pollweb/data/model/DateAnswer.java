/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

import java.time.LocalDate;

/**
 *
 * @author Pagliarini Alberto
 */
public interface DateAnswer extends Answer{
    
    LocalDate getAnswer();
    
    void setAnswer(LocalDate answer);
    
    @Override
    boolean isValid();
    
    @Override
    String toString();
    
}
