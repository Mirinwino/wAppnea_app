package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

//public class LogIn extends AppCompatActivity {
public class LogIn extends AppCompatActivity{
    public static Context context_login;
    private Button BtLogIn1, BtLogIn2, BtLogIn3;

    public static int LogInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        context_login=this;
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
