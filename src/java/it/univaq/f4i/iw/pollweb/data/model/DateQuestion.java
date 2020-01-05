/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

import java.time.LocalDate;

/**
 *
 * @author Pagliarini Andrea
 */
public interface DateQuestion extends Question{
    
    LocalDate getMinDate();

    void setMinDate(LocalDate minDate);
            
    LocalDate getMaxDate();

    void setMaxDate(LocalDate maxDate);
    
    @Override
    String getQuestionType();    
}
