/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.DateQuestion;
import java.time.LocalDate;
import java.time.Month;

/**
 *
 * @author andrea
 */
public class DateQuestionImpl extends QuestionImpl implements DateQuestion{
    
    private LocalDate minDate;
    private LocalDate maxDate;
    
    public DateQuestionImpl(){
        this.minDate=LocalDate.of(1, Month.JANUARY, 1);
        this.maxDate=LocalDate.of(9999, Month.DECEMBER, 31);
    }
    
    @Override
    public LocalDate getMinDate() {
        return minDate;
    }
    
    @Override
    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }
    
    @Override
    public LocalDate getMaxDate() {
        return maxDate;
    }
    
    @Override
    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }
    
    @Override
    public String getQuestionType() {
        return "date";
    }
}
