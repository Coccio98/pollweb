/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.business.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.business.model.Option;
import it.univaq.f4i.iw.pollweb.business.model.Question;
import it.univaq.f4i.iw.pollweb.data.dao.OptionDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class OptionDAOImpl extends DAO implements OptionDAO {
    
    private PreparedStatement sOptionsByQuestion, iOption, uOption, dOption,dOptionByQuestion;

    public OptionDAOImpl(DataLayer d) {
        super(d);
    }

    @Override
    public void destroy() throws DataException {
        try {
            sOptionsByQuestion.close();
            iOption.close();
            uOption.close();
            dOption.close();
            dOptionByQuestion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        super.destroy();
    }

    @Override
    public void init() throws DataException {
        try {
            this.sOptionsByQuestion = connection.prepareStatement("SELECT * FROM options WHERE id_question=? ORDER BY options.position");
            this.iOption = connection.prepareStatement("INSERT INTO options (text,position,id_question) VALUES (?,?,?)");
            this.uOption = connection.prepareStatement("UPDATE options SET text=?,position=? WHERE id=?");
            this.dOption = connection.prepareStatement("DELETE FROM options WHERE id=?");
            this.dOptionByQuestion = connection.prepareStatement("DELETE FROM options WHERE id_question=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing Data Layer", ex);
        }
    }
    
    

    @Override
    public List<Option> findByQuestion(Question question) throws DataException {
        List<Option> options = new ArrayList<>();
        ResultSet rs = null;
        try {
            sOptionsByQuestion.setLong(1, question.getId());
            rs = sOptionsByQuestion.executeQuery();
            while (rs.next()) {
                Option o = new Option();
                o.setId(rs.getLong("id"));
                o.setText(rs.getString("text"));
                o.setPosition(rs.getShort("position"));
                options.add(o);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load options from question", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return options;
    }

    @Override
    public void saveOrUpdate(Option option, long questionId) throws DataException {
        //text,position,id_question
        if(option.getId() == 0){
            try{
                iOption.setString(1, option.getText());
                iOption.setShort(2, option.getPosition());
                iOption.setLong(3, questionId);
                if (iOption.executeUpdate() == 0) {
                    System.out.println(iOption.toString());
                }
            }   catch (SQLException ex) {
                throw new DataException("Unable to save Option", ex);
            }
        }else {
            try{
                uOption.setString(1, option.getText());
                uOption.setShort(2, option.getPosition());
                uOption.setLong(3,option.getId());
                if (uOption.executeUpdate() == 0) {
                    System.out.println(uOption.toString());
                }
            }   catch (SQLException ex) {
                throw new DataException("Unable to save Option", ex);
            }
        }
    }

    @Override
    public void delete(ChoiceQuestion question, int del) throws DataException {
        try {
            List<Option> options = question.getOptions();
            for(int i=del+1; i < options.size(); i++){
                options.get(i).setPosition((short)(i-1));
                saveOrUpdate(options.get(i), question.getId());
            }
            dOption.setLong(1, options.get(del).getId());
            dOption.execute();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete option", ex);
        }
    }
    
    @Override
    public void deleteByQuestion(long question) throws DataException {
        try {
            dOptionByQuestion.setLong(1, question);
            dOptionByQuestion.execute();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete option", ex);
        }
    }
}
