package com.example.wappnea;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.example.wappnea.DemoBase;

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
    public static int plottingflag;
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
                        // FOR PLOT ----------------------------------------------------------------
                        //addEntry(Double.parseDouble(receiveString));
                        //Thread.sleep(50);
                        // FOR PLOT ----------------------------------------------------------------
                        //Log.d(LOG_Thread,"Value receive String: " + receiveString);
                        //Thread.sleep(25);
                    }
                    else{
                        isr.close();
                        plottingflag=1;
                    }
                }catch(Exception e){
                    Log.d(LOG_Thread, e.getMessage());
                }
            }
            isr.close();
            plottingflag=1;
            new Thread(new Runnable() {
                public int k;
                @Override
                public void run() {
                    while(plottingflag==1){
                        for (k = 0; k < abData.size(); k++) {
                            // Don't generate garbage runnables inside the loop.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addEntry(abData.get(k));
                                }
                            });
                            try {
                                Thread.sleep(25);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
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

    // FOR PLOT ------------------------------------------------------------------------------------
    private void addEntry(double inputData) {
        LineData data = Live_chart.getData();
        if (data != null) {
            try {
            ILineDataSet set = data.getDataSetByIndex(0);
            //set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            float f = (float)inputData;

            data.addEntry(new Entry(set.getEntryCount(), f),0);
            //data.notifyDataChanged();

            // let the chart know it's data has changed
            Live_chart.notifyDataSetChanged();

            // limit the number of visible entries
            //Live_chart.setVisibleXRangeMaximum(2400);
            //Live_chart.setVisibleYRange(30, AxisDependency.LEFT);
            Live_chart.setVisibleXRange(200,400);

            // move to the latest entry
            Live_chart.moveViewToX(data.getEntryCount());

            }catch(Exception e){
                Log.d(LOG_Thread, e.getMessage()+"plot error");
            }
        }

    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "abdominal belt");
        set.setAxisDependency(AxisDependency.LEFT);
        set.setColor(Color.YELLOW);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(Color.YELLOW);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
    // FOR PLOT ------------------------------------------------------------------------------------

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
