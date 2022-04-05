package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btnStartSleep);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhileSleeping();
            }
        });
    }

    public void openWhileSleeping() {
        Intent intent = new Intent(this, WhileSleeping.class);
        startActivity(intent);
    }
}