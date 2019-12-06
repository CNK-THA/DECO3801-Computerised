package com.example.deco3801computerised;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Allow users to log a new record to the system
 * @author Team Computerised DECO3801 Sem 2 2019
 * @contact c.kachornvuthidej@uqconnect.edu.au
 */
public class AddRecord extends AppCompatActivity {

    StringBuilder id = new StringBuilder();

    StringBuilder startTime = new StringBuilder();
    StringBuilder endTime = new StringBuilder();
    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
    String act = null;

    List<String> arraySpinner = new ArrayList<String>(); //Type of activities to choose from

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //Load pre-configured activities
        arraySpinner.add("Walking/Wheeling");
        arraySpinner.add("Running/Fast Wheeling");
        arraySpinner.add("Swimming");
        arraySpinner.add("Cycling");
        arraySpinner.add("Sleeping");
        arraySpinner.add("Lying");
        arraySpinner.add("Sitting");

        //Read user information saved in the internal storage
        try {
            FileInputStream data = openFileInput("userIdNumber");
            //To read ID number
            int i = 0;
            while((i = data.read()) != -1 && i != '\n'){
                id.append((char)i);
            }

            StringBuilder activities = new StringBuilder();
            while((i = data.read()) != -1){
                if(i == '\n') { //end of each activities, add then reset
                    arraySpinner.add(activities.toString());
                    activities.setLength(0);
                } else {
                    activities.append((char)i);
                }
            }
            data.close();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
        gatherData();
    }


    /**
     * Gather all dat inputted by user to be sent to the server
     */
    private void gatherData() {
        //Read the logged time
        final Spinner s = (Spinner) findViewById(R.id.activitySpin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        final TimePicker start=(TimePicker)findViewById(R.id.timeStart);
        final TimePicker end = (TimePicker)findViewById(R.id.timeEnd);

        start.setIs24HourView(true);
        end.setIs24HourView(true);



        Button add = (Button)findViewById(R.id.send);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTime.append(start.getHour());
                startTime.append(start.getMinute());

                endTime.append(end.getHour());
                endTime.append(end.getMinute());

                if(Integer.parseInt(startTime.toString()) >= Integer.parseInt(endTime.toString())) {
                    error();
                    startTime = new StringBuilder();
                    endTime = new StringBuilder();
                } else {
                    act =  s.getSelectedItem().toString();
                    sendData();
                    startActivity(new Intent(AddRecord.this, Menu.class));

                }

            }
        });
    }

    /**
     * Send the gathered data to the cloud server (Google Cloud)
     */
    private void sendData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child(id.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            User user;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                //If the date exist for this user, get the array and add to it otherwise,
                //make a new array
                Time newData = new Time(startTime.toString(), endTime.toString(), act);

               if(user.data.containsKey(currentDate.format(new java.util.Date()))){
                    user.data.get(currentDate.format(new java.util.Date())).add(newData);

                } else {
                    ArrayList<Time> newDataCollection = new ArrayList<>();
                    newDataCollection.add(newData);
                    user.data.put(currentDate.format(new java.util.Date()), newDataCollection);

                }

                myRef.child(id.toString()).setValue(user);

               writeToLocal(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error sending data to database");
            }
        });
    }

    /**
     * As a backup mechanism, save the data to the internal storage as well
     * @param user The user to save these data to
     */
    private void writeToLocal(User user) {
        try{
            FileOutputStream outputStream;
            outputStream = openFileOutput("userActivity", Context.MODE_PRIVATE);
            outputStream.write(user.DataToString().getBytes());
            outputStream.close();
        } catch (Exception e) {

        }
    }


    /**
     * Admin entered a wrong password, display an error message.
     */
    private void error() {
        // Build an AlertDialog
        AlertDialog builder = new AlertDialog.Builder(AddRecord.this).create();
        builder.setTitle("Error");
        builder.setMessage("Ensure correct start/end time is selected");
        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

}
