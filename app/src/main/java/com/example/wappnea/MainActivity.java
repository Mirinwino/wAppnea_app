package com.example.wappnea;

// MainActivity.java
// This module is used for a welcoming page before user goes to sleep.
// The button will started a new intent with WhileSleeping.java in which
// most of the analysis is conducted.
// Agnese Calvani, Esra Gizem Gungor, Miriam Peinado Martin, Omer Altan

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //context will be used for finding external storage directory later
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_StartSleep = findViewById(R.id.btnStartSleep);
        btn_StartSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starting the new intent
                Intent intent_1 = new Intent(MainActivity.this, WhileSleeping.class);
                startActivity(intent_1);
            }
        });
    }
}


