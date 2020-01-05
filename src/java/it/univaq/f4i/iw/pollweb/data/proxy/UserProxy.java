/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.proxy;

import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.impl.UserImpl;

/**
 *
 * @author Pagliarini Andrea
 */
public class UserProxy extends UserImpl{
    
    private DataLayer dataLayer;
    private boolean dirty;
    
    public UserProxy(DataLayer dl){
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
    public void setName(String name) {
        super.setName(name);
        setDirty(true);
    }
    
    @Override
    public void setSurname(String surname) {
        super.setSurname(surname);
        setDirty(true);
    }
    
    @Override
    public void setEmail(String email) {
        super.setEmail(email);
        setDirty(true);
    }
    
    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        setDirty(true);
    }
    
    @Override
    public void setType(Type type) {
        super.setType(type);
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
