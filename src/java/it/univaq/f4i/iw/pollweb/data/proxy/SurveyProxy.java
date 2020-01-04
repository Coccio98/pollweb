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
import it.univaq.f4i.iw.pollweb.data.model.User;
import it.univaq.f4i.iw.pollweb.data.impl.SurveyImpl;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andrea
 */
public class SurveyProxy extends SurveyImpl {
    
    private DataLayer dataLayer;
    private boolean dirty;
    private long managerId;

    public SurveyProxy(DataLayer dl) {
        super();
        super.setManager(null);
        super.setQuestions(null);
        super.setResponses(null);
        this.dataLayer = dl;
        this.dirty = false;
        this.managerId = 0;
    }
    
    @Override
    public void setActive(boolean active) {
        if (isActive() != active) {
            super.setActive(active);
            setDirty(true);
        }
    }

    @Override
    public void setManager(User manager) {
        super.setManager(manager);
        setDirty(true);
    }

    @Override
    public User getManager() {
        if (super.getManager() == null) {
            try {
                super.setManager(((Pollweb_DataLayer) this.dataLayer).getUserDAO().findById(this.managerId));
            } catch (DataException ex) {
                Logger.getLogger(SurveyProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getManager();
    }

    @Override
    public void setQuestions(List<Question> questions) {
        super.setQuestions(questions);
        setDirty(true);
    }

    @Override
    public List<Question> getQuestions() {
        if (super.getQuestions() == null || super.getQuestions().size() <= 0) {
            try {
                super.setQuestions(((Pollweb_DataLayer) this.dataLayer).getQuestionDAO().findBySurvey(this));
            } catch (DataException ex) {
                Logger.getLogger(SurveyProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getQuestions();
    }
    
    @Override
    public void setResponses(List<SurveyResponse> responses) {
        super.setResponses(responses);
        setDirty(true);
    }

    @Override
    public List<SurveyResponse> getResponses() {
        if (super.getResponses() == null) {
            try {
                super.setResponses(((Pollweb_DataLayer) this.dataLayer).getSurveyResponseDAO().findBySurvey(this));
            } catch (DataException ex) {
                Logger.getLogger(SurveyProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getResponses();
    }
    
    @Override
    public void setClosingText(String closingText) {
        if (! getClosingText().equals(closingText)) {
            super.setClosingText(closingText);
            setDirty(true);
        }
    }

    @Override
    public void setOpeningText(String openingText) {
        if (! getOpeningText().equals(openingText)) {
            super.setOpeningText(openingText);
            setDirty(true);
        }
    }

    @Override
    public void setTitle(String title) {
        if (! getTitle().equals(title)) {
            super.setTitle(title);
            setDirty(true);
        }
    }

    @Override
    public void setId(long id) {
        if (getId() != id) {
            super.setId(id);
            setDirty(true);
        }
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

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
        super.setManager(null);
    }
}
