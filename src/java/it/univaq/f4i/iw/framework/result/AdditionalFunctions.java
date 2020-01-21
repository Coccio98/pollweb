/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.univaq.f4i.iw.framework.result;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.security.SecurityLayer;

/**
 *
 * @author Pagliarini Alberto
 */
public class AdditionalFunctions {
    
    //funzione che trasforma un array di tre elementi in una striga che verrà parsata in una data,
    //viene utilizzato per rendere utilizzabili gli input di tipo data delle form
    public static String toDateString(String[] date) throws DataException{
        
        if (date.length==3 && !date[0].isEmpty() && !date[1].isEmpty() && !date[2].isEmpty()){
            return String.format("%04d", SecurityLayer.checkNumeric(date[2]))+
                "-"+String.format("%02d", SecurityLayer.checkNumeric(date[0]))+
                "-"+String.format("%02d", SecurityLayer.checkNumeric(date[1]));
        }
        return "";
        
    }
}
