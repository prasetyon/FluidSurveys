package assembly;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal2.R;

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

public class AssemblyAddActivity extends AppCompatActivity {

    Button btnBack, btnSubmit;
    TextView tvProses;
    EditText edBagian;
    Spinner spinnerLokasi, spinnerFrame, spinnerNama, spinnerWelded, spinnerType;
    boolean doubleBackToExitPressedOnce = false;
    Context Activity;
    private Bundle extras;
    private boolean created=false;
    private String lokasi, frame, nama, welded, type;
    private String create="";
    private String projectID;
    private String username;
    private String role;
    private String from;
    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembly_add);
        setTitle("ASSEMBLY");

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");

        tvProses = (TextView) findViewById(R.id.tvProses);
        edBagian = (EditText) findViewById(R.id.edBagian);

        edBagian.setEnabled(false);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);

        spinnerLokasi = (Spinner) findViewById(R.id.spinnerLokasi);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.location_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerLokasi.setAdapter(spinnerAdapter);
        spinnerLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lokasi = spinnerLokasi.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerNama = (Spinner) findViewById(R.id.spinnerNama);
        ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(this, R.array.nama_array, android.R.layout.simple_spinner_item);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerNama.setAdapter(spinnerAdapter2);
        spinnerNama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nama = spinnerLokasi.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerWelded = (Spinner) findViewById(R.id.spinnerWelded);
        ArrayAdapter<CharSequence> spinnerAdapter3 = ArrayAdapter.createFromResource(this, R.array.welded_array, android.R.layout.simple_spinner_item);
        spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerWelded.setAdapter(spinnerAdapter3);
        spinnerWelded.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                welded = spinnerWelded.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> spinnerAdapter4 = ArrayAdapter.createFromResource(this, R.array.type_of_weld, android.R.layout.simple_spinner_item);
        spinnerAdapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerType.setAdapter(spinnerAdapter4);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = spinnerType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFrame = (Spinner) findViewById(R.id.spinnerFrame);
        ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(this, R.array.frame_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerFrame.setAdapter(spinnerAdapter1);
        spinnerFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                frame = spinnerFrame.getSelectedItem().toString();
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
                case R.id.btnSubmit:

                    new NewBlock().execute(projectID, nama, lokasi, frame);
                    break;
                case R.id.btnBack:
                    intent = new Intent(AssemblyAddActivity.this, AssemblyActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    class NewBlock extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public NewBlock(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Creating block...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            created = true;
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(created && create.equals("1")){
                Toast.makeText(AssemblyAddActivity.this, "You block has been created!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AssemblyAddActivity.this, AssemblyListActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(AssemblyAddActivity.this, s, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id_proyek = params[0];
            String nama_blok = params[1];
            String lokasi_blok = params[2];
            String frame = params[3];
            String create_url = "http://mobile4day.com/welding-inspection/new_block.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id_proyek", "UTF-8")+"="+URLEncoder.encode(id_proyek,"UTF-8")+"&"+
                                URLEncoder.encode("nama_blok","UTF-8")+"="+URLEncoder.encode(nama_blok,"UTF-8")+"&"+
                                URLEncoder.encode("lokasi_blok","UTF-8")+"="+URLEncoder.encode(lokasi_blok,"UTF-8")+"&"+
                                URLEncoder.encode("frame","UTF-8")+"="+URLEncoder.encode(frame,"UTF-8")+"&"+
                                URLEncoder.encode("welded","UTF-8")+"="+URLEncoder.encode(welded,"UTF-8")+"&"+
                                URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");
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
