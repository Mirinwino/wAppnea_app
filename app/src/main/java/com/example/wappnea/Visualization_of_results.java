package com.example.wappnea;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SeekBar.OnSeekBarChangeListener;

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

import java.io.Serializable;
import java.util.ArrayList;

public class Visualization_of_results extends AppCompatActivity {
    private LineChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    public ArrayList<Entry> v1, v2, v3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_of_results);

        setTitle("Results");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Start plot definition -------------------------------------------------------------------
        tvX = findViewById(R.id.tvXMax);
        seekBarX = findViewById(R.id.seekBar1);

        chart = findViewById(R.id.chart1);
        //chart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);
        // enable description text
        chart.getDescription().setEnabled(true);

        // enable touch gestures, scaling and dragging
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = chart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.DKGRAY);
        leftAxis.setAxisMaximum(6f);
        leftAxis.setAxisMinimum(-6f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setTextColor(Color.DKGRAY);
        rightAxis.setAxisMaximum(2f);
        rightAxis.setAxisMinimum(-2f);
        rightAxis.setDrawGridLines(true);

        setData(WhileSleeping.abData.size());
        // End plot definition ---------------------------------------------------------------------

    }

    private void setData(int count){
        // set1 = dataset with the abdominal belt signal (abData)
        // set2 = set with the results of apnea prediction (apneafilter)

        ArrayList<Entry> values1 = new ArrayList<>();
        //ArrayList<Entry> values2 = new ArrayList<>(); //defined in WhileSleeping.java

        // We need to create ArrayList<Entry> from the ArrayList<Double> that is abData
        for (int i = 0; i < count; i++) {
            double val = WhileSleeping.abData.get(i);
            float f = (float)val;
            values1.add(new Entry(i, f));
        }

        LineDataSet set1, set2;

        //start definition set1 --------------------------------------------------------------------
        set1 = new LineDataSet(values1, "Abdominal signal");

        set1.setAxisDependency(AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircles(false);
        //set1.setFillFormatter(new MyFillFormatter(0f));
        //set1.setDrawHorizontalHighlightIndicator(false);
        set1.setVisible(true);
        //set1.setCircleHoleColor(Color.WHITE);
        //end definition set1 ----------------------------------------------------------------------

        //start definition set2 --------------------------------------------------------------------
        // create a dataset and give it a type
        set2 = new LineDataSet(WhileSleeping.values2, "Apnea detection");
        set2.setAxisDependency(AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setDrawCircles(false);
        set2.setLineWidth(2f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        //set2.setFillFormatter(new MyFillFormatter(900f));
        //end definition set2 ----------------------------------------------------------------------

        // create a data object with the data sets
        LineData data = new LineData(set1, set2);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));
        //tvY.setText(String.valueOf(seekBarY.getProgress()));

        setData(seekBarX.getProgress());

        // redraw
        chart.invalidate();
    }

    // BACK FROM TOP MENU --------------------------------------------------------------------------
    // this event will enable the back function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
