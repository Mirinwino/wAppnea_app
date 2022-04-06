package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WhileSleeping extends AppCompatActivity {
    private Button button_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_while_sleeping);

        //"setOnClickListener" run if the specified button is clicked.
        //Due to having two different button and different actions for each one,it is used.
        button_2 = (Button) findViewById(R.id.btnStopSleep);
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // The application turns back to main activity.
                Intent intent_2 = new Intent(WhileSleeping.this, NightSummary.class);
                startActivity(intent_2);
            }
        });
    }
}