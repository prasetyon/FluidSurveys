package report;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal1.R;

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

import pl.polidea.view.ZoomView;

import static android.view.View.GONE;
import static dokumen.UploadFileActivity.UPLOAD_URL;

public class SSPReport extends AppCompatActivity {

    TextView tvProjectName, tvBlockName, tvBlockLocation, tvArea, tvInspectionDate, tvProses, tvStage, tvDocumentName, tvSurfaceContaminant,
            tvSolventCleaning, tvSolventStd, tvPreparationMethod, tvCleanlinessGrade, tvTypeOfAbrasive, tvAbrasiveSize, tvAbrasiveGrade, tvRustGrade,
            tvResultDQR, tvResultSPM, tvWaterSolubleSalt, tvDryBulb, tvWetBulb, tvSurfaceTemperature, tvRelativeHumidity, tvDewPoint, tvBlastingType,
            tvPaintingMethod, tvPaintName, tvColourShadeNo, tvBatchNo, tvThinnerNo, tvVolumeSolid, tvApplicationDate, tvPotLife, tvCuringTime;
    TextView tvDQRI1, tvDQRI2, tvSPMI1, tvSPMI2, tvSPMI3, tvComment;
    TextView tvCleanliness, tvWeather, tvWind, tvMixing, tvPumpType, tvPumpRatio;
    ImageView ivDocumentation, ivDQR1, ivDQR2, ivSPM1, ivSPM2, ivSPM3;
    Button btnOk;
    Context Activity;
    View v;
    private Bundle extras;
    private String projectID;
    private String project;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String namaKomponen;
    private String idBlock;
    private String proses;
    private Uri filePath;

    private void initiate()
    {
        btnOk = (Button) findViewById(R.id.btnOk);
        ivDQR1 = (ImageView) findViewById(R.id.ivDQR1);
        ivDQR2 = (ImageView) findViewById(R.id.ivDQR2);
        ivSPM1 = (ImageView) findViewById(R.id.ivSPM1);
        ivSPM2 = (ImageView) findViewById(R.id.ivSPM2);
        ivSPM3 = (ImageView) findViewById(R.id.ivSPM3);
        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        tvBlockName = (TextView) findViewById(R.id.tvBlockName);
        tvBlockLocation = (TextView) findViewById(R.id.tvBlockLocation);
        tvProses = (TextView) findViewById(R.id.tvProses);
        tvStage = (TextView) findViewById(R.id.tvStage);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvComment = (TextView) findViewById(R.id.tvComment);
        tvInspectionDate = (TextView) findViewById(R.id.tvInspectionDate);
        tvDocumentName = (TextView) findViewById(R.id.tvDocumentName);
        tvSurfaceContaminant = (TextView) findViewById(R.id.tvSurfaceContaminant);
        tvPreparationMethod = (TextView) findViewById(R.id.tvPreparationMethod);
        tvCleanlinessGrade = (TextView) findViewById(R.id.tvCleanlinessGrade);
        tvTypeOfAbrasive = (TextView) findViewById(R.id.tvTypeOfAbrasive);
        tvAbrasiveSize = (TextView) findViewById(R.id.tvAbrasiveSize);
        tvAbrasiveGrade = (TextView) findViewById(R.id.tvAbrasiveGrade);
        tvRustGrade = (TextView) findViewById(R.id.tvRustGrade);
        tvSolventCleaning = (TextView) findViewById(R.id.tvSolventCleaning);
        tvBlastingType = (TextView) findViewById(R.id.tvBlastingType);
        tvSolventStd = (TextView) findViewById(R.id.tvSolventStd);
        tvResultDQR = (TextView) findViewById(R.id.tvResultDQR);
        tvResultSPM = (TextView) findViewById(R.id.tvResultSPM);
        tvWaterSolubleSalt = (TextView) findViewById(R.id.tvWaterSolubleSalt);
        tvDryBulb = (TextView) findViewById(R.id.tvDryBulb);
        tvWetBulb = (TextView) findViewById(R.id.tvWetBulb);
        tvSurfaceTemperature = (TextView) findViewById(R.id.tvSurfaceTemperature);
        tvRelativeHumidity = (TextView) findViewById(R.id.tvRelativeHumidity);
        tvDewPoint = (TextView) findViewById(R.id.tvDewPoint);
        tvPaintingMethod = (TextView) findViewById(R.id.tvPaintingMethod);
        tvPaintName = (TextView) findViewById(R.id.tvPaintName);
        tvColourShadeNo = (TextView) findViewById(R.id.tvColourShadeNo);
        tvBatchNo = (TextView) findViewById(R.id.tvBatchNo);
        tvThinnerNo = (TextView) findViewById(R.id.tvThinnerNo);
        tvVolumeSolid = (TextView) findViewById(R.id.tvVolumeSolid);
        tvApplicationDate = (TextView) findViewById(R.id.tvApplicationDate);
        tvPotLife = (TextView) findViewById(R.id.tvPotLife);
        tvCuringTime = (TextView) findViewById(R.id.tvCuringTime);
        tvDQRI1 = (TextView) findViewById(R.id.tvDQRI1);
        tvDQRI2 = (TextView) findViewById(R.id.tvDQRI2);
        tvSPMI1 = (TextView) findViewById(R.id.tvSPMI1);
        tvSPMI2 = (TextView) findViewById(R.id.tvSPMI2);
        tvSPMI3 = (TextView) findViewById(R.id.tvSPMI3);
        tvCleanliness = (TextView) findViewById(R.id.tvCleanliness);
        tvWeather = (TextView) findViewById(R.id.tvWeather);
        tvWind = (TextView) findViewById(R.id.tvWind);
        tvMixing = (TextView) findViewById(R.id.tvMixing);
        tvPumpType = (TextView) findViewById(R.id.tvPumpType);
        tvPumpRatio = (TextView) findViewById(R.id.tvPumpRatio);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDocument(v);
            }
        });
    }

    private void initiate2()
    {
        btnOk = (Button) v.findViewById(R.id.btnOk);
        ivDQR1 = (ImageView) v.findViewById(R.id.ivDQR1);
        ivDQR2 = (ImageView) v.findViewById(R.id.ivDQR2);
        ivSPM1 = (ImageView) v.findViewById(R.id.ivSPM1);
        ivSPM2 = (ImageView) v.findViewById(R.id.ivSPM2);
        ivSPM3 = (ImageView) v.findViewById(R.id.ivSPM3);
        tvProjectName = (TextView) v.findViewById(R.id.tvProjectName);
        tvBlockName = (TextView) v.findViewById(R.id.tvBlockName);
        tvBlockLocation = (TextView) v.findViewById(R.id.tvBlockLocation);
        tvProses = (TextView) v.findViewById(R.id.tvProses);
        tvStage = (TextView) v.findViewById(R.id.tvStage);
        tvArea = (TextView) v.findViewById(R.id.tvArea);
        tvComment = (TextView) v.findViewById(R.id.tvComment);
        tvInspectionDate = (TextView) v.findViewById(R.id.tvInspectionDate);
        tvDocumentName = (TextView) v.findViewById(R.id.tvDocumentName);
        tvSurfaceContaminant = (TextView) v.findViewById(R.id.tvSurfaceContaminant);
        tvPreparationMethod = (TextView) v.findViewById(R.id.tvPreparationMethod);
        tvCleanlinessGrade = (TextView) v.findViewById(R.id.tvCleanlinessGrade);
        tvTypeOfAbrasive = (TextView) v.findViewById(R.id.tvTypeOfAbrasive);
        tvAbrasiveSize = (TextView) v.findViewById(R.id.tvAbrasiveSize);
        tvAbrasiveGrade = (TextView) v.findViewById(R.id.tvAbrasiveGrade);
        tvRustGrade = (TextView) v.findViewById(R.id.tvRustGrade);
        tvSolventCleaning = (TextView) v.findViewById(R.id.tvSolventCleaning);
        tvBlastingType = (TextView) v.findViewById(R.id.tvBlastingType);
        tvSolventStd = (TextView) v.findViewById(R.id.tvSolventStd);
        tvResultDQR = (TextView) v.findViewById(R.id.tvResultDQR);
        tvResultSPM = (TextView) v.findViewById(R.id.tvResultSPM);
        tvWaterSolubleSalt = (TextView) v.findViewById(R.id.tvWaterSolubleSalt);
        tvDryBulb = (TextView) v.findViewById(R.id.tvDryBulb);
        tvWetBulb = (TextView) v.findViewById(R.id.tvWetBulb);
        tvSurfaceTemperature = (TextView) v.findViewById(R.id.tvSurfaceTemperature);
        tvRelativeHumidity = (TextView) v.findViewById(R.id.tvRelativeHumidity);
        tvDewPoint = (TextView) v.findViewById(R.id.tvDewPoint);
        tvPaintingMethod = (TextView) v.findViewById(R.id.tvPaintingMethod);
        tvPaintName = (TextView) v.findViewById(R.id.tvPaintName);
        tvColourShadeNo = (TextView) v.findViewById(R.id.tvColourShadeNo);
        tvBatchNo = (TextView) v.findViewById(R.id.tvBatchNo);
        tvThinnerNo = (TextView) v.findViewById(R.id.tvThinnerNo);
        tvVolumeSolid = (TextView) v.findViewById(R.id.tvVolumeSolid);
        tvApplicationDate = (TextView) v.findViewById(R.id.tvApplicationDate);
        tvPotLife = (TextView) v.findViewById(R.id.tvPotLife);
        tvCuringTime = (TextView) v.findViewById(R.id.tvCuringTime);
        tvDQRI1 = (TextView) v.findViewById(R.id.tvDQRI1);
        tvDQRI2 = (TextView) v.findViewById(R.id.tvDQRI2);
        tvSPMI1 = (TextView) v.findViewById(R.id.tvSPMI1);
        tvSPMI2 = (TextView) v.findViewById(R.id.tvSPMI2);
        tvSPMI3 = (TextView) v.findViewById(R.id.tvSPMI3);
        tvCleanliness = (TextView) v.findViewById(R.id.tvCleanliness);
        tvWeather = (TextView) v.findViewById(R.id.tvWeather);
        tvWind = (TextView) v.findViewById(R.id.tvWind);
        tvMixing = (TextView) v.findViewById(R.id.tvMixing);
        tvPumpType = (TextView) v.findViewById(R.id.tvPumpType);
        tvPumpRatio = (TextView) v.findViewById(R.id.tvPumpRatio);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOk.setVisibility(View.INVISIBLE);
                printDocument(v);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sspreport);

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        namaKomponen = extras.getString("KOMPONEN_NAME");
        projectID = extras.getString("PROJECT_ID");
        idBlock = extras.getString("ID");
        proses = extras.getString("PROSES");

        /*initiate();
        new getBlockDetail().execute();
        new getSsp().execute();
        new getProject().execute();*/

        v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_sspreport, null, true);
        initiate2();
        new getBlockDetail().execute();
        new getSsp().execute();
        new getProject().execute();
        v.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setVisibility(GONE);
        ZoomView zoomView;
        zoomView = new ZoomView(SSPReport.this);
        zoomView.addView(v);

        LinearLayout llView = (LinearLayout) findViewById(R.id.llView);
        llView.setVisibility(GONE);

        LinearLayout main_container = (LinearLayout) findViewById(R.id.llZoom);
        main_container.addView(zoomView);
    }

    private void showSsp(String s)
    {
        JSONObject jsonObject;
        //Toast.makeText(RawMaterialCheckActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            tvProjectName.setText(projectName);
            tvInspectionDate.setText(jo.getString("ins_date"));
            tvDocumentName.setText(jo.getString("doc_name"));

            if(jo.getString("surface_cont")!=null) tvSurfaceContaminant.setText("Yes");
            else tvSurfaceContaminant.setText("No");
            if(jo.getString("solvent").equals("1")) tvSolventCleaning.setText("Yes");
            else tvSolventCleaning.setText("No");
            tvSolventStd.setText(jo.getString("std_solvent"));
            if(jo.getString("blasting").equals("1")) tvPreparationMethod.setText("Blasting");
            else tvPreparationMethod.setText("Mechanical Tool");
            tvBlastingType.setText(jo.getString("blast_type"));
            if(jo.getString("st2").equals("1")) tvCleanlinessGrade.setText("St 2");
            else if(jo.getString("st3").equals("1")) tvCleanlinessGrade.setText("St 3");
            else if(jo.getString("sa1").equals("1")) tvCleanlinessGrade.setText("Sa 1");
            else if(jo.getString("sa2").equals("1")) tvCleanlinessGrade.setText("Sa 2");
            else if(jo.getString("sa212").equals("1")) tvCleanlinessGrade.setText("Sa 2 1/2");
            else if(jo.getString("sa3").equals("1")) tvCleanlinessGrade.setText("Sa 3");
            tvTypeOfAbrasive.setText(jo.getString("abrasive"));
            tvAbrasiveSize.setText(jo.getString("abrasive_size"));
            tvAbrasiveGrade.setText(jo.getString("grade"));
            tvRustGrade.setText(jo.getString("rust_grade"));

            if(!jo.getString("dqr1").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivDQR1))
                    .execute(jo.getString("dqr1"));
            if(!jo.getString("dqr2").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivDQR2))
                    .execute(jo.getString("dqr2"));
            if(!jo.getString("spm1").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivSPM1))
                    .execute(jo.getString("spm1"));
            if(!jo.getString("spm2").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivSPM2))
                    .execute(jo.getString("spm2"));
            if(!jo.getString("spm3").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivSPM3))
                    .execute(jo.getString("spm3"));

            tvResultDQR.setText(jo.getString("result_dqr"));
            tvResultSPM.setText(jo.getString("result_spm"));
            tvWaterSolubleSalt.setText(jo.getString("result_ssf"));
            tvDryBulb.setText(jo.getString("dry_bulb"));
            tvWetBulb.setText(jo.getString("wet_bulb"));
            tvSurfaceTemperature.setText(jo.getString("stl_temp"));
            tvRelativeHumidity.setText(jo.getString("rel_humid"));
            tvDewPoint.setText(jo.getString("dew_point"));
            tvPaintingMethod.setText(jo.getString("std_paint_name")); // ??
            tvPaintName.setText(jo.getString("paint_name"));
            tvColourShadeNo.setText(jo.getString("color_shade"));
            tvBatchNo.setText(jo.getString("batchno"));
            tvThinnerNo.setText(jo.getString("thinnerno"));
            tvVolumeSolid.setText(jo.getString("vol_solid"));
            tvApplicationDate.setText(jo.getString("app_date"));
            tvCuringTime.setText(jo.getString("curing_time"));
            tvPotLife.setText(jo.getString("pot_life"));

            tvDQRI1.setText(jo.getString("dqri1"));
            tvDQRI2.setText(jo.getString("dqri2"));
            tvSPMI1.setText(jo.getString("spmi1"));
            tvSPMI2.setText(jo.getString("spmi2"));
            tvSPMI3.setText(jo.getString("spmi3"));
            tvComment.setText(jo.getString("comment"));
            tvCleanliness.setText(jo.getString("cleanliness"));
            tvWeather.setText(jo.getString("weather"));
            tvWind.setText(jo.getString("wind"));
            tvMixing.setText(jo.getString("mixing"));
            tvPumpType.setText(jo.getString("pump_type"));
            tvPumpRatio.setText(jo.getString("pump_ratio"));

            if(!jo.getString("doc1").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation1)).execute(jo.getString("doc1"));
            if(!jo.getString("doc2").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation2)).execute(jo.getString("doc2"));
            if(!jo.getString("doc3").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation3)).execute(jo.getString("doc3"));
            if(!jo.getString("doc4").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation4)).execute(jo.getString("doc4"));
            if(!jo.getString("doc5").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation5)).execute(jo.getString("doc5"));
            if(!jo.getString("doc6").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation6)).execute(jo.getString("doc6"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getSsp extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getSsp() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Getting process details...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            showSsp(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url;

            if(proses.equals("SSPSP"))
                create_url = "http://mobile4day.com/ship-inspection/get_sspsp.php";
            else
                create_url = "http://mobile4day.com/ship-inspection/get_ssp1c.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(idBlock, "UTF-8");
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
            tvBlockLocation.setText(jo.getString("lokasi"));
            tvArea.setText(jo.getString("area"));
            if(proses.equals("SSPSP")){
                tvProses.setText("Secondary Surface Preparation and Shop Primer");
                if(jo.getString("sspsp").equals("ae")) tvStage.setText("After Erection");
                else if(jo.getString("sspsp").equals("bc")) tvStage.setText("Block Stage");
            }
            else if(proses.equals("SSP1C")){
                tvProses.setText("Secondary Surface Preparation and 1st Coat");
                if(jo.getString("ssp1c").equals("ae")) tvStage.setText("After Erection");
                else if(jo.getString("ssp1c").equals("bc")) tvStage.setText("Block Stage");
            }

            //tvInspectionDate.setText();
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
            ProgressDialog.setMessage("Reading block...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(AfterErectionListActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

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

            create_url = "http://mobile4day.com/ship-inspection/get_blok_detail.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(idBlock, "UTF-8");
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

            String id_proyek = "" + projectID;
            String create_url = "http://mobile4day.com/ship-inspection/get_project.php";

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
            ProgressDialog.setMessage("Reading all project...");
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

            String create_url = "http://mobile4day.com/ship-inspection/get_user_detail.php";
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
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        btnOk = (Button) v.findViewById(R.id.btnOk);
        btnOk.setVisibility(GONE);
        LinearLayout content = (LinearLayout) v.findViewById(R.id.activity_raw_material_report);
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();
        File file,f=null;
        String x = "Block_" + projectName + "_" + proses + "_" + idBlock + "_" + formattedDate +".png";
        try{
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            {
                file =new File(android.os.Environment.getExternalStorageDirectory(),"Ship Inspection Report");
                if(!file.exists())
                {
                    file.mkdirs();

                }
                f = new File(file, x);
            }
            FileOutputStream ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            Toast.makeText(SSPReport.this, "Your report has been saved", Toast.LENGTH_SHORT).show();
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
        filePath = Uri.parse("/storage/emulated/0/Ship Inspection Report/" + name);
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
