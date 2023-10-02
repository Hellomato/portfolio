/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Controller.LoginController;
import Controller.HomepageController;
import Controller.OptionsController;
import View.LoginView;
import View.HomepageView;
import View.OptionsView;
import desktopApplication.Model.Users.Guard;
import java.io.IOException;

/**
 *
 * @author Reecus
 */
public class Main
{
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        LoginView view = new LoginView();
        LoginController controller = new LoginController(view);
        view.setVisible(true);     
    }
    
}