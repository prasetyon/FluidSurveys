package report;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal2.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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
import java.util.UUID;

import static dokumen.UploadFileActivity.UPLOAD_URL;

public class AssemblyReport extends AppCompatActivity {

    TextView tvProjectName, tvBlockName, tvBlockLocation, tvWelded, tvType, tvInspectionDate, tvDocumentName, tvMaterial, tvWeldingProcess, tvFlux, tvShieldingGas;
    TextView tvSpatters, tvPorosity, tvSurfaceConcavity, tvPinhole, tvSurfaceColdLap, tvSurfaceUndercut, tvSurfaceUnderfill,
            tvCrack, tvExcessive, tvStopStart, tvWideBead, tvHighLow, tvDeformation;
    TextView tvStdSpatters, tvStdPorosity, tvStdSurfaceConcavity, tvStdPinhole, tvStdSurfaceColdLap, tvStdSurfaceUndercut, tvStdSurfaceUnderfill,
            tvStdCrack, tvStdExcessive, tvStdStopStart, tvStdWideBead, tvStdHighLow, tvStdDeformation;
    TextView tvResSpatters, tvResPorosity, tvResSurfaceConcavity, tvResPinhole, tvResSurfaceColdLap, tvResSurfaceUndercut, tvResSurfaceUnderfill,
            tvResCrack, tvResExcessive, tvResStopStart, tvResWideBead, tvResHighLow, tvResDeformation;
    TextView tvComment;
    Button btnOk;
    Context Activity;
    private Bundle extras;
    private boolean created=false;
    private String create="";
    private String projectID;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String blockID;
    private String blockLocation;
    private String proses;
    private Uri filePath;

    private void initiate()
    {
        btnOk = (Button) findViewById(R.id.btnOk);
        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        tvBlockName = (TextView) findViewById(R.id.tvBlockName);
        tvBlockLocation = (TextView) findViewById(R.id.tvBlockLocation);
        tvWelded = (TextView) findViewById(R.id.tvWelded);
        tvType = (TextView) findViewById(R.id.tvType);
        tvInspectionDate = (TextView) findViewById(R.id.tvInspectionDate);
        tvDocumentName = (TextView) findViewById(R.id.tvDocumentName);
        tvMaterial = (TextView) findViewById(R.id.tvMaterial);
        tvWeldingProcess = (TextView) findViewById(R.id.tvWeldingProcess);
        tvFlux = (TextView) findViewById(R.id.tvFlux);
        tvShieldingGas = (TextView) findViewById(R.id.tvShieldingGas);
        tvComment = (TextView) findViewById(R.id.tvComment);

        tvSpatters = (TextView) findViewById(R.id.tvSpatters);
        tvPorosity = (TextView) findViewById(R.id.tvPorosity);
        tvSurfaceConcavity = (TextView) findViewById(R.id.tvSurfaceConcavity);
        tvPinhole = (TextView) findViewById(R.id.tvPinhole);
        tvSurfaceColdLap = (TextView) findViewById(R.id.tvSurfaceColdLap);
        tvSurfaceUndercut = (TextView) findViewById(R.id.tvSurfaceUndercut);
        tvSurfaceUnderfill = (TextView) findViewById(R.id.tvSurfaceUnderfill);
        tvCrack = (TextView) findViewById(R.id.tvCrack);
        tvExcessive = (TextView) findViewById(R.id.tvExcessive);
        tvStopStart = (TextView) findViewById(R.id.tvStopStart);
        tvWideBead = (TextView) findViewById(R.id.tvWideBead);
        tvHighLow = (TextView) findViewById(R.id.tvHighLow);
        //tvDeformation = (TextView) findViewById(R.id.tvDeformation);

        tvStdSpatters = (TextView) findViewById(R.id.tvStdSpatters);
        tvStdPorosity = (TextView) findViewById(R.id.tvStdPorosity);
        tvStdSurfaceConcavity = (TextView) findViewById(R.id.tvStdSurfaceConcavity);
        tvStdPinhole = (TextView) findViewById(R.id.tvStdPinhole);
        tvStdSurfaceColdLap = (TextView) findViewById(R.id.tvStdSurfaceColdLap);
        tvStdSurfaceUndercut = (TextView) findViewById(R.id.tvStdSurfaceUndercut);
        tvStdSurfaceUnderfill = (TextView) findViewById(R.id.tvStdSurfaceUnderfill);
        tvStdCrack = (TextView) findViewById(R.id.tvStdCrack);
        tvStdExcessive = (TextView) findViewById(R.id.tvStdExcessive);
        tvStdStopStart = (TextView) findViewById(R.id.tvStdStopStart);
        tvStdWideBead = (TextView) findViewById(R.id.tvStdWideBead);
        tvStdHighLow = (TextView) findViewById(R.id.tvStdHighLow);
        //tvStdDeformation = (TextView) findViewById(R.id.tvStdDeformation);

        tvResSpatters = (TextView) findViewById(R.id.tvResSpatters);
        tvResPorosity = (TextView) findViewById(R.id.tvResPorosity);
        tvResSurfaceConcavity = (TextView) findViewById(R.id.tvResSurfaceConcavity);
        tvResPinhole = (TextView) findViewById(R.id.tvResPinhole);
        tvResSurfaceColdLap = (TextView) findViewById(R.id.tvResSurfaceColdLap);
        tvResSurfaceUndercut = (TextView) findViewById(R.id.tvResSurfaceUndercut);
        tvResSurfaceUnderfill = (TextView) findViewById(R.id.tvResSurfaceUnderfill);
        tvResCrack = (TextView) findViewById(R.id.tvResCrack);
        tvResExcessive = (TextView) findViewById(R.id.tvResExcessive);
        tvResStopStart = (TextView) findViewById(R.id.tvResStopStart);
        tvResWideBead = (TextView) findViewById(R.id.tvResWideBead);
        tvResHighLow = (TextView) findViewById(R.id.tvResHighLow);
        //tvResDeformation = (TextView) findViewById(R.id.tvResDeformation);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDocument(v);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembly_report);
        setTitle("Assembly Report");

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");
        blockID = extras.getString("ID");
        proses = extras.getString("PROSES");

        initiate();

        new getBlock().execute();
        new getProject().execute();
        new getBlockDetail().execute();
    }

    private void showBlock(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            boolean check = false;
            String norm, std;
            Integer val, std1, std2;

            norm = jo.getString("spatters");
            std = jo.getString("std_spatters");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvSpatters.setText(jo.getString("spatters"));
            tvStdSpatters.setText(jo.getString("std_spatters"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResSpatters.setText("Accept");
            else tvResSpatters.setText("Reject");

            norm = jo.getString("porosity");
            std = jo.getString("std_porosity");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvPorosity.setText(jo.getString("porosity"));
            tvStdPorosity.setText(jo.getString("std_porosity"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResPorosity.setText("Accept");
            else tvResPorosity.setText("Reject");

            norm = jo.getString("sc");
            std = jo.getString("std_sc");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvSurfaceConcavity.setText(jo.getString("sc"));
            tvStdSurfaceConcavity.setText(jo.getString("std_sc"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResSurfaceConcavity.setText("Accept");
            else tvResSurfaceConcavity.setText("Reject");

            norm = jo.getString("pinhole");
            std = jo.getString("std_pinhole");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvPinhole.setText(jo.getString("pinhole"));
            tvStdPinhole.setText(jo.getString("std_pinhole"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResPinhole.setText("Accept");
            else tvResPinhole.setText("Reject");

            norm = jo.getString("scl");
            std = jo.getString("std_scl");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvSurfaceColdLap.setText(jo.getString("scl"));
            tvStdSurfaceColdLap.setText(jo.getString("std_scl"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResSurfaceColdLap.setText("Accept");
            else tvResSurfaceColdLap.setText("Reject");

            norm = jo.getString("undercut");
            std = jo.getString("std_undercut");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvSurfaceUndercut.setText(jo.getString("undercut"));
            tvStdSurfaceUndercut.setText(jo.getString("std_undercut"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResSurfaceUndercut.setText("Accept");
            else tvResSurfaceUndercut.setText("Reject");

            norm = jo.getString("underfill");
            std = jo.getString("std_underfill");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvSurfaceUnderfill.setText(jo.getString("underfill"));
            tvStdSurfaceUnderfill.setText(jo.getString("std_underfill"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResSurfaceUnderfill.setText("Accept");
            else tvResSurfaceUnderfill.setText("Reject");

            norm = jo.getString("crack");
            std = jo.getString("std_crack");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvCrack.setText(jo.getString("crack"));
            tvStdCrack.setText(jo.getString("std_crack"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResCrack.setText("Accept");
            else tvResCrack.setText("Reject");

            norm = jo.getString("excessive");
            std = jo.getString("std_excessive");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvExcessive.setText(jo.getString("excessive"));
            tvStdExcessive.setText(jo.getString("std_excessive"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResExcessive.setText("Accept");
            else tvResExcessive.setText("Reject");

            norm = jo.getString("stop");
            std = jo.getString("std_stop");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvStopStart.setText(jo.getString("stop"));
            tvStdStopStart.setText(jo.getString("std_stop"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResStopStart.setText("Accept");
            else tvResStopStart.setText("Reject");

            norm = jo.getString("wb");
            std = jo.getString("std_wb");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvWideBead.setText(jo.getString("wb"));
            tvStdWideBead.setText(jo.getString("std_wb"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResWideBead.setText("Accept");
            else tvResWideBead.setText("Reject");

            norm = jo.getString("hl");
            std = jo.getString("std_hl");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvHighLow.setText(jo.getString("hl"));
            tvStdHighLow.setText(jo.getString("std_hl"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResHighLow.setText("Accept");
            else tvResHighLow.setText("Reject");

            tvInspectionDate.setText(jo.getString("date"));
            tvMaterial.setText(jo.getString("material_spec"));
            tvWeldingProcess.setText(jo.getString("welding_process"));
            tvFlux.setText(jo.getString("flux"));
            tvShieldingGas.setText(jo.getString("shielding"));
            tvComment.setText(jo.getString("comment"));

            if(jo.getString("doc1")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation1)).execute(jo.getString("doc1"));
            if(jo.getString("doc2")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation2)).execute(jo.getString("doc2"));
            if(jo.getString("doc3")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation3)).execute(jo.getString("doc3"));
            if(jo.getString("doc4")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation4)).execute(jo.getString("doc4"));
            if(jo.getString("doc5")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation5)).execute(jo.getString("doc5"));
            if(jo.getString("doc6")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation6)).execute(jo.getString("doc6"));

            tvDocumentName.setText(jo.getString("doc_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getBlock extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getBlock() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading all block...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(AssemblyDetailActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showBlock(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url;
            String from = "swsp";

            create_url = "http://mobile4day.com/welding-inspection/get_process.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(blockID, "UTF-8") +"&"+
                        URLEncoder.encode("from","UTF-8")+"="+URLEncoder.encode(from,"UTF-8");
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

    private void showBlockDetail(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            tvBlockName.setText(jo.getString("nama_blok"));
            tvBlockLocation.setText(jo.getString("posisi"));
            tvWelded.setText(jo.getString("welded_to"));
            tvType.setText(jo.getString("type"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getBlockDetail extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getBlockDetail() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading all block...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(AssemblyDetailActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showBlockDetail(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url;

            create_url = "http://mobile4day.com/welding-inspection/get_blok_detail.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(blockID, "UTF-8");
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

    private void showProject(String s)
    {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            tvProjectName.setText(jo.getString("nama_proyek"));
            new getUserDetail().execute(jo.getString("nik1"));
            new getUserDetail().execute(jo.getString("nik2"));
            new getUserDetail().execute(jo.getString("nik3"));
            new getUserDetail().execute(jo.getString("nik4"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getProject extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getProject(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ProgressDialog.setMessage("Reading all project...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(DetailProjectActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showProject(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id_proyek = projectID;
            String create_url = "http://mobile4day.com/welding-inspection/get_project.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("id_proyek", "UTF-8")+"="+URLEncoder.encode(id_proyek,"UTF-8");
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

    private void showUserDetail(String s)
    {
        JSONObject jsonObject;
        //Toast.makeText(DetailProjectActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);
            //Toast.makeText(UserListActivity.this, "Sertif='" + jo.getString("sertif") + "'", Toast.LENGTH_SHORT).show();

            String dep = jo.getString("department");
            if(dep.equals("Coating Inspector")) new DownloadImageTask((ImageView) findViewById(R.id.ivSignInspector)).execute(jo.getString("signature"));
            else if(dep.equals("Owner")) new DownloadImageTask((ImageView) findViewById(R.id.ivSignOwner)).execute(jo.getString("signature"));
            else if(dep.equals("Shipyard")) new DownloadImageTask((ImageView) findViewById(R.id.ivSignShipyard)).execute(jo.getString("signature"));
            else if(dep.equals("Class")) new DownloadImageTask((ImageView) findViewById(R.id.ivSignClass)).execute(jo.getString("signature"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getUserDetail extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getUserDetail(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ProgressDialog.setMessage("Reading all project...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(DetailProjectActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showUserDetail(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/welding-inspection/get_user_detail.php";
            String nik = params[0];

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("nik", "UTF-8")+"="+URLEncoder.encode(nik,"UTF-8");
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void printDocument(View view)
    {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        btnOk.setVisibility(View.INVISIBLE);
        LinearLayout content = (LinearLayout) findViewById(R.id.activity_raw_material_report);
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();
        File file,f=null;
        String x = "Assembly" + projectName + "_" + proses + "_" + blockID + "_" + formattedDate +".png";
        try{
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            {
                file =new File(android.os.Environment.getExternalStorageDirectory(),"Welding Inspection Report");
                if(!file.exists())
                {
                    file.mkdirs();

                }
                f = new File(file, x);
            }
            FileOutputStream ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            Toast.makeText(AssemblyReport.this, "Your report has been saved", Toast.LENGTH_SHORT).show();
            ostream.close();
            uploadMultipart(x);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void uploadMultipart(String name) {
        //getting name for the image

        //getting the actual path of the image
        filePath = Uri.parse("/storage/emulated/0/Welding Inspection Report/" + name);
        String path = filePath.toString();
        //Toast.makeText(InspeksiFinalReportActivity.this, "Path: " + path, Toast.LENGTH_LONG).show();

        if (path == null) {
            Toast.makeText(this, "Please move your file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("id_proyek", projectID) //Adding text parameter to the request
                        .addParameter("proses", "ETC") //Adding text parameter to the request
                        .addParameter("name", name) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
