package com.example.wappnea;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

        FileInputStream fIn = null;
        File file = new File(path, this.fileName);
        Log.d(LOG_Thread, file.getAbsolutePath());
        String ret = "";
        try {
            fIn = new FileInputStream(file);
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
        catch (IOException e) {
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
        for (int i = 0; i<windowNum-1;  i++){
            for (int j = 0; j < 40; j++) {
                abDataValues[i][j] = abData.get(i*40+j);
                //Log.d(LOG_WhileSleeping,"Value: " + i+"-"+j + " "+ abDataValues[i][j]);
            }
        }
        return abDataValues;
    }

    public void ReadTxtFile() {
        //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //if (permissionCheck== PackageManager.PERMISSION_DENIED){
        //    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
        //}
        //requestPermissions(PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        //Log.d(LOG_TAG,"Permission granted: " + (permissionCheck == PackageManager.PERMISSION_GRANTED));
        //Log.d(LOG_TAG,"Media mounted: " + Environment.MEDIA_MOUNTED);


    }
}
