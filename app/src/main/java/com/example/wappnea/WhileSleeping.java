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
import java.util.Calendar;
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
    public double[][] windows;
    double[][] features;
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
        // define date and time format and get current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timef = new SimpleDateFormat("HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());
        String startTime = timef.format(c.getTime());

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
                    int numApp=0;
                    if(t1.isStopped()==false){
                        windows=t1.exit();
                    }
                    else{
                        windows= t1.abDataValues;
                    }
                    double apneaFilter[]=new double[windows.length*40];
                    features=extract_feature(windows);
                    double[] My_0 = new double[4];
                    for (int i=0; i<windows.length;i++){
                        for (int j=0;j<4;j++) {
                            My_0[j] = features[j][i];
                        }
                        if(decision_tree(My_0)==1){
                            for(int k=0; k<40; k++){
                                apneaFilter[40*i+k]=1;
                            }
                            //numApp++;
                            //if(i<windows.length-3){
                            //    i=i+2;
                            //}
                        }
                        else{
                            for(int k=0; k<40; k++){
                                apneaFilter[40*i+k]=0;
                            }
                        }
                    }
                    for (int i=40; i<apneaFilter.length; i=i+40){
                        if(apneaFilter[i]-apneaFilter[i-40]==1){
                            if(apneaFilter[i+40]-apneaFilter[i]==0){
                                numApp++;
                            }
                        }
                    }
                    Log.d(LOG_WhileSleeping,"Value: " + numApp);

                    Intent intent_2 = new Intent(WhileSleeping.this, NightSummary.class);

                    //calculate end time and duration based on number of samples acquired
                    long start = c.getTimeInMillis();
                    long duration = windows.length*5*1000; // in milliseconds
                    long end = start+duration;
                    Calendar c2 = Calendar.getInstance();
                    c2.setTimeInMillis(end);
                    String endTime = timef.format(c2.getTime());
                    //info to pass to the next activity
                    Bundle info = new Bundle();
                    info.putString("label_date",date);
                    info.putString("label_startTime", startTime);
                    info.putLong("label_duration", duration);
                    info.putString("label_endTime", endTime);
                    info.putInt("label_numEvents", numApp);

                    intent_2.putExtras(info);

                    startActivity(intent_2);
                }
                catch (Exception e) {
                    Log.d(LOG_WhileSleeping,e.getMessage());
                }
            }
        });
    }

    public double[][] extract_feature(double[][] dataWindows){

        double[] mean = new double[dataWindows.length];
        double[] std = new double[dataWindows.length];
        double[] energy = new double[dataWindows.length];
        double[] mean_der = new double[dataWindows.length];

        for (int i=0;i<dataWindows.length;i++){
            mean[i] = mean(dataWindows[i]);
            std[i] = std(dataWindows[i]);
            energy[i] = energy(dataWindows[i]);
            mean_der[i] = mean_derivative(dataWindows[i]);
        }

        double[] standardized_mean = standardization(mean);
        double[] standardized_std = standardization(std);
        double[] standardized_energy = standardization(energy);
        double[] standardized_mean_der = standardization(mean_der);

        double[][] all_features = {standardized_mean,standardized_std,standardized_energy,standardized_mean_der};

        return all_features;
    }

    // reference https://searchcode.com/codesearch/view/78094523/
    private static double mean(double[] dataWindow){

        double result =0.0;
        for (int i=0;i<dataWindow.length;i++){
            result += dataWindow[i];
        }

        result /= (double)dataWindow.length;

        return result;
    }
    //https://www.geeksforgeeks.org/java-program-to-calculate-standard-deviation/
    private static double std(double[] dataWindow){
        double standardDeviation = 0.0;
        double meanResult =mean(dataWindow);
        for (int i = 0; i < dataWindow.length; i++) {
            standardDeviation = standardDeviation + Math.pow((dataWindow[i] - meanResult), 2);
        }
        double res = Math.sqrt(standardDeviation / dataWindow.length);
        return res;
    }

    private static double energy(double[] dataWindow){
        double energy = 0.0;
        for (int i = 0; i < dataWindow.length; i++) {
            energy=energy+(dataWindow[i])*(dataWindow[i]);
        }
        energy /= (double)dataWindow.length;

        return energy;
    }

    private static double mean_derivative(double[] dataWindow){

        double[] mean_der = new double[dataWindow.length];

        for (int i=1;i<dataWindow.length;i++){
            mean_der[i] = Math.abs(dataWindow[i] - dataWindow[i-1]);
        }

        return mean(mean_der);
    }

    // reference https://searchcode.com/codesearch/view/78094523/
    private static double[] subMean(double[] dataWindow){

        double[] subst = new double[dataWindow.length];
        double meanResult =mean(dataWindow);

        for (int i=0;i<dataWindow.length;i++){
            subst[i] = dataWindow[i] - meanResult;
        }
        return subst;
    }

    private static double[] standardization(double[] parameterValues){
        double meanParam = mean(parameterValues);
        double stdParam = std(parameterValues);
        double[] standardizedParameterValues = new double[parameterValues.length];

        for (int i=0; i<parameterValues.length; i++){
            standardizedParameterValues[i] = (parameterValues[i]-meanParam)/stdParam;
        }
        return standardizedParameterValues;
    }

    private static double decision_tree(double[] features){
        double estimated_class = 0;

        if (features[2]<-0.365707){
            if (features[3]<-0.708075){
                if(features[2]<-0.897993){
                    if(features[1]<-1.72754){
                        estimated_class = 1;
                    }
                }
                else if (features[3]<-1.61229){
                    if(features[1]<-2.06214){
                        estimated_class = 1;
                    }
                }
                else{
                    estimated_class = 1;
                }
            }
            else if(features[2]<-0.52561){
                estimated_class = 1;
            }
            else if (features[1]<-0.579833){
                estimated_class = 1;
            }
            else if(features[0]>=0.151772){
                estimated_class = 1;
            }
        }
        else if (features[2]<0.139){
            if(features[2]<-0.240547){
                if(features[3]<0.242473){
                    if(features[0]>=0.995959){
                        estimated_class = 1;
                    }
                }
                else{
                    estimated_class = 1;
                }
            }
            else if(features[0]>=1.65685){
                estimated_class = 1;
            }
        }

        return  estimated_class;
    }

}