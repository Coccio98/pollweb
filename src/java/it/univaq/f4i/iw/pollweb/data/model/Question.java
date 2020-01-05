/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

/**
 *
 * @author Pagliarini Andrea
 */
public interface Question {
    
    long getId();

    void setId(long id);

    String getCode();

    void setCode(String code);

    short getPosition();

    void setPosition(short position);
    
    String getText();

    void setText(String text);

    String getNote();

    void setNote(String note);

    boolean isMandatory();

    void setMandatory(boolean mandatory);
    
    String getQuestionType();
    
}
