/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.model;

import it.univaq.f4i.iw.pollweb.data.impl.ParticipantImpl;
import java.util.List;

/**
 *
 * @author Pagliarini Alberto
 */
public interface ReservedSurvey extends Survey {
    
    List<ParticipantImpl> getParticipants();
    
    void setParticipants(List<ParticipantImpl> participants);
    
    @Override
    boolean isReserved();
    
}
