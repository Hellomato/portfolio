/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PmsModel.Users;

import Controller.ObjectIO;
import PmsModel.objects.appointment;
import PmsModel.objects.medicine;
import PmsModel.objects.prescription;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;


/**
 * Doctor class 
 * @author Adam Edwards
 */
public class Doctor extends User {

    private appointment currentAppointment;
    private appointment[] futureAppointments; 
    private int rating;

    /**
     * default constructor for doctor
     */
    public Doctor() {
        futureAppointments = new appointment[10];
    }

    /**
     * get the appointment at i in the array of appointments
     * @param i
     * @return
     */
    public appointment getFutureAppointment(int i) {
        return futureAppointments[i];
    }

    /**
     * get the array of appointments
     * @return
     */
    public appointment[] getFutureAppointments() {
        return futureAppointments;
    }

    /**
     * adds an appointment to the future appointments array
     * if doctor has no room (is too busy) throws exception
     * @param appt
     * @throws Exception
     */
    public void setFutureAppointments(appointment appt) throws Exception {
        for (int i = 0; i < futureAppointments.length; i++){
            if(futureAppointments[i] == null){
                futureAppointments[i] = appt;
            }else if(i == futureAppointments.length){
                throw new IndexOutOfBoundsException();
            }
            
            
        }
    }

    /**
     * get the current appointment
     * @return
     */
    public appointment getCurrentAppointment() {
        return currentAppointment;
    }

    /**
     * set appointment 
     * @param currentAppointment
     */
    public void setCurrentAppointment(appointment currentAppointment) {
        this.currentAppointment = currentAppointment;
    }

    /**
     * adds rating received to current rating then averages 
     * @param rating
     */
    public void giveRating(int rating) {
        this.rating += rating / 2;
    }

    /**
     * gets doctor's rating
     * @return
     */
    public int getRating() {
        return rating;
    }
    
    /**
     * create a doctor with all required info
     * @param Name
     * @param Surname
     * @param age
     * @param gender
     * @param Password
     */
    public Doctor(String Name, String Surname, int age, String gender, String Password) {
        this.name = Name;
        this.surname = Surname;
        this.age = age;
        this.gender = gender;
        this.password = Password;
        futureAppointments = new appointment[10];
        
        this.ID = generateID() ;
    }
    
    private String generateID(){
        File dir = new File("data\\");
        int ID = 1000;
        String IDString;
       File[] foundFiles = dir.listFiles(new FilenameFilter(){
        public boolean accept(File dir, String name){
            return name.startsWith("D");
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
       IDString = "D";
       IDString += Integer.toString(ID);
       return IDString;
    }

    

   
}
