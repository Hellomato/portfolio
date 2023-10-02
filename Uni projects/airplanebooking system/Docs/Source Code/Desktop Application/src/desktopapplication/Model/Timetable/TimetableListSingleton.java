/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopApplication.Model.Timetable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import desktopApplication.Model.Strategies.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A class to hold multiple timetable entries
 * 
 * @author Eugen
 */
public class TimetableListSingleton {
    
    private static TimetableListSingleton uniqueInstance = null;
    
    private ArrayList<TimetableEntry> timetable;


    /***
     * Retrieve the journey's information and stores them in a list
     * 
     * @throws IOException 
     */
    private TimetableListSingleton()
    {
        Gson gson = new Gson();
        
        JsonDataRetriever  retriever = new JsonDataRetriever();

        Type listType = new TypeToken<List<TimetableEntry>>() {}.getType();

        timetable = new Gson().fromJson(retriever.GetResponse("TIMETABLE_RECORDS").toString(), listType);
    }
    
    public List<TimetableEntry> getTimetable() {
        return timetable;
    }

    public static TimetableListSingleton getInstance() throws IOException 
    {
        if (uniqueInstance == null)
        {
            uniqueInstance = new TimetableListSingleton();
        }
        return uniqueInstance;
    }

}
