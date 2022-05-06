package com.example.wappnea;

// LogIn.java
// This module is containing the choice of three different option to chose.
// It is design to indicate different users in real-life application.
// For the sake of software completeness, each button leads different test data from database right now.
// Agnese Calvani, Esra Gizem Gungor, Miriam Peinado Martin, Omer Altan

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class LogIn extends AppCompatActivity{
    // 3 buttons initialized here.
    private Button BtLogIn1, BtLogIn2, BtLogIn3;
    // LogInUser is used for specifying the data will be processed.
    public static int LogInUser;
    // When the user click a button, the identity will be kept as
    // 1,2 or 3 and a new intent of welcoming window will be created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        BtLogIn1 = findViewById(R.id.BtLogIn1);
        BtLogIn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInUser = 1;
                Intent intent_plot = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent_plot);
            }
        });

        BtLogIn2 = findViewById(R.id.BtLogIn2);
        BtLogIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInUser = 2;
                Intent intent_plot = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent_plot);
            }
        });

        BtLogIn3 = findViewById(R.id.BtLogIn3);
        BtLogIn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInUser = 3;
                Intent intent_plot = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent_plot);
            }
        });
    }
}
