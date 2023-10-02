package models.Timetable;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import models.DataObserverTools.DataObservable;
import models.DataObserverTools.DataObserver;
import models.Strategies.GetRequestStrategy;
import models.Strategies.HttpRequest;
import okhttp3.HttpUrl;
import okhttp3.Response;

public class TimetableHolder implements DataObservable
{
    private List<TimetableRecord> timetable;
    private List<DataObserver> observers;

    private Type timetableListType = new TypeToken<ArrayList<TimetableRecord>>(){}.getType();
    private String urlString = "http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/api/TIMETABLE_RECORDS";

    public TimetableHolder()
    {
        observers = new ArrayList<>();
    }

    /**
     * Uses the GetRequestStrategy method and the GSON parsing class to retrieve and store timetable information from the API.
     * @param parameters A list of specific parameters for a request. Passed in string format.
     */
    public void RetrieveTimetable(String parameters)
    {
        //this needs to be changed according to the correct api path
        final HttpUrl url = HttpUrl.parse(urlString + parameters);

        Thread t1 = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    //send http get request
                    HttpRequest request = new HttpRequest(new GetRequestStrategy(), url, null);
                    Response response = request.Send();

                    // Parse the incoming JSON data into individual TimetableRecord objects.
                    Gson gson = new Gson();
                    timetable = gson.fromJson(response.body().string(), timetableListType);

                    NotifyObservers();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        //start thread
        t1.start();
    }

    public void retrieveActiveJourneys(final ArrayList<String>  journeysID)
    {

        Thread t1 = new Thread(new Runnable()
        {
            public void run()
            {

                timetable = new ArrayList<TimetableRecord>();
                try
                {
                    for (String id : journeysID){

                        HttpUrl url = HttpUrl.parse(urlString + "/search?journeyNo="+id+"&departureStation=&arrivalStation=" +
                                "&departureTime=&arrivalTime=");

                        //send http get request
                        HttpRequest request = new HttpRequest(new GetRequestStrategy(), url, null);
                        Response response = request.Send();

                        // Parse the incoming JSON data into individual TimetableRecord objects.
                        String mJsonString = response.body().string();
                        JsonParser parser = new JsonParser();
                        JsonElement mJson = parser.parse(mJsonString);
                        Gson gson = new Gson();
                        ArrayList record = gson.fromJson(mJson, timetableListType);
                        timetable.addAll(record);

                    }

                    NotifyObservers();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        //start thread
        t1.start();
    }

    public void retrieveExpiredJourneys(final ArrayList<String>  journeysID)
    {

        Thread t1 = new Thread(new Runnable()
        {
            public void run()
            {
                timetable = new ArrayList<TimetableRecord>();
                try
                {
                    for (String id : journeysID){

                        HttpUrl url = HttpUrl.parse("http://web.socem.plymouth.ac.uk/IntProj/PRCS252J/" +
                                "api/HISTORY_RECORDS?journeyNo="+id);

                        //send http get request
                        HttpRequest request = new HttpRequest(new GetRequestStrategy(), url, null);
                        Response response = request.Send();

                        // Parse the incoming JSON data into individual TimetableRecord objects.
                        String mJsonString = response.body().string();
                        JsonParser parser = new JsonParser();
                        JsonElement mJson = parser.parse(mJsonString);
                        Gson gson = new Gson();
                        ArrayList record = gson.fromJson(mJson, timetableListType);
                        timetable.addAll(record);

                    }

                    NotifyObservers();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        //start thread
        t1.start();
    }




    /**
     * Registers a DataObserver object to this DataObservable. Adds the DataObserver object to the observers ArrayList.
     * @param o DataObserver object to register.
     */
    @Override
    public void RegisterObserver(DataObserver o)
    {
        observers.add(o);
    }

    /**
     * Removes a DataObserver object from the observers ArrayList.
     * @param o DataObserver object to remove.
     */
    @Override
    public void RemoveObserver(DataObserver o)
    {
        if (observers.contains(o))
        {
            observers.remove(o);
        }
    }

    /**
     * Runs the Update method in every registered DataObserver object to notify them of changes.
     */
    @Override
    public void NotifyObservers()
    {
        for (DataObserver o : observers)
        {
            o.Update();
        }
    }

    // Getter methods

    /**
     * Getter method. Gets the timetable List object.
     * @return List object containing timetable data. Must be called after RetrieveData is called or will result in a null reference.
     */
    public List<TimetableRecord> getTimetable() {
        return timetable;
    }

}