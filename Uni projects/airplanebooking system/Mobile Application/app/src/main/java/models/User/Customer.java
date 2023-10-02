package models.User;

import com.example.controllers.BookedJourneys;
import com.example.controllers.HomeScreen;
import com.example.controllers.LoginController;
import com.example.controllers.SignUp;
import com.example.controllers.TimetableRecordView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.Strategies.GetRequestStrategy;
import models.Strategies.HttpRequest;
import models.Strategies.PostRequestStrategy;
import okhttp3.HttpUrl;
import okhttp3.Response;

import static android.provider.Telephony.Carriers.PASSWORD;

/***
 * Customer class used to instantiate the Customer object which will be present in most of the activities to
 * carry out basic operations.
 */
public class Customer implements Serializable {

    //global variables
    private String username;
    private String password;
    private int id;

    /***
     * constructor method takes username and password as arguments
     * @param username Username string used for login parameters in an API call, used for user validation.
     * @param password Password string used for login parameters in an API call, used for user validation.
     */
    public Customer(String username, String password ) {
        this.username = username;
        this.password = password;

    }

    /***
     * Get ID of a given user
     */
    public void requestID() {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    //this needs to be changed according to the correct api path
                    HttpUrl url = HttpUrl.parse("http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/CUSTOMER/getID?username=" + username);
                    //send http post request
                    HttpRequest request = new HttpRequest(new GetRequestStrategy(), url, null);
                    Response response = request.Send();
                    Gson gson = new Gson();
                    id = gson.fromJson(response.body().string(), int.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //start thread
        t1.start();

        //wait until it is dead
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the active tickets associated with the user.
     * @param customerID ID of the user object
     */
    public void myActiveJourneysID( final int customerID ) {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    //this needs to be changed according to the correct api path
                    HttpUrl url = HttpUrl.parse("http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/TICKETS/TICKETByID?id=" + customerID);

                    //send http post request
                    HttpRequest request = new HttpRequest(new GetRequestStrategy(), url, null);
                    Response response = request.Send();

                    //pass response to login activity
                    BookedJourneys.setResponse(response);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //start thread
        t1.start();

        //wait until it is dead
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the expired tickets associated with a specific user ID.
     * @param customerID ID of the user object
     */
    public void myExpiredJourneysID( final int customerID ) {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    //this needs to be changed according to the correct api path
                    HttpUrl url = HttpUrl.parse("http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/EXPIRED_TICKETS/EXPIRED_TICKETByID?id=" + customerID);

                    //send http post request
                    HttpRequest request = new HttpRequest(new GetRequestStrategy(), url, null);
                    Response response = request.Send();

                    //pass response to login activity
                    BookedJourneys.setResponse(response);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //start thread
        t1.start();

        //wait until it is dead
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /***
     * this login method sends a http request to the API's controller and passes the response
     * code to the main activity. The whole code is run in asynchronous
     * @param username Username string used for login parameters in an API call, used for user validation.
     * @param password Password string used for login parameters in an API call, used for user validation.
     */
    public void Login( final String username, final String password ) {

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    //this needs to be changed according to the correct api path
                    HttpUrl url = HttpUrl.parse("http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/login");

                    //username and password are here serialized into a JSON Object
                    Map<String, String> data = new HashMap<>();
                    data.put("USERNAME", username);
                    data.put("PASSWORD", password);
                    JSONObject jsonObject = new JSONObject(data);

                    //send http post request
                    HttpRequest request = new HttpRequest(new PostRequestStrategy(), url, jsonObject);
                    Response response = request.Send();

                    //pass response to login activity
                    LoginController.setResponse(response);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //start thread
        t1.start();

        //wait until it is dead
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /***
     * Send POST request to create a new Customer record in the database.
     * @param information Array containing the information of the input field in the Signup screen.
     */
    public static void SignUp( final ArrayList information ) {

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    //create CustomerRecord object
                    CustomerRecord record = new CustomerRecord();

                    //set fields of the CustomerRecord object
                    record.setCUSTOMER_ID(0);
                    record.setCUSTOMER_FORENAME(information.get(0).toString());
                    record.setCUSTOMER_SURNAME(information.get(1).toString());
                    record.setUSERNAME("cu"+information.get(2).toString());
                    record.setPASSWORD(information.get(3).toString());
                    record.setDATE_OF_BIRTH(information.get(4).toString());
                    record.setPOST_CODE(information.get(5).toString());
                    record.setADDRESS_LINE(information.get(6).toString());

                    //http address of the Customer functions of the API
                    HttpUrl urlString = HttpUrl.parse("http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/CUSTOMERs");

                    //create Gson object to parse the data
                    Gson gson = new Gson();
                    String data = gson.toJson(record);
                    //convert record to string
                    JSONObject json = null;
                    try {
                        //and then to JSONObject
                        json = new JSONObject(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //send http post request
                    HttpRequest request = new HttpRequest(new PostRequestStrategy(), urlString, json);
                    Response response = request.Send();

                    //pass response to signup activity
                    SignUp.setResponse(response);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //start thread
        t1.start();

        //wait until it is dead
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /***
     * Method to add a purchased ticket to the database
     * @param journeyID used to identify the journey purchased
     * @param price the price of ticket
     */
    public void purchaseTicket( final String journeyID, final String price ) {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {

                    HttpUrl url = HttpUrl.parse("http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/TICKETs");

                    Map<String, String> data = new HashMap<>();
                    data.put("JOURNEY_ID", journeyID);
                    data.put("CUSTOMER_ID", Integer.toString(id));
                    data.put("PRICE", price);
                    data.put("TICKET_NO", "0");
                    JSONObject jsonObject = new JSONObject(data);

                    HttpRequest request = new HttpRequest(new PostRequestStrategy(), url, jsonObject);
                    Response response = request.Send();
                    TimetableRecordView.setResponse(response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // Getter methods

    /**
     * Getter method. Returns password property.
     * @return password String.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Getter method. Returns firstName property.
     * @return firstName String.
     */


    /**
     * Getter method. Returns username property.
     *
     * @return username String.
     */
    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }


}
