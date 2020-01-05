/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

import java.util.List;

/**
 *
 * @author Pagliarini Andrea
 */
public interface ReservedSurvey extends Survey {
    
    List<Participant> getParticipants();
    
    void setParticipants(List<Participant> participants);
    
    @Override
    boolean isReserved();
    
}
