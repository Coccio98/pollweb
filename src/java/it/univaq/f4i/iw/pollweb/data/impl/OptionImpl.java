/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.Option;

/**
 *
 * @author andrea
 */
public class OptionImpl implements Option{
    
    private long id;
    private short position;
    private String text;
    
    public OptionImpl() {
        id = 0;
        position = 0;
        text = "";
    }
    
    public OptionImpl(String t) {
        this.text = t;
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
    public short getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(short number) {
        this.position = number;
    }
    
    @Override
    public String getText() {
        return text;
    }
    
    @Override
    public void setText(String text) {
        this.text = text;
    }
}
