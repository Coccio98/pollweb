/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.proxy;

import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.impl.OptionImpl;

/**
 *
 * @author Pagliarini Alberto
 */
public class OptionProxy extends OptionImpl{
    private DataLayer dataLayer;
    private boolean dirty;
    
    public OptionProxy(DataLayer dl){
        super();
        this.dataLayer = dl;
        this.dirty = false;
    }
    
    @Override
    public void setId(long id) {
        super.setId(id);
        setDirty(true);
    }
    
    @Override
    public void setPosition(short number) {
        super.setPosition(number);
        setDirty(true);
    }
    
    @Override
    public void setText(String text) {
        super.setText(text);
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
