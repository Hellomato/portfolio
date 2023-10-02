package com.example.controllers;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobileapplication.R;

import java.util.ArrayList;
import java.util.Calendar;

import models.User.Customer;
import okhttp3.Response;

/***
 * This activity manages  all the actions that can be carried out in the Sign up screen.
 */
public class SignUp extends AppCompatActivity implements View.OnClickListener {


    private int mYear, mMonth, mDay;
    private static Response response;
    TextView txtFirstName, txtSurname, txtUsername, txtPassword, txtDateBirth, txtAddress, txtPostcode, btnSubmit;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_sign_up);

        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtDateBirth = (EditText) findViewById(R.id.txtDateBirth);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtPostcode = (EditText) findViewById(R.id.txtPostcode);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //set onClick listeners
        btnSubmit.setOnClickListener(this);
        txtDateBirth.setOnClickListener(this);
    }


    @Override
    public void onClick( View v ) {

        if (v == btnSubmit) {
            //create information array to get all the values input in the TextView items
            ArrayList<String> information = new ArrayList<String>();

            //get values to string
            String CUSTOMER_FORENAME = txtFirstName.getText().toString();
            String CUSTOMER_SURNAME = txtSurname.getText().toString();
            String USERNAME = txtUsername.getText().toString();
            String PASSWORD = txtPassword.getText().toString();
            String DATE_OF_BIRTH = txtDateBirth.getText().toString();
            String POST_CODE = txtPostcode.getText().toString();
            String ADDRESS_LINE = txtAddress.getText().toString();

            //add to array
            information.add(CUSTOMER_FORENAME);
            information.add(CUSTOMER_SURNAME);
            information.add(USERNAME);
            information.add(PASSWORD);
            information.add(DATE_OF_BIRTH);
            information.add(POST_CODE);
            information.add(ADDRESS_LINE);

            //send post request
            Customer.SignUp(information);
            if (response.code() == 201) {
                new AlertDialog.Builder(SignUp.this)
                        .setTitle("Account successfully created!")
                        .setMessage("Do you want to be redirected to the login screen?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialog, int which ) {
                                Intent intent = new Intent(SignUp.this, LoginController.class);
                                startActivity(intent);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            } else {
                new AlertDialog.Builder(SignUp.this)
                        .setTitle("Oops, something went wrong!")
                        .setMessage("We cannot create this account")
                        .show();
            }

        } else if (v == txtDateBirth) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet( DatePicker view, int year,
                                               int monthOfYear, int dayOfMonth ) {

                            txtDateBirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    public static void setResponse( Response response ) {
        SignUp.response = response;
    }
}
