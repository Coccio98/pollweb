/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.pollweb.business.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.business.model.Option;
import it.univaq.f4i.iw.pollweb.business.model.Question;
import java.util.List;

/**
 *
 * @author andrea
 */
public interface OptionDAO {
    
    List<Option> findByQuestion(Question question) throws DataException;
    void saveOrUpdate(Option option, long questionId) throws DataException;
    void delete(ChoiceQuestion question, int del) throws DataException;
    void deleteByQuestion(long question) throws DataException;
}
