package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.lang.Math;
import java.lang.String;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_1 = findViewById(R.id.btnStartSleep);

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhileSleeping();
            }
        });
    }

    private void openWhileSleeping() {
        Intent intent_1 = new Intent(this, WhileSleeping.class);
        startActivity(intent_1);
    }

    private static double[][] extract_feature(double[][] dataWindows){

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

        if (features[3]<-0.365707){
            if (features[4]<-0.708075){
                if(features[3]<-0.897993){
                    if(features[2]<-1.72754){
                        estimated_class = 1;
                    }
                }
                    else if (features[4]<-1.61229){
                        if(features[2]<-2.06214){
                            estimated_class = 1;
                        }
                    }
                    else{
                        estimated_class = 1;
                    }
            }
                else if(features[3]<-0.52561){
                    estimated_class = 1;
                }
                    else if (features[2]<-0.579833){
                        estimated_class = 1;
                    }
                        else if(features[1]>=0.151772){
                            estimated_class = 1;
                        }
        }
            else if (features[3]<0.139){
                if(features[3]<-0.240547){
                    if(features[4]<0.242473){
                        if(features[1]>=0.995959){
                            estimated_class = 1;
                        }
                    }
                    else{
                        estimated_class = 1;
                    }
                }
                    else if(features[1]>=1.65685){
                        estimated_class = 1;
                    }
            }

        return  estimated_class;
    }
}


