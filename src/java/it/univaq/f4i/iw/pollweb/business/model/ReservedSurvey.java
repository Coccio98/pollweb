/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.model;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author andrea
 */
public class ReservedSurvey extends Survey {
    
    private List<Participant> participants = new ArrayList<>();

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
    
    @Override
    public boolean isReserved(){
        return true;
    }
}
