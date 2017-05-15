package activity;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.DatePicker;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddProjectActivity extends AppCompatActivity {

    Button btnBack, btnSubmit;
    EditText edOwner, edNamaProyek, edPanjangKapal, edLebarKapal, edTinggiKapal, edMuatanKapal, edSaratKapal, edJenisKapal, edMulaiProyek, edSelesaiProyek;
    EditText edDaftarNIK1, edDaftarNIK2, edDaftarNIK3, edDaftarNIK4;
    boolean doubleBackToExitPressedOnce = false;
    private DatePickerDialog dpProyek;
    private SimpleDateFormat dateFormatter;
    private Bundle extras;
    private String username;
    private String role;
    private String from;
    private String proJectName;
    private Context context;
    private boolean created=false;
    private String create="";

    private void initiate()
    {
        edOwner = (EditText) findViewById(R.id.edOwner);
        edNamaProyek = (EditText) findViewById(R.id.edNamaProyek);
        edPanjangKapal = (EditText) findViewById(R.id.edPanjangKapal);
        edLebarKapal = (EditText) findViewById(R.id.edLebarKapal);
        edTinggiKapal = (EditText) findViewById(R.id.edTinggiKapal);
        edMuatanKapal = (EditText) findViewById(R.id.edMuatanKapal);
        edSaratKapal = (EditText) findViewById(R.id.edSaratKapal);
        edJenisKapal = (EditText) findViewById(R.id.edJenisKapal);
        edMulaiProyek = (EditText) findViewById(R.id.edMulaiProyek);
        edSelesaiProyek = (EditText) findViewById(R.id.edSelesaiProyek);
        edDaftarNIK1 = (EditText) findViewById(R.id.edDaftarNIK1);
        edDaftarNIK2 = (EditText) findViewById(R.id.edDaftarNIK2);
        edDaftarNIK3 = (EditText) findViewById(R.id.edDaftarNIK3);
        edDaftarNIK4 = (EditText) findViewById(R.id.edDaftarNIK4);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(activity);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(activity);
        edMulaiProyek.setOnClickListener(activity);
        edSelesaiProyek.setOnClickListener(activity);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        setTitle("NEW PROJECT");
        context = this;

        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        proJectName = extras.getString("PROJECT_NAME");

        initiate();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnSubmit:
                    String owner_proyek = edOwner.getText().toString();
                    String nama_proyek = edNamaProyek.getText().toString();
                    String admin = username;
                    String panjang_kapal = edPanjangKapal.getText().toString();
                    String lebar_kapal = edLebarKapal.getText().toString();
                    String tinggi_kapal = edTinggiKapal.getText().toString();
                    String muatan_kapal = edMuatanKapal.getText().toString();
                    String mulai_proyek = edMulaiProyek.getText().toString();
                    String selesai_proyek = edSelesaiProyek.getText().toString();
                    String nik1 = edDaftarNIK1.getText().toString();
                    String nik2 = edDaftarNIK2.getText().toString();
                    String nik3 = edDaftarNIK3.getText().toString();
                    String nik4 = edDaftarNIK4.getText().toString();
                    String sarat = edSaratKapal.getText().toString();
                    String jenis = edJenisKapal.getText().toString();
                    new BackgroundTask().execute(owner_proyek, nama_proyek, admin, panjang_kapal, lebar_kapal, tinggi_kapal, muatan_kapal, mulai_proyek, selesai_proyek, nik1, nik2, nik3, nik4, sarat, jenis);
                    break;
                case R.id.btnBack:
                    intent = new Intent(AddProjectActivity.this, MainPageActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.edMulaiProyek:
                case R.id.edSelesaiProyek:
                    showDate(v);
                    break;
            }
        }
    };

    public void showDate(View v){
        final View vView = v;
        Calendar newCalendar = Calendar.getInstance();
        dpProyek = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                switch (vView.getId()) {
                    case R.id.edMulaiProyek:
                        edMulaiProyek.setText(dateFormatter.format(newDate.getTime()));
                        break;
                    case R.id.edSelesaiProyek:
                        edSelesaiProyek.setText(dateFormatter.format(newDate.getTime()));
                        break;
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dpProyek.show();
    }

    class BackgroundTask extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public BackgroundTask(){
            ProgressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Creating project...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            created = true;
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(created && create.equals("1")){
                Toast.makeText(AddProjectActivity.this, "You project has been created!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProjectActivity.this, MainPageActivity.class);
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

            String owner_proyek = params[0];
            String nama_proyek = params[1];
            String admin = params[2];
            String panjang_kapal = params[3];
            String lebar_kapal = params[4];
            String tinggi_kapal = params[5];
            String muatan_kapal = params[6];
            String mulai_proyek = params[7];
            String selesai_proyek = params[8];
            String nik1 = params[9];
            String nik2 = params[10];
            String nik3 = params[11];
            String nik4 = params[12];
            String sarat = params[13];
            String jenis = params[14];

            String create_url = "http://mobile4day.com/welding-inspection/new_project.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("owner_proyek", "UTF-8")+"="+URLEncoder.encode(owner_proyek,"UTF-8")+"&"+
                                URLEncoder.encode("nama_proyek","UTF-8")+"="+URLEncoder.encode(nama_proyek,"UTF-8")+"&"+
                                URLEncoder.encode("admin","UTF-8")+"="+URLEncoder.encode(admin,"UTF-8")+"&"+
                                URLEncoder.encode("panjang_kapal","UTF-8")+"="+URLEncoder.encode(panjang_kapal,"UTF-8")+"&"+
                                URLEncoder.encode("lebar_kapal","UTF-8")+"="+URLEncoder.encode(lebar_kapal,"UTF-8")+"&"+
                                URLEncoder.encode("tinggi_kapal","UTF-8")+"="+URLEncoder.encode(tinggi_kapal,"UTF-8")+"&"+
                                URLEncoder.encode("muatan_kapal","UTF-8")+"="+URLEncoder.encode(muatan_kapal,"UTF-8")+"&"+
                                URLEncoder.encode("mulai_proyek","UTF-8")+"="+URLEncoder.encode(mulai_proyek,"UTF-8")+"&"+
                                URLEncoder.encode("selesai_proyek","UTF-8")+"="+URLEncoder.encode(selesai_proyek,"UTF-8")+"&"+
                                URLEncoder.encode("nik1","UTF-8")+"="+URLEncoder.encode(nik1,"UTF-8")+"&"+
                                URLEncoder.encode("nik2","UTF-8")+"="+URLEncoder.encode(nik2,"UTF-8")+"&"+
                                URLEncoder.encode("nik3","UTF-8")+"="+URLEncoder.encode(nik3,"UTF-8")+"&"+
                                URLEncoder.encode("nik4","UTF-8")+"="+URLEncoder.encode(nik4,"UTF-8")+"&"+
                                URLEncoder.encode("sarat","UTF-8")+"="+URLEncoder.encode(sarat,"UTF-8")+"&"+
                                URLEncoder.encode("jenis","UTF-8")+"="+URLEncoder.encode(jenis,"UTF-8");
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