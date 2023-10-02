/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopApplication.Model.Users;

import desktopApplication.Model.Strategies.XMLDataRetriever;
import desktopApplication.Model.Strategies.XMLParser;
import java.util.ArrayList;

/**
 *
 * @author gabryel
 */
public class Guard extends Staff {

    public Guard(String name, String password) {
        super(name, password);
    }
    /**
     * Allows the user to Login
     * @return true if the detail are correct otherwise false 
     */ 
    public boolean Login(){
        boolean correctDetails = false;
        if (name.charAt(0) == 'G' || name.charAt(0) == 'g'){
            correctDetails = LogOn();
        }
        return correctDetails;
    }
    /**
     * Checks to see if the ticket is in the database
     * @param id The ticket id
     * @return true if the response code is 200 otherwise false;
     */
    public boolean ValidateTicket(String id)
    {
        int responseCode;
        XMLDataRetriever  retriever = new XMLDataRetriever();
        responseCode = retriever.RetrieveValidateTicketResponseCode(id);
        if (responseCode == 200){
            return true;
        }
        else{
            return false;
        }
        
    }
    
    
}
