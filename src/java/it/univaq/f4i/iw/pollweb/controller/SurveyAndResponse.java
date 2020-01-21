/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.controller;

import it.univaq.f4i.iw.framework.result.AdditionalFunctions;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import it.univaq.f4i.iw.pollweb.data.dao.AnswerDAO;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import it.univaq.f4i.iw.pollweb.data.model.Answer;
import it.univaq.f4i.iw.pollweb.data.model.ChoiceAnswer;
import it.univaq.f4i.iw.pollweb.data.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.data.model.DateAnswer;
import it.univaq.f4i.iw.pollweb.data.model.DateQuestion;
import it.univaq.f4i.iw.pollweb.data.model.NumberAnswer;
import it.univaq.f4i.iw.pollweb.data.model.NumberQuestion;
import it.univaq.f4i.iw.pollweb.data.model.Participant;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.model.ReservedSurvey;
import it.univaq.f4i.iw.pollweb.data.model.ShortTextAnswer;
import it.univaq.f4i.iw.pollweb.data.model.ShortTextQuestion;
import it.univaq.f4i.iw.pollweb.data.model.TextAnswer;
import it.univaq.f4i.iw.pollweb.data.model.TextQuestion;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyDAO;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyResponseDAO;
import it.univaq.f4i.iw.pollweb.data.dao.Pollweb_DataLayer;
import java.io.IOException;
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
public class SurveyAndResponse extends PollWebBaseController {
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
            //genera la lista di tutti i sondaggi pubblici aperti
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
            //verifica che il sondaggio esista
            if (survey != null) {
                TemplateResult res = new TemplateResult(getServletContext());
                //verifica se un partecipante sia loggato in questo punto,
                //se lo è viene sloggato
                if(request.getSession().getAttribute("p") != null){                    
                    request.getSession().removeAttribute("p");
                    request.setAttribute("surveys", (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findAllNotReserved());
                    res.activate("surveys.ftl.html", request, response);                  
                }else{
                    //controllo sul sondaggio, se è pubblico si viene postati alla schermata di compilazione
                    //se privato si viene portati alla schermata di log in
                    if(survey.isReserved()){
                        //attributo utilizzato come action del form del log in per specificare il fatto che il sia un partecipante
                        //e non un manager
                        request.setAttribute("login", "survey?n=" + survey.getId());
                        request.setAttribute("survey", survey);
                        request.setAttribute("page_title", "Login");
                        res.activate("login.ftl.html", request, response);
                    }else{                   
                        request.setAttribute("page_title", survey.getTitle());
                        request.setAttribute("survey", survey);
                        res.activate("survey.ftl.html", request, response);
                    }
                }                
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
        TemplateResult res = new TemplateResult(getServletContext());
        SurveyResponse surveyResponse = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyResponseDAO().createSurveyResponse();
        //lista che salva le risposte date, utilizzata in caso di errore per riempire i campi vuoti
        //e per il racap terminata la compilazzione
        List<Answer> answers = new ArrayList<>();
        //lista che immagazzina gli errori riscontrati durante l'invio della risposta
        List<String> errors = new ArrayList<>();
        //variabile per il controllo dell'errone della domanda
        boolean errorController= true;
        try{
            SurveyDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = dao.findByIdOpen(n);
            surveyResponse.setSurvey(survey);
            AnswerDAO anseswerDao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getAnswerDAO();
            //for per creare una risposta a ogni domanda
            for (Question question: survey.getQuestions()){
                //verifica che una domanda obbligatoria venga riempita
                //se no imposta il controller a falso e aggiunge un errore alla lista
                if (question.isMandatory() && (request.getParameter(question.getCode()) == null || request.getParameter(question.getCode()).isEmpty())){
                    errorController = false;
                    errors.add("This question is mandatory!");
                } else {
                    errors.add(null);
                }
                //verifica che la domanda abbia una risposta
                if ((request.getParameter(question.getCode()) != null) && 
                        (!request.getParameter(question.getCode()).isEmpty() ||
                        (request.getParameterValues(question.getCode()).length>1 && request.getParameterValues(question.getCode())[1]!=null 
                        && !request.getParameterValues(question.getCode())[1].isEmpty())||
                        (request.getParameterValues(question.getCode()).length>2 && request.getParameterValues(question.getCode())[2]!=null 
                        && !request.getParameterValues(question.getCode())[2].isEmpty()))) {
                    String text = request.getParameter(question.getCode());
                    Answer answer = null;
                    //caso domanda di tipo scelta
                    if (question instanceof ChoiceQuestion) {
                        ChoiceQuestion cq = (ChoiceQuestion) question;
                        ChoiceAnswer ca = anseswerDao.createChoiceAnswer();
                        for (String s: request.getParameterValues(question.getCode())) {
                            ca.getOptions().add(cq.getOption(Short.valueOf(s)));
                        }
                        answer = ca;
                        answer.setQuestion(cq);
                        if (!answer.isValid()) {                          
                            errorController = false;
                            errors.set(errors.size()-1, "Select between " + cq.getMaxNumberOfChoices() + " and " + cq.getMinNumberOfChoices() + " choices!");                                                                                     
                        }
                        //caso domanda di tipo data
                    } else if (question instanceof DateQuestion) {
                        DateQuestion dq = (DateQuestion)question;
                        DateAnswer da = anseswerDao.createDateAnswer();
                        String date[] =  request.getParameterValues(question.getCode());
                        try{
                            da.setAnswer(LocalDate.parse(AdditionalFunctions.toDateString(date)));
                            answer = da;
                            answer.setQuestion(dq);
                            if (!answer.isValid()) {                               
                                errorController = false;
                                errors.set(errors.size()-1, "Select between " + dq.getMaxDate() + " and " + dq.getMinDate() + "!");                                                          
                            }
                        }catch(Exception ex){
                            answers.add(null);
                            errorController = false;
                            errors.set(errors.size()-1, "Invalid Date");
                        }
                        //caso domanda di tipo numerico
                    } else if (question instanceof NumberQuestion) {
                        NumberQuestion nq = (NumberQuestion)question;
                        NumberAnswer na = anseswerDao.createNumberAnswer();
                        na.setAnswer(Float.valueOf(text));
                        answer = na;
                        answer.setQuestion(nq);
                        if (!answer.isValid()) {                           
                            errorController = false;
                            errors.set(errors.size()-1, "Select between " + nq.getMaxValue() + " and " + nq.getMinValue() + "!");                                                         
                        }
                        //caso domanda di tipo testo
                    } else if (question instanceof TextQuestion){
                        if(question instanceof ShortTextQuestion){
                            ShortTextQuestion tq = (ShortTextQuestion)question;
                            ShortTextAnswer ta = anseswerDao.createShortTextAnswer();
                            ta.setAnswer(text);
                            answer = ta;
                            answer.setQuestion(tq);
                            if (!answer.isValid()) {
                                errorController = false;
                                errors.set(errors.size()-1, "Write between " + tq.getMaxLength() + " and " + tq.getMinLength() + " characters!");                                                                                        
                            }
                            if (!((ShortTextAnswer)answer).matchPattern()){
                                errorController = false;
                                errors.set(errors.size()-1, "Doesn't match the pattern!");
                            }
                        } else {
                            TextQuestion tq = (TextQuestion)question;
                            TextAnswer ta = anseswerDao.createTextAnswer();
                            ta.setAnswer(text);
                            answer = ta;
                            answer.setQuestion(tq);
                            if (!answer.isValid()) {
                                errorController = false;
                                errors.set(errors.size()-1, "Write between " + tq.getMaxLength() + " and " + tq.getMinLength() + " characters!");                                                          
                            }
                        }
                    }
                    //se la domanda aggiunte non è null, aggiungila alla riposta del sondaggio
                    if (answer != null){
                        surveyResponse.getAnswers().add(answer);
                        answers.add(answer);
                    }
                }else{
                    answers.add(null);
                }
            }
            request.setAttribute("answer", answers);
            //controllo se ci sono errori
            if(errorController){
                //verifica che tutte le risposte date siano valide
                if(surveyResponse.isValid()){
                    //salvataggio risposta
                    SurveyResponseDAO srdao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyResponseDAO();
                    srdao.saveOrUpdate(surveyResponse, n);
                    request.setAttribute("send", "");
                    //se un partecipante è loggato allora salvare il fatto che ha risposto al sondaggio
                    if(request.getSession().getAttribute("p") != null){
                        Participant p = (Participant) request.getSession().getAttribute("p");
                        p.setSubmitted(true);
                        ((Pollweb_DataLayer) request.getAttribute("datalayer")).getParticipantDAO().saveOrUpdate(p,n);
                    }
                } else{
                    request.setAttribute("message", "Invalid Value");
                    action_error(request, response);
                }
            } else {
                request.setAttribute("error", errors);
            }
            request.setAttribute("page_title", survey.getTitle());
            request.setAttribute("survey", survey);
            res.activate("survey.ftl.html", request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
            
            ex.printStackTrace();
        }
    }
    
    //autenticazione in caso di sondaggio riservato
    private void action_autentication(HttpServletRequest request, HttpServletResponse response, int n) throws IOException, ServletException, TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        //verifica che i campi siano inseriti
        if (email != null && password != null) {
            try{
                SurveyDAO dao =((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
                ReservedSurvey survey = (ReservedSurvey)dao.findByIdOpen(n);
                //verifica se il sondaggio scelto esista
                if (survey != null) {
                    //verifica se il partecipante è presente tra quelli a cui è
                    //permesso accedere al sondaggio scelto
                    for(Participant p: survey.getParticipants()){
                        if((p.getEmail()).equals(email) && (p.getPassword()).equals(password)){
                            //verifica che il partecipante non abbia ancora risposto
                            if (p.isSubmitted()){
                                request.setAttribute("error_autentication", "You have already answered!");
                                request.setAttribute("login", "survey?n=" + survey.getId());
                                request.setAttribute("survey", survey);
                                request.setAttribute("page_title", "Login");
                                res.activate("login.ftl.html", request, response);
                            } else {
                                request.getSession().setAttribute("p", p);
                                request.setAttribute("p", p);
                                request.setAttribute("page_title", survey.getTitle());
                                request.setAttribute("survey", survey);
                                res.activate("survey.ftl.html", request, response);
                            }
                        }
                    }
                    //verifica se l'autenticazione sia andata a buon fine
                    if (request.getSession().getAttribute("p") == null && request.getAttribute("error_autentication") == null){
                        request.setAttribute("error_autentication", "Incorrect credentials!");
                        request.setAttribute("login", "survey?n=" + survey.getId());
                        request.setAttribute("survey", survey);
                        request.setAttribute("page_title", "Login");
                        res.activate("login.ftl.html", request, response);
                    }
                    
                } else {
                    request.setAttribute("error", "Not a reserved survey!");
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
            //gestore del logout di un revisore o dell'admin
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
                } else {
                    if (request.getParameter("send") != null) {
                        action_send_survey(request, response, n);
                    } else {
                        action_survey(request, response, n);
                    }
                }          
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
