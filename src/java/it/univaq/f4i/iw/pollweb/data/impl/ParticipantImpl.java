/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.Participant;

/**
 *
 * @author andrea
 */
public class ParticipantImpl implements Participant{
    private long id;
    private String email;
    private String password;
    private String name;
    private boolean submitted;
    
    public ParticipantImpl() {
        this.id=0;
        this.submitted = false;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public void setSubmitted(boolean b) {
        this.submitted = b;
    }
    
    @Override
    public boolean isSubmitted() {
        return this.submitted;
    }
}
