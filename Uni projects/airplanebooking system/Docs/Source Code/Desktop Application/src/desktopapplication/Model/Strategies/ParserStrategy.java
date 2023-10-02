/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopApplication.Model.Strategies;

import java.util.ArrayList;

/**
 *
 * @author Adam Edwards
 */
public interface ParserStrategy {

    /**
     * Parses an incoming StringBuffer object into separated string arrays in an ArrayList. Each string array contains the distinct elements of the individual record.
     * @param response The StringBuffer object to parse.
     * @return ArrayList of String arrays containing distinct elements of records.
     */
    public ArrayList<String[]> parse(StringBuffer response);
}
