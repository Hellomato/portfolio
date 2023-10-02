/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopApplication.Model.Timetable;

/**
 * A class to hold the journeys' information
 * 
 * @author GMW99
 */

public class TimetableEntry
{
    private int JOURNEY_NO;
    private String DEPARTURE_STATION;
    private String ARRIVAL_STATION;
    private String DEPARTURE_TIME;
    private String ARRIVAL_TIME;
    private String JOURNEY_COMMENTS;
    private String JOURNEY_PRICE;


    // Getter methods

    public String getJOURNEY_PRICE() {
        return JOURNEY_PRICE;
    }

    /**
     * Getter method. Returns Journey_No property.
     * @return Journey_No integer.
     */
    public int getJourney_No() {
        return JOURNEY_NO;
    }

    /**
     * Getter method. Returns Departure_Station property.
     * @return Departure_Station String.
     */
    public String getDeparture_Station() {
        return DEPARTURE_STATION;
    }

    /**
     * Getter method. Returns Arrival_Station property.
     * @return Arrival_Station String.
     */
    public String getArrival_Station() {
        return ARRIVAL_STATION;
    }

    /**
     * Getter method. Returns Departure_Time property.
     * @return Departure_Time String.
     */
    public String getDeparture_Time() {
        return DEPARTURE_TIME;
    }

    /**
     * Getter method. Returns Arrival_Time property.
     * @return Arrival_Time String.
     */
    public String getArrival_Time() {
        return ARRIVAL_TIME;
    }

    /**
     * Getter method. Returns Journey_Comments property.
     * @return Journey_Comments String.
     */
    public String getJourney_Comments() {
        return JOURNEY_COMMENTS;
    }
}