/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.User;

/**
 *
 * @author andrea
 */
public class UserImpl implements User{
    
    private long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Type type;
    
    
    public UserImpl() {
        this.id = 0;
        this.name = "";
        this.surname = "";
        this.email = "";
        this.password = "";
        this.type = Type.RESPONSIBLE;
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
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String getSurname() {
        return surname;
    }
    
    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    @Override
    public String getEmail() {
        return email;
    }
    
    @Override
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public Type getType() {
        return type;
    }
    
    @Override
    public void setType(Type type) {
        this.type = type;
    }
}
