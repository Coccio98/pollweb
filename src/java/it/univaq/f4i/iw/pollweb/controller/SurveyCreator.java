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
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.ChoiceQuestion;
import it.univaq.f4i.iw.pollweb.data.model.DateQuestion;
import it.univaq.f4i.iw.pollweb.data.model.NumberQuestion;
import it.univaq.f4i.iw.pollweb.data.model.Question;
import it.univaq.f4i.iw.pollweb.data.model.ReservedSurvey;
import it.univaq.f4i.iw.pollweb.data.model.ShortTextQuestion;
import it.univaq.f4i.iw.pollweb.data.model.TextQuestion;
import it.univaq.f4i.iw.pollweb.data.model.Option;
import it.univaq.f4i.iw.pollweb.data.model.User;
import it.univaq.f4i.iw.pollweb.data.dao.OptionDAO;
import it.univaq.f4i.iw.pollweb.data.dao.QuestionDAO;
import it.univaq.f4i.iw.pollweb.data.dao.Pollweb_DataLayer;
import it.univaq.f4i.iw.pollweb.data.dao.SurveyDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Pagliarini Andrea
 */
public class SurveyCreator extends PollWebBaseController {
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
    //indirizzamento alla pagina di creazione
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
            User user = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getUserDAO()).findById((long)request.getSession().getAttribute("userid"));
            TemplateResult res = new TemplateResult(getServletContext());
            //controllo che i campi di testo e la riservatezza siano inseriti
            if((request.getParameter("title") != null && !request.getParameter("title").isEmpty()) && 
                (request.getParameter("openT") != null && !request.getParameter("openT").isEmpty())&&
                (request.getParameter("closeT") != null && !request.getParameter("closeT").isEmpty())&&
                request.getParameter("reserved") != null){
                //creazione del sondaggio publico o riservato
                if(!request.getParameter("reserved").equals("yes")){
                    Survey survey = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).createSurvey();
                    survey.setTitle(request.getParameter("title"));
                    survey.setOpeningText(request.getParameter("openT"));
                    survey.setClosingText(request.getParameter("closeT"));
                    survey.setManager(user);
                    (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).saveOrUpdate(survey);
                    request.setAttribute("surveyId", survey.getId());
                } else {
                    ReservedSurvey survey = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).createReservedSurvey();
                    survey.setTitle(request.getParameter("title"));
                    survey.setOpeningText(request.getParameter("openT"));
                    survey.setClosingText(request.getParameter("closeT"));
                    survey.setManager(user);
                    (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).saveOrUpdate(survey);
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
    
    //creazione di una nuova domanda
    private void action_add_question(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try{
            TemplateResult res = new TemplateResult(getServletContext());
            QuestionDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getQuestionDAO();
            SurveyDAO surveyDao = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO());
            Survey survey = surveyDao.findById(SecurityLayer.checkNumeric(request.getParameter("surveyId")));
            //verifica dell'appartenenza del sondaggio all'utente loggato
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                //controllo se i campi siano rispettati
                if(request.getParameter("text") != null &&
                    request.getParameter("note") != null &&
                    request.getParameter("mandatory") != null &&
                    !request.getParameter("text").isEmpty()){
                    Question q = null;
                    //vari tipi di domanda
                    switch(request.getParameter("type")){
                        case "shortText":
                            q = dao.createShortTextQuestion();
                            break;
                        case "longText":
                            q = dao.createTextQuestion();
                            break;
                        case "number":
                            q = dao.createNumberQuestion();
                        break;
                        case "date":
                            q = dao.createDateQuestion();
                        break;
                        case "choice":
                            q = dao.createChoiceQuestion();
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

                    dao.newQuestion(q, SecurityLayer.checkNumeric(request.getParameter("surveyId")));
                    request.setAttribute("questionId", q.getId());
                    request.setAttribute("type", request.getParameter("type"));
                } else {
                    request.setAttribute("error_creation", "Fill all mandatory fields");
                }

                request.setAttribute("position", request.getParameter("position"));
                request.setAttribute("surveyId", request.getParameter("surveyId"));
                
                //controllo errore
                if(request.getAttribute("error_creation") != null){               
                    request.setAttribute("page_title", "Add Question");                
                } else {                
                    request.setAttribute("page_title", "Question Rules");
                }
                res.activate("survey_creation.ftl.html", request, response);
            } else {
                request.setAttribute("message", "This is not your survey");
                action_error(request, response);
            }

        }catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    //definizione dei requisiti di una nuova domanda
    private void action_update_question(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try{
            TemplateResult res = new TemplateResult(getServletContext());
            QuestionDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getQuestionDAO();
            int max;
            int min;
            SurveyDAO surveyDao = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO());
            Survey survey = surveyDao.findById(SecurityLayer.checkNumeric(request.getParameter("surveyId")));
            //verifica dell'appartenenza del sondaggio all'utente loggato
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                //verifica che i campi max e min siano inseriti
                if(request.getParameter("maxValue") != null &&
                    request.getParameter("minValue") != null){
                    //switch per i tipi di domanda
                    switch(request.getParameter("type")){
                        //domanda di tipo testo breve
                        case "shortText":
                            //controllo se il campo espressione regolare sia presente
                            if(request.getParameter("expression") != null){
                                //controllo sulla lunghezza massima
                                if(request.getParameter("maxValue").isEmpty() || SecurityLayer.checkNumeric(request.getParameter("maxValue")) > 240){
                                    max = 240;
                                } else {
                                    max = SecurityLayer.checkNumeric(request.getParameter("maxValue"));
                                }
                                //controllo sulla lunghezza minima
                                if(request.getParameter("minValue").isEmpty()){
                                    min = 0;
                                }else{
                                    min = SecurityLayer.checkNumeric(request.getParameter("minValue"));
                                }
                                //verifica che max sia maggiore o uguale a min
                                if (max >= min){
                                    ShortTextQuestion q = dao.createShortTextQuestion();
                                    q.setId(SecurityLayer.checkNumeric(request.getParameter("questionId")));
                                    q.setMaxLength(max);
                                    q.setMinLength(min);
                                    //controllo sull'espressione regolare
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
                            //domanda di tipo testo lungo
                        case "longText":{   
                            //controllo sulla lunghezza massima
                            if(request.getParameter("maxValue").isEmpty() || SecurityLayer.checkNumeric(request.getParameter("maxValue")) > 10000){
                                max = 10000;
                            }else{
                                max = SecurityLayer.checkNumeric(request.getParameter("maxValue"));
                            }
                            //controllo sulla lunghezza minima
                            if(request.getParameter("minValue").isEmpty()){
                                min = 0;
                            }else{
                                min = SecurityLayer.checkNumeric(request.getParameter("minValue"));
                            }
                            //verifica che max sia maggiore o uguale a min
                            if (max >= min){
                                TextQuestion q = dao.createTextQuestion();
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
                            //domanda di tipo numerico
                        case "number":{ 
                            //controllo sul valore massimo
                            if(request.getParameter("maxValue").isEmpty()){
                                max = Integer.MAX_VALUE;
                            }else{
                                max = SecurityLayer.checkNumeric(request.getParameter("maxValue"));
                            }
                            //controllo sul valore minimo
                            if(request.getParameter("minValue").isEmpty()){
                                min = Integer.MIN_VALUE;
                            }else{
                                min = SecurityLayer.checkNumeric(request.getParameter("minValue"));
                            }
                            //verifica che max sia maggiore o uguale a min
                            if (max >= min){
                                NumberQuestion q = dao.createNumberQuestion();
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
                            //domanda di tipo data
                        case "date":{                       
                            LocalDate localDateMax;
                            LocalDate localDateMin;
                            //controllo sulla data massima
                            if(!request.getParameter("maxValue").isEmpty()){
                                localDateMax = LocalDate.parse(request.getParameter("maxValue"));                            
                            } else {
                                localDateMax = LocalDate.of(9999, Month.DECEMBER, 31);
                            }
                            //controllo sulla data minima
                            if(!request.getParameter("minValue").isEmpty()){
                                localDateMin = LocalDate.parse(request.getParameter("minValue"));                           
                            } else {
                                localDateMin = LocalDate.of(1, Month.JANUARY, 1);
                            }
                            //verifica che dateMin non sia dopo dateMax
                            if(!(localDateMin.isAfter(localDateMax))){
                                DateQuestion q = dao.createDateQuestion();
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
                            //domanda di tipo scelta
                        case "choice":
                            //controllo se il numero delle selte che dovranno esere aggiunte è presente
                            if(request.getParameter("number") != null &&
                                !request.getParameter("number").isEmpty()){
                                //controllo sul massimo numero di scelte
                                if(!request.getParameter("maxValue").isEmpty() && SecurityLayer.checkNumeric(request.getParameter("maxValue")) != 0 && 
                                        SecurityLayer.checkNumeric(request.getParameter("number")) > SecurityLayer.checkNumeric(request.getParameter("maxValue"))){
                                    max = SecurityLayer.checkNumeric(request.getParameter("maxValue"));
                                } else {
                                    max = SecurityLayer.checkNumeric(request.getParameter("number"));
                                }
                                //controllo sul minimo numero di scelte
                                if(!request.getParameter("minValue").isEmpty()){
                                    min =(short)SecurityLayer.checkNumeric(request.getParameter("minValue"));
                                } else {
                                    min = 0;
                                }
                                //verifica che max sia maggiore o uguale a min
                                if (max >= min){
                                    ChoiceQuestion q = dao.createChoiceQuestion();
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
                                    request.setAttribute("error_creation", "Max Choice must be bigger or equalS than Min Choice");
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
                    //verifica della presenza di errori, se non ce ne sono aumenta la posizione
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
            } else {
                request.setAttribute("message", "This is not your survey");
                action_error(request, response);
            }
            
        }catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    
    }
    //aggiunta delle opzioni ad una domanda a scelta
    private void action_add_choices(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try{
            TemplateResult res = new TemplateResult(getServletContext());
            List<Option> options = new ArrayList<>();
            short i=0;
            OptionDAO optionDao = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getOptionDAO());
            SurveyDAO surveyDao = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO());
            Survey survey = surveyDao.findById(SecurityLayer.checkNumeric(request.getParameter("surveyId")));
            //verifica dell'appartenenza del sondaggio all'utente loggato
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                //for per prendere e salvare tutte le opzioni inserite
                for (String s: request.getParameterValues("option")) {

                    Option o = optionDao.createOption();
                    o.setPosition(i);
                    o.setText(s);
                    options.add(o);
                    i++;
                }
                //salvo tutte le opzioni
                for(Option op: options){
                    optionDao.saveOrUpdate(op, SecurityLayer.checkNumeric(request.getParameter("questionId")));
                }
                request.setAttribute("surveyId", request.getParameter("surveyId"));
                request.setAttribute("position", request.getParameter("position"));
                request.setAttribute("page_title", "Add Question");
                res.activate("survey_creation.ftl.html", request, response);
            } else {
                request.setAttribute("message", "This is not your survey");
                action_error(request, response);
            }
        }catch (Exception ex) {
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
             //controllo se un manager ha effettuato un login
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("create") != null) {
                    action_create_survey(request, response);    
                } else if (request.getParameter("addQuestion") != null){
                    action_add_question(request, response);
                } else if(request.getParameter("saveQuestion")!= null){
                    action_update_question(request, response);
                } else if(request.getParameter("saveOptions")!= null){
                    action_add_choices(request, response);
                } else {
                    action_default(request, response);
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