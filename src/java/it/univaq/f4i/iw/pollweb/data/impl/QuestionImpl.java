/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.Question;


/**
 *
 * @author andrea
 */
public abstract class QuestionImpl implements Question{
    
    private long id;
    private String code;
    private short position;
    private String text;
    private String note;
    private boolean mandatory;
    
    public QuestionImpl() {
        this.mandatory = false;
    }
    
    @Override
    public long getId() {
        return id;
    }
    
    @Override
    public void setId(long id) {
        this.id = id;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public short getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(short position) {
        this.position = position;
    }
    
    @Override
    public String getText() {
        return text;
    }
    
    @Override
    public void setText(String text) {
        this.text = text;
    }
    
    @Override
    public String getNote() {
        return note;
    }
    
    @Override
    public void setNote(String note) {
        this.note = note;
    }
    
    @Override
    public boolean isMandatory() {
        return mandatory;
    }
    
    @Override
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
    
    @Override
    public String getQuestionType(){
        return null;
    }
}
