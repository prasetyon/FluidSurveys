package rawMaterial;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal1.R;

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

public class RawMaterialAddActivity extends AppCompatActivity {

    Spinner spinnerProject;
    Button btnBack, btnSubmit;
    EditText edNamaKomponen, edNamaBlok, edProses;
    CheckBox cbComply;
    Context Activity;
    private Bundle extras;
    private boolean created;
    private String create;
    private String projectID ;
    private String komponenID ;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String project;

    private void initiate()
    {
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnUpdate);
        edNamaKomponen = (EditText) findViewById(R.id.edNamaKomponen);
        edProses = (EditText) findViewById(R.id.edProses);
        edNamaBlok = (EditText) findViewById(R.id.edNamaBlok);
        cbComply = (CheckBox) findViewById(R.id.cbComply);
        edProses.setEnabled(false);

        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_add);
        setTitle("RAW MATERIALS");

        Activity = this;

        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");
        komponenID = extras.getString("COMPONENT_ID");

        initiate();

        spinnerProject = (Spinner) findViewById(R.id.spinnerJenisFabrikasi);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.fabrikasi_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerProject.setAdapter(spinnerAdapter);
        spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                project = spinnerProject.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnUpdate:
                    String id_proyek = projectID;
                    String nama_komponen = edNamaKomponen.getText().toString();
                    String jenis_komponen = project;
                    String nama_blok = edNamaBlok.getText().toString();
                    String standard;
                    if(cbComply.isChecked()) standard="1";
                    else standard="0";
                    new BackgroundTask().execute(id_proyek, nama_komponen, jenis_komponen, nama_blok, standard);
                    break;
                case R.id.btnBack:
                    intent = new Intent(RawMaterialAddActivity.this, RawMaterialActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    class BackgroundTask extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public BackgroundTask() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Creating component...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            created = true;
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if (created && create.equals("1")) {
                Toast.makeText(RawMaterialAddActivity.this, "You component has been created!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RawMaterialAddActivity.this, RawMaterialListActivity.class);
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
            String id_proyek = params[0];
            String nama_komponen = params[1];
            String jenis_komponen = params[2];
            String nama_blok = params[3];
            String standard = params[4];

            String create_url = "http://mobile4day.com/ship-inspection/new_component.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_proyek", "UTF-8") + "=" + URLEncoder.encode(id_proyek, "UTF-8") + "&" +
                                URLEncoder.encode("nama_komponen", "UTF-8") + "=" + URLEncoder.encode(nama_komponen, "UTF-8") + "&" +
                                URLEncoder.encode("jenis_komponen", "UTF-8") + "=" + URLEncoder.encode(jenis_komponen, "UTF-8") + "&" +
                                URLEncoder.encode("nama_blok", "UTF-8") + "=" + URLEncoder.encode(nama_blok, "UTF-8") + "&" +
                                URLEncoder.encode("standard", "UTF-8") + "=" + URLEncoder.encode(standard, "UTF-8");
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
}
