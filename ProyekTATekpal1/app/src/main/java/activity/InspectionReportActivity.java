package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import report.BlockReport;
import report.InspeksiFinalReportActivity;
import report.PullOffReport;
import report.RawMaterialReportActivity;
import report.SSPReport;
import report.SWPReport;

public class InspectionReportActivity extends AppCompatActivity {

    Button btnBack, btnViewReport;
    Spinner spinnerProses, spinnerID, spinnerSubProses;
    boolean doubleBackToExitPressedOnce = false;
    Context Activity;
    private Bundle extras;
    private String projectID;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String proses="", IDs="";

    private void initiate()
    {
        btnBack = (Button) findViewById(R.id.btnBack);
        btnViewReport = (Button) findViewById(R.id.btnViewReport);
        btnBack.setOnClickListener(activity);
        btnViewReport.setOnClickListener(activity);

        spinnerID = (Spinner) findViewById(R.id.spinnerID);
        spinnerID.setVisibility(View.INVISIBLE);
        spinnerProses = (Spinner) findViewById(R.id.spinnerProses);
        spinnerSubProses = (Spinner) findViewById(R.id.spinnerSubProses);
        spinnerSubProses.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_report);
        setTitle("REPORT");

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");

        initiate();

        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.proses_list_2, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerProses.setAdapter(spinnerAdapter);
        spinnerProses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String project = spinnerProses.getSelectedItem().toString();

                if(project.equals("Raw Material")){
                    //Toast.makeText(InspectionReportActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                    proses = "PSPSP";

                    spinnerID.setVisibility(View.VISIBLE);
                    spinnerSubProses.setVisibility(View.INVISIBLE);

                    new getID().execute("komponen");
                }
                else if(project.equals("Block")){
                    //Toast.makeText(InspectionReportActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                    proses = "";
                    spinnerID.setVisibility(View.VISIBLE);
                    spinnerSubProses.setVisibility(View.VISIBLE);

                    new getID().execute("blok");
                }
                else if(project.equals("Select a proses...")){
                    spinnerID.setVisibility(View.INVISIBLE);
                    spinnerSubProses.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            if(v.getId() == R.id.btnViewReport){
                if(proses.equals("PSPSP")) intent = new Intent(InspectionReportActivity.this, RawMaterialReportActivity.class);
                else if(proses.equals("SWP1") || proses.equals("SWP2")|| proses.equals("SWP1AE") || proses.equals("SWP2AE")) intent = new Intent(InspectionReportActivity.this, SWPReport.class);
                else if(proses.equals("SSPSP") || proses.equals("SSP1C")) intent = new Intent(InspectionReportActivity.this, SSPReport.class);
                else if(proses.equals("1C1SC") || proses.equals("1SC2SC") || proses.equals("2SC2C") || proses.equals("2C3C") || proses.equals("3C4C") || proses.equals("4C5C")) intent = new Intent(InspectionReportActivity.this, BlockReport.class);
                else if(proses.equals("FINAL")) intent = new Intent(InspectionReportActivity.this, InspeksiFinalReportActivity.class);
                else if(proses.equals("PULLOFF")) intent = new Intent(InspectionReportActivity.this, PullOffReport.class);
            }
            else if(v.getId() == R.id.btnBack){
                intent = new Intent(InspectionReportActivity.this, MainPageActivity.class);
            }
            extras.putString("PROSES", proses);
            extras.putString("ID", IDs);
            intent.putExtras(extras);
            startActivity(intent);
            //finish();
        }
    };

    private void showIDSpinner(String list)
    {
        spinnerID.setVisibility(View.VISIBLE);
        List<String> listBlok = new ArrayList<>();

        JSONObject jsonObject;
        //Toast.makeText(DetailProjectActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(list);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                listBlok.add(jo.getString("id") + ". " + jo.getString("name"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listBlok);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerID.setAdapter(spinnerAdapter);
        spinnerID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String project1 = spinnerID.getSelectedItem().toString().substring(0, spinnerID.getSelectedItem().toString().indexOf(" "));
                IDs = project1;

                if(!proses.equals("PSPSP"))
                    new getProses().execute(IDs);

                //Toast.makeText(UploadFileActivity.this, "Proses = " + proses + "Bagian = " + bagian + "ID = " + IDs + "Blok = " + blok, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class getID extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getID() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Getting details...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            showIDSpinner(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String from = params[0];

            String create_url = "http://mobile4day.com/ship-inspection/get_id.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_proyek", "UTF-8") + "=" + URLEncoder.encode(projectID, "UTF-8") + "&" +
                                URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
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

    private void showProsesSpinner(String list)
    {
        spinnerSubProses.setVisibility(View.VISIBLE);
        List<String> listBlok = new ArrayList<>();

        JSONObject jsonObject;
        //Toast.makeText(DetailProjectActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(list);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            String swp1 = jo.getString("swp1");
            if(swp1.equals("ae") || swp1.equals("bc")) listBlok.add("Steel Work Preparation 1");
            String sspsp = jo.getString("sspsp");
            if(sspsp.equals("ae") || sspsp.equals("bc")) listBlok.add("Secondary Surface Preparation and Shop Primer");
            String swp2 = jo.getString("swp2");
            if(swp2.equals("ae") || swp2.equals("bc")) listBlok.add("Steel Work Preparation 2");
            String swp1ae = jo.getString("swp1ae");
            if(swp1ae.equals("ae") || swp1ae.equals("bc")) listBlok.add("Steel Work Preparation 1 After Erection");
            String swp2ae = jo.getString("swp2ae");
            if(swp2ae.equals("ae") || swp2ae.equals("bc")) listBlok.add("Steel Work Preparation 2 After Erection");
            String ssp1c = jo.getString("ssp1c");
            if(ssp1c.equals("ae") || ssp1c.equals("bc")) listBlok.add("Secondary Surface Preparation and 1st Coat");
            String b1c1sc = jo.getString("1c1sc");
            if(b1c1sc.equals("ae") || b1c1sc.equals("bc")) listBlok.add("1st Coat and 1st Stripe Coat");
            String b1sc2sc = jo.getString("1sc2sc");
            if(b1sc2sc.equals("ae") || b1sc2sc.equals("bc")) listBlok.add("1st Stripe Coat and 2nd Stripe Coat");
            String b2sc2c = jo.getString("2sc2c");
            if(b2sc2c.equals("ae") || b2sc2c.equals("bc")) listBlok.add("2nd Stripe Coat and 2nd Coat");
            String b2c3c = jo.getString("2c3c");
            if(b2c3c.equals("ae") || b2c3c.equals("bc")) listBlok.add("2nd Coat and 3rd Coat");
            String b3c4c = jo.getString("3c4c");
            if(b3c4c.equals("ae") || b3c4c.equals("bc")) listBlok.add("3rd Coat and 4th Coat");
            String b4c5c = jo.getString("4c5c");
            if(b4c5c.equals("ae") || b4c5c.equals("bc")) listBlok.add("4th Coat and 5th Coat");
            String pulloff = jo.getString("pulloff");
            if(pulloff.equals("ae") || pulloff.equals("bc")) listBlok.add("Pull Off Strength Test");
            listBlok.add("Final Inspection");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listBlok);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSubProses.setAdapter(spinnerAdapter);
        spinnerSubProses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from = spinnerSubProses.getSelectedItem().toString();
                if(from.equals("Steel Work Preparation 1") || from.equals("Steel Work Preparation 2") || from.equals("Steel Work Preparation 1 After Erection") || from.equals("Steel Work Preparation 2 After Erection")) {
                    if(from.equals("Steel Work Preparation 1")) proses = "SWP1";
                    else if(from.equals("Steel Work Preparation 2")) proses = "SWP2";
                    else if(from.equals("Steel Work Preparation 1 After Erection")) proses = "SWP1AE";
                    else if(from.equals("Steel Work Preparation 2 After Erection")) proses = "SWP2AE";
                }
                else if(from.equals("Final Inspection")) {
                    proses = "FINAL";
                }
                else if(from.equals("Pull Off Strength Test")) {
                    proses = "PULLOFF";
                }
                else if(from.equals("Secondary Surface Preparation and Shop Primer") || from.equals("Secondary Surface Preparation and 1st Coat")) {
                    if(from.equals("Secondary Surface Preparation and Shop Primer")) proses = "SSPSP";
                    else if(from.equals("Secondary Surface Preparation and 1st Coat")) proses = "SSP1C";
                }
                else {
                    if(from.equals("1st Coat and 1st Stripe Coat")) proses = "1C1SC";
                    else if(from.equals("1st Stripe Coat and 2nd Stripe Coat")) proses = "1SC2SC";
                    else if(from.equals("2nd Stripe Coat and 2nd Coat")) proses = "2SC2C";
                    else if(from.equals("2nd Coat and 3rd Coat")) proses = "2C3C";
                    else if(from.equals("3rd Coat and 4th Coat")) proses = "3C4C";
                    else if(from.equals("4th Coat and 5th Coat")) proses = "4C5C";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class getProses extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getProses() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Getting details...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            showProsesSpinner(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id_blok = params[0];

            String create_url = "http://mobile4day.com/ship-inspection/get_proses.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(id_blok, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
