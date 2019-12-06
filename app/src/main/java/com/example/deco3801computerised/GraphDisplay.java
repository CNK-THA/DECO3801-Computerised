package com.example.deco3801computerised;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Display the logged activity in a bar graph format
 */
public class GraphDisplay extends AppCompatActivity {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_display);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Activity Summary, (Hours)");


        barChart = findViewById(R.id.chart);
        getEntries();
        barDataSet = new BarDataSet(barEntries, "Activity");
        barData = new BarData(barDataSet);
        barChart.setData(barData);

        //Set background colours
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(13f);

        //Disabled all grid format
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getLegend().setEnabled(false);

        //Display only x axis line
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);



        //Activities to be displayed
        final String[] label = {"Walk/Wheel", "Run/Fast Wheel",
                "Swimming", "Cycling", "Sleeping", "Lying", "Sitting"};
        barChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(label));
        barChart.getXAxis().setTextSize(12f);
    }


    /**
     * Retrieve saved information of the logged actvity to be display
     */
    private void getEntries() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 0));
        barEntries.add(new BarEntry(1f, 3));
        barEntries.add(new BarEntry(2f, 1));
        barEntries.add(new BarEntry(3f, 1));
        barEntries.add(new BarEntry(4f, 4));
        barEntries.add(new BarEntry(5f, 3));


    }




}


