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
import it.univaq.f4i.iw.pollweb.business.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.business.model.DateQuestion;
import it.univaq.f4i.iw.pollweb.business.model.NumberQuestion;
import it.univaq.f4i.iw.pollweb.business.model.Question;
import it.univaq.f4i.iw.pollweb.business.model.ReservedSurvey;
import it.univaq.f4i.iw.pollweb.business.model.ShortTextQuestion;
import it.univaq.f4i.iw.pollweb.business.model.TextQuestion;
import it.univaq.f4i.iw.pollweb.business.model.Option;
import it.univaq.f4i.iw.pollweb.business.model.User;
import it.univaq.f4i.iw.pollweb.data.dao.OptionDAO;
import it.univaq.f4i.iw.pollweb.data.dao.QuestionDAO;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyDAO;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyResponseDAO;
import it.univaq.f4i.iw.pollweb.data.dao.UserDAO;
import it.univaq.f4i.iw.pollweb.data.dao.Pollweb_DataLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Pagliarini Andrea
 */
public class SurveyCreationController extends PollWebBaseController {
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
            request.setAttribute("page_title", "Create New Surveys");
            res.activate("survey_creation.ftl.html", request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_create_survey(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            User user = ((UserDAO)((DataLayer) request.getAttribute("datalayer")).getDAO(User.class)).findById((long)request.getSession().getAttribute("userid"));
            TemplateResult res = new TemplateResult(getServletContext());
            if((request.getParameter("title") != null && !request.getParameter("title").isEmpty()) && 
                (request.getParameter("openT") != null && !request.getParameter("openT").isEmpty())&&
                (request.getParameter("closeT") != null && !request.getParameter("closeT").isEmpty())&&
                request.getParameter("reserved") != null){
                if(!request.getParameter("reserved").equals("yes")){
                    Survey survey = ((SurveyDAO) ((DataLayer) request.getAttribute("datalayer")).getDAO(Survey.class)).createSurvey();
                    survey.setTitle(request.getParameter("title"));
                    survey.setOpeningText(request.getParameter("openT"));
                    survey.setClosingText(request.getParameter("closeT"));
                    survey.setManager(user);
                    ((SurveyDAO) ((DataLayer) request.getAttribute("datalayer")).getDAO(Survey.class)).saveOrUpdate(survey);
                    request.setAttribute("surveyId", survey.getId());
                } else {
                    ReservedSurvey survey = ((SurveyDAO) ((DataLayer) request.getAttribute("datalayer")).getDAO(Survey.class)).createReservedSurvey();
                    survey.setTitle(request.getParameter("title"));
                    survey.setOpeningText(request.getParameter("openT"));
                    survey.setClosingText(request.getParameter("closeT"));
                    survey.setManager(user);
                    ((SurveyDAO) ((DataLayer) request.getAttribute("datalayer")).getDAO(Survey.class)).saveOrUpdate(survey);
                    request.setAttribute("surveyId", survey.getId());
                }
                request.setAttribute("position", 0);
                request.setAttribute("page_title", "Add Question");
                res.activate("survey_creation.ftl.html", request, response);
            }else{
                request.setAttribute("error_creation", "Fill all the fields");
                action_default(request, response);
            }
            
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_add_question(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try{
            TemplateResult res = new TemplateResult(getServletContext());
            if(request.getParameter("text") != null &&
                request.getParameter("note") != null &&
                request.getParameter("mandatory") != null &&
                !request.getParameter("text").isEmpty()){
                Question q = new Question();
                switch(request.getParameter("type")){
                    case "shortText":
                        q = new ShortTextQuestion();
                        break;
                    case "longText":
                        q = new TextQuestion();
                        break;
                    case "number":
                        q = new NumberQuestion();
                    break;
                    case "date":
                        q = new DateQuestion();
                    break;
                    case "choice":
                        q = new ChoiceQuestion();
                    break;
                default:
                    request.setAttribute("message", "Invalid Type");
                    action_error(request, response);
                }
                
                q.setText(request.getParameter("text"));
                q.setNote(request.getParameter("note"));
                if(request.getParameter("mandatory").equals("yes")){
                    q.setMandatory(true);
                }else{
                    q.setMandatory(false);
                }
                q.setPosition((short)SecurityLayer.checkNumeric(request.getParameter("position")));
                
                ((QuestionDAO) ((DataLayer) request.getAttribute("datalayer")).getDAO(Question.class)).newQuestion(q, SecurityLayer.checkNumeric(request.getParameter("surveyId")));
                request.setAttribute("questionId", q.getId());
                request.setAttribute("type", request.getParameter("type"));
            } else {
                request.setAttribute("error_creation", "Fill all mandatory fields");
            }
            
            request.setAttribute("position", request.getParameter("position"));
            request.setAttribute("surveyId", request.getParameter("surveyId"));
            
            if(request.getAttribute("error_creation") != null){               
                request.setAttribute("page_title", "Add Question");                
            } else {                
                request.setAttribute("page_title", "Question Rules");
            }
            res.activate("survey_creation.ftl.html", request, response);

        }catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void action_update_question(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try{
            TemplateResult res = new TemplateResult(getServletContext());
            QuestionDAO dao = (QuestionDAO) ((DataLayer) request.getAttribute("datalayer")).getDAO(Question.class);
            int max;
            int min;
            if(request.getParameter("maxValue") != null &&
                request.getParameter("minValue") != null){
                switch(request.getParameter("type")){
                    case "shortText":
                        if(request.getParameter("expression") != null){
                            if(request.getParameter("maxValue").isEmpty() || SecurityLayer.checkNumeric(request.getParameter("maxValue")) > 240){
                                max = 240;
                            } else {
                                max = SecurityLayer.checkNumeric(request.getParameter("maxValue"));
                            }
                            if(request.getParameter("minValue").isEmpty()){
                                min = 0;
                            }else{
                                min = SecurityLayer.checkNumeric(request.getParameter("minValue"));
                            }
                            if (max >= min){
                                ShortTextQuestion q = new ShortTextQuestion();
                                q.setId(SecurityLayer.checkNumeric(request.getParameter("questionId")));
                                q.setMaxLength(max);
                                q.setMinLength(min);                      
                                if(request.getParameter("expression").isEmpty()){
                                    q.setPattern(".");
                                }else{
                                    q.setPattern(request.getParameter("expression"));
                                }
                                q.setPosition((short)SecurityLayer.checkNumeric(request.getParameter("position")));
                                dao.newQuestion(q, SecurityLayer.checkNumeric(request.getParameter("surveyId")));
                                request.setAttribute("page_title", "Add Question");
                            } else {
                                request.setAttribute("error_creation", "Max Length must be bigger than Min Length");
                            }
                        }
                        break;
                    case "longText":{                       
                        if(request.getParameter("maxValue").isEmpty() || SecurityLayer.checkNumeric(request.getParameter("maxValue")) > 10000){
                            max = 10000;
                        }else{
                            max = SecurityLayer.checkNumeric(request.getParameter("maxValue"));
                        }
                        if(request.getParameter("minValue").isEmpty()){
                            min = 0;
                        }else{
                            min = SecurityLayer.checkNumeric(request.getParameter("minValue"));
                        }
                        if (max >= min){
                            TextQuestion q = new TextQuestion();
                            q.setId(SecurityLayer.checkNumeric(request.getParameter("questionId")));
                            q.setMaxLength(max);
                            q.setMinLength(min);
                            q.setPosition((short)SecurityLayer.checkNumeric(request.getParameter("position")));
                            dao.newQuestion(q, SecurityLayer.checkNumeric(request.getParameter("surveyId")));
                            request.setAttribute("page_title", "Add Question");
                        } else {
                            request.setAttribute("error_creation", "Max Length must be bigger than Min Length");
                        }
                    }
                        break;
                    case "number":{                           
                        if(request.getParameter("maxValue").isEmpty()){
                            max = Integer.MAX_VALUE;
                        }else{
                            max = SecurityLayer.checkNumeric(request.getParameter("maxValue"));
                        }
                        if(request.getParameter("minValue").isEmpty()){
                            min = Integer.MIN_VALUE;
                        }else{
                            min = SecurityLayer.checkNumeric(request.getParameter("minValue"));
                        }
                        if (max >= min){
                            NumberQuestion q = new NumberQuestion();
                            q.setId(SecurityLayer.checkNumeric(request.getParameter("questionId")));
                            q.setMaxValue(max);
                            q.setMinValue(min);
                            q.setPosition((short)SecurityLayer.checkNumeric(request.getParameter("position")));
                            dao.newQuestion(q, SecurityLayer.checkNumeric(request.getParameter("surveyId")));
                            request.setAttribute("page_title", "Add Question");
                        } else {
                            request.setAttribute("error_creation", "Max Value must be bigger than Min Value");
                        }
                    }
                        break;
                    case "date":{                       
                        LocalDate localDateMax;
                        LocalDate localDateMin;
                        if(!request.getParameter("maxValue").isEmpty()){
                            localDateMax = LocalDate.parse(request.getParameter("maxValue"));                            
                        } else {
                            localDateMax = LocalDate.of(9999, Month.DECEMBER, 31);
                        }
                        if(!request.getParameter("minValue").isEmpty()){
                            localDateMin = LocalDate.parse(request.getParameter("minValue"));                           
                        } else {
                            localDateMin = LocalDate.of(1, Month.JANUARY, 1);
                        }
                        if(!(localDateMin.isAfter(localDateMax))){
                            DateQuestion q = new DateQuestion();
                            q.setId(SecurityLayer.checkNumeric(request.getParameter("questionId")));
                            q.setMaxDate(localDateMax);
                            q.setMinDate(localDateMin);
                            q.setPosition((short)SecurityLayer.checkNumeric(request.getParameter("position")));
                            dao.newQuestion(q, SecurityLayer.checkNumeric(request.getParameter("surveyId")));
                            request.setAttribute("page_title", "Add Question");
                        } else {
                            request.setAttribute("error_creation", "Max Date must be bigger than Min Date");
                        }
                    }
                        break;
                    case "choice":
                        if(request.getParameter("number") != null &&
                            !request.getParameter("number").isEmpty()){                            
                            if(!request.getParameter("maxValue").isEmpty() && request.getParameter("maxValue") != "0" && 
                                    SecurityLayer.checkNumeric(request.getParameter("number")) > SecurityLayer.checkNumeric(request.getParameter("maxValue"))){
                                max = SecurityLayer.checkNumeric(request.getParameter("maxValue"));
                            } else {
                                max = SecurityLayer.checkNumeric(request.getParameter("number"));
                            }
                            if(!request.getParameter("minValue").isEmpty()){
                                min =(short)SecurityLayer.checkNumeric(request.getParameter("minValue"));
                            } else {
                                min = 0;
                            }
                            if (max >= min){
                                ChoiceQuestion q = new ChoiceQuestion();
                                q.setId(SecurityLayer.checkNumeric(request.getParameter("questionId")));
                                q.setMaxNumberOfChoices((short)max);
                                q.setMinNumberOfChoices((short)min);
                                q.setPosition((short)SecurityLayer.checkNumeric(request.getParameter("position")));
                                dao.newQuestion(q, SecurityLayer.checkNumeric(request.getParameter("surveyId")));
                                request.setAttribute("choices", request.getParameter("number"));
                                request.setAttribute("questionId", q.getId());
                                request.setAttribute("type", request.getParameter("type"));
                                request.setAttribute("page_title", "Add Option");
                            } else {
                                request.setAttribute("error_creation", "Max Choice must be bigger than Min Choice");
                            }                            
                        } else {
                            request.setAttribute("error_creation", "Fill all mandatory fields");
                        }
                        break;
                    default:
                        request.setAttribute("message", "Invalid Type");
                        action_error(request, response);
                }
                request.setAttribute("surveyId", request.getParameter("surveyId"));
                int p = SecurityLayer.checkNumeric(request.getParameter("position"));
                if (request.getAttribute("error_creation") == null){               
                    p++;
                } else{
                    request.setAttribute("questionId", request.getParameter("questionId"));
                    request.setAttribute("type", request.getParameter("type"));
                }                            
                request.setAttribute("position", p);
                res.activate("survey_creation.ftl.html", request, response);
            } else {
                request.setAttribute("message", "Something goes wrong!");
                action_error(request, response);
            }
        }catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    
    }
    
    private void action_add_choices(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try{
            TemplateResult res = new TemplateResult(getServletContext());
            List<Option> options = new ArrayList<>();
            short i=0;
            for (String s: request.getParameterValues("option")) {
                Option o = new Option();
                o.setPosition(i);
                o.setText(s);
                options.add(o);
                i++;
            }
            for(Option op: options){
                ((OptionDAO) ((DataLayer) request.getAttribute("datalayer")).getDAO(Option.class)).saveOrUpdate(op, SecurityLayer.checkNumeric(request.getParameter("questionId")));
            }
            request.setAttribute("surveyId", request.getParameter("surveyId"));
            request.setAttribute("position", request.getParameter("position"));
            request.setAttribute("page_title", "Add Question");
            res.activate("survey_creation.ftl.html", request, response);
        }catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("create") != null) {
                    action_create_survey(request, response);    
                } else {
                    if (request.getParameter("addQuestion") != null){
                        action_add_question(request, response);
                    } else {
                        if(request.getParameter("saveQuestion")!= null){
                            action_update_question(request, response);
                        } else {
                            if(request.getParameter("saveOptions")!= null){
                                action_add_choices(request, response);
                            } else {
                                action_default(request, response);
                            }
                        }
                    }
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