/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopApplication.Model.Strategies;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 *
 * @author Adam Edwards
 */
public class JsonDataRetriever implements RetrieverStrategy {
    private final String apiUrl;
    
    /**
     * Constructor class. Sets the incoming URL field to the current hard-coded value.
     */
    public JsonDataRetriever()
    {
        apiUrl = "http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/";
    }
    
    /**
     * Receives a response in Json format from the API hosted at the URL in the incomingURL field
     * @param parameters The parameters of the request, e.g. username strings and function calls.
     * @return StringBuffer object containing Json data to be parsed into a more readable format.
     */
    @Override
    public StringBuffer GetResponse(String parameters)
    {
        try
        {
            String input;
            String requestURL;
            
            requestURL = apiUrl + parameters;
            
            // Creates a new URL object and opens a connection
            URL urlObject = new URL(requestURL);
            HttpURLConnection con = (HttpURLConnection) urlObject.openConnection();
            
            // Appends a requested data return type and encoding to the request header
            // to ensure data is received in a readable format for the Json parser
            con.addRequestProperty("Content-Type", "application/json; charset=utf-8");
            
            // Debug code. Prints URL and response code.
            System.out.println(apiUrl);
            System.out.println(con.getResponseCode());
            
            // Creates stream readers to read the incoming Json data
            InputStreamReader inStream = new InputStreamReader(con.getInputStream());
            BufferedReader inBuffered = new BufferedReader(inStream);
            StringBuffer receivedResponse = new StringBuffer();
            
            // While data is still being read, append to the StringBuffer receivedResponse
            while ((input = inBuffered.readLine()) != null)
            {
                receivedResponse.append(input);
            }
            
            inBuffered.close();
            System.out.println(receivedResponse.toString());
            
            return receivedResponse;
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return null;
        
    }
}
    
