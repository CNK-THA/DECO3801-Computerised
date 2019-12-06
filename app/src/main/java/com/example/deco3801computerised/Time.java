package com.example.deco3801computerised;

import androidx.annotation.NonNull;

/**
 * Indiviual object for each activity logged. Contains information of start/end time and activity
 * type.
 * @author Team Computerised DECO3801 Sem 2 2019
 * @contact c.kachornvuthidej@uqconnect.edu.au
 */
public class Time {

    String startTime;
    String endTime;
    String activity;

    public Time(){

    }

    public Time(String start, String end, String act) {

        startTime = start;
        endTime = end;
        activity = act;
    }

    @NonNull
    @Override
    public String toString() {
        return "start:" + startTime + "," + "end:" + endTime + "," + "act:" + activity + "\n";
    }
}
