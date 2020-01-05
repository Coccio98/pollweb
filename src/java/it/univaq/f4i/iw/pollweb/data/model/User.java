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
public interface User {
    
    public enum Type {
        RESPONSIBLE,
        ADMINISTRATOR
    }
    
    long getId();
    
    void setId(long id);
    
    String getName();
    
    void setName(String name);
    
    String getSurname();
    
    void setSurname(String surname);
    
    String getEmail();
    
    void setEmail(String email);
    
    String getPassword();
    
    void setPassword(String password);
    
    Type getType();
    
    void setType(Type type);
}
