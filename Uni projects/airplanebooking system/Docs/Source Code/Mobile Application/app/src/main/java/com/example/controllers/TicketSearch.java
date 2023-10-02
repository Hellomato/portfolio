package com.example.controllers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.mobileapplication.R;

import java.util.ArrayList;
import java.util.Calendar;

import models.User.Customer;

/***
 * Class for the activity to search for available journeys.
 */
public class TicketSearch extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    // Global variables
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Customer user;
    Button btnDatePicker, btnTimePicker, btnSearch;
    EditText txtDate, txtTime, txtFrom, txtTo;
    Spinner spTravelSettings;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btnDatePicker = (Button) findViewById(R.id.btnDate);
        btnTimePicker = (Button) findViewById(R.id.btnTime);
        spTravelSettings = findViewById(R.id.spTravelSettings);
        txtDate = (EditText) findViewById(R.id.inDate);
        txtTime = (EditText) findViewById(R.id.inTime);
        txtFrom = (EditText) findViewById(R.id.inFrom);
        txtTo = (EditText) findViewById(R.id.inTo);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        //get user from previous activity
        user = (Customer) getIntent().getSerializableExtra("user");

        //set listeners
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        //populate the spinner in the view
        populateSpinner();
    }


    /**
     * Populate the 'spTime' spinner.
     */
    void populateSpinner() {

        //create settings array
        ArrayList<String> list = new ArrayList<>();
        list.add("Arrive on");
        list.add("Depart by");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, list);

        spTravelSettings.setAdapter(adapter);

    }

    /***
     * Pass the search criteria to the Timetable View as an intent.
     */
    public void searchJourney( android.view.View g ) {

        btnSearch.setEnabled(false);

        String parameter = "";

        String departurePoint = txtFrom.getText().toString();
        String destinationPoint = txtTo.getText().toString();

        String toggleTime = spTravelSettings.getSelectedItem().toString();

        String time = txtTime.getText().toString();

        // If the time selector has no selected value add a default time.
        if (time.isEmpty())
        {
            if (toggleTime == "Arrive on")
            {
                time = "00:00";
            }
            else
            {
                time = "23:59";
            }
        }
        else
        {
            // Split the time value
            String[] splitTime;
            splitTime = time.split(":");

            // if the hour value is not two digits long, add a leading zero
            if (splitTime[1].length() < 2)
            {
                splitTime[1] = "0" + splitTime[1];
            }

            // if the minute value is not two digits long, add a leading zero
            if (splitTime[0].length() < 2)
            {
                splitTime[0] = "0" + splitTime[0];
            }

            // Add the reformatted hour and minute values back into the original string, ready for a request.
            time = splitTime[0] + ":" + splitTime[1];
        }

        String datetime = txtDate.getText().toString() +" "+ time;

        if (toggleTime == "Arrive on") {
            parameter += "/search?journeyNo=&departureStation=" + departurePoint + "&arrivalStation="
                    + destinationPoint + "&departureTime=&arrivalTime=" + datetime;
        } else if (toggleTime == "Depart by") {
            parameter += "/search?journeyNo=&departureStation=" + departurePoint + "&arrivalStation="
                    + destinationPoint + "&departureTime=" + datetime + "&arrivalTime=";
        }

        // create intent
        Intent intent = new Intent(TicketSearch.this, Timetable.class);

        //attach variables to new intent
        intent.putExtra("parameter", parameter);
        intent.putExtra("user", user);
        startActivity(intent);

        btnSearch.setEnabled(true);
    }

    @Override
    public void onClick( View v ) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            //show date picker
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet( DatePicker view, int year,
                                               int monthOfYear, int dayOfMonth ) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet( TimePicker view, int hourOfDay,
                                               int minute ) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticket_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timetable) {
            Intent intent = new Intent(TicketSearch.this, Timetable.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(TicketSearch.this, HomeScreen.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_tickets) {
            Intent intent = new Intent(TicketSearch.this, BookedJourneys.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            //TODO
        }else if(id == R.id.nav_logout){
            new AlertDialog.Builder(TicketSearch.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ) {
                            Intent intent = new Intent(TicketSearch.this, LoginController.class);
                            startActivity(intent);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
