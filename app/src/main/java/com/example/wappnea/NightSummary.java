package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class NightSummary extends AppCompatActivity {
    private Button button_3;

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

        SimpleDateFormat timef = new SimpleDateFormat("HH:mm:ss");
        Calendar c3 = Calendar.getInstance();
        c3.setTimeInMillis(durationMill);
        String durationTime = timef.format(c3.getTime());

        //AHI number of events per hour
        double durationHour = durationMill/(1000.0*60.0*60.0);
        double AHI = numEvents/durationHour;
        String AHIString = String.format("%.1f", AHI);

        dateObj.setText(date);
        startTimeObj.setText(startTime);
        endTimeObj.setText(endTime);
        durationObj.setText(durationTime);
        AHIObj.setText((AHIString));


        button_3 = (Button) findViewById(R.id.btnBackHome);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void openMainActivity() {
        Intent intent_3 = new Intent(this, MainActivity.class);
        startActivity(intent_3);
    }

    private void initializeView() {
        dateObj = (TextView) findViewById(R.id.textView1);
        startTimeObj = (TextView) findViewById(R.id.textView2);
        endTimeObj = (TextView) findViewById(R.id.textView3);
        durationObj = (TextView) findViewById(R.id.textView4);
        AHIObj = (TextView) findViewById(R.id.textView5);
    }
}