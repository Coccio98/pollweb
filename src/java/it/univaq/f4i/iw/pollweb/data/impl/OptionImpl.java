/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

/**
 *
 * @author andrea
 */
public class OptionImpl {
    
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public short getPosition() {
        return position;
    }

    public void setPosition(short number) {
        this.position = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
