package com.example.deco3801computerised;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

/**
 * The Main menu page after user logged in. Display a calendar view.
 * @author Team Computerised DECO3801 Sem 2 2019
 * @contact c.kachornvuthidej@uqconnect.edu.au
 */
public class Menu extends AppCompatActivity {

    private FirebaseAuth mAuth;

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        calendar();

        TextView date = (TextView)findViewById(R.id.Date);

        date.setText("October 2019"); //hard code date

        navigation();
    }

    /**
     * Create a bottom navigation menu "graphing" and "logging"
     */
    private void navigation() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_upload:
                        Toast.makeText(Menu.this, "Upload", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_graph:
                        Toast.makeText(Menu.this, "Graph", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Menu.this, GraphDisplay.class));
                        break;
                }
                return true;
            }
        });

        Button addRecord = (Button)findViewById(R.id.addRecord);
       addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, AddRecord.class));
            }
        });
    }


    /**
     * Create and display a calendar view of logged activities
     */
    private void calendar() {
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        ReadData();
        //Event ev1 = new Event(Color.RED, 1477040400000L, "Teachers' Professional Day");

        //compactCalendar.addEvent(ev1);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                /*if (dateClicked.toString().compareTo("Fri Sep 6 00:00:00 AST 2019") == 0) {
                    Toast.makeText(context, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
                }*/


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                TextView date = (TextView)findViewById(R.id.Date);
                date.setText(dateFormatMonth.format(firstDayOfNewMonth));
               // actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
    }


    /**
     * Display the day that activities were logged on the calendar view.
     */
    private void ReadData() {
        try{
            FileInputStream in = openFileInput("userActivity");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader read= new BufferedReader(inputStreamReader);
            String c;
            while((c = read.readLine()) != null) {
                if(!c.contains("start")) { //this is the date we want, not time!
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = sdf.parse(c);

                    Event ev1 = new Event(Color.RED, date.getTime(), "Activity recorded on this day!");
                    compactCalendar.addEvent(ev1);

                }
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
