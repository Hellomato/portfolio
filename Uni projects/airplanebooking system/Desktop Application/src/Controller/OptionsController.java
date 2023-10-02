/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import View.OptionsView;
import View.HomepageView;
import View.LoginView;
import View.ValidateTicketView;
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
public class OptionsController {
    
    // Views
    private OptionsView optionsView;
    
    // Model
    private Guard model;
    
    public OptionsController(OptionsView optionsView, Guard model) throws IOException {
        
        // set the global model and view
        this.optionsView = (OptionsView) optionsView;
        this.model = model;
        
        //this will keep the window size fixed
        this.optionsView.setResizable(false);
        this.optionsView.setVisible(true);
        
        this.optionsView.addSignOutListener(new OptionsController.SignOutListener());
        this.optionsView.addValidateTicketListener(new ValidateTicketListener());
        this.optionsView.addTimetableListener((ActionListener) new TimetableListener());
        SetUsername(model);
    }
    
    /***
     * Set the greeting in the options page
     * 
     * @param model User object
     */
    private void SetUsername(Guard model){
        
        String username = model.getName();
        this.optionsView.setUsernameLbl(username);
        
    }
    
    /**
     * Changes the page to the Validate ticket page
     */
    class ValidateTicketListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            optionsView.dispose();
            ValidateTicketView view = new ValidateTicketView();
            view.setLocation(optionsView.getX(), optionsView.getY());
            ValidateTicketController controller = new ValidateTicketController(view,model);
        }
        
    }
    class TimetableListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
                optionsView.dispose();
                HomepageView view = new HomepageView();
                view.setLocation(optionsView.getX(), optionsView.getY());
            try { 
                HomepageController controller = new HomepageController(view,model);
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
               optionsView.dispose();

               LoginView view = new LoginView();
               view.setLocation(optionsView.getX(), optionsView.getY());
               LoginController controller = new LoginController(view);
            }         
        }
    }
}
