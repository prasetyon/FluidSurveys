package subAssembly;

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
import android.widget.EditText;
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

public class SubAssemblyCheckActivity extends AppCompatActivity {

    Button btnBack, btnSubmit;
    EditText edNamaKomponen, edNamaBlok, edProsesSub, edBagianSub;
    boolean doubleBackToExitPressedOnce;
    Spinner spinnerProject;
    Context Activity;
    private Bundle extras;
    private boolean created;
    private String create;
    private String projectID;
    private String project;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String namaKomponen;
    private String idKomponen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_assembly_check);
        setTitle("SUB ASSEMBLY");

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        namaKomponen = extras.getString("KOMPONEN_NAME");
        projectID = extras.getString("PROJECT_ID");
        idKomponen = extras.getString("COMPONENT_ID");

        edNamaKomponen = (EditText) findViewById(R.id.edNamaKomponen);
        edNamaBlok = (EditText) findViewById(R.id.edNamaBlok);
        edProsesSub = (EditText) findViewById(R.id.edProsesSub);
        edBagianSub = (EditText) findViewById(R.id.edBagianSub);

        edBagianSub.setEnabled(false);
        edProsesSub.setEnabled(false);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);

        spinnerProject = (Spinner) findViewById(R.id.spinnerJenisSub);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.fabrikasi_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerProject.setAdapter(spinnerAdapter);
        spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                project = spinnerProject.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new getComponent().execute(idKomponen);
    }

    private void showProject(String s)
    {
        JSONObject jsonObject;
        //Toast.makeText(DetailProjectActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            edProsesSub.setText(jo.getString("proses"));
            edNamaKomponen.setText(jo.getString("nama_komponen"));
            edNamaBlok.setText(jo.getString("nama_blok"));
            if(jo.getString("jenis_komponen").equals("Pelat")) spinnerProject.setSelection(0);
            else spinnerProject.setSelection(1);
            edBagianSub.setText(jo.getString("bagian"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getComponent extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getComponent() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Getting component details...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            showProject(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id_komponen = params[0];

            String create_url = "http://mobile4day.com/welding-inspection/get_component_detail.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_komponen", "UTF-8") + "=" + URLEncoder.encode(id_komponen, "UTF-8");
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

    class updateComponent extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updateComponent() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("updating component...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            created = true;
            //Toast.makeText(Activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if (created && create.equals("1")) {
                Toast.makeText(SubAssemblyCheckActivity.this, "You component has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SubAssemblyCheckActivity.this, SubAssemblyListActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            String nama_komponen = params[0];
            String jenis_komponen = params[1];
            String nama_blok = params[2];
            String id_komponen = params[3];

            String create_url = "http://mobile4day.com/welding-inspection/update_component.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("nama_komponen", "UTF-8") + "=" + URLEncoder.encode(nama_komponen, "UTF-8") + "&" +
                                URLEncoder.encode("jenis_komponen", "UTF-8") + "=" + URLEncoder.encode(jenis_komponen, "UTF-8") + "&" +
                                URLEncoder.encode("nama_blok", "UTF-8") + "=" + URLEncoder.encode(nama_blok, "UTF-8") + "&" +
                                URLEncoder.encode("id_komponen", "UTF-8") + "=" + URLEncoder.encode(id_komponen, "UTF-8");
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
                create = response;
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
            Intent intent;
            switch (v.getId()){
                case R.id.btnSubmit:
                    String componentName = edNamaKomponen.getText().toString();
                    String blockName = edNamaBlok.getText().toString();
                    new updateComponent().execute(componentName, project, blockName, idKomponen);
                    break;
                case R.id.btnBack:
                    intent = new Intent(SubAssemblyCheckActivity.this, SubAssemblyListActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
            }
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
