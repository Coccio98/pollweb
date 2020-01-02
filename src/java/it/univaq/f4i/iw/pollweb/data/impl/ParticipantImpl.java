/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import java.util.Objects;

/**
 *
 * @author andrea
 */
public class ParticipantImpl {
    private long id;
    private String email;
    private String password;
    private String name;
    private ReservedSurveyImpl reservedSurvey;
    private boolean submitted;
    
    public ParticipantImpl() {
        this.id=0;
        this.submitted = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setSubmitted(boolean b) {
        this.submitted = b;
    }
    
    public boolean isSubmitted() {
        return this.submitted;
    }

    public ReservedSurveyImpl getReservedSurvey() {
        return reservedSurvey;
    }

    public void setReservedSurvey(ReservedSurveyImpl reservedSurvey) {
        this.reservedSurvey = reservedSurvey;
    }
}
