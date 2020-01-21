/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.pollweb.controller;

import it.univaq.f4i.iw.framework.result.AdditionalFunctions;
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
 * @author Pagliarini Andrea
 */
public class MySurveyVisualizeAndEdit extends PollWebBaseController {
    
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
            //Creazione della lista dei sondaggi creati dall'utente loggato
            User user = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getUserDAO()).findById((long)request.getSession().getAttribute("userid"));
            request.setAttribute("surveys", (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findByManager(user));
            res.activate("my_surveys.ftl.html", request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    }
    
    private void save_survey(HttpServletRequest request, HttpServletResponse response, int m) throws IOException, ServletException, TemplateManagerException{
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            SurveyDAO dao = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO());
            Survey survey = dao.findById(m);
            //verifica dell'appartenenza del sondaggio all'utente loggato
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                //salvataggio della modifica del titolo, testo di apertura o testo di chiusura del sondaggio
                if(request.getParameter("title") != null && !request.getParameter("title").isEmpty()){
                    survey.setTitle(request.getParameter("title"));
                }
                if(request.getParameter("openT") != null && !request.getParameter("openT").isEmpty()){
                    survey.setOpeningText(request.getParameter("openT"));
                }
                if(request.getParameter("closeT") != null && !request.getParameter("closeT").isEmpty()){
                    survey.setClosingText(request.getParameter("closeT"));
                }
                dao.saveOrUpdate(survey);
                request.setAttribute("survey", survey);
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
    
    private void edit_survey(HttpServletRequest request, HttpServletResponse response, int m) throws IOException, ServletException, TemplateManagerException {
         try {
            TemplateResult res = new TemplateResult(getServletContext());
            Survey survey =(((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findById(m);
            QuestionDAO questionDao= ((Pollweb_DataLayer) request.getAttribute("datalayer")).getQuestionDAO();
            //verifica dell'appartenenza del sondaggio all'utente loggato
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                //spostamento della domanda più in alto nel sondaggio
                if(request.getParameter("up") != null && request.getParameter("down") == null && request.getParameter("del") == null && SecurityLayer.checkNumeric(request.getParameter("up")) > 0){
                    int up = SecurityLayer.checkNumeric(request.getParameter("up"));
                    survey.getQuestions().get(up).setPosition((short)(up-1));
                    survey.getQuestions().get(up-1).setPosition((short)up);
                    questionDao.update(survey.getQuestions().get(up), m);
                    questionDao.update(survey.getQuestions().get(up-1), m);
                }
                //spostamento della domanda più in basso nel sondaggio
                if(request.getParameter("down") != null && request.getParameter("up") == null && request.getParameter("del") == null && SecurityLayer.checkNumeric(request.getParameter("down")) < (survey.getQuestions().size()-1)){
                    int down = SecurityLayer.checkNumeric(request.getParameter("down"));
                    survey.getQuestions().get(down).setPosition((short)(down+1));
                    survey.getQuestions().get(down+1).setPosition((short)down);
                    questionDao.update(survey.getQuestions().get(down), m);
                    questionDao.update(survey.getQuestions().get(down+1), m);
                }
                //eliminazione di una domanda nel sondaggio
                try{
                    if(request.getParameter("down") == null && request.getParameter("up") == null && request.getParameter("del") != null && !survey.getQuestions().isEmpty()){
                        int del=SecurityLayer.checkNumeric(request.getParameter("del"));
                        questionDao.delete(survey,del);
                    }               
                    request.setAttribute("survey", (((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findById(m));
                    request.setAttribute("page_title", "Edit Survey");
                    res.activate("my_survey_detail.ftl.html", request, response);
                } catch (Exception ex) {
                    request.setAttribute("message", "You cannot delete a question if someone already answered");
                    action_error(request, response);
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
    
    private void toggle_survey(HttpServletRequest request, HttpServletResponse response, int t) throws IOException, ServletException, TemplateManagerException {
        try {
            SurveyDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = dao.findById(t);
            //verifica dell'appartenenza del sondaggio all'utente loggato
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                //attivazione e disattivazione del sondaggio
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
    
    //aggiunta di una domanda a una sondaggio, l'utente verra porto a una pagina
    //di inserimento gestita dal controller SurveyCreator
    private void add_question(HttpServletRequest request, HttpServletResponse response, int a) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Add Question");
            Survey survey =(((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO()).findById(a);
            //verifica dell'appartenenza del sondaggio all'utente loggato
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
            //verifica dell'appartenenza del sondaggio all'utente loggato
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                //richiamo del metodo di creazione per esportare le risposte ad un sondaggio
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
    
    //aggiunta di un dei partecipanti ad un sondaggio riservato
    private void add_participant(HttpServletRequest request, HttpServletResponse response, int u)throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Add User");
            SurveyDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = dao.findById(u);
            ParticipantDAO participantDao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getParticipantDAO();
            //verifica sulla riservatezza del sondaggio
            if(survey.isReserved()){
                //verifica dell'appartenenza del sondaggio all'utente loggato
                if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                    //verifica se è stata fatta un'operazione di invio dei dati di un partecipante
                    if(request.getParameter("send") != null){
                        //verifica della correttezza dei dati inviati
                        if(request.getParameter("name")!=null && request.getParameter("email")!=null && request.getParameter("password")!=null &&
                                !request.getParameter("name").isEmpty() && !request.getParameter("email").isEmpty() && !request.getParameter("password").isEmpty()){
                            //salvataggio di un nuovo partecipante
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
            res.activate("add_participant.ftl.html", request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Data access exception: " + ex.getMessage());
            action_error(request, response);
        }
    
    }
    
    //modifiche varie sulle domande
    private void edit_question(HttpServletRequest request, HttpServletResponse response, int m1, int m2)throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            SurveyDAO surveyDao =((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = surveyDao.findById(m1);
            //verifica dell'appartenenza del sondaggio all'utente loggato
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                QuestionDAO questionDao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getQuestionDAO();
                Question question = questionDao.findById(m2);
                OptionDAO optionDao = (((Pollweb_DataLayer) request.getAttribute("datalayer")).getOptionDAO());
                //verifica se è stata richiesta una cancellazione di un campo di una comanda a scelta
                if (request.getParameter("del")!= null){
                    //verifica che la domanda sia a scelta
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
                    //verifica se è stata richiesta l'aggiunta di un campo ad una domanda a scelta
                    //e se il nuovo campo della domanda è corretto
                } else if(request.getParameter("addOption")!= null &&
                    request.getParameter("option")!= null &&
                    !request.getParameter("option").isEmpty()){
                    //verifica che la domanda sia a scelta
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
                    //verifica se è stato richiesto il salvataggio di una domanda
                } else if(request.getParameter("save")!= null){
                    //verifica se i campi sono corratti
                    if(request.getParameter("text") != null &&
                        request.getParameter("note") != null &&
                        request.getParameter("mandatory") != null &&
                        !request.getParameter("text").isEmpty()){
                        //modifica dei campi comuni
                        question.setText(request.getParameter("text"));
                        question.setNote(request.getParameter("note"));
                        if(request.getParameter("mandatory").equals("yes")){
                            question.setMandatory(true);
                        }else{
                            question.setMandatory(false);
                        }
                        //switch che valuta il tipo della domanda
                        switch(question.getQuestionType()){
                            //domanda di tipo testo breve
                            case "short text":
                                //verifica della correttezza dei campi
                                if(request.getParameter("maxValue") != null &&
                                    request.getParameter("minValue") != null &&
                                    request.getParameter("expression") != null){
                                    //controllo sul campo lunghezza massima
                                    if(request.getParameter("maxValue").isEmpty() || SecurityLayer.checkNumeric(request.getParameter("maxValue")) > 240){
                                        ((ShortTextQuestion)question).setMaxLength(240);
                                    }else{
                                        ((ShortTextQuestion)question).setMaxLength(SecurityLayer.checkNumeric(request.getParameter("maxValue")));
                                    }
                                    //controllo sul campo lunghezza minima
                                    if(request.getParameter("minValue").isEmpty()){
                                        ((ShortTextQuestion)question).setMinLength(0);
                                    }else{
                                        ((ShortTextQuestion)question).setMinLength(SecurityLayer.checkNumeric(request.getParameter("minValue")));
                                    }
                                    //controllo sul campo espressione regolare
                                    if(request.getParameter("expression").isEmpty()){
                                        ((ShortTextQuestion)question).setPattern(".");
                                    }else{
                                        ((ShortTextQuestion)question).setPattern(request.getParameter("expression"));
                                    }
                                }
                                break;
                                //domanda ti tipo testo lungo
                            case "long text":
                                //verifica della correttezza dei campi
                                if(request.getParameter("maxValue") != null &&
                                    request.getParameter("minValue") != null){
                                    //controllo sul campo lunghezza massima
                                    if(request.getParameter("maxValue").isEmpty() || SecurityLayer.checkNumeric(request.getParameter("maxValue")) > 10000){
                                        ((TextQuestion)question).setMaxLength(10000);
                                    }else{
                                        ((TextQuestion)question).setMaxLength(SecurityLayer.checkNumeric(request.getParameter("maxValue")));
                                    }
                                    //controllo sul campo lunghezza minima
                                    if(request.getParameter("minValue").isEmpty()){
                                        ((TextQuestion)question).setMinLength(0);
                                    }else{
                                        ((TextQuestion)question).setMinLength(SecurityLayer.checkNumeric(request.getParameter("minValue")));
                                    }
                                }
                                break;
                                //domanda di tipo numerico
                            case "number":
                                //verifica della correttezza dei campi
                                if(request.getParameter("maxValue") != null &&
                                    request.getParameter("minValue") != null){
                                    //conntrollo sul campo valore massimo
                                    if(request.getParameter("maxValue").isEmpty()){
                                        ((NumberQuestion)question).setMaxValue(Integer.MAX_VALUE);
                                    }else{
                                        ((NumberQuestion)question).setMaxValue(SecurityLayer.checkNumeric(request.getParameter("maxValue")));
                                    }
                                    //controllo sul campo valore minimo
                                    if(request.getParameter("minValue").isEmpty()){
                                        ((NumberQuestion)question).setMinValue(Integer.MIN_VALUE);
                                    }else{
                                        ((NumberQuestion)question).setMinValue(SecurityLayer.checkNumeric(request.getParameter("minValue")));
                                    }
                                }
                                break;
                                //domanda di tipo data
                            case "date":
                                //verifica della correttezza dei campi
                                if(request.getParameterValues("maxValue")!=null && 
                                        request.getParameterValues("minValue")!=null){
                                    String dateMax[] = request.getParameterValues("maxValue");
                                    //conntrollo sul campo data massima
                                    try{
                                        LocalDate localDate = LocalDate.parse(AdditionalFunctions.toDateString(dateMax));
                                        ((DateQuestion)question).setMaxDate(localDate);
                                    }catch(Exception ex){}
                                    String dateMin[] = request.getParameterValues("minValue");
                                    //conntrollo sul campo data minima                                  
                                    try{
                                        LocalDate localDate = LocalDate.parse(AdditionalFunctions.toDateString(dateMin));
                                        ((DateQuestion)question).setMinDate(localDate);
                                    }catch(Exception ex){}
                                }
                                break;
                                //domanda di tipo choice
                            case "choice":
                                //verifica della correttezza dei campi
                                if(request.getParameter("maxValue") != null &&
                                    request.getParameter("minValue") != null){
                                    //controllo sul massimo numero dei campi selezionabili
                                    if(!request.getParameter("maxValue").isEmpty() && SecurityLayer.checkNumeric(request.getParameter("maxValue")) > 0 &&
                                            ((ChoiceQuestion)question).getOptions().size() > SecurityLayer.checkNumeric(request.getParameter("maxValue"))){
                                        ((ChoiceQuestion)question).setMaxNumberOfChoices((short)SecurityLayer.checkNumeric(request.getParameter("maxValue")));
                                    } else {
                                        ((ChoiceQuestion)question).setMaxNumberOfChoices((short)((ChoiceQuestion)question).getOptions().size());
                                    }
                                    //controllo sul minimo numero dei campi selezionabili
                                    if(!request.getParameter("minValue").isEmpty()){
                                        ((ChoiceQuestion)question).setMinNumberOfChoices((short)SecurityLayer.checkNumeric(request.getParameter("minValue")));
                                    }
                                    //controllo se il massimo è maggiore del minimo
                                    //se non lo è genera un messaggio di errore utilizato poi nel tameplate
                                    if(((ChoiceQuestion)question).getMaxNumberOfChoices() < ((ChoiceQuestion)question).getMinNumberOfChoices()){
                                        request.setAttribute("error_creation", "Max Choice must be bigger or equal than Min Choice");
                                    }
                                } else {
                                    request.setAttribute("error_creation", "Fill all mandatory fields");
                                }
                                break;
                            default:
                                request.setAttribute("message", "Invalid Type");
                                action_error(request, response);
                        }
                        //se è stato riscontrato un errore allaggiunta della domanda choice si da la possibilità di correggerlo
                        if(request.getAttribute("error_creation") != null){
                            request.setAttribute("question", question);
                            request.setAttribute("m1", m1);
                            request.setAttribute("m2", m2);
                            request.setAttribute("page_title", "Edit Question");
                            res.activate("edit_question.ftl.html", request, response);
                        } else {
                            questionDao.update(question, m1);
                            request.setAttribute("page_title", "Edit Survey");
                            request.setAttribute("survey", survey);
                            res.activate("my_survey_detail.ftl.html", request, response);
                            
                        }
                    } else {
                        request.setAttribute("error_creation", "Fill all mandatory fields");
                        action_error(request, response);
                    }
                    
                } else {   
                    //questo viene utilizzato nel momento in cui si vuole visualizzare una domanda
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
    
    private void show_answers(HttpServletRequest request, HttpServletResponse response, int r)throws IOException, ServletException, TemplateManagerException {
         try {
            SurveyDAO dao = ((Pollweb_DataLayer) request.getAttribute("datalayer")).getSurveyDAO();
            Survey survey = dao.findById(r);
            //verifica dell'appartenenza del sondaggio all'utente loggato
            if(survey.getManager().getId() == (long)request.getSession().getAttribute("userid")){
                TemplateResult res = new TemplateResult(getServletContext());
                request.setAttribute("page_title", "Add User");
                request.setAttribute("survey", survey);
                res.activate("answers.ftl.html", request, response);
                
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
            //se un partecipante dovesse essere loggato quando si trova in questo punto viene fatto il log out del suddetto
            if(request.getSession().getAttribute("p") != null){
                request.getSession().removeAttribute("p");
            }
            //controllo se un manager ha effettuato un login
            if (SecurityLayer.checkSession(request) != null) {
                if (request.getParameter("m") != null) {
                    if(request.getParameter("saveSurvey") != null) {
                        save_survey(request, response, SecurityLayer.checkNumeric(request.getParameter("m")));
                    } else {
                        edit_survey(request, response, SecurityLayer.checkNumeric(request.getParameter("m")));
                    }
                } else {
                    if (request.getParameter("t") != null) {
                        toggle_survey(request, response, SecurityLayer.checkNumeric(request.getParameter("t")));
                    } else if (request.getParameter("exp") != null){
                        export(request, response, SecurityLayer.checkNumeric(request.getParameter("exp")));
                    }
                    if (request.getParameter("a") != null) {
                        add_question(request, response, SecurityLayer.checkNumeric(request.getParameter("a")));
                    } else if(request.getParameter("u") != null){
                        add_participant(request, response, SecurityLayer.checkNumeric(request.getParameter("u")));
                    } else if(request.getParameter("m1") != null && request.getParameter("m2") != null){
                        edit_question(request, response, SecurityLayer.checkNumeric(request.getParameter("m1")), SecurityLayer.checkNumeric(request.getParameter("m2")));
                    } else if(request.getParameter("r")!=null){
                        show_answers(request, response, SecurityLayer.checkNumeric(request.getParameter("r")));
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
