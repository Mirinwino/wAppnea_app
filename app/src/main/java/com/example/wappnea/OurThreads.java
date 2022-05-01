package com.example.wappnea;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OurThreads extends WhileSleeping implements Runnable{
    public String LOG_Thread = "thread";
    private final String fileName = "abdoData22.txt";
    private boolean endThread;
    private String threadName;
    Thread thread;
    public double[][] abDataValues;


    OurThreads(String theName){
        threadName = theName;
        thread = new Thread(this, threadName);
        System.out.println("New thread: " + thread);
        endThread = false;
        thread.start();
    }
    // execution of thread starts from run() method
    public void run()
    {
        String ret = "";
        try {
            //String path = MainActivity.context.getFilesDir().toString();  //for internal storage
            File[] Dirs = ContextCompat.getExternalFilesDirs(MainActivity.context, null);
            File file = new File(Dirs[1],fileName); ///note: if u do not have sdcard chose 0, if u have chose 1
            FileInputStream fIn= new FileInputStream(file);
            //InputStream fIn=MainActivity.context.getResources().getAssets().open(fileName); //for internal storage
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String receiveString = "";
            while ( (receiveString = bufferedReader.readLine()) != null ) {
                try {
                    if(endThread==false){
                        abData.add(Double.parseDouble(receiveString));
                        //Log.d(LOG_Thread,"Value: " + receiveString);
                        //Thread.sleep(25);
                    }
                    else{
                        isr.close();
                    }
                }catch(Exception e){
                    Log.d(LOG_Thread, e.getMessage());
                }
            }
            Log.d(LOG_Thread,"The reading ends");
            exit();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d(LOG_Thread, e.getMessage());
            e.printStackTrace();
        }
        Log.d(LOG_Thread, endThread + " Stopped.");
    }


    boolean isStopped() {
        return endThread;
    }
    // for stopping the thread
    public double[][] exit()
    {
        endThread = true;
        int windowNum=abData.size()/40;
        abDataValues = new double[windowNum][40];
        for (int i = 0; i<windowNum;  i++){
            for (int j = 0; j < 40; j++) {
                abDataValues[i][j] = abData.get(i*40+j);
                //Log.d(LOG_WhileSleeping,"Value: " + i+"-"+j + " "+ abDataValues[i][j]);
            }
        }
        return abDataValues;
    }
}
