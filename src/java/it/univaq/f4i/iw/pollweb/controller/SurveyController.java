/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.controller;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.model.Answer;
import it.univaq.f4i.iw.pollweb.data.model.ChoiceAnswer;
import it.univaq.f4i.iw.pollweb.data.impl.ChoiceQuestionImpl;
import it.univaq.f4i.iw.pollweb.data.model.DateAnswer;
import it.univaq.f4i.iw.pollweb.data.impl.DateQuestionImpl;
import it.univaq.f4i.iw.pollweb.data.model.NumberAnswer;
import it.univaq.f4i.iw.pollweb.data.impl.NumberQuestionImpl;
import it.univaq.f4i.iw.pollweb.data.impl.ParticipantImpl;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.model.ReservedSurvey;
import it.univaq.f4i.iw.pollweb.data.model.ShortTextAnswer;
import it.univaq.f4i.iw.pollweb.data.impl.ShortTextQuestionImpl;
import it.univaq.f4i.iw.pollweb.data.model.TextAnswer;
import it.univaq.f4i.iw.pollweb.data.impl.TextQuestionImpl;
import it.univaq.f4i.iw.pollweb.data.dao.ParticipantDAO;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyDAO;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyResponseDAO;
import it.univaq.f4i.iw.pollweb.data.dao.Pollweb_DataLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Pagliarini Andrea
 */
public class SurveyController extends PollWebBaseController {
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
    private void action_survey_list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Surveys");
            request.setAttribute("surveys", (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findAllNotReserved());
            res.activate("surveys.ftl.html", request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_survey(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException {
        try {
            SurveyDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = dao.findByIdOpen(n);
            if (survey != null) {
                TemplateResult res = new TemplateResult(getServletContext());
                if(request.getSession().getAttribute("p") != null){
                    request.setAttribute("page_title", survey.getTitle());
                    request.setAttribute("survey", survey);
                    if(request.getAttribute("send")!= null){
                        request.getSession().invalidate();
                    }
                }else{                    
                    if(survey.isReserved()){                    
                        request.setAttribute("login", "");
                        request.setAttribute("survey", survey);
                        request.setAttribute("page_title", "Login");                    
                    }else{                   
                        request.setAttribute("page_title", survey.getTitle());
                        request.setAttribute("survey", survey);
                    }
                }
                res.activate("survey.ftl.html", request, response);
            } else {                 
                request.setAttribute("message", "Unable to load survey");
                action_error(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_send_survey (HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException {
        SurveyResponse surveyResponse = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyResponseDAO().createSurveyResponse();
        List<Answer> answers = new ArrayList<>();
        List<String> error = new ArrayList<>();
        boolean choiceController = true;
        boolean mandatoryController= true;
        try{
            SurveyDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = dao.findByIdOpen(n);
            surveyResponse.setSurvey(survey);
            
            for (Question question: survey.getQuestions()){
                if (question.isMandatory() && (request.getParameter(question.getCode()) == null || request.getParameter(question.getCode()).equals(""))){
                    mandatoryController = false;
                    error.add("This question is mandatory!");
                } else {
                    error.add(null);
                }
                if (request.getParameter(question.getCode()) != null && (! request.getParameter(question.getCode()).equals("")) ) {
                    String text = request.getParameter(question.getCode());
                    Answer answer = null;
                    if (question instanceof ChoiceQuestionImpl) {
                        ChoiceQuestionImpl cq = (ChoiceQuestionImpl) question;
                        ChoiceAnswer ca = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getAnswerDAO().createChoiceAnswer();
                        for (String s: request.getParameterValues(question.getCode())) {
                                ca.getOptions().add(cq.getOption(Short.valueOf(s)));
                            }
                            answer = ca;
                        if (!((cq.getMaxNumberOfChoices()== 0 || request.getParameterValues(question.getCode()).length <= cq.getMaxNumberOfChoices()) && request.getParameterValues(question.getCode()).length >= cq.getMinNumberOfChoices())) {
                             choiceController=false;
                            request.setAttribute("error_Number", answers.size());                          
                        }
                    } else if (question instanceof DateQuestionImpl) {
                        DateAnswer da = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getAnswerDAO().createDateAnswer();
                        da.setAnswer(LocalDate.parse(text));
                        answer = da;
                    } else if (question instanceof NumberQuestionImpl) {
                        NumberAnswer na = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getAnswerDAO().createNumberAnswer();
                        na.setAnswer(Float.valueOf(text));
                        answer = na;
                    } else if (question instanceof TextQuestionImpl){
                        if(question instanceof ShortTextQuestionImpl){
                            ShortTextAnswer ta = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getAnswerDAO().createShortTextAnswer();
                            ta.setAnswer(text);
                            answer = ta;
                        } else {
                            TextAnswer ta = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getAnswerDAO().createTextAnswer();
                            ta.setAnswer(text);
                            answer = ta;
                        }
                    }
                    if (answer != null){
                        answer.setQuestion(question);
                        surveyResponse.getAnswers().add(answer);
                        answers.add(answer);
                    }
                }else{
                    answers.add(null);
                }
            }
            request.setAttribute("answer", answers);
            if (choiceController){
                if(mandatoryController){
                    if(surveyResponse.isValid()){
                    SurveyResponseDAO srdao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyResponseDAO();
                    srdao.saveOrUpdate(surveyResponse, n);
                    request.setAttribute("send", "");
                    if(request.getSession().getAttribute("p") != null){
                        ParticipantImpl p = (ParticipantImpl) request.getSession().getAttribute("p");
                        p.setSubmitted(true);
                        ((ParticipantDAO)((DataLayer) request.getAttribute("datalayer")).getDAO(ParticipantImpl.class)).saveOrUpdate(p,n);
                    }
                    } else{
                        request.setAttribute("message", "Invalid Value");
                        action_error(request, response);
                    }
                } else {
                    request.setAttribute("error", error);
                }
            }else{
                request.setAttribute("too_much", "Sellect more o less choice");
            }
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
            
            ex.printStackTrace();
        }
    }
    
    private void action_autentication(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email != null && password != null) {
            try{
                SurveyDAO dao =((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
                ReservedSurvey survey = (ReservedSurvey)dao.findByIdOpen(n);
                if (survey != null) {
                    for(ParticipantImpl p: survey.getParticipants()){
                        if((p.getEmail()).equals(email) && (p.getPassword()).equals(password)){
                            if (p.isSubmitted()){
                                request.setAttribute("error_autentication", "You have already answered!");
                            } else {
                                request.getSession().setAttribute("p", p);
                                request.setAttribute("p", p);
                            }
                        }
                    }
                    if (request.getSession().getAttribute("p") == null && request.getAttribute("error_autentication") == null){
                        request.setAttribute("error_autentication", "Incorrect credentials!");
                    }
                    
                } else {
                    request.setAttribute("login_error", "Incorrect credentials");
                    action_survey(request, response,n);
                }
            } catch (Exception ex) {
                request.setAttribute("message", "Data access exception: " + ex.getMessage());
                action_error(request, response);
            }
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int n;
        try {           
            HttpSession s = SecurityLayer.checkSession(request);
            if (s != null){               
                if(request.getParameter("logout") != null){
                    SecurityLayer.disposeSession(request);
                    response.sendRedirect("survey");
                }
            }
            if (request.getParameter("n") != null) {
                n = SecurityLayer.checkNumeric(request.getParameter("n"));
                if(request.getParameter("login") != null){                    
                    action_autentication(request, response, n);
                }
                if (request.getParameter("send") != null) {
                    action_send_survey(request, response, n);
                }
                action_survey(request, response, n);          
            }else{
                action_survey_list(request, response);
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
