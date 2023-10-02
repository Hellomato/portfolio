package com.example.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ListenerInterfaces.OnTimetableRecordListener;
import com.example.mobileapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import models.DataObserverTools.DataObserver;
import models.Timetable.TimetableHolder;
import models.Timetable.TimetableRecord;
import models.User.Customer;

/**
 * Activity java class responsible for showing the lists of any journey type across the
 * application. The activity is refreshed only when the journey shown are from the General Timetable
 * or from the feature to search for a journey.
 */
public class Timetable extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnTimetableRecordListener, DataObserver, DrawerLayout.DrawerListener {

    //global variables
    private RecyclerView TimetableRecyclerView;
    private List<TimetableRecord> timetable;
    private static Timer timer = new Timer();
    TimetableRefreshHandler timetableHandler;
    private TimetableHolder holder;
    private TimetableAdapter adapter;

    //global variables passed as intents
    private ArrayList<String> ticketsID;
    private ArrayList<String> activeJourneysID;
    private ArrayList<String> expiredJourneysID;
    private String parameter;
    private String ticketID;
    private Customer user;




    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        timetableHandler = new TimetableRefreshHandler();

        timetable = new ArrayList<>();
        holder = new TimetableHolder();
        adapter = new TimetableAdapter(this);

        holder.RegisterObserver(this);


        setContentView(R.layout.activity_timetable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.addDrawerListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TimetableRecyclerView = (RecyclerView) findViewById(R.id.timetable_recycler);
        TimetableRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TimetableRecyclerView.setAdapter(adapter);

        //get intents of possible IDs of tickets
        activeJourneysID = getIntent().getStringArrayListExtra("active_journeys_id");
        expiredJourneysID = getIntent().getStringArrayListExtra("expired_journeys_id");
        parameter = getIntent().getStringExtra("parameter");

        //get the tickets' IDs
        ticketsID = getIntent().getStringArrayListExtra("tickets_id");
        ticketID = getIntent().getStringExtra("ticket_id");

        //get user extra from previous activity
        user = (Customer) getIntent().getSerializableExtra("user");

        showJourneys();
    }

    /***
     * Decide what is the search query to be executed.
     */
    public void showJourneys() {

        //show active tickets if there is the correspondent intent package
        if (activeJourneysID != null) {
            setTitle("Active Tickets");
            holder.retrieveActiveJourneys(activeJourneysID);

        }
        //expired tickets
        else if (expiredJourneysID != null) {
            setTitle("Expired Tickets");
            holder.retrieveExpiredJourneys(expiredJourneysID);
        }
        //all available journeys
        else {
            //start timer to refresh the page every 15 seconds
            runAsynchronous();

            if (parameter != null) {
                setTitle("Search Tickets");
                holder.RetrieveTimetable(parameter);
            } else {
                setTitle("General Timetable");
                holder.RetrieveTimetable("");
            }
        }
    }

    /***
     * Stop the Timer from running and clear its memory space.
     */
    public void stopAsynchronous() {
        //mark all tasks associated with this timer as cancelled
        timer.cancel();
        //remove all cancelled tasks
        timer.purge();
    }

    /***
     * Use timer.scheduleAtFixedRate to schedule a the Activity refreshing task.
     */
    private void runAsynchronous() {

        if (activeJourneysID == null && expiredJourneysID == null) {
            stopAsynchronous();

            timer = new Timer();
            //launch scheduler
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    //the repeated task starts here
                    Intent intent = new Intent(Timetable.this, Timetable.class);
                    intent.putExtra("user", user);
                    intent.putExtra("parameter", parameter);
                    startActivity(intent);
                }
                //first parameter is the time to delay the first execution
                //second parameter is the delay between the termination of one execution and the commencement of the next
                //both are in milliseconds
            }, 10000, 15000);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            //cleanse the timer object from previous task
            if (ticketsID != null) {
                stopAsynchronous();
                Intent intent = new Intent(Timetable.this, BookedJourneys.class);
                intent.putExtra("user", user);
                startActivity(intent);
            } else {
                stopAsynchronous();
                Intent intent = new Intent(Timetable.this, HomeScreen.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timetable, menu);
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
        if (id == R.id.nav_search) {
            stopAsynchronous();
            Intent intent = new Intent(Timetable.this, TicketSearch.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_dashboard) {
            stopAsynchronous();
            Intent intent = new Intent(Timetable.this, HomeScreen.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            //TODO
        } else if (id == R.id.nav_tickets) {
            stopAsynchronous();
            Intent intent = new Intent(Timetable.this, BookedJourneys.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            stopAsynchronous();
            new AlertDialog.Builder(Timetable.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ) {
                            Intent intent = new Intent(Timetable.this, LoginController.class);
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

    /**
     * Triggers when a timetable record is pressed by the user.
     *
     * @param position int representing the specific record the user selected.
     */
    @Override
    public void onRecordClick( int position ) {
        stopAsynchronous();

        System.out.println("OnRecordClick has run!");
        System.out.println("Timetable record selected: " + position);

        // Get the timetable record data
        List<TimetableRecord> records;
        records = holder.getTimetable();

        // Create a new intent for a new activity
        Intent intent = new Intent(Timetable.this, TimetableRecordView.class);


        // Bundle parameters from the selected record entry to send to the next activity for data output.
        Bundle b = new Bundle();
        b.putString("Departure_Station", records.get(position).getDeparture_Station());
        b.putString("Arrival_Station", records.get(position).getArrival_Station());
        b.putString("Departure_Time", records.get(position).getDeparture_Time());
        b.putString("Arrival_Time", records.get(position).getArrival_Time());
        b.putInt("Journey_No", records.get(position).getJourney_No());

        b.putFloat("Journey_Price",records.get(position).getJOURNEY_PRICE());

        // Place parameters in the intent and start the new activity.s
        intent.putExtras(b);
        intent.putExtra("user", user);

        //send the ticket number as intent
        if (activeJourneysID != null || expiredJourneysID != null) {
            ticketID = ticketsID.get(position);
            intent.putExtra("ticket_id", ticketID);
            intent.putExtra("tickets_id", ticketsID);
            intent.putExtra("active_journeys_id", activeJourneysID);
            intent.putExtra("expired_journeys_id", expiredJourneysID);
        } else {
            intent.putExtra("parameter", parameter);
        }

        startActivity(intent);
    }

    @Override
    public void Update() {
        timetableHandler.obtainMessage(1).sendToTarget();
    }

    @Override
    public void onDrawerSlide( @NonNull View view, float v ) {

    }

    /***
     * Stop the refreshing when the drawer menu is opened.
     * @param view
     */
    @Override
    public void onDrawerOpened( View view ) {
        //cleanse the timer object from previous task to allow the user
        //to see the options in the menu without refreshing
        stopAsynchronous();
    }

    @Override
    public void onDrawerClosed( View view ) {
        //start refreshing again upon closure of the drawer
        runAsynchronous();

    }

    @Override
    public void onDrawerStateChanged( int i ) {

    }

    // Internal class used to handle update calls from external threads.
    class TimetableRefreshHandler extends Handler {
        public void handleMessage( Message msg ) {
            updateTimetableList();
        }

        /***
         * Updates the Timetable UI from the retrieved data when called.
         */
        public void updateTimetableList() {
            System.out.println("Altering View.");
            timetable = holder.getTimetable();
            adapter.notifyDataSetChanged();

            TextView retrieveText = findViewById(R.id.retrieve_data_text);
            retrieveText.setVisibility(View.GONE);
        }
    }

    ;

    // Internal class used as an adapter for the ViewHolder class.
    class TimetableAdapter extends RecyclerView.Adapter<TimetableViewHolder> {
        private OnTimetableRecordListener onTimetableRecordListener;

        /**
         * Constructor class. Constructs the adapter used to create the ViewHolder class.
         *
         * @param onTimetableRecordListener Listener class used to listen for user selection of specific timetable entries.
         */
        public TimetableAdapter( OnTimetableRecordListener onTimetableRecordListener ) {
            super();
            this.onTimetableRecordListener = onTimetableRecordListener;
        }

        @NonNull
        @Override
        public TimetableViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup, int i ) {
            return new TimetableViewHolder(viewGroup, onTimetableRecordListener);
        }

        @Override
        public void onBindViewHolder( @NonNull TimetableViewHolder userViewHolder, int i ) {
            userViewHolder.bind(timetable.get(i));
        }

        @Override
        public int getItemCount() {
            return timetable.size();
        }
    }

    // ViewHolder used to set values for each entry in the RecyclerView.
    class TimetableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mDepartureTimeView;
        private TextView mArrivalTimeView;
        private TextView mJourneyNoView;
        private TextView mDepartureStationView;
        private TextView mArrivalStationView;
        private TextView mPriceView;

        OnTimetableRecordListener onTimetableRecordListener;

        /**
         * Constructor class. Constructs the TimetableViewHolder class.
         *
         * @param container                 container used to add timetable records to the RecyclerView
         * @param onTimetableRecordListener Listener class used to listen for user selection of specific timetable entries.
         */
        public TimetableViewHolder( ViewGroup container, OnTimetableRecordListener onTimetableRecordListener ) {
            super(LayoutInflater.from(Timetable.this).inflate(R.layout.activity_recycler_timetable_record, container, false));
            mDepartureTimeView = (TextView) itemView.findViewById(R.id.departure_time_view);
            mArrivalTimeView = (TextView) itemView.findViewById(R.id.arrival_time_view);
            mJourneyNoView = (TextView) itemView.findViewById(R.id.journey_no_view);
            mDepartureStationView = (TextView) itemView.findViewById(R.id.departure_station_view);
            mArrivalStationView = (TextView) itemView.findViewById(R.id.arrival_station_view);
            mPriceView = (TextView) itemView.findViewById(R.id.price_view);

            this.onTimetableRecordListener = onTimetableRecordListener;
            itemView.setOnClickListener(this);
        }

        /**
         * Binds values from an input TimetableRecord to the text fields of the TimetableRecordView UI element.
         *
         * @param record The TimetableRecord object to pull data from.
         */
        public void bind( TimetableRecord record ) {
            String journeyNumber;
            journeyNumber = Integer.toString(record.getJourney_No());

            String formattedDepartureTime = "";
            String formattedArrivalTime = "";

            for (String s : record.getDeparture_Time().split("T")) {
                formattedDepartureTime += s + " / ";
            }

            formattedDepartureTime += " -> ";

            for (String s : record.getArrival_Time().split("T")) {
                formattedArrivalTime += s + " / ";
            }


            mDepartureTimeView.setText(formattedDepartureTime);
            mArrivalTimeView.setText(formattedArrivalTime);
            mJourneyNoView.setText(journeyNumber);
            mDepartureStationView.setText(record.getDeparture_Station() + " -> ");
            mArrivalStationView.setText(record.getArrival_Station());
            mPriceView.setText("£" + Float.toString(record.getJOURNEY_PRICE()));
        }

        /**
         * Triggers when an element in the RecyclerView is clicked and sends the selected item as a parameter for onRecordClick.
         *
         * @param v View connected to the onClick element.
         */
        @Override
        public void onClick( View v ) {
            onTimetableRecordListener.onRecordClick(getAdapterPosition());
        }
    }

}