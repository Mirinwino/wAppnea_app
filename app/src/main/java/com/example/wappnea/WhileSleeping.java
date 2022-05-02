package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import android.widget.Toast;

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

public class WhileSleeping extends AppCompatActivity{

    private Button btn_WakeUp;

    public static LineChart Live_chart;

    //variable for the plot
    public static ArrayList<Entry> values2 = new ArrayList<>();

    public static ArrayList<Double> abData = new ArrayList<Double>();
    public double apneaFilter[];
    public double[][] windows;
    double[][] features;
    public String LOG_WhileSleeping = "whileSleeping";
    //public String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    public String LOG_perm = "permission";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_while_sleeping);

        setTitle("Input data");
        // Start plot definition -------------------------------------------------------------------
        Live_chart = findViewById(R.id.plotWhileSleep);

        // set an alternative background color
        Live_chart.setBackgroundColor(Color.DKGRAY);
        // enable description text
        Live_chart.getDescription().setEnabled(true);

        // enable touch gestures, scaling and dragging
        Live_chart.setTouchEnabled(true);
        Live_chart.setDragEnabled(true);
        Live_chart.setScaleEnabled(true);
        Live_chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        Live_chart.setPinchZoom(false);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        Live_chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = Live_chart.getLegend();

        // modify the legend ...
        l.setForm(LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = Live_chart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = Live_chart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(6f);
        leftAxis.setAxisMinimum(-6f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = Live_chart.getAxisRight();
        rightAxis.setEnabled(false);
        // End plot definition ---------------------------------------------------------------------

        // define date and time format and get current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timef = new SimpleDateFormat("HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String date = sdf.format(c.getTime());
        String startTime = timef.format(c.getTime());

        MainActivity.context = getApplicationContext();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_PERMISSION_CODE);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                requestPermissions(PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        Log.d(LOG_perm,"Permission granted: " + PackageManager.PERMISSION_GRANTED);
        Log.d(LOG_perm,"Media mounted: " + Environment.MEDIA_MOUNTED);

        OurThreads t1 = new OurThreads("txt_reading");

        //"setOnClickListener" run if the specified button is clicked.
        btn_WakeUp = findViewById(R.id.btnStopSleep);
        btn_WakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // The application goes to summary screen.
                try {
                    int numApp=0;
                    if(t1.isStopped()==false){
                        windows=t1.exit();
                        OurThreads.plottingflag=0;
                    }
                    else{
                        windows= t1.abDataValues;
                        OurThreads.plottingflag=0;
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
                            else {
                                for(int k=0; k<40; k++) {
                                    apneaFilter[i+k] = 0;
                                }
                            }
                        }
                    }

                    for (int i = 0; i < apneaFilter.length; i++) {
                        double val = apneaFilter[i];
                        float f = (float)val;
                        values2.add(new Entry(i, f));
                    }

                    Log.d(LOG_WhileSleeping,"Value num app: " + numApp);

                    // go to night summary activity
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

        if (features[2]<-0.362501){
            if (features[1]<-0.749474){
                if(features[3]<-1.36836){
                    if(features[0]<0.0798692){
                        if(features[0]<-0.0602798) {
                            estimated_class = 1;
                        }
                    }
                    else{
                        estimated_class = 1;
                    }
                }
                else if (features[2]<-0.701544){
                    if(features[2]>=-0.886344){
                        estimated_class = 1;
                    }
                }
                else{
                    estimated_class = 1;
                }
            }
            else if(features[2]<-0.457291){
                estimated_class = 1;
            }
            else if (features[3]<-0.616622){
                estimated_class = 1;
            }
            else if(features[3]>=-0.317697){
                estimated_class = 1;
            }
        }
        else if (features[2]<0.166415){
            if(features[2]<-0.239886){
                if(features[3]>=0.126378){
                    estimated_class = 1;
                }
            }
        }

        return  estimated_class;
    }


    // Edit the set view



}
