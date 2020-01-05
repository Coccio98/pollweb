/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.proxy;

import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.impl.ShortTextQuestionImpl;

/**
 *
 * @author Pagliarini Andrea
 */
public class ShortTextQuestionProxy extends ShortTextQuestionImpl{
    private DataLayer dataLayer;
    private boolean dirty;
    
    public ShortTextQuestionProxy(DataLayer dataLayer){
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
    public void setMinLength(int minLength) {
        super.setMinLength(minLength);
        setDirty(true);
    }
    
    @Override
    public void setMaxLength(int maxLength) {
        super.setMaxLength(maxLength);
        setDirty(true);
    }
    
    @Override
    public void setPattern(String pattern) {
        super.setPattern(pattern);
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
