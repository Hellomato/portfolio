/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PmsModel.Users;

import PmsModel.Users.Doctor;
import PmsModel.objects.appointment;
import PmsModel.objects.prescription;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * patient class
 * @author Adam Edwards
 */
public class Patient extends User {
   
    /**
     * is sign up approved
     */
    public boolean isApproved = false;

    /**
     * is termination requested
     */
    public boolean requestTermination = false;

    /**
     * is termination approved
     */
    public boolean terminationApproved = false;
    
    private appointment currentAppointment;
    private prescription currentPrescription;
    
    private List<appointment> history;

    /**
     * constructor for default patient
     */
    public Patient() {
       history = new ArrayList<appointment>(20);
    }

    /**
     * a list of appointments the patient has had
     * @return
     */
    public List<appointment> getHistory() {
        return history;
    }
    
    
    /**
     * create a patient with all required values
     * @param Name
     * @param Surname
     * @param age
     * @param gender
     * @param password
     */
    public Patient(String Name, String Surname, int age, String gender, String password){
        this.name = Name;
        this.surname = Surname;
        this.age = age;
        this.gender = gender;
        this.password = password;
        //this.ID = "P" + max(patent ids);
        this.ID = generateID();
        history = new ArrayList<appointment>(20);
    }
             
    /**
     * returns the patient current appointment
     * @return
     */
    public appointment getCurrentAppointment() {
        return currentAppointment;
    }

    /**
     * add current appointment to history then sets current appointment
     * @param currentAppointment
     */
    public void setCurrentAppointment(appointment currentAppointment) {
        try{
        history.add(this.currentAppointment);
        }catch(Exception e){
        System.out.println(e);
        };
        this.currentAppointment = currentAppointment;
    }

    /**
     * get patient current prescription
     * @return
     */
    public prescription getCurrentPrescription() {
        return currentPrescription;
    }

    /**
     * set prescription of patient
     * @param currentPrescription
     */
    public void setCurrentPrescription(prescription currentPrescription) {
        this.currentPrescription = currentPrescription;
    }
    
    private String generateID(){
        File dir = new File("data\\");
        int ID = 1000;
        String IDString;
       File[] foundFiles = dir.listFiles(new FilenameFilter(){
        public boolean accept(File dir, String name){
            return name.startsWith("P");
        }
       });
       for (File f : foundFiles){
          IDString = f.getName();
          IDString = IDString.substring(1,5);
          if(ID <= Integer.parseInt(IDString)){
              ID = Integer.parseInt(IDString);
          }
       }
       ID += 1;
       IDString = "P";
       IDString += Integer.toString(ID);
       return IDString;
    }
   
    
}
