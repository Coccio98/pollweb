/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.pollweb.data.impl.ChoiceQuestionImpl;
import it.univaq.f4i.iw.pollweb.data.impl.OptionImpl;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import java.util.List;

/**
 *
 * @author andrea
 */
public interface OptionDAO {
    
    List<OptionImpl> findByQuestion(Question question) throws DataException;
    void saveOrUpdate(OptionImpl option, long questionId) throws DataException;
    void delete(ChoiceQuestionImpl question, int del) throws DataException;
    void deleteByQuestion(long question) throws DataException;
}
