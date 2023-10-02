/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import View.LoginView;
import View.HomepageView;
import View.OptionsView;
import desktopApplication.Model.Users.Driver;
import desktopApplication.Model.Users.CabinCrew;
import desktopApplication.Model.Users.Guard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author gabryel
 */
public class LoginController {
    // Views
    private LoginView loginView;

    // Models
    private Driver driverModel;
    private CabinCrew cabinCrewModel;
    private Guard guardModel;

    
    public LoginController(LoginView loginView ) {             
        this.loginView = (LoginView) loginView;
        this.loginView.addLoginListener(new LoginListener());
        this.loginView.setVisible(true); 
    }
    
    // this login listener is temporary and needs some changes, perhaps a factory pattern
    class LoginListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String username;
            String password;
            username = loginView.getTxtUserName().getText();
            password = "";
            for (char c : loginView.getPfPassword().getPassword())
            {
                password += c;
            }
            
            driverModel = new Driver(username, password);
            cabinCrewModel = new CabinCrew(username, password);
            guardModel = new Guard(username, password);
            
            if (driverModel.Login() == true)
            {
                JOptionPane.showMessageDialog(null, "You are logged in!");
                loginView.dispose();
                        
                HomepageView view = new HomepageView();
                view.setLocation(loginView.getX(), loginView.getY());
                try {
                    HomepageController controller = new HomepageController(view, driverModel);
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                view.getBtnBack().setVisible(false);
            }
            else if (cabinCrewModel.Login() == true ){
                JOptionPane.showMessageDialog(null, "You are logged in!");
                loginView.dispose();
                
                HomepageView view = new HomepageView();
                view.setLocation(loginView.getX(), loginView.getY());
                try {
                    HomepageController controller = new HomepageController(view, cabinCrewModel);
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                view.getBtnBack().setVisible(false);
          
            }
            else if (guardModel.Login() == true ){
                JOptionPane.showMessageDialog(null, "You are logged in!");
                loginView.dispose();

                OptionsView view = new OptionsView();
                view.setLocation(loginView.getX(), loginView.getY());
                try {
                    OptionsController controller = new OptionsController(view, guardModel);
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Oops, something went wrong!");
                System.out.println("Oops, something went wrong!");
            }
        }
    }
}
