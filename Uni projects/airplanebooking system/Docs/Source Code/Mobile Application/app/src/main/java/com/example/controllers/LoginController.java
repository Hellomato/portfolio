package com.example.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.mobileapplication.R;

import models.User.Customer;
import okhttp3.Response;

/***
 * Java class for the login activity (first screen of the application).
 */
public class LoginController extends AppCompatActivity implements View.OnClickListener {

    //global variables
    private EditText Name;
    private EditText Password;
    private Button btnLogin;
    private static Response response = null;
    private Customer user;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);

        Name = (EditText) findViewById(R.id.etUsername);
        Password = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //set button listener
        btnLogin.setOnClickListener(this);

    }

    /***
     * Go to subscription screen.
     * @param view
     */
    public void createAccount( View view ) {
        Intent intent = new Intent(LoginController.this, SignUp.class);
        startActivity(intent);
    }

    public static void setResponse( Response response ) {
        LoginController.response = response;
    }

    @Override
    public void onClick( View v ) {

        btnLogin.setEnabled(false);
        String userName ="cu" + Name.getText().toString();
        String userPassword = Password.getText().toString();
        user = new Customer(userName, userPassword);
        user.Login(userName, userPassword);

        if (response.code() == 200) {
            user.requestID();
            Intent intent = new Intent(LoginController.this, HomeScreen.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            new AlertDialog.Builder(LoginController.this)
                    .setTitle("Wrong credentials!")
                    .setMessage("Please retry.")
                    .show();

        }
        btnLogin.setEnabled(true);

    }
}

