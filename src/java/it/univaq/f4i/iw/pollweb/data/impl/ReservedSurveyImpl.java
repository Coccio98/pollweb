/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.impl;

import it.univaq.f4i.iw.pollweb.data.model.Participant;
import it.univaq.f4i.iw.pollweb.data.model.ReservedSurvey;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author andrea
 */
public class ReservedSurveyImpl extends SurveyImpl implements ReservedSurvey{
    
    private List<Participant> participants = new ArrayList<>();
    
    @Override
    public List<Participant> getParticipants() {
        return participants;
    }
    
    @Override
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
    
    @Override
    public boolean isReserved(){
        return true;
    }
}
