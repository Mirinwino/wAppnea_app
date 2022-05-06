package com.example.wappnea;

// Visualization_of_results.java
// This module creates plots of whole data, detection of application and
// doctor annotations to compare with color of blue, red and green respectively.
// The plots will be in a chart might be scrolled, zoomed and converted to landscape.
// Agnese Calvani, Esra Gizem Gungor, Miriam Peinado Martin, Omer Altan

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
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
import java.util.ArrayList;
import java.util.List;

public class Visualization_of_results extends AppCompatActivity implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private LineChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

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
        chart.setPinchZoom(false);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.DKGRAY);

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

        // limit the number of visible entries
        chart.setVisibleXRangeMaximum(2400*3); //limit to 5min

        // move to the latest entry
        chart.moveViewToX(data.getEntryCount());

        // End plot definition ---------------------------------------------------------------------
    }


    private void setData(int count){
        // set1 = dataset with the abdominal belt signal (abData)
        // set2 = set with the results of apnea prediction (apneafilter)
        // set3 = set with the real results of apnea prediction (reallabels)

        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values3 = new ArrayList<>();
        ArrayList<Entry> values4 = new ArrayList<>();
        //ArrayList<Entry> values2 = new ArrayList<>(); //defined in WhileSleeping.java

        // We need to create ArrayList<Entry> from the ArrayList<Double> that is abData
        for (int i = 0; i < count; i++) {
            double val = WhileSleeping.abData.get(i);
            float f = (float)val;
            values1.add(new Entry(i, f));
        }
        for (int i = 0; i < count; i++) {
            values3.add(WhileSleeping.values2.get(i));
        }
        for (int i = 0; i < count; i++) {
            double val = WhileSleeping.reallabels.get(i);
            float f = (float)val;
            values4.add(new Entry(i, f));
        }

        LineDataSet set1, set2, set3;

        // create a dataset and give it a type
        set1 = new LineDataSet(values1, "Abdominal signal");
        set2 = new LineDataSet(values3, "Apnea detection");
        set3 = new LineDataSet(values4, "Apnea doctor labels");

        //start definition set1 --------------------------------------------------------------------
        set1.setAxisDependency(AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircles(false);
        set1.setVisible(true);
        //end definition set1 ----------------------------------------------------------------------

        //start definition set2 --------------------------------------------------------------------
        set2.setAxisDependency(AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setDrawCircles(false);
        set2.setLineWidth(5f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        //end definition set2 ----------------------------------------------------------------------

        //start definition set3 --------------------------------------------------------------------
        set3.setAxisDependency(AxisDependency.RIGHT);
        set3.setColor(Color.GREEN);
        set3.setDrawCircles(false);
        set3.setDrawFilled(true);
        set3.setLineWidth(0.5f);
        set3.setFillAlpha(40);
        set3.setFillColor(Color.GREEN);
        set3.setHighLightColor(Color.rgb(244, 117, 117));
        //end definition set3 ----------------------------------------------------------------------

        // create a data object with the data sets
        LineData data = new LineData(set1, set2, set3);
        data.setValueTextColor(Color.LTGRAY);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText(String.valueOf(seekBarX.getProgress()));

        int value_progress = (seekBarX.getProgress() * WhileSleeping.abData.size()/100);

        setData(value_progress);

        // redraw
        chart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.actionToggleValues: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (chart.getData() != null) {
                    chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
                    chart.invalidate();
                }
                break;
            }
            case R.id.actionToggleFilled: {

                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawCirclesEnabled())
                        set.setDrawCircles(false);
                    else
                        set.setDrawCircles(true);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleCubic: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.CUBIC_BEZIER);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleStepped: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.STEPPED);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionToggleHorizontalCubic: {
                List<ILineDataSet> sets = chart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.HORIZONTAL_BEZIER);
                }
                chart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                if (chart.isPinchZoomEnabled())
                    chart.setPinchZoom(false);
                else
                    chart.setPinchZoom(true);

                chart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled());
                chart.notifyDataSetChanged();
                break;
            }
            case R.id.animateX: {
                chart.animateX(2000);
                break;
            }
            case R.id.animateY: {
                chart.animateY(2000);
                break;
            }
            case R.id.animateXY: {
                chart.animateXY(2000, 2000);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }
    @Override
    public void onNothingSelected() {
    }
}
