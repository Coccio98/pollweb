/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.proxy;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.dao.Pollweb_DataLayer;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.impl.ShortTextAnswerImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pagliarini Andrea
 */
public class ShortTextAnswerProxy extends ShortTextAnswerImpl{
    
    private DataLayer dataLayer;
    private boolean dirty;
    
    public ShortTextAnswerProxy(DataLayer dataLayer) {
        super();
        super.setQuestion(null);
        this.dataLayer = dataLayer;
        this.dirty = false;
    }
    
    @Override
    public void setId(long id){
        super.setId(id);
        setDirty(true);
    }
    
    @Override
    public void setQuestion(Question question) {
        super.setQuestion(question);
        setDirty(true);
    }
    
    @Override
    public Question getQuestion() {
        if (super.getQuestion() == null) {
            try {
                super.setQuestion(((Pollweb_DataLayer) this.dataLayer).getQuestionDAO().findByAnswer(this.getId()));
            } catch (DataException ex) {
                Logger.getLogger(SurveyResponseProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getQuestion();
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
