/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.framework.result;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.pollweb.data.model.Answer;
import it.univaq.f4i.iw.pollweb.data.model.Survey;
import it.univaq.f4i.iw.pollweb.data.model.SurveyResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Pagliarini Alberto
 */
public class CsvFileWriter {
    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
     
    //CSV file header
    private static final String FILE_HEADER = "survey_response_number;question_number;answer";
 
    public static void writeCsvFile(HttpServletRequest request, Survey survey) throws DataException {
                          
        FileWriter fileWriter = null;
        try {
            List<SurveyResponse> sr = survey.getResponses();
            String home = System.getProperty("user.home");
            fileWriter = new FileWriter(new File(home + "/Downloads/", "Survey_Response_" + survey.getTitle() +".csv"));
 
            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());
             
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            int i=1;
            //Write a new student object list to the CSV file
            for (SurveyResponse response : sr) {
                List<Answer> answers = response.getAnswers();
                for(Answer answer: answers){
                    fileWriter.append(String.valueOf(i));
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(String.valueOf(answer.getQuestion().getPosition() + 1));              
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(answer.toString());
                    fileWriter.append(NEW_LINE_SEPARATOR);     
                }
                i++;
            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
             
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
             
        }
    }
}
