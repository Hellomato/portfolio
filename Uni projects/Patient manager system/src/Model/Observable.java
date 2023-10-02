/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Observer;

/**
 * observable
 * @author Hellomato
 */
public interface Observable {

    /**
     * register a new observer
     * @param observer
     */
    public void registerObserver(Observer observer);

    /**
     * remove observer
     * @param observer
     */
    public void removeObserver(Observer observer);

    /**
     * notify observer
     */
    public void notifyObserver();

    
}
