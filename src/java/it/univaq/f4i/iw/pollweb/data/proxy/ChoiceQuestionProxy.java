/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.proxy;

import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.impl.ChoiceQuestionImpl;
import it.univaq.f4i.iw.pollweb.data.model.Option;
import java.util.List;

/**
 *
 * @author Pagliarini Alberto
 */
public class ChoiceQuestionProxy extends ChoiceQuestionImpl{
    private DataLayer dataLayer;
    private boolean dirty;
    
    public ChoiceQuestionProxy(DataLayer dataLayer){
        super();
        this.dataLayer = dataLayer;
        this.dirty = false;
    }
    
    @Override
    public void setId(long id){
        super.setId(id);
        setDirty(true);
    }
    
    @Override
    public void setCode(String code){
        super.setCode(code);
        setDirty(true);
    }
    
    @Override
    public void setPosition(short position) {
        super.setPosition(position);
        setDirty(true);
    }
    
     @Override
    public void setText(String text) {
        super.setText(text);
        setDirty(true);
    }
    
    @Override
    public void setNote(String note) {
        super.setNote(note);
        setDirty(true);
    }
    
    @Override
    public void setMandatory(boolean mandatory) {
        super.setMandatory(mandatory);
        setDirty(true);
    }
    
    @Override
    public void setMaxNumberOfChoices(short maxNumberOfChoices) {
        super.setMaxNumberOfChoices(maxNumberOfChoices);
        setDirty(true);
    }
    
    @Override
    public void setMinNumberOfChoices(short minNumberOfChoices) {
        super.setMinNumberOfChoices(minNumberOfChoices);
        setDirty(true);
    }
    
    @Override
    public void setOptions(List<Option> choices) {
        super.setOptions(choices);
        setDirty(true);
    }
    
    @Override
    public void addOption(Option option) {
        super.addOption(option);
        setDirty(true);
    }
    
    public DataLayer getDataLayer() {
        return dataLayer;
    }

    public void setDataLayer(DataLayer dataLayer) {
        this.dataLayer = dataLayer;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
