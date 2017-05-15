package com.mobile4day.camp2017;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private View chart;
    private Button btnInsert, btnPeserta, btnFinance;
    private RelativeLayout rlMain;
    private TextView tvTotal;
    private String status;
    private boolean login = false;
    private int check = 0;
    private TextView contentText;

    private String[] mMonth = new String[]{
            "Sword", "Shield", "CShield", "Panitia", "Konselor", "Alumni", "Umum"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlMain = (RelativeLayout) findViewById(R.id.activity_main);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnPeserta = (Button) findViewById(R.id.btnPeserta);
        btnFinance = (Button) findViewById(R.id.btnFinance);
        btnInsert.setOnClickListener(activity);
        btnPeserta.setOnClickListener(activity);
        btnFinance.setOnClickListener(activity);

        Intent intent = getIntent();
        if(intent==null) {
            rlMain.setVisibility(View.INVISIBLE);
            checkPasscode();
        }
        else{
            new getBalance().execute();
        }
    }

    private void checkPasscode()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.auth);
        dialog.setTitle("Check Passcode");
        dialog.setCancelable(true);
        final EditText edPasscode = (EditText) dialog.findViewById(R.id.edPasscode);
        final Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edPasscode.getText().toString().equals("51705174")){
                    check = 1;
                    login = true;
                    rlMain.setVisibility(View.VISIBLE);
                    new getBalance().execute();
                }
                else {
                    check = -1;
                    login = false;
                    finish();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId())
            {
                case R.id.btnInsert:
                    showDialog();
                    new getBalance().execute();
                    break;
                case R.id.btnPeserta:
                    intent = new Intent(MainActivity.this, PesertaActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btnFinance:
                    intent = new Intent(MainActivity.this, FinanceActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private void showDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.peserta);
        dialog.setTitle("New Transaction");
        dialog.setCancelable(true);
        final EditText edNRP = (EditText) dialog.findViewById(R.id.edNRP);
        final EditText edName = (EditText) dialog.findViewById(R.id.edName);
        final EditText edSex = (EditText) dialog.findViewById(R.id.edSex);
        final EditText edHp = (EditText) dialog.findViewById(R.id.edHp);
        final EditText edPenyakit = (EditText) dialog.findViewById(R.id.edPenyakit);
        final EditText edAlergi = (EditText) dialog.findViewById(R.id.edAlergi);
        final TextView tvID = (TextView) dialog.findViewById(R.id.tvID);
        contentText = (TextView) dialog.findViewById(R.id.tvID);
        final Spinner spinnerStatus = (Spinner) dialog.findViewById(R.id.spinnerStatus);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        final Button btnScanQR = (Button) dialog.findViewById(R.id.btnScanQR);
        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQR(v);
                btnScanQR.setVisibility(View.GONE);
            }
        });

        final ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this, R.array.status_list, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = spinnerStatus.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nrp, nama, sex, hp, penyakit, alergi, id;
                nrp = edNRP.getText().toString();
                nama = edName.getText().toString();
                sex = edSex.getText().toString();
                hp = edHp.getText().toString();
                penyakit = edPenyakit.getText().toString();
                alergi = edAlergi.getText().toString();
                id = tvID.getText().toString();

                /*if(nrp.length()!=10)
                    Toast.makeText(MainActivity.this, "Check your nrp", Toast.LENGTH_SHORT).show();
                else*/ new newPeserta().execute(nrp, status, nama, sex, hp, penyakit, alergi, id);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void scanQR(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(String.valueOf("SCAN"));
        integrator.setResultDisplayDuration(0);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // display it on screen
            contentText.setText(scanContent);

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void drawChart(int peserta, int pkk, int cpkk, int panitia, int konselor, int alumni, int umum)
    {
        int[] expense = {peserta,pkk,cpkk,panitia,konselor,alumni,umum};

        // Creating an XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("Banyak Peserta");

        for(int i=0;i<expense.length;i++){
            expenseSeries.add(i,expense[i]);
        }

        // Creating a dataset to hold  series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(expenseSeries);

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
        expenseRenderer.setColor(Color.BLACK); //color of the graph set to cyan
        expenseRenderer.setFillPoints(true);
        expenseRenderer.setLineWidth(2);
        expenseRenderer.setDisplayChartValues(true);
        expenseRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Rekapitulasi Pendaftar CAMP 2017");
        //multiRenderer.setXTitle("Status Peserta");
        //multiRenderer.setYTitle("Banyak Pendaftar");

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
        multiRenderer.setZoomButtonsVisible(false);
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
        multiRenderer.setYAxisMax(300);
        multiRenderer.setYAxisMin(0);
        //setting used to move the graph on xaxiz to .5 to the right
        multiRenderer.setXAxisMin(-1);
        //setting max values to be display in x axis
        multiRenderer.setXAxisMax(mMonth.length-0.5);
        //setting bar size or space between two bars
        multiRenderer.setBarSpacing(0.3);
        //Setting margin color of the graph to transparent
        multiRenderer.setMarginsColor(getResources().getColor(R.color.transparent_background));
        multiRenderer.setApplyBackgroundColor(true);

        // Setting the color of graph component
        multiRenderer.setBackgroundColor(Color.WHITE);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setLabelsColor(Color.BLACK);


        //setting the margin size for the graph in the order top, left, bottom, right
        multiRenderer.setMargins(new int[]{50, 10, 30, 30});

        for(int i=0; i< mMonth.length;i++){
            multiRenderer.addXTextLabel(i, mMonth[i]);
        }

        // Adding expenseRenderer to multipleRenderer
        multiRenderer.addSeriesRenderer(expenseRenderer);

        //this part is used to display graph on the xml
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
        //remove any views before u paint the chart
        chartContainer.removeAllViews();
        //drawing bar chart
        chart = ChartFactory.getBarChartView(MainActivity.this, dataset, multiRenderer, BarChart.Type.DEFAULT);
        //adding the view to the linearlayout
        chartContainer.addView(chart);
    }

    class newPeserta extends AsyncTask<String, String, String> {
        ProgressDialog ProgressDialog;

        public newPeserta(){
            ProgressDialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Adding participant...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")){
                Toast.makeText(MainActivity.this, "New participant has been added!", Toast.LENGTH_SHORT).show();
                new getBalance().execute();
            }
            else Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String nrp = params[0];
            String status = params[1];
            String nama = params[2];
            String sex = params[3];
            String hp = params[4];
            String penyakit = params[5];
            String alergi = params[6];
            String id = params[7];

            String create_url = "http://mobile4day.com/pkmbk-camp/new_participant.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("nrp", "UTF-8")+"="+URLEncoder.encode(nrp,"UTF-8")+"&"+
                                URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8")+"&"+
                                URLEncoder.encode("nama","UTF-8")+"="+URLEncoder.encode(nama,"UTF-8")+"&"+
                                URLEncoder.encode("sex","UTF-8")+"="+URLEncoder.encode(sex,"UTF-8")+"&"+
                                URLEncoder.encode("hp","UTF-8")+"="+URLEncoder.encode(hp,"UTF-8")+"&"+
                                URLEncoder.encode("penyakit","UTF-8")+"="+URLEncoder.encode(penyakit,"UTF-8")+"&"+
                                URLEncoder.encode("alergi","UTF-8")+"="+URLEncoder.encode(alergi,"UTF-8")+"&"+
                                URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while((line = bufferedReader.readLine())!= null){
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString().trim();
        }
    }

    private void showBalance(String s)
    {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            int peserta, pkk, cpkk, panitia, konselor, alumni, umum;
            String total;
            peserta = Integer.parseInt(jo.getString("sword"));
            pkk  = Integer.parseInt(jo.getString("shield"));
            cpkk = Integer.parseInt(jo.getString("calon_shield"));
            panitia = Integer.parseInt(jo.getString("panitia"));
            konselor = Integer.parseInt(jo.getString("konselor"));
            alumni = Integer.parseInt(jo.getString("alumni"));
            umum = Integer.parseInt(jo.getString("umum"));
            total = jo.getString("total");

            tvTotal.setText("Total Peserta : " + total);
            drawChart(peserta, pkk, cpkk, panitia, konselor, alumni, umum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getBalance extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getBalance(){
            ProgressDialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading total participant...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(MainActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showBalance(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/pkmbk-camp/get_info.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("info", "UTF-8")+"="+URLEncoder.encode("","UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while((line = bufferedReader.readLine())!= null){
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString().trim();
        }
    }
}