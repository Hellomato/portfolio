 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopApplication.Model.Users;

import desktopApplication.Model.Strategies.*;
import desktopapplication.Model.Strategies.PostRequest;
import desktopapplication.Model.Strategies.GetRequest;
import java.util.ArrayList;
import okhttp3.HttpUrl;
import okhttp3.Response;
import com.google.gson.Gson;

/**
 * A class that Staff that can be inherited for each new staff member added
 * @author gabryel
 */
public class Staff {
    protected String name;
    protected String id;
    protected String password;

    public Staff(String name, String password) {
        this.name = name;
        this.password = password;
    }

    /***
     * Sends a post a request to the api with user details in the body
     * @return true if the response code is 200 otherwise false;
     */
    protected boolean LogOn()
    {        
        PostRequest request = new PostRequest();
        
        HttpUrl url = HttpUrl.parse("http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/login");
        String data = "{\"username\": \"" + name + "\", \"password\": \"" + password + "\"}";
        
        Response response = request.Request(url, data);
        
        int responseCode = response.code();
        
        // Checks if the received response from the API query is "1".
        if (responseCode == 200)
        {
            return true;
            
        }
        else
        {
            return false;
        }   
    }
    /***
     * Gets the ID of a given username
     */
    public String requestID(){
        String uri = "STAFF/getID?username="+name;
        Gson gson = new Gson();
        
        JsonDataRetriever  retriever = new JsonDataRetriever();
        id = new Gson().fromJson(retriever.GetResponse(uri).toString(),String.class);
        return id;
    }
    
        // Getters
    /**
     * Returns password
     * @return returns the set password
     */
    protected String getPassword() {
        return password;
    }
    /**
    * gets the Name of the Person 
    * @return the name
    */
    public String getName() {
        return name;
    }
    /**
    * gets the ID of the Person 
    * @return the ID
    */
    protected String getId() {
        return id;
    }   
    // Setters
    /**
     * Sets the password
     * @param password password to be set
     */
    protected void setPassword(String password) {
        this.password = password;
    }
    /**
    * sets the Name of the Person 
    * @param name takes in the name
    */
    protected void setName(String name) {
        this.name = name;
    }
    
}
