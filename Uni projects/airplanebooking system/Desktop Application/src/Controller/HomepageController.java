/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import View.HomepageView;
import View.LoginView;
import View.OptionsView;
import desktopApplication.Model.Timetable.TimetableEntry;
import desktopApplication.Model.Users.Guard;
import desktopApplication.Model.Users.Staff;
import desktopapplication.Model.Timetable.PersonalTimetable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;



/**
 *
 * @author Eugen
 */
public class HomepageController {
    
    // Views
    private HomepageView homepageView;
    
    // Model
    private Staff model;
    private String id;
    private Timer populateTableTimer = new Timer();
    private PersonalTimetable data = new PersonalTimetable();
    public HomepageController(HomepageView homepageView, Staff model) throws IOException {
        
        // set the global model and view
        this.homepageView = (HomepageView) homepageView;
        this.model = model;
        
        //this will keep the window size fixed
        this.homepageView.setResizable(false);
        this.homepageView.setVisible(true);
        
        this.homepageView.addSignOutListener(new HomepageController.SignOutListener());
        this.homepageView.addBackListener(new BackListener());
        SetUsername(model);
        id = model.requestID();
        int MINUTES = 1; // The delay in minutes

        populateTableTimer.schedule(new TimerTask() {
        @Override
            public void run() { // Function runs every MINUTES minutes.
                // Run the code you want here
            try{
                PopulateTable(); 
            }
            catch(IOException ex){
                
            }

    }
    }, 0, 1000 * 60 * MINUTES);
        
    }
    
    /***
     * Set the greeting in the homepage
     * 
     * @param model User object
     */
    private void SetUsername(Staff model){
        
        String username = model.getName();
        this.homepageView.setUsernameLbl(username);
        
    }

    /***
     * Populate timetable
     */
    private void PopulateTable() throws IOException {
        data.update(id);
        List<TimetableEntry> timetable = data.getPersonalTimetable();
        homepageView.clearTable2();
        homepageView.AddRowToJTable(timetable);
        
    }

    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                homepageView.dispose();
                OptionsView optionsView = new OptionsView();
                optionsView.setLocation(homepageView.getX(), homepageView.getY());
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
               populateTableTimer.cancel();                       
               JOptionPane.showMessageDialog(null, "You are being logged out!");
               homepageView.dispose();

               LoginView view = new LoginView();
               view.setLocation(homepageView.getX(), homepageView.getY());
               LoginController controller = new LoginController(view);
            }         
        }
    }
}
