/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopApplication.Model.Strategies;

/**
 *
 * @author Adam Edwards
 */
public interface RetrieverStrategy {

    /**
     * Receives a response from the API hosted at the URL in the incomingURL field
     * @param parameters The parameters of the request, e.g. username strings and function calls.
     * @return StringBuffer object containing data to be parsed into a more readable format.
     */
    public StringBuffer GetResponse(String parameters);
    
}
