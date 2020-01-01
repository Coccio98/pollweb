/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.business.controller;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import it.univaq.f4i.iw.pollweb.business.model.Survey;
import it.univaq.f4i.iw.pollweb.business.model.User;
import it.univaq.f4i.iw.pollweb.business.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyDAO;
import it.univaq.f4i.iw.pollweb.data.dao.UserDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pagliarini Andrea
 */
public class LoginController extends PollWebBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_autentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email != null && password != null) {
            try{
                UserDAO dao = (UserDAO) ((DataLayer) request.getAttribute("datalayer")).getDAO(User.class);
                User user = dao.findByEmailAndPassword(email,password);
                if (user != null) {
                    SecurityLayer.createSession(request, email, user.getId(), user.getType());
                    response.sendRedirect("survey");
                } else {
                    request.setAttribute("login_error", "Incorrect credentials");
                    action_default(request, response);
                }
            } catch (Exception ex) {
                request.setAttribute("message", "Data access exception: " + ex.getMessage());
                action_error(request, response);
            }
        }
    
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
                TemplateResult res = new TemplateResult(getServletContext());
                request.setAttribute("page_title", "Login");
                res.activate("login.ftl.html", request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if(SecurityLayer.checkSession(request) == null){            
                if (request.getParameter("email") != null && request.getParameter("password") != null) {
                    action_autentication(request, response);
                } else {
                    action_default(request, response);
                }
            } else  {
                request.setAttribute("message", "Already logged");
                action_error(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //out.close();
        }
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "View and compile a Survey";
    }// </editor-fold>
}
