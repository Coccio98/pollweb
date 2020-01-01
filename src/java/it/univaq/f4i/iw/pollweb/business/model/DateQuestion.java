/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.model;

import java.time.LocalDate;
import java.time.Month;

/**
 *
 * @author andrea
 */
public class DateQuestion extends Question {
    
    public final static LocalDate UNCOSTRAINED = null;
    
    private LocalDate minDate;
    private LocalDate maxDate;
    
    public DateQuestion(){
        this.minDate=LocalDate.of(1, Month.JANUARY, 1);
        this.maxDate=LocalDate.of(9999, Month.DECEMBER, 31);
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }
    
    @Override
    public String getQuestionType() {
        return "date";
    }
}
