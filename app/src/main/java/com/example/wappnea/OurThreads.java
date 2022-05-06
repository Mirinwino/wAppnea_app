package com.example.wappnea;

// OurThreads.java
// This java module is a containing thread operation for reading data,
// converting to a data matrix, and continuous plotting. When the class
// is defined, the thread operation is directly starting since class definition
// including .start().
// Agnese Calvani, Esra Gizem Gungor, Miriam Peinado Martin, Omer Altan


import android.graphics.Color;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class OurThreads extends WhileSleeping implements Runnable{

    public String LOG_Thread = "thread";
    private String fileName = "";
    private String fileNameLabels = "";
    // threadName and thread are use for initialization of thread operation
    private String threadName;
    Thread thread;
    // endThread and plottingflag are indicators for stopping the thread.
    private boolean endThread;
    public static int plottingflag=1;
    public double[][] abDataValues;

    // Definition of thread with a string name, in case of defining different
    // thread operations in the future applications.
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
        // the specified data from the first intent is used in here to
        // choose desired data.
        switch (LogIn.LogInUser) {
            case 1: {
                fileName = "abdoData22.txt";
                fileNameLabels = "labels22.txt";
                break;
            }
            case 2: {
                fileName = "abdoData25.txt";
                fileNameLabels = "labels25.txt";
                break;
            }
            case 3: {
                fileName = "abdoData23.txt";
                fileNameLabels = "labels23.txt";
                break;
            }
        }

        //--------READING DATA-------------
        String ret = "";
        try {
            //How external storage location can be defined
            //String path = MainActivity.context.getFilesDir().toString();  //for internal storage
            File[] Dirs = ContextCompat.getExternalFilesDirs(MainActivity.context, null);

            // start reading abdominal data --------------------------------------------------------
            File file = new File(Dirs[1],fileName); ///note: if u do not have sdcard chose 0, if u have chose 1
            FileInputStream fIn= new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String receiveString = "";
            while ( (receiveString = bufferedReader.readLine()) != null ) {
                try {
                    if(endThread==false){
                        abData.add(Double.parseDouble(receiveString));
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
            // end reading abdominal data ----------------------------------------------------------

            // start reading labels for doctor annotations---------------------------------------------------------------
            File filelabels = new File(Dirs[1],fileNameLabels); ///note: if u do not have sdcard chose 0, if u have chose 1
            FileInputStream fInLabels= new FileInputStream(filelabels);
            InputStreamReader isrlabels = new InputStreamReader(fInLabels);
            BufferedReader bufferedReaderlabels = new BufferedReader(isrlabels);
            String receiveStringlabels = "";
            while ( (receiveStringlabels = bufferedReaderlabels.readLine()) != null ) {
                try {
                    if(endThread==false){
                        reallabels.add(Double.parseDouble(receiveStringlabels));
                    }
                    else{
                        isrlabels.close();
                    }
                }catch(Exception e){
                    Log.d(LOG_Thread, e.getMessage());
                }
            }
            isrlabels.close();
            // end reading labels ------------------------------------------------------------------

            // continuous plotting in another thread operation
            if (endThread == false){
                new Thread(new Runnable() {
                    public int k;
                    @Override
                    public void run() {
                        while(plottingflag==1){
                            for (k = 0; k < abData.size(); k++) {
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
            }
            else{
                plottingflag=0;
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

    // ENTERING DATA POINT FOR CONTINUOUS PLOT ------------------------------------------------------------------------------------
    // It has an input as data value that will be added at the end of plotted line.
    private void addEntry(double inputData) {
        LineData data = Live_chart.getData();
        if (data != null) {
            try {
                //loading already plotted line
                ILineDataSet set = data.getDataSetByIndex(0);

                if (set == null) {
                    set = createSet();
                    data.addDataSet(set);
                }

                float f = (float)inputData;
                data.addEntry(new Entry(set.getEntryCount(), f),0);

                // let the chart know it's data has changed
                Live_chart.notifyDataSetChanged();

                // limit the number of visible entries
                Live_chart.setVisibleXRange(200,400);

                // move to the latest entry
                Live_chart.moveViewToX(data.getEntryCount());

            }catch(Exception e){
                Log.d(LOG_Thread, e.getMessage()+"plot error");
            }
        }
    }

    //CREATING GRAPH FOR PLOTTING ON IT
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
            }
        }
        return abDataValues;
    }

}
