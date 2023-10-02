/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PmsModel.objects;

/**
 * a medicine
 * @author Adam Edwards
 */
public class medicine implements java.io.Serializable {
    
    private String name;
    private String Description;

    /**
     * get the name property
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * get description property
     * @return
     */
    public String getDescription() {
        return Description;
    }

    /**
     * contruct a new medicine
     * @param name
     * @param Description
     */
    public medicine(String name, String Description) {
        this.name = name;
        this.Description = Description;
    }

    /**
     * default contstructor
     */
    public medicine() {
    }
    
}
