package com.example.wappnea;

// WhileSleeping.java
// This java module includes graph initialization for continuous plotting,
// external storage read permission check, taking the data from thread named
// OurThread.java, applying feature extraction, standardization and decision tree,
// specifying time information. Standard deviation, mean, energy and mean derivative
// are features calculated via functions defined at the bottom of class.
// Agnese Calvani, Esra Gizem Gungor, Miriam Peinado Martin, Omer Altan

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import java.util.ArrayList;
import android.content.pm.PackageManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class WhileSleeping extends AppCompatActivity{
    private Button btn_WakeUp;
    //Live_chart is initializing the plotting section for continuous plot
    public static LineChart Live_chart;
    //values2 for the plot of application detection result
    public static ArrayList<Entry> values2 = new ArrayList<>();
    //duration, abData, reallabels, windows and features will keep
    //the sleeping time, whole data as array, doctor annotations,
    //and data matrix with 5-second (40-sample) chunks and extracted features
    public static long duration;
    public static ArrayList<Double> abData = new ArrayList<Double>();
    public static ArrayList<Double> reallabels = new ArrayList<Double>();
    public double[][] windows;
    double[][] features;
    public String LOG_WhileSleeping = "whileSleeping";
    //Here values for permission check and request is defined.
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

        // Start plot definition -------------------------------------------------------------------
        setTitle("Input data");
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

        // context is defining here to find the external storage location in thread application
        MainActivity.context = getApplicationContext();

        // Reading permission from external storage is checked and given if it is necessary. Manifest is also changed accordingly.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_PERMISSION_CODE);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.d(LOG_perm,"Permission granted: " + PackageManager.PERMISSION_GRANTED);
            } else {
                requestPermissions(PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        }
        Log.d(LOG_perm,"Permission granted: " + PackageManager.PERMISSION_GRANTED);
        Log.d(LOG_perm,"Media mounted: " + Environment.MEDIA_MOUNTED);

        // Thread for reading and plotting data continuously is defined here. It is starting directly when it is defined.
        OurThreads t1 = new OurThreads("txt_reading");

        // "setOnClickListener" run if the specified button is clicked.
        btn_WakeUp = findViewById(R.id.btnStopSleep);
        btn_WakeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    int numApp=0;
                    // when the button is clicked, it will stop the reading and continuous plotting first.
                    if(t1.isStopped()==false){
                        windows=t1.exit();
                        OurThreads.plottingflag=0;
                    }
                    else{
                        windows= t1.abDataValues;
                        OurThreads.plottingflag=0;
                    }

                    // apneaFilter will kept when the apnea event occurs within 5 seconds windows
                    double apneaFilter[]=new double[windows.length*40];

                    //functions are defined at bottom of this class.
                    features=extract_feature(windows);

                    // A vector is defined to use as one column of feature matrix in decision tree.
                    double[] My_0 = new double[4];

                    // decision_tree function is applied to determine apnea stages
                    for (int i=0; i<windows.length;i++){
                        for (int j=0;j<4;j++) {
                            My_0[j] = features[j][i];
                        }
                        if(decision_tree(My_0)==1){
                            for(int k=0; k<40; k++){
                                //if there is apnea, the location is valued as 1; if not, 0.
                                apneaFilter[40*i+k]=1;
                            }
                        }
                        else{
                            for(int k=0; k<40; k++){
                                apneaFilter[40*i+k]=0;
                            }
                        }
                    }

                    // 5 second filtering is applied here. If the apnea stage is just a
                    // single 5 second, it will be regarded.
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
                    // The application detection result is put into the plot to
                    // transfer into the Visualization_of_results.java intent.
                    for (int i = 0; i < apneaFilter.length; i++) {
                        double val = apneaFilter[i];
                        float f = (float)val;
                        values2.add(new Entry(i, f));
                    }
                    Log.d(LOG_WhileSleeping,"Value num app: " + numApp);

                    // The application goes to summary screen.
                    Intent intent_2 = new Intent(WhileSleeping.this, NightSummary.class);

                    //calculate end time and duration based on number of samples acquired
                    long start = c.getTimeInMillis();
                    duration = windows.length*5*1000; // in milliseconds
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
                    //Transfering the calculations to the next intent
                    intent_2.putExtras(info);
                    startActivity(intent_2);
                }
                catch (Exception e) {
                    Log.d(LOG_WhileSleeping,e.getMessage());
                }
            }
        });
    }

    // This function has input of data matrix by
    // (number of windows) x (number of sample at 5 seconds).
    // It use mean, std, energy and mean_der functions to extract
    // features. Then it standardize with standardization function.
    // The return is the feature matrix containing all 4 features.
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

    // This function has input of data vector within 5 seconds window.
    // It extracts mean of data at this window.
    private static double mean(double[] dataWindow){

        double result =0.0;
        for (int i=0;i<dataWindow.length;i++){
            result += dataWindow[i];
        }

        result /= (double)dataWindow.length;

        return result;
    }

    // This function has input of data vector within 5 seconds window.
    // It extracts standard deviation of data at this window.
    private static double std(double[] dataWindow){
        double standardDeviation = 0.0;
        double meanResult =mean(dataWindow);
        for (int i = 0; i < dataWindow.length; i++) {
            standardDeviation = standardDeviation + Math.pow((dataWindow[i] - meanResult), 2);
        }
        double res = Math.sqrt(standardDeviation / dataWindow.length);
        return res;
    }

    // This function has input of data vector within 5 seconds window.
    // It extracts energy of data at this window.
    private static double energy(double[] dataWindow){
        double energy = 0.0;
        for (int i = 0; i < dataWindow.length; i++) {
            energy=energy+(dataWindow[i])*(dataWindow[i]);
        }
        energy /= (double)dataWindow.length;

        return energy;
    }

    // This function has input of data vector within 5 seconds window.
    // It extracts data's mean of derivative vector at this window.
    private static double mean_derivative(double[] dataWindow){

        double[] mean_der = new double[dataWindow.length];

        for (int i=1;i<dataWindow.length;i++){
            mean_der[i] = Math.abs(dataWindow[i] - dataWindow[i-1]);
        }

        return mean(mean_der);
    }

    // This function has input of data vector indicates a feature parameter.
    // It standardize the parameters by subtracting the mean and dividing to standard deviation.
    private static double[] standardization(double[] parameterValues){
        double meanParam = mean(parameterValues);
        double stdParam = std(parameterValues);
        double[] standardizedParameterValues = new double[parameterValues.length];

        for (int i=0; i<parameterValues.length; i++){
            standardizedParameterValues[i] = (parameterValues[i]-meanParam)/stdParam;
        }
        return standardizedParameterValues;
    }

    // This function has input of data vector indicates a feature parameter.
    // It applies decision tree classification with parameters
    // reached from trained model in MATLAB
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
}
