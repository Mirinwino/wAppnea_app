package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.Manifest;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Handler;
import android.os.Message;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class WhileSleeping extends AppCompatActivity {
    private Button button_2;
    public ArrayList<Double> abData = new ArrayList<Double>();
    public String LOG_WhileSleeping = "whileSleeping";
    public String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

    //private static final int REQUEST_EXTERNAL_STORAGE = 1;
    //private static String[] PERMISSIONS_STORAGE = {
    //        Manifest.permission.READ_EXTERNAL_STORAGE,
    //};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_while_sleeping);
        Log.i(LOG_WhileSleeping, "The storage directory is at:" + this.path);
        OurThreads t1 = new OurThreads("txt_reading");
        //"setOnClickListener" run if the specified button is clicked.
        //Due to having two different button and different actions for each one,it is used.
        button_2 = (Button) findViewById(R.id.btnStopSleep);
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // The application goes to summary screen.
                try {
                    t1.exit();
                    Intent intent_2 = new Intent(WhileSleeping.this, NightSummary.class);
                    startActivity(intent_2);
                }
                catch (Exception e) {
                    Log.d(LOG_WhileSleeping,e.getMessage());
                }
            }
        });
    }
}