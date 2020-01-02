/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.data.dao;

import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.pollweb.data.model.User;
import it.univaq.f4i.iw.pollweb.data.proxy.UserProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrea
 */
public class UserDAO_MySQL extends DAO implements UserDAO {

    private PreparedStatement sUser,sUserById, sUserByEmailAndPassword;
    private PreparedStatement iUser, uUser, dUser;
    
    public UserDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void destroy() throws DataException {
        try {
            sUser.close();
            sUserById.close();
            sUserByEmailAndPassword.close();
            iUser.close();
            uUser.close();
            dUser.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 
        super.destroy();
    }
    
    @Override
    public UserProxy createUser() {
        return new UserProxy(getDataLayer());
    }

    @Override
    public void init() throws DataException {
        try {
            sUser = connection.prepareStatement("SELECT * FROM users");
            sUserById = connection.prepareStatement("SELECT * FROM users WHERE id=?");
            sUserByEmailAndPassword = connection.prepareStatement("SELECT * FROM users WHERE email=? AND password=?");
            iUser = connection.prepareStatement("INSERT INTO users (name,surname,email,password,role) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uUser = connection.prepareStatement("UPDATE users SET name=?,surname=?,email,password=?,role=? WHERE id=?");
            dUser = connection.prepareStatement("DELETE FROM users WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing Data Layer", ex);
        }
    }
    
    private User createUser(ResultSet rs) throws DataException{
        try {
            UserProxy u = createUser();
            u.setId(rs.getLong("id"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            u.setName(rs.getString("name"));
            u.setSurname(rs.getString("surname"));
            if (rs.getString("role").equals("administrator")) {
                u.setType(User.Type.ADMINISTRATOR);
            } else {
                u.setType(User.Type.RESPONSIBLE);
            }
            return u;
        } catch (SQLException ex) {
            throw new DataException("Unable to create User form ResultSet", ex);
        } 
    }
    
    @Override
    public List<User> findAll() throws DataException {
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try {
            rs = sUser.executeQuery();
            while (rs.next()) {
                User user = createUser(rs);
                users.add(user);
            }
            return users;
        } catch (SQLException ex) {
            throw new DataException("Unable to load user by id", ex);
        }
    }
    
    @Override
    public User findById(long id) throws DataException {
        ResultSet rs = null;
        User user = null;
        try {
            sUserById.setLong(1, id);
            rs = sUserById.executeQuery();
            if (rs.next()) {
                user = createUser(rs);
            }
            return user;
        } catch (SQLException ex) {
            throw new DataException("Unable to load user by id", ex);
        }
    }
    
    
    @Override
    public User findByEmailAndPassword(String email, String password) throws DataException {
        ResultSet rs = null;
        User user = null;
        try{
            sUserByEmailAndPassword.setString(1, email);
            sUserByEmailAndPassword.setString(2, password);
            rs = sUserByEmailAndPassword.executeQuery();
            if (rs.next()) {
                user = createUser(rs);
            }
            return user;
            
        } catch (SQLException ex) {
            throw new DataException("Unable to load user by email and password", ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void saveOrUpdate(User user) throws DataException {
        // INSERT
        if (user.getId() == 0) {
            try {
                iUser.setString(1, user.getName());
                iUser.setString(2, user.getSurname());
                iUser.setString(3, user.getEmail());
                // TODO encode password
                iUser.setString(4, user.getPassword());
                if (user.getType() == User.Type.ADMINISTRATOR) {
                    iUser.setString(5, "administrator");
                } else {
                    iUser.setString(5, "responsible");
                }
                iUser.execute();
            } catch (SQLException ex) {
                throw new DataException("Unable to save User", ex);
            }
        // UPDATE
        } else {
            
        }
    }

    @Override
    public void delete(User user) throws DataException {
        try {
            dUser.setLong(1, user.getId());
            dUser.execute();
        } catch (SQLException ex) {
            throw new DataException("Unable to delete user", ex);
        }
    }
}


    
