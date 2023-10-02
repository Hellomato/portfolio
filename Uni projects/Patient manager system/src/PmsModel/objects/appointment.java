/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PmsModel.objects;

import PmsModel.Users.Doctor;
import PmsModel.Users.Patient;

/**
 * an appointment consisting of doctor patient at a date and time
 * @author Adam Edwards
 */
public class appointment implements java.io.Serializable{
    private String date = "";
    private Doctor D;
    private Patient P;
    private String time = "";
    private String Notes = " ";
    private prescription prescription;

    /**
     * construct an appointment with all variables pre-determined
     * @param date
     * @param D
     * @param P
     * @param time
     */
    public appointment(String date, Doctor D, Patient P, String time) {
        this.date = date;
        this.D = D;
        this.P = P;
        this.time = time;
    }
    
    /**
     * get a string of the important parts of this appointment
     * @return
     */
    public String getSummary(){
        String output;
        output = this.date + " " + this.time + " Dr. " + D.getSurname();
        return output;
    }

    /**
     * get a string of all field values
     * @return
     */
    public String getFull(){
        
        String output = this.date + " " + this.time + " Dr. " + D.getSurname() + " " ;
            
        if(this.prescription != null){
           output +=this.prescription.getString() + " ";
        }
        output += this.getNotes();
        
        
        return output;
    }
    
    /**
     *default appointment constructor
     */
    public appointment() {
    }

    /**
     * get date of appointment
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * set the date for the appointment
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * return the doctor on this appointment 
     * @return
     */
    public Doctor getD() {
        return D;
    }

    /**
     * assigns a doctor to this appointment
     * @param D
     */
    public void setD(Doctor D) {
        this.D = D;
    }

    /**
     * get the patient
     * @return
     */
    public Patient getP() {
        return P;
    }

    /**
     * assigns a patient to this appointment
     * @param P
     */
    public void setP(Patient P) {
        this.P = P;
    }

    /**
     * get time string value
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     * set time string value
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * get notes string value
     * @return
     */
    public String getNotes() {
        return Notes;
    }

    /**
     * set notes string value
     * @param Notes
     */
    public void setNotes(String Notes) {
        this.Notes = Notes;
    }
    
}
