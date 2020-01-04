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
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pagliarini Alberto
 */
public class NewUserController extends PollWebBaseController {
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        } else {
            (new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("users", (((Pollweb_DataLayer) request.getAttribute("datalayer")).getUserDAO()).findAll());
            request.setAttribute("page_title", "Add New User");
            res.activate("new_user.ftl.html", request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void add_user(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            User user = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getUserDAO()).createUser();
            if(request.getParameter("name")!= null && !request.getParameter("name").isEmpty() &&
                    request.getParameter("surname")!= null && !request.getParameter("surname").isEmpty() &&
                    request.getParameter("email")!= null && !request.getParameter("email").isEmpty() &&
                    request.getParameter("password")!= null && !request.getParameter("password").isEmpty()){
                user.setName(request.getParameter("name"));
                user.setSurname(request.getParameter("surname"));
                user.setEmail(request.getParameter("email"));
                user.setPassword(request.getParameter("password"));
                (((Pollweb_DataLayer) request.getAttribute("datalayer")).getUserDAO()).saveOrUpdate(user);
            } else {
                request.setAttribute("error", "Fill all the fields!");
            }           
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (SecurityLayer.checkSession(request) != null) {
                if(request.getSession().getAttribute("type") == User.Type.ADMINISTRATOR){
                    if(request.getParameter("add") != null){
                        add_user(request,response);
                    }
                    action_default(request, response);
                } else{
                    request.setAttribute("message", "You can not stay here!");
                    action_error(request, response);
                }
            }else {
                request.setAttribute("message", "You must be logged");
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
