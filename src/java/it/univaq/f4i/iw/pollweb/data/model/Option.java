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
public interface Option {
    
    long getId();

    void setId(long id);
    
    short getPosition();

    void setPosition(short number);

    String getText();

    void setText(String text);
}
