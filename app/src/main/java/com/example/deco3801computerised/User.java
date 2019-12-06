package com.example.deco3801computerised;

import android.os.MessageQueue;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;

/**
 * User Object storing all information of a user
 * @author Team Computerised DECO3801 Sem 2 2019
 * @contact c.kachornvuthidej@uqconnect.edu.au
 */
public class User {

    public boolean dataGraph;
    public int monitorPos; //0 for wrist, 1 for hip
    public int diaryEpoch;

    public HashMap<String,ArrayList<Time>> data = new HashMap<>(); //Data for activity logging

    public User() {

    }

    public User(boolean graph, int pos, int epoch) {
        dataGraph = graph;
        monitorPos = pos;
        diaryEpoch = epoch;

    }

    /**
     * Parse the data into time\nstart:[startTime],end:[endtime],act:[activity]\n format for use
     * @return parsed string in the above format
     */
    public String DataToString() {
        StringBuilder dataAsString = new StringBuilder();
        for(String key: data.keySet()) {
            dataAsString.append(key);
            dataAsString.append("\n");
            for(Time t: data.get(key)) {
                dataAsString.append(t.toString());
            }
        }

        return dataAsString.toString();
    }
}
