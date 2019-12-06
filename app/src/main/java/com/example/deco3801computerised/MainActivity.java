package com.example.deco3801computerised;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;

/**
 * First screen that the user will see upon launching the app.
 * To change admin password see AdminAccess.java
 * @author Team Computerised DECO3801 Sem 2 2019
 * @contact c.kachornvuthidej@uqconnect.edu.au
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adminAccess(); //set button event listener first
        login();
    }

    /**
     * Event listener for the button login clicking. Check whether user existed or not.
     */
    private void adminAccess() {
        Button adminButton = (Button)findViewById(R.id.admin);
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AdminAccess.class));
            }
        });
    }

    /**
     * Attempt to login to the system by checking if file named "userIdNumber" exist or not. The
     * file is stored in the internal storage system.
     *
     */
    private void login(){
        Button loginButton = (Button)findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Check if file exist (user has already logged in before)
                    FileInputStream data = openFileInput("userIdNumber");
                    data.close();

                    //User exist, go to calendar view
                    startActivity(new Intent(MainActivity.this, Menu.class));
                } catch (Exception e) {
                    noExistingUser();
                }

            }
        });
    }

    /**
     * Display an error message that this is a new user and has to setup an aaccount first before
     * logging in.
     */
    private void noExistingUser() {
        // Build an AlertDialog
        AlertDialog builder = new AlertDialog.Builder(MainActivity.this).create();
        builder.setTitle("Error");
        builder.setMessage("No active user, please contact your admin");

        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }


}
