/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.proxy;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.dao.QuestionDAO;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.impl.TextAnswerImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pagliarini Alberto
 */
public class TextAnswerProxy extends TextAnswerImpl{
    
    private DataLayer dataLayer;
    private boolean dirty;
    
    public TextAnswerProxy(DataLayer dataLayer) {
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
                super.setQuestion(((QuestionDAO) this.dataLayer.getDAO(Question.class)).findByAnswer(this.getId()));
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
