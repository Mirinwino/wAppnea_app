package com.example.wappnea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.lang.Math;
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
}


