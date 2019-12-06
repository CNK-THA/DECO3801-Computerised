package com.example.deco3801computerised;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;

import java.io.FileInputStream;


/**
 * @author Team Computerised DECO3801 Sem 2 2019
 * @contact c.kachornvuthidej@uqconnect.edu.au
 *
 * Attempt to login to the system as an admin using admin password.
 */
public class AdminAccess extends AppCompatActivity {

    //Change the text in quotation marks to change the password of admin logging in
    private StringBuilder adminPassword = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_access);
        checkPassword();
    }

    /**
     * Check if the given admin password is correct or not when user attempt to login.
     * Set button onclick event.
     */
    private void checkPassword() {

        try {
            FileInputStream data = openFileInput("adminPassword");
            //To read adminPassword
            int i = 0;
            while ((i = data.read()) != -1) {
                adminPassword.append((char) i);
            }
        } catch(Exception e) { //If no password has been set, use the default 1234
            adminPassword.append("1234");
        }

        Button adminButton = (Button)findViewById(R.id.admin_access);
        final EditText passwordField = (EditText)findViewById(R.id.enterPassword);
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordField.getText().toString().equals(adminPassword.toString())) {
                    startActivity(new Intent(AdminAccess.this, AdminSettings.class));
                } else {
                   wrongPassword();
                }
            }
        });
    }

    /**
     * Admin entered a wrong password, display an error message.
     */
    private void wrongPassword() {
        // Build an AlertDialog
        AlertDialog builder = new AlertDialog.Builder(AdminAccess.this).create();
        builder.setTitle("Error");
        builder.setMessage("Incorrect password entered.");
        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
