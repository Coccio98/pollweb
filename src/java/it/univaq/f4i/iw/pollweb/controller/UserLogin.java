/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.controller;

import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import it.univaq.f4i.iw.pollweb.data.dao.Pollweb_DataLayer;
import it.univaq.f4i.iw.pollweb.data.model.User;
import it.univaq.f4i.iw.pollweb.data.dao.UserDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pagliarini Andrea
 */
public class UserLogin extends PollWebBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    //autenticazione del manager o dell'admin
    private void action_autentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        //controllo che i campi siano inseriti
        if (email != null && password != null) {
            try{
                UserDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getUserDAO();
                User user = dao.findByEmailAndPassword(email,password);
                //verifica che l'utente essita
                if (user != null) {
                    SecurityLayer.createSession(request, email, user.getId(), user.getType());
                    response.sendRedirect("survey");
                } else {
                    request.setAttribute("error_autentication", "Incorrect credentials");
                    action_default(request, response);
                }
            } catch (Exception ex) {
                request.setAttribute("message", "Data access exception: " + ex.getMessage());
                action_error(request, response);
            }
        }
    
    }
    
    //apre la schermata di login
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
                TemplateResult res = new TemplateResult(getServletContext());
                request.setAttribute("login", "login");
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
            //se un partecipante dovesse essere loggato quando si trova in questo punto viene fatto il log out del suddetto
            if(request.getSession().getAttribute("p") != null){
                request.getSession().removeAttribute("p");
            }
            //controllo se un manager NON ha effettuato un login
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
