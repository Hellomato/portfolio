/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PmsModel.Users;

import PmsModel.Users.User;
import java.io.File;
import java.io.FilenameFilter;

/**
 * admin class
 * @author Adam Edwards
 */
public class admin extends User {

    /**
     * default constructor for admin class
     */
    public admin() {
    }

    /**
     * create a new admin with all the information
     * @param Name
     * @param Surname
     * @param age
     * @param gender
     * @param password
     */
    public admin(String Name, String Surname, int age, String gender, String password){
        this.name = Name;
        this.surname = Surname;
        this.age = age;
        this.gender = gender;
        this.password = password;
        //this.ID = "A" + max(patent ids);
        this.ID = generateID();
    }
    
    

    

    
    private String generateID(){
        File dir = new File("data\\");
        int ID = 1000;
        String IDString;
       File[] foundFiles = dir.listFiles(new FilenameFilter(){
        public boolean accept(File dir, String name){
            return name.startsWith("A");
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
       IDString = "A";
       IDString += Integer.toString(ID);
       return IDString;
    }
}
