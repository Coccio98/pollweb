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
public interface ShortTextAnswer extends TextAnswer{
    
    @Override
    boolean isValid();
    
    @Override
    String toString();
    
}
