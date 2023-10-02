/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopapplication.Model.Timetable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import desktopApplication.Model.Strategies.JsonDataRetriever;
import desktopApplication.Model.Timetable.TimetableEntry;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gabryel
 */
public class PersonalTimetable {
    private ArrayList<TimetableEntry> personalTimetable;
    /**
     * Constructor method
     */
    public PersonalTimetable() {
        personalTimetable = new ArrayList<>();
    }

    /**
     * Removes everything from the list
     */
    public void clearList(){
        personalTimetable.clear();
    }
    /**
     * Updates the personal Timetable to be filled with the correct values
     * @param parameter the idea of the Staff account
     */
    public void update(String parameter){
        Gson gson = new Gson();
        clearList();
        JsonDataRetriever  retriever = new JsonDataRetriever();
        Type listType = new TypeToken<List<desktopApplication.Model.Timetable.TimetableEntry>>() {}.getType();

        personalTimetable = new Gson().fromJson(retriever.GetResponse("TIMETABLE_RECORDS/STAFF?staffNo="+ parameter).toString(), listType);
    }
    /**
     * This gets the personalTimetable
     * @return personalTimetalbe
     */
    public List<TimetableEntry> getPersonalTimetable() {
        return personalTimetable;
    }

}
