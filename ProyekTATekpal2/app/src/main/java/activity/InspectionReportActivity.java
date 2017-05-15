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

import com.example.prasetyon.proyektatekpal2.R;

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

import report.AssemblyReport;
import report.ErectionReport;
import report.InspeksiFinalReport;
import report.SubAssemblyReport;

public class InspectionReportActivity extends AppCompatActivity {

    Button btnBack, btnViewReport;
    Spinner spinnerProses, spinnerID;
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

                if(project.equals("Sub Assembly")){
                    //Toast.makeText(InspectionReportActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                    proses = "SA";

                    spinnerID.setVisibility(View.VISIBLE);

                    new getID().execute("komponen");
                }
                else if(project.equals("Assembly")){
                    //Toast.makeText(InspectionReportActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                    proses = "SWSP";
                    spinnerID.setVisibility(View.VISIBLE);

                    new getID().execute("blok");
                }
                else if(project.equals("Erection")){
                    //Toast.makeText(InspectionReportActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                    proses = "SWPP";
                    spinnerID.setVisibility(View.VISIBLE);

                    new getID().execute("blok");
                }
                else if(project.equals("Final Inspection")){
                    //Toast.makeText(InspectionReportActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                    proses = "FINAL";
                    spinnerID.setVisibility(View.VISIBLE);

                    new getID().execute("blok");
                }
                else if(project.equals("Select a proses...")){
                    spinnerID.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

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

                if(proses.equals("SA"))
                    listBlok.add(jo.getString("id") + ". " + jo.getString("name"));
                else
                    listBlok.add(jo.getString("id") + ". " + jo.getString("name")+" welded to "+jo.get("welded"));
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

            String create_url = "http://mobile4day.com/welding-inspection/get_id.php";
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

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            if(v.getId() == R.id.btnViewReport){
                if(proses.equals("SA")) intent = new Intent(InspectionReportActivity.this, SubAssemblyReport.class);
                else if(proses.equals("SWSP")) intent = new Intent(InspectionReportActivity.this, AssemblyReport.class);
                else if(proses.equals("SWPP")) intent = new Intent(InspectionReportActivity.this, ErectionReport.class);
                else if(proses.equals("FINAL")) intent = new Intent(InspectionReportActivity.this, InspeksiFinalReport.class);
            }
            else if(v.getId() == R.id.btnBack){
                intent = new Intent(InspectionReportActivity.this, MainPageActivity.class);
            }
            extras.putString("PROSES", proses);
            extras.putString("ID", IDs);
            intent.putExtras(extras);
            startActivity(intent);
            if(v.getId()==R.id.btnBack) finish();
        }
    };

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
