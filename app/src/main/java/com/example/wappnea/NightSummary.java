package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class NightSummary extends AppCompatActivity{
    private Button btn_Plots, btnBackHome1;

    TextView dateObj, startTimeObj, endTimeObj, durationObj, AHIObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_summary);
        initializeView();

        Bundle info = getIntent().getExtras();
        String date = info.getString("label_date");
        String startTime = info.getString("label_startTime");
        Long durationMill = info.getLong("label_duration");
        String endTime = info.getString("label_endTime");
        int numEvents = info.getInt("label_numEvents");

        int seconds = (int) (durationMill / 1000) % 60 ;
        int minutes = (int) ((durationMill / (1000*60)) % 60);
        int hours   = (int) ((durationMill / (1000*60*60)) % 24);

        String durationTime = ""+hours+"h "+minutes+"m "+seconds+"s";

        //AHI number of events per hour
        double durationHour = durationMill/(1000.0*60.0*60.0);
        double AHI = numEvents/durationHour;
        String AHIString = String.format("%.1f", AHI);

        dateObj.setText(date);
        startTimeObj.setText(startTime);
        endTimeObj.setText(endTime);
        durationObj.setText(durationTime);
        AHIObj.setText((AHIString));

        btnBackHome1 = findViewById(R.id.btnBackHome1);
        btnBackHome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_plot = new Intent(NightSummary.this, LogIn.class);
                intent_plot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_plot.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_plot.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent_plot.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_plot);
                Runtime.getRuntime().exit(0);

            }
        });

        btn_Plots = findViewById(R.id.btnPlots);
        btn_Plots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_plot = new Intent(NightSummary.this, Visualization_of_results.class);
                startActivity(intent_plot);
            }
        });
    }

    private void initializeView() {
        dateObj = (TextView) findViewById(R.id.textView1);
        startTimeObj = (TextView) findViewById(R.id.textView2);
        endTimeObj = (TextView) findViewById(R.id.textView3);
        durationObj = (TextView) findViewById(R.id.textView4);
        AHIObj = (TextView) findViewById(R.id.textView5);
    }
}