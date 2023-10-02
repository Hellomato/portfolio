package com.example.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mobileapplication.R;

import models.User.Customer;

/***
 * The mobile application Dashboard activity class.
 */
public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //global variables
    private Customer user;
    TextView txtGreeting;
    CardView timetable, search, settings, myTickets;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtGreeting = (TextView) findViewById(R.id.txtGreeting);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        timetable = (CardView) findViewById(R.id.crdTimetable);
        search = (CardView) findViewById(R.id.crdSearch);
        settings = (CardView) findViewById(R.id.crdSettings);
        myTickets = (CardView) findViewById(R.id.crdTickets);

        timetable.setOnClickListener((View.OnClickListener) this);
        search.setOnClickListener((View.OnClickListener) this);
        settings.setOnClickListener((View.OnClickListener) this);
        myTickets.setOnClickListener((View.OnClickListener) this);

        //get user object from previous activity
        user = (Customer) getIntent().getSerializableExtra("user");
        //display greeting
        displayCustomer();

    }

    /***
     * Simple method to display the username of the user.
     */
    public void displayCustomer() {

        String username = user.getUsername();
        txtGreeting.append(username.substring(2));

    }

    /***
     * On click listener when a button in the dashboard is pressed.
     * @param v
     */
    @Override
    public void onClick( View v ) {
        if (v == timetable) {
            Intent intent = new Intent(HomeScreen.this, Timetable.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (v == settings) {
            new AlertDialog.Builder(HomeScreen.this)
                    .setTitle("Section not available")
                    .setMessage("This menu represents a proof of concept and has not been implemented yet.")
                    .show();
        } else if (v == search) {
            Intent intent = new Intent(HomeScreen.this, TicketSearch.class);
            intent.putExtra("user", user);
            startActivity(intent);

        } else if (v == myTickets) {
            Intent intent = new Intent(HomeScreen.this, BookedJourneys.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.

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
     * Side-menu items listener.
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item ) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timetable) {
            Intent intent = new Intent(HomeScreen.this, Timetable.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
        else if (id == R.id.nav_search) {
            Intent intent = new Intent(HomeScreen.this, TicketSearch.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            //TODO
        } else if (id == R.id.nav_tickets) {
            Intent intent = new Intent(HomeScreen.this, BookedJourneys.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }else if(id == R.id.nav_logout){
            new AlertDialog.Builder(HomeScreen.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int which ) {
                        Intent intent = new Intent(HomeScreen.this, LoginController.class);
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
