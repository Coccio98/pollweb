/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.controller;

import it.univaq.f4i.iw.framework.result.CsvFileWriter;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import it.univaq.f4i.iw.pollweb.data.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.data.model.DateQuestion;
import it.univaq.f4i.iw.pollweb.data.model.NumberQuestion;
import it.univaq.f4i.iw.pollweb.data.model.Option;
import it.univaq.f4i.iw.pollweb.data.model.Participant;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.model.ShortTextQuestion;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.TextQuestion;
import it.univaq.f4i.iw.pollweb.data.model.User;
import it.univaq.f4i.iw.pollweb.data.dao.OptionDAO;
import it.univaq.f4i.iw.pollweb.data.dao.ParticipantDAO;
import it.univaq.f4i.iw.pollweb.data.dao.Pollweb_DataLayer;
import it.univaq.f4i.iw.pollweb.data.dao.QuestionDAO;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyDAO;
import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pagliarini Alberto
 */
public class MySurveyController extends PollWebBaseController {
    
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
            request.setAttribute("page_title", "My Surveys");
            User user = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getUserDAO()).findById((long)request.getSession().getAttribute("userid"));
            request.setAttribute("surveys", (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findByManager(user));
            res.activate("my_surveys.ftl.html", request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void edit_survey(HttpServletRequest request, HttpServletResponse response, int m) throws IOException, ServletException, TemplateManagerException {
         try {
            TemplateResult res = new TemplateResult(getServletContext());
            Survey survey =(((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findById(m);
            QuestionDAO questionDao= ((Pollweb_DataLayer) request.getAttribute("datalayer")).getQuestionDAO();
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                if(request.getParameter("up") != null && request.getParameter("down") == null && request.getParameter("del") == null && SecurityLayer.checkNumeric(request.getParameter("up")) > 0){
                    int up = SecurityLayer.checkNumeric(request.getParameter("up"));
                    survey.getQuestions().get(up).setPosition((short)(up-1));
                    survey.getQuestions().get(up-1).setPosition((short)up);
                    questionDao.update(survey.getQuestions().get(up), m);
                    questionDao.update(survey.getQuestions().get(up-1), m);
                }
                if(request.getParameter("down") != null && request.getParameter("up") == null && request.getParameter("del") == null && SecurityLayer.checkNumeric(request.getParameter("down")) < (survey.getQuestions().size()-1)){
                    int down = SecurityLayer.checkNumeric(request.getParameter("down"));
                    survey.getQuestions().get(down).setPosition((short)(down+1));
                    survey.getQuestions().get(down+1).setPosition((short)down);
                    questionDao.update(survey.getQuestions().get(down), m);
                    questionDao.update(survey.getQuestions().get(down+1), m);
                }
                if(request.getParameter("down") == null && request.getParameter("up") == null && request.getParameter("del") != null && !survey.getQuestions().isEmpty()){
                    int del=SecurityLayer.checkNumeric(request.getParameter("del"));
                    questionDao.delete(survey,del);
                }
                request.setAttribute("survey", (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findById(m));
                request.setAttribute("page_title", "Edit Survey");
                res.activate("my_survey_detail.ftl.html", request, response);
            } else {
                request.setAttribute("message", "This is not your survey");
                action_error(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void toggle_survey(HttpServletRequest request, HttpServletResponse response, int t) throws IOException, ServletException, TemplateManagerException {
        try {
            SurveyDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = dao.findById(t);
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                if (!survey.isActive()){
                    survey.setActive(true);
                } else {
                    survey.setActive(false);                      
                }
                dao.saveOrUpdate(survey);
            } else {
                request.setAttribute("message", "This is not your survey");
                action_error(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void add_question(HttpServletRequest request, HttpServletResponse response, int a) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Add Question");
            Survey survey =(((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findById(a);
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                request.setAttribute("position", survey.getQuestions().size());
                request.setAttribute("surveyId", a);
                res.activate("survey_creation.ftl.html", request, response);
            } else {
                request.setAttribute("message", "This is not your survey");
                action_error(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void export(HttpServletRequest request, HttpServletResponse response, int exp)throws IOException, ServletException, TemplateManagerException {
        try {
            SurveyDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = dao.findById(exp);
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                CsvFileWriter.writeCsvFile(request, survey);
            } else {
                request.setAttribute("message", "This is not your survey");
                action_error(request, response);
            }
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void add_user(HttpServletRequest request, HttpServletResponse response, int u)throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Add User");
            SurveyDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = dao.findById(u);
            ParticipantDAO participantDao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getParticipantDAO();
            if(survey.isReserved()){
                if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                    if(request.getParameter("send") != null){
                        if(request.getParameter("name")!=null && request.getParameter("email")!=null && request.getParameter("password")!=null &&
                                !request.getParameter("name").isEmpty() && !request.getParameter("email").isEmpty() && !request.getParameter("password").isEmpty()){
                            Participant participant = participantDao.createParticipant();
                            participant.setName(request.getParameter("name"));
                            participant.setEmail(request.getParameter("email"));
                            participant.setPassword(request.getParameter("password"));
                            participantDao.saveOrUpdate(participant, u);
                        } else {
                            request.setAttribute("error", "Fill all field");
                        }
                    }
                } else {
                    request.setAttribute("message", "This is not your survey");
                    action_error(request, response);
                }
            } else {
                    request.setAttribute("message", "This is not a reserved survey");
                    action_error(request, response);
                }
            request.setAttribute("u", survey);
            res.activate("my_surveys.ftl.html", request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    
    }
    
    private void edit_question(HttpServletRequest request, HttpServletResponse response, int m1, int m2)throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            SurveyDAO surveyDao =((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = surveyDao.findById(m1);
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                QuestionDAO questionDao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getQuestionDAO();
                Question question = questionDao.findById(m2);
                OptionDAO optionDao = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getOptionDAO());
                if (request.getParameter("del")!= null){
                    if (question.getQuestionType().equals("choice")){
                        optionDao.delete((ChoiceQuestion)question, SecurityLayer.checkNumeric(request.getParameter("del")));
                        request.setAttribute("question", questionDao.findById(m2));
                        request.setAttribute("m1", m1);
                        request.setAttribute("m2", m2);
                        request.setAttribute("page_title", "Edit Question");
                        res.activate("edit_question.ftl.html", request, response);
                    } else {
                        request.setAttribute("message", "You should not be here!");
                        action_error(request, response);
                    }
                } else if(request.getParameter("addOption")!= null &&
                    request.getParameter("option")!= null &&
                    !request.getParameter("option").isEmpty()){
                    if (question.getQuestionType().equals("choice")){
                        Option option = optionDao.createOption();
                        option.setText(request.getParameter("option"));
                        option.setPosition((short)((ChoiceQuestion) question).getOptions().size());
                        optionDao.saveOrUpdate(option, m2);                        
                        request.setAttribute("question", questionDao.findById(m2));
                        request.setAttribute("m1", m1);
                        request.setAttribute("m2", m2);
                        request.setAttribute("page_title", "Edit Question");
                        res.activate("edit_question.ftl.html", request, response);
                    } else {
                        request.setAttribute("message", "You should not be here!");
                        action_error(request, response);
                    }
                    
                } else if(request.getParameter("save")!= null){
                    if(request.getParameter("text") != null &&
                        request.getParameter("note") != null &&
                        request.getParameter("mandatory") != null &&
                        !request.getParameter("text").isEmpty()){
                        
                        question.setText(request.getParameter("text"));
                        question.setNote(request.getParameter("note"));
                        if(request.getParameter("mandatory").equals("yes")){
                            question.setMandatory(true);
                        }else{
                            question.setMandatory(false);
                        }
                        switch(question.getQuestionType()){
                            case "short text":
                                if(request.getParameter("maxValue") != null &&
                                    request.getParameter("minValue") != null &&
                                    request.getParameter("expression") != null){

                                    if(request.getParameter("maxValue").isEmpty() || SecurityLayer.checkNumeric(request.getParameter("maxValue")) > 240){
                                        ((ShortTextQuestion)question).setMaxLength(240);
                                    }else{
                                        ((ShortTextQuestion)question).setMaxLength(SecurityLayer.checkNumeric(request.getParameter("maxValue")));
                                    }
                                    if(request.getParameter("minValue").isEmpty()){
                                        ((ShortTextQuestion)question).setMinLength(0);
                                    }else{
                                        ((ShortTextQuestion)question).setMinLength(SecurityLayer.checkNumeric(request.getParameter("minValue")));
                                    }
                                    if(request.getParameter("expression").isEmpty()){
                                        ((ShortTextQuestion)question).setPattern(".");
                                    }else{
                                        ((ShortTextQuestion)question).setPattern(request.getParameter("expression"));
                                    }
                                }
                                break;
                            case "long text":
                                if(request.getParameter("maxValue") != null &&
                                    request.getParameter("minValue") != null){

                                    if(request.getParameter("maxValue").isEmpty() || SecurityLayer.checkNumeric(request.getParameter("maxValue")) > 10000){
                                        ((TextQuestion)question).setMaxLength(10000);
                                    }else{
                                        ((TextQuestion)question).setMaxLength(SecurityLayer.checkNumeric(request.getParameter("maxValue")));
                                    }
                                    if(request.getParameter("minValue").isEmpty()){
                                        ((TextQuestion)question).setMinLength(0);
                                    }else{
                                        ((TextQuestion)question).setMinLength(SecurityLayer.checkNumeric(request.getParameter("minValue")));
                                    }
                                }
                                break;
                            case "number":
                                if(request.getParameter("maxValue") != null &&
                                    request.getParameter("minValue") != null){

                                    if(request.getParameter("maxValue").isEmpty()){
                                        ((NumberQuestion)question).setMaxValue(Integer.MAX_VALUE);
                                    }else{
                                        ((NumberQuestion)question).setMaxValue(SecurityLayer.checkNumeric(request.getParameter("maxValue")));
                                    }
                                    if(request.getParameter("minValue").isEmpty()){
                                        ((NumberQuestion)question).setMinValue(Integer.MIN_VALUE);
                                    }else{
                                        ((NumberQuestion)question).setMinValue(SecurityLayer.checkNumeric(request.getParameter("minValue")));
                                    }
                                }
                                break;
                            case "date":
                                if(request.getParameter("maxValue") != null &&
                                    request.getParameter("minValue") != null){
                                    if(!request.getParameter("maxValue").isEmpty()){
                                        LocalDate localDate = LocalDate.parse(request.getParameter("maxValue"));
                                        ((DateQuestion)question).setMaxDate(localDate);
                                    }
                                    if(!request.getParameter("minValue").isEmpty()){
                                        LocalDate localDate = LocalDate.parse(request.getParameter("minValue"));
                                        ((DateQuestion)question).setMinDate(localDate);
                                    }
                                }
                                break;
                            case "choice":
                                if(request.getParameter("maxValue") != null &&
                                    request.getParameter("number") != null){

                                    if(!request.getParameter("maxValue").isEmpty() && request.getParameter("maxValue") != "0"){
                                        ((ChoiceQuestion)question).setMaxNumberOfChoices((short)SecurityLayer.checkNumeric(request.getParameter("maxValue")));
                                    } else {
                                        ((ChoiceQuestion)question).setMaxNumberOfChoices((short)SecurityLayer.checkNumeric(request.getParameter("number")));
                                    }
                                    if(!request.getParameter("minValue").isEmpty()){
                                        ((ChoiceQuestion)question).setMinNumberOfChoices((short)SecurityLayer.checkNumeric(request.getParameter("minValue")));
                                    }                                    
                                } else {
                                    request.setAttribute("error_creation", "Fill all mandatory fields");
                                }
                                break;
                            default:
                                request.setAttribute("message", "Invalid Type");
                                action_error(request, response);
                        }
                        questionDao.update(question, m1);
                        request.setAttribute("page_title", "Edit Survey");
                        request.setAttribute("survey", survey);
                        res.activate("my_survey_detail.ftl.html", request, response);
                    } else {
                        request.setAttribute("error_creation", "Fill all mandatory fields");
                        action_error(request, response);
                    }
                    
                } else {                   
                    request.setAttribute("question", question);
                    request.setAttribute("m1", m1);
                    request.setAttribute("m2", m2);
                    request.setAttribute("page_title", "Edit Question");
                    res.activate("edit_question.ftl.html", request, response);
                }                    
            } else {
                request.setAttribute("message", "This is not your survey");
                action_error(request, response);
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
                if (request.getParameter("m") != null) {
                    edit_survey(request, response, SecurityLayer.checkNumeric(request.getParameter("m")));
                } else {
                    if (request.getParameter("t") != null) {
                        toggle_survey(request, response, SecurityLayer.checkNumeric(request.getParameter("t")));
                    } else if (request.getParameter("exp") != null){
                            export(request, response, SecurityLayer.checkNumeric(request.getParameter("exp")));
                    }
                    if (request.getParameter("a") != null) {
                        add_question(request, response, SecurityLayer.checkNumeric(request.getParameter("a")));
                    } else if(request.getParameter("u") != null){
                        add_user(request, response, SecurityLayer.checkNumeric(request.getParameter("u")));
                    } else if(request.getParameter("m1") != null && request.getParameter("m2") != null){
                        edit_question(request, response, SecurityLayer.checkNumeric(request.getParameter("m1")), SecurityLayer.checkNumeric(request.getParameter("m2")));
                    }else{
                        action_default(request, response);
                    }
                }
            } else {
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
