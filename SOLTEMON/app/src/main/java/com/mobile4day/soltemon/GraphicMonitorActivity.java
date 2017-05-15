package com.mobile4day.soltemon;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class GraphicMonitorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_monitor);

        LinearLayout llHour = (LinearLayout) findViewById(R.id.llPerJam);
        LinearLayout llDay = (LinearLayout) findViewById(R.id.llPerHari);
        drawChartHour(llHour, 1);
        drawChartHour(llDay, 2);
    }

    private void drawChartHour(LinearLayout llView, int from)
    {
        // Creating an XYSeries for Expense
        XYSeries usageDay = new XYSeries("Usage per day");
        String x_values[];
        String y_values[];
        if(from==1) {
            x_values = new String[]{"00:00", "02:00", "04:00", "06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00"};
             y_values = new String[]{"210", "230", "240", "235", "220", "215", "205", "215", "225", "245", "230", "220"};
        }
        else{
            x_values = new String[]{"00:00", "02:00", "04:00", "06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00"};
            y_values = new String[]{"300", "280", "275", "120", "70", "50", "55", "60", "50", "150", "240", "270"};
        }

        for(int i=0; i<y_values.length; i++){
            usageDay.add(i, Double.parseDouble(y_values[i]));
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(usageDay);

        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.BLACK); //color of the graph set to cyan
        expenseRenderer.setPointStyle(PointStyle.CIRCLE);
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(2);
        expenseRenderer.setDisplayChartValues(true);
        expenseRenderer.setDisplayChartValues(true);

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multiRenderer.setXLabels(0);
        if(from==1) {
            multiRenderer.setChartTitle("Tegangan per Jam");
            multiRenderer.setXTitle("Waktu");
            multiRenderer.setYTitle("Volt");
        }
        else{
            multiRenderer.setChartTitle("Penggunaan per Hari");
            multiRenderer.setXTitle("Waktu");
            multiRenderer.setYTitle("Watt");
        }

        /***
         * Customizing graphs
         */
        //setting text size of the title
        multiRenderer.setChartTitleTextSize(28);
        //setting text size of the axis title
        multiRenderer.setAxisTitleTextSize(20);
        //setting text size of the graph lable
        multiRenderer.setLabelsTextSize(20);
        //setting zoom buttons visiblity
        multiRenderer.setZoomButtonsVisible(true);
        //setting pan enablity which uses graph to move on both axis
        multiRenderer.setPanEnabled(false, false);
        //setting click false on graph
        multiRenderer.setClickEnabled(false);
        //setting zoom to false on both axis
        multiRenderer.setZoomEnabled(false, false);
        //setting lines to display on y axis
        multiRenderer.setShowGridY(false);
        //setting lines to display on x axis
        multiRenderer.setShowGridX(false);
        //setting legend to fit the screen size
        multiRenderer.setFitLegend(true);
        //setting displaying line on grid
        multiRenderer.setShowGrid(false);
        //setting zoom to false
        multiRenderer.setZoomEnabled(false);
        //setting external zoom functions to false
        multiRenderer.setExternalZoomEnabled(false);
        //setting displaying lines on graph to be formatted(like using graphics)
        multiRenderer.setAntialiasing(true);
        //setting to in scroll to false
        multiRenderer.setInScroll(false);
        //setting to set legend height of the graph
        multiRenderer.setLegendHeight(30);
        //setting x axis label align
        multiRenderer.setXLabelsAlign(Paint.Align.CENTER);
        //setting y axis label to align
        multiRenderer.setYLabelsAlign(Paint.Align.LEFT);
        //setting text style
        multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
        //setting no of values to display in y axis
        multiRenderer.setYLabels(7);
        // setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
        // if you use dynamic values then get the max y value and set here
        multiRenderer.setYAxisMax(350);
        multiRenderer.setYAxisMin(0);
        //setting used to move the graph on xaxiz to .5 to the right
        multiRenderer.setXAxisMin(-0.5);
        //setting max values to be display in x axis
        multiRenderer.setXAxisMax(x_values.length-0.5);
        //setting bar size or space between two bars
        multiRenderer.setBarSpacing(0.3);
        //Setting margin color of the graph to transparent
        //multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent_background));
        multiRenderer.setApplyBackgroundColor(true);

        // Setting the color of graph component
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setXLabelsColor(Color.WHITE);
        multiRenderer.setYLabelsColor(0, Color.WHITE);
        multiRenderer.setLabelsColor(Color.WHITE);


        //setting the margin size for the graph in the order top, left, bottom, right
        multiRenderer.setMargins(new int[]{50, 30, 30, 30});

        for(int i=0; i<x_values.length;i++){
            multiRenderer.addXTextLabel(i, x_values[i]);
        }

        // Adding expenseRenderer to multipleRenderer
        multiRenderer.addSeriesRenderer(expenseRenderer);

        //this part is used to display graph on the xml
        //LinearLayout chartContainer = (LinearLayout) findViewById(R.id.llPerHari);
        //remove any views before u paint the chart
        llView.removeAllViews();
        //drawing bar chart
        View chart = ChartFactory.getLineChartView(GraphicMonitorActivity.this, dataset, multiRenderer);
        //adding the view to the linearlayout
        llView.addView(chart);
    }
}
