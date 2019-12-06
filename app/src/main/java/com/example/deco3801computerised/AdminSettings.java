package com.example.deco3801computerised;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author Team Computerised DECO3801 Sem 2 2019
 * @contact c.kachornvuthidej@uqconnect.edu.au
 *
 * Options that the admin can do with the app.
 */
public class AdminSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        //Create a new account
        Button newacc = (Button)findViewById(R.id.newacc);
        newacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSettings.this, AdminAddUser.class));
            }
        });

        //Delete the existing account
        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = getFilesDir();
                File file = new File(dir, "userIdNumber");
                file.delete();
                changeSuccess();
                startActivity(new Intent(AdminSettings.this, MainActivity.class));
            }
        });

        final EditText newPass = (EditText)findViewById(R.id.newPassword);
        //Change the admin password
        Button adminpass = (Button)findViewById(R.id.adminpassword);
        adminpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPass.getText().toString().length() == 0) { //The given ID is empty, error
                    errorCreating();
                }

                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput("adminPassword", Context.MODE_PRIVATE);
                    outputStream.write(newPass.getText().toString().getBytes());
                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                changeSuccess();
            }
        });

    }
    /**
     * Password has been changed successfully.
     */
    private void changeSuccess() {
        // Build an AlertDialog
        AlertDialog builder = new AlertDialog.Builder(AdminSettings.this).create();
        builder.setTitle("Success");
        builder.setMessage("Operation is successful");

        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    /**
     * There is an error changing admin password, display the error message to prompt re-try
     */
    private void errorCreating() {
        // Build an AlertDialog
        AlertDialog builder = new AlertDialog.Builder(AdminSettings.this).create();
        builder.setTitle("Error");
        builder.setMessage("Please ensure all information are inputted correctly");

        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}
