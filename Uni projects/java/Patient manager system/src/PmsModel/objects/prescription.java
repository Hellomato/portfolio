/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PmsModel.objects;

/**
 * a prescription consisting of a medicine and dosage
 * @author Adam Edwards
 */
public class prescription implements java.io.Serializable{
    private medicine medicine;
    private int quantity;
    private String Dosage;

    /**
     * default constructor
     */
    public prescription() {
    }

    /**
     * returns a string summarising the prescription
     * @return string
     */
    public String getString(){
        String output;
        output = this.medicine.getName() + " " + this.quantity + " " + this.Dosage;
        return output;
    }
    
    /**
     * constructor for a prescription with all fields
     * @param medicine
     * @param quantity
     * @param Dosage
     */
    public prescription(medicine medicine, int quantity, String Dosage) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.Dosage = Dosage;
    }

    /**
     * get the medicine
     * @return
     */
    public medicine getMedicine() {
        return medicine;
    }

    /**
     * set the medicine 
     * @param medicine
     */
    public void setMedicine(medicine medicine) {
        this.medicine = medicine;
    }

    /**
     * get the quantity
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * set the quantity 
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * get the dosage property
     * @return
     */
    public String getDosage() {
        return Dosage;
    }

    /**
     * set the dosage property
     * @param Dosage
     */
    public void setDosage(String Dosage) {
        this.Dosage = Dosage;
    }
    
    
}
