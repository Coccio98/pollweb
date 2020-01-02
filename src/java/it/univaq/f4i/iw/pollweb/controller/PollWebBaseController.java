/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.controller;

import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.pollweb.data.model.User;
import it.univaq.f4i.iw.pollweb.data.dao.Pollweb_DataLayer;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author andrea
 */
public abstract class PollWebBaseController extends HttpServlet {
    
    @Resource(name = "jdbc/pollweb")
    DataSource dataSource;
    
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    
    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        DataLayer datalayer = null;
        try {
            datalayer = new Pollweb_DataLayer(dataSource);
            datalayer.init();
            request.setAttribute("datalayer", datalayer);
            if (request.getSession().getAttribute("username") != null) {
                request.setAttribute("username", request.getSession().getAttribute("username"));
                request.setAttribute("userType", request.getSession().getAttribute("type"));
                request.setAttribute("administrator", User.Type.ADMINISTRATOR);
            }
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace(); //for debugging only
            (new FailureResult(getServletContext())).activate(
                    (ex.getMessage() != null || ex.getCause() == null) ? ex.getMessage() : ex.getCause().getMessage(), request, response);
        } finally {
            try {
                datalayer.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }
}
