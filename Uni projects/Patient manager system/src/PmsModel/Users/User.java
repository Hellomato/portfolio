/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PmsModel.Users;


/**
 * abstract user class that defines what a user needs
 * @author Adam Edwards
 */
public abstract class User implements java.io.Serializable {

    /**
     * users first name
     */
    protected String name;

    /**
     * users surname
     */
    protected String surname;

    /**
     * users id
     */
    protected String ID;

    /**
     * users age
     */
    protected int age;

    /**
     * users gender
     */
    protected String gender;

    /**
     * users password
     */
    protected String password; 

    /**
     * if a user has a notification
     */
    protected boolean hasNotification;

    /**
     * Notification message
     */
    protected String notificationMessage;

    /**
     * check if user has a notification
     * @return
     */
    public boolean isHasNotification() {
        return hasNotification;
    }

    /**
     * set if the user has a notification
     * side note this can be done when notificaiton message is changed for easier code use
     * @param hasNotification
     */
    public void setHasNotification(boolean hasNotification) {
        this.hasNotification = hasNotification;
    }

    /**
     * get the notification message
     * @return
     */
    public String getNotificationMessage() {
        return notificationMessage;
    }

    /**
     * set the notification message
     * @param notificationMessage
     */
    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }
    
    /**
     * get patient firstname
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * get patient surname
     * @return
     */
    public String getSurname() {
        return surname;
    }

    /**
     * get patient password
     * secure innit?
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * get patient id
     * @return
     */
    public String getID() {
        return ID;
    }

    /**
     * get patient age
     * @return
     */
    public int getAge() {
        return age;
    }

    /**
     * return patient gender
     * @return
     */
    public String getGender() {
        return gender;
    }

    
}
