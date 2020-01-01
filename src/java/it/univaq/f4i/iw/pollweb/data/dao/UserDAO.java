/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.pollweb.business.model.User;
import java.util.List;

/**
 *
 * @author andrea
 */
public interface UserDAO {
    
    List<User> findAll() throws DataException;
    User findById(long id) throws DataException;
    User findByEmail(String email) throws DataException;
    User findByEmailAndPassword(String email, String password) throws DataException;
    void saveOrUpdate(User user) throws DataException;
    void delete(User user) throws DataException;
}
