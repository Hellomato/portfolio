package com.example.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.Config.Config;
import com.example.mobileapplication.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import models.Strategies.HttpRequest;
import models.Strategies.PostRequestStrategy;

import models.User.Customer;
import okhttp3.Response;

/***
 * Allow the user to see more information about specific journeys, to buy the journeys, or to search
 * for similar journeys.
 */
public class TimetableRecordView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //global variables passed as intents
    private ArrayList<String> activeJourneysID;
    private ArrayList<String> expiredJourneysID;
    private ArrayList<String> ticketsID;
    private String parameter;
    private Button bookTicket;
    private String ticketID;
    private Customer user;

    //global variables
    private int journeyID;
    private float price;
    private static Response httpresponse = null;

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_record_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        activeJourneysID = getIntent().getStringArrayListExtra("active_journeys_id");
        expiredJourneysID = getIntent().getStringArrayListExtra("expired_journeys_id");
        parameter = getIntent().getStringExtra("parameter");

        //get the tickets' IDs
        ticketsID = getIntent().getStringArrayListExtra("tickets_id");


        // Code to populate the TextView objects on the content_timetable_record_view.xml file.
        final Bundle b;
        final TextView mJourneyNo;
        final TextView mDepartureStation;
        final TextView mArrivalStation;
        TextView mDepartureTime;
        TextView mDepartureDate;
        TextView mArrivalTime;
        final TextView mArrivalDate;
        TextView mPrice;
        TextView mTicketNo;

        String[] departureDateTime;
        String[] arrivalDateTime;

        // Get the intent used to generated this activity, then retrieve the parameters passed with it.
        b = getIntent().getExtras();

        // Get references to the TextView objects.
        mJourneyNo = (TextView) findViewById(R.id.journey_no);
        mDepartureStation = (TextView) findViewById(R.id.departure_station);
        mArrivalStation = (TextView) findViewById(R.id.arrival_station);
        mDepartureTime = (TextView) findViewById(R.id.departure_time);
        mDepartureDate = (TextView) findViewById(R.id.departure_date);
        mArrivalTime = (TextView) findViewById(R.id.arrival_time);
        mArrivalDate = (TextView) findViewById(R.id.arrival_date);
        mPrice = (TextView) findViewById(R.id.price);
        mTicketNo = (TextView) findViewById((R.id.ticketNo));
        bookTicket = (Button) findViewById(R.id.book_ticket_button);

        // Split the date and time into two strings.
        departureDateTime = b.getString("Departure_Time").split("T");
        arrivalDateTime = b.getString("Arrival_Time").split("T");

        price = b.getFloat("Journey_Price");


        // Set TextView text elements to the values passed as parameters.
        mJourneyNo.setText("Journey Number: " + Integer.toString(b.getInt("Journey_No")));
        mDepartureStation.setText("Departure Station: " + b.getString("Departure_Station"));
        mArrivalStation.setText("Arrival Station: " + b.getString("Arrival_Station"));
        mDepartureTime.setText("Departure Time: " + departureDateTime[1]);
        mDepartureDate.setText("Departure Date: " + departureDateTime[0]);
        mArrivalTime.setText("Arrival Time: " + arrivalDateTime[1]);
        mArrivalDate.setText("Arrival Date: " + arrivalDateTime[0]);
        mPrice.setText("Price: " + price);


        //Start Paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        ticketID = getIntent().getStringExtra("ticket_id");
        user = (Customer) getIntent().getSerializableExtra("user");
        journeyID = b.getInt("Journey_No");
        //connect button to variable
        bookTicket = (Button) findViewById(R.id.book_ticket_button);

        if (ticketID != null) {
            mTicketNo.setVisibility(View.VISIBLE);
            bookTicket.setText("find a similar journey");
            mTicketNo.setText("Ticket Reference NO: " + ticketID);
            bookTicket.setOnClickListener(new View.OnClickListener() {
                public void onClick( View v ) {
                    Intent intent = new Intent(TimetableRecordView.this, Timetable.class);
                    String parameter = "/search?journeyNo=&departureStation=" + b.getString("Departure_Station")
                            + "&arrivalStation=" + b.getString("Arrival_Station") + "&departureTime=&arrivalTime=";
                    intent.putExtra("parameter", parameter);
                    intent.putExtra("user", user);
                    intent.putExtra("ticket_id", ticketID);
                    startActivity(intent);
                }
            });

        } else {
            bookTicket.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick( View view ) {
                    bookTicket.setEnabled(false);

                    processPayment();
                }
            });
        }

    }

    /**
     * Set up paypal intent in order to run purchase
     */
    private void processPayment() {
        //create new payment with the price and notes for user to read
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(price), "GBP", "Ticket for Journey", PayPalPayment.PAYMENT_INTENT_SALE);

        //create new intent for paypal payment activity with config and the payment wanted
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        //start activity with the result of the transaction
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    /***
     * Based on Result code supply user with information/add ticket to database
     * @param requestCode the request code
     * @param resultCode the result code
     * @param data any data passed from paypal Intent
     */
    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //get confirmation of payment from paypal activity
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {

                    //add ticket to database
                    user.purchaseTicket(Integer.toString(journeyID), Float.toString(price));
                    //inform user
                    alert();

                }
            } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                //inform user the transaction was Cancelled
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            } else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                //inform user transaction was invalid
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /***
     * This method is to check if the ticket was able to purchase
     * if 201 then it is assumed everything worked
     */
    private void alert() {
        bookTicket.setEnabled(true);
        String text;
        // get activity context
        Context context = TimetableRecordView.this.getApplicationContext();
        if (httpresponse == null) {
            text = "No connection to server please try again later";
        } else if (httpresponse.code() == 201) {
            text = "Ticket Purchased";
        } else {
            text = "Something Went Wrong";
            Log.d("Http Response", httpresponse.toString());
        }

        // create a toast alert
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        // set it to be centred
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(TimetableRecordView.this, Timetable.class);
            intent.putExtra("user", user);
            intent.putExtra("parameter", parameter);
            intent.putExtra("active_journeys_id", activeJourneysID);
            intent.putExtra("expired_journeys_id", expiredJourneysID);
            intent.putExtra("tickets_id", ticketsID);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timetable_record_view, menu);
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

        if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(TimetableRecordView.this, HomeScreen.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(TimetableRecordView.this, TicketSearch.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            //TODO
        } else if (id == R.id.nav_tickets) {
            Intent intent = new Intent(TimetableRecordView.this, BookedJourneys.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(TimetableRecordView.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ) {
                            Intent intent = new Intent(TimetableRecordView.this, LoginController.class);
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

    public static void setResponse( Response response ) {
        httpresponse = response;
    }
}

