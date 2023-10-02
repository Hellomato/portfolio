/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import View.ValidateTicketView;
import View.LoginView;
import View.OptionsView;
import desktopApplication.Model.Users.Guard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;



/**
 *
 * @author Gabryel
 */
public class ValidateTicketController {
    
    // Views
    private ValidateTicketView validateTicketView;
    
    // Model
    private Guard model;
    
    public ValidateTicketController(ValidateTicketView validateTicketView, Guard model){
        
        // set the global model and view
        this.validateTicketView = (ValidateTicketView) validateTicketView;
        this.model = model;
        
        //this will keep the window size fixed
        this.validateTicketView.setResizable(false);
        this.validateTicketView.setVisible(true);
        
        this.validateTicketView.addSignOutListener(new ValidateTicketController.SignOutListener());
        this.validateTicketView.addValidateTicketListener(new ValidateTicketListener());
        this.validateTicketView.addBackListener(new BackListener() );
        SetUsername(model);
    }
    
    /***
     * Set the greeting in the Validate Ticket page
     * 
     * @param model User object
     */
    private void SetUsername(Guard model){
        
        String username = model.getName();
        this.validateTicketView.setUsernameLbl(username);
        
    }
    /**
     * Checks to see if the ticket is valid and displays Valid or Not Valid depending
     */
    class ValidateTicketListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String input;
            validateTicketView.setLblConformation("");
            input = validateTicketView.getTicketNumberTextField().getText();
            if (model.ValidateTicket(input) == true){
                validateTicketView.setLblConformation("Valid");
            }
            else{
                validateTicketView.setLblConformation("Not Valid");
            }
        }
        
    }
    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                validateTicketView.dispose();
                OptionsView optionsView = new OptionsView();
                optionsView.setLocation(validateTicketView.getX(), validateTicketView.getY());
            try { 
                OptionsController controller = new OptionsController(optionsView,(Guard) model);
            } catch (IOException ex) {
                Logger.getLogger(OptionsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /***
     * Sign the user out and redirect to login screen
     */
    class SignOutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            int confirm = JOptionPane.showOptionDialog(null,
            "Are you sure you want to log out?",
            "Signout Confirmation", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, null, null);
            
            if (confirm == JOptionPane.YES_OPTION) {
               JOptionPane.showMessageDialog(null, "You are being logged out!");
               validateTicketView.dispose();

               LoginView view = new LoginView();
               view.setLocation(validateTicketView.getX(), validateTicketView.getY());
               LoginController controller = new LoginController(view);
            }         
        }
    }
}
