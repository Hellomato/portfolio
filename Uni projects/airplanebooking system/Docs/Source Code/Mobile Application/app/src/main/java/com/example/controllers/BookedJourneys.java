package com.example.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mobileapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import models.User.Customer;
import okhttp3.Response;

/***
 * This class is the Activity represents the activity responsible for viewing the
 * past tickets of the user. It displays the journeys by sending the information
 * regarding the tickets to Timetable.java as intent.
 */
public class BookedJourneys extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //global variables
    private Customer user;
    private static Response response = null;

    //front-end elements
    CardView activeBookings, pastBookings;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_journeys);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //card items in the GUI
        activeBookings = (CardView) findViewById(R.id.crdActiveBookings);
        pastBookings = (CardView) findViewById(R.id.crdPastBookings);

        //set listeners
        activeBookings.setOnClickListener((View.OnClickListener) this);
        pastBookings.setOnClickListener((View.OnClickListener) this);

        user =(Customer) getIntent().getSerializableExtra("user");

    }

    /***
     * Onclick detects what button the user clicked in the BookedJourneys page
     * @param v can be activeBookings or expired Bookings
     */
    @Override
    public void onClick( View v ) {

        String extraName;
        try {
            if (v == activeBookings) {
                user.myActiveJourneysID(user.getId());
                extraName = "active_journeys_id";
                displayTickets(extraName);
            } else if (v == pastBookings){
                user.myExpiredJourneysID(user.getId());
                extraName = "expired_journeys_id";
                displayTickets(extraName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /***
     * Decide which bookings are to retrieve depending on the button selected
     * @param extraName This will be the name of the journey array that will be sent as intent.
     * @throws IOException
     * @throws JSONException
     */
    public void displayTickets(String extraName) throws IOException, JSONException {

        if(response.code() == 200){

            //Array declaration and initialisation
            ArrayList<String> journeysID = new ArrayList<String>();
            ArrayList<String> ticketsID = new ArrayList<String>();

            //gets the body from the response received after using the user function
            JSONArray resultsArray = new JSONArray(response.body().string());

            //collect ids and ticket no in a JSONarray and split it in two different arrays
            for (int i=0;i<resultsArray.length();i++){
                JSONObject obj = resultsArray.getJSONObject(i);
                String journeyID = obj.optString("JOURNEY_ID");
                String ticketID = obj.optString("TICKET_NO");
                journeysID.add(journeyID);
                ticketsID.add(ticketID);
            }

            //start new activity and send the information gathered
            Intent intent = new Intent(BookedJourneys.this, Timetable.class);
            intent.putExtra(extraName,journeysID);
            intent.putExtra("tickets_id", ticketsID);
            intent.putExtra("user", user);
            startActivity(intent);

        }else {
            //Display alert Dialog on a failed attempt
            AlertDialog alertDialog = new AlertDialog.Builder(BookedJourneys.this).create();
            alertDialog.setTitle("Oops");
            alertDialog.setMessage("Something went wrong. Please retry later.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }


    /***
     * When back button is pressed, return to the dashboard.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(BookedJourneys.this, HomeScreen.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.booked_journeys, menu);
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

    /***
     * Side drawer-menu listener.
     * @param item Selected item.
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timetable) {
            Intent intent = new Intent(BookedJourneys.this, Timetable.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }else if (id == R.id.nav_search) {
            Intent intent = new Intent(BookedJourneys.this, TicketSearch.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }else if (id == R.id.nav_settings) {
            //TODO
        }else if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(BookedJourneys.this, HomeScreen.class);
            intent.putExtra("user",user);
            startActivity(intent);
        }else if(id == R.id.nav_logout){
            new AlertDialog.Builder(BookedJourneys.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ) {
                            Intent intent = new Intent(BookedJourneys.this, LoginController.class);
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


    /***
     * Static setter for htttp response result.
     * @param response
     */
    public static void setResponse( Response response ) {
        BookedJourneys.response = response;
    }
}
