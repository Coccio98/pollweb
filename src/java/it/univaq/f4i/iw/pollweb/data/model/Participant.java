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
public interface Participant {
    
    long getId();

    void setId(long id);

    String getEmail();

    void setEmail(String email);

    String getPassword();

    void setPassword(String password);
    
    String getName();

    void setName(String name);
    
    void setSubmitted(boolean b);
    
    boolean isSubmitted();

}
