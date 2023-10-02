/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.FileNotFoundException;
import javax.swing.JFrame;
import PmsModel.Users.Doctor;
import PmsModel.Users.Patient;
import PmsModel.Users.User;
import PmsModel.Users.admin;
import PmsModel.Users.secretary;
import guiView.PatientView;
import guiView.adminView;
import guiView.docView;
import guiView.secView;
import java.awt.event.ActionListener;
import PmsModel.objects.appointment;
import java.awt.event.ActionEvent;
import guiView.Menu;
import javax.swing.JOptionPane;

/**
 * controller class for pms
 * @author Adam Edwards
 */
public class controller {

    private User currentUser;
    private Menu View;
    
    /**
     * loads the user file and checks input password vs stored password then log in
     * @param username
     * @param password
     */
    public void login(String username, String password){
        char userFirst = username.charAt(0);
        //ObjectIO.load(uName);
       
        try{
        switch(userFirst){
            case 'D':
                currentUser = new Doctor();
                currentUser = (Doctor) ObjectIO.load(username);
                if(password.equals(currentUser.getPassword())){
                    View.setFeedback("Logged in");
                    docView newview = new docView();
                     newview.setUser((Doctor) currentUser);
                     View.setVisible(false);
                     newview.setVisible(true);
                }else{
                    View.setFeedback("Login Failed");
                }
                break;
            case 'S':
                currentUser = new secretary();
                currentUser = (secretary) ObjectIO.load(username);
                if(password.equals(currentUser.getPassword())){
                    View.setFeedback("Logged in");
                    secView newview = new secView();
                     newview.setUser((secretary) currentUser);
                     View.setVisible(false);
                     newview.setVisible(true);
                   
                }else{
                    View.setFeedback("Login Failed");
                }
                break;
            case 'P':
                currentUser = new Patient();
                currentUser = (Patient) ObjectIO.load(username);
                if(password.equals(currentUser.getPassword())){
                    View.setFeedback("Logged in");
                     PatientView newview = new PatientView();
                     newview.setUser((Patient) currentUser);
                     View.setVisible(false);
                     newview.setVisible(true);
                }else{
                    View.setFeedback("Login Failed");
                }
                break;
            case 'A':
                currentUser = new admin();
                currentUser = (admin) ObjectIO.load(username);
                if(password.equals(currentUser.getPassword())){
                    View.setFeedback("Logged in");
                    adminView newview = new adminView();
                     newview.setUser((admin) currentUser);
                     View.setVisible(false);
                     newview.setVisible(true);
                }else{
                    View.setFeedback("Login Failed");
                }
                break;
            default:
                View.setFeedback("Login Failed");
        }
        }catch(FileNotFoundException e){
            View.setFeedback("Incorrect login details");
        }catch(StringIndexOutOfBoundsException e){
            View.setFeedback("Please Enter a username");
        }catch (Exception e){
            View.setFeedback("Something went Wrong please inform Admin");
            System.out.println(e);
            for(StackTraceElement a : e.getStackTrace()){
                System.out.print(a);
            }
            
            if(currentUser.isHasNotification()){
                JOptionPane.showMessageDialog(null, currentUser.getNotificationMessage());
            }
        }
    }
    
    private void onclose(){
        try{
        ObjectIO.save(currentUser, currentUser.getID());
        }catch(Exception e){
            
        }
    }

    /**
     * constructor for controller class
     * @param currentUser
     */
    public controller(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * constructor for controller class
     * @param View
     */
    public controller(Menu View) {
        this.View = View;
    }

    /**
     * constructor for controller class
     */
    public controller() {
    }
    
}
