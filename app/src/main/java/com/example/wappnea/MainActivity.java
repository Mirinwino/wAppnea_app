package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    // reference https://searchcode.com/codesearch/view/78094523/
    private static double mean(double[] datawindow){

        double result =0.0;
        for (int i=0;i<datawindow.length;i++){
            result += datawindow[i];
        }

        result /= (double)datawindow.length;

        return result;
    }
    //https://www.geeksforgeeks.org/java-program-to-calculate-standard-deviation/
    private static double std(double[] datawindow){
        double standardDeviation = 0.0;
        double meanresult =mean(datawindow);
        for (int i = 0; i < datawindow.length; i++) {
            standardDeviation = standardDeviation + Math.pow((datawindow[i] - meanresult), 2);
        }
        double res = Math.sqrt(standardDeviation / datawindow.length);
        return res;
    }

    private static double energy(double[] datawindow){
        double energy = 0.0;
        for (int i = 0; i < datawindow.length; i++) {
            energy=energy+(datawindow[i])*(datawindow[i]);
        }
        energy /= (double)datawindow.length;

        return energy;
    }

    // reference https://searchcode.com/codesearch/view/78094523/
    private static double[] substractMean(double[] datawindow){

        double[] substracted = new double[datawindow.length];
        double meanresult =mean(datawindow);

        for (int i=0;i<datawindow.length;i++){
            substracted[i] = datawindow[i] - meanresult;
        }
        return substracted;
    }
}


