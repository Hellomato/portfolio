/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 * observer
 * @author Adam Edwards
 */
public interface Observer {
    
    /**
     * upadte observerable
     * @param Feedback
     * @param value
     */
    public void update(String Feedback, String value);
}
