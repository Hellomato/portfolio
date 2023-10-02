/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopApplication.Model.Users;

/**
 *
 * @author gabryel
 */
public class CabinCrew extends Staff {

    public CabinCrew(String name, String password) {
        super(name, password);
    }
    /**
     * Allows the user to Login
     * @return true if the detail are correct otherwise false 
     */ 
    public boolean Login(){
        boolean correctDetails = false;
        if (name.charAt(0) == 'C' || name.charAt(0) == 'c'){
            correctDetails = LogOn();
        }
        return correctDetails;
    }
    
}
