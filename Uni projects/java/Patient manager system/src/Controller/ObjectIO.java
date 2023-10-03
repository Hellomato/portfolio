/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;

/**
 * object IO class to make saving and loading easier
 * @author Adam Edwards
 */
public final class ObjectIO {

    /**
     * save A given object
     * @param object
     * @param fileName
     * @throws Exception
     */
    public static void save(Serializable object, String fileName) throws Exception{
        File f = new File("data\\" + fileName + ".txt");
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
    }
    
    /**
     * load a given object
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Serializable load(String fileName) throws Exception{
        File f = new File("data\\" + fileName + ".txt");
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);   
        return (Serializable) ois.readObject();
    }
    
    /**
     * delete a given object
     * @param fileName
     * @throws Exception
     */
    public static void delete(String fileName) throws Exception{
        File f = new File("data\\" + fileName + ".txt");
        if(f.delete()){
            System.out.println("file delted");
            
        }else{
            throw new FileNotFoundException();
        }
    }
    
}
