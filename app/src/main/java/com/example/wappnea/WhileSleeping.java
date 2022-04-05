package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WhileSleeping extends AppCompatActivity {
    private Button button;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_summary);

        button = (Button) findViewById(R.id.btnStopSleep);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNightSummary();
            }
        });
    }

    public void openNightSummary() {
        Intent intent = new Intent(this, NightSummary.class);
        startActivity(intent);
    }
}