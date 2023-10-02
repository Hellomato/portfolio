/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PmsModel.Users;

import Controller.ObjectIO;
import PmsModel.Users.User;
import PmsModel.Users.Patient;
import PmsModel.Users.Doctor;
import PmsModel.objects.appointment;
import java.io.File;
import java.io.FilenameFilter;

/**
 * secretary class
 * @author Adam Edwards
 */
public class secretary extends User{

    /**
     *  constructor for secretary class
     */
    public secretary() {
    }

    /**
     * create a secretary with all given fields
     * @param Name
     * @param Surname
     * @param age
     * @param gender
     * @param password
     */
    public secretary(String Name, String Surname, int age, String gender, String password){
        this.name = Name;
        this.surname = Surname;
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.ID = generateID();
    }
    
 
    private String generateID(){
        File dir = new File("data\\");
        int ID = 1000;
        String IDString;
       File[] foundFiles = dir.listFiles(new FilenameFilter(){
        public boolean accept(File dir, String name){
            return name.startsWith("S");
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
       IDString = "S";
       IDString += Integer.toString(ID);
       return IDString;
    }
}
