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

public class BlockReport extends AppCompatActivity {

    TextView tvProjectName, tvBlockName, tvBlockLocation, tvArea, tvInspectionDate, tvProses, tvStage, tvDocumentName, tvStripeCoatCondition, tvNoDefect, tvNoMisscoat,
            tvMaxNDFT, tvMinNDFT, tvAreaDFT, tvNumberOfPoint, tvMinDFT, tvMaxDFT, tvAvgDFT,
            tvDryBulb, tvWetBulb, tvSurfaceTemperature, tvRelativeHumidity, tvDewPoint,
            tvPaintingMethod, tvPaintName, tvColourShadeNo, tvBatchNo, tvThinnerNo, tvVolumeSolid, tvApplicationDate, tvPotLife, tvCuringTime,
            tvDFTMeasurement, tvApplication, tvComment;
    TextView tvWeather, tvWind, tvMixing, tvPumpType, tvPumpRatio;
    ImageView ivStripeCoatCondition, ivNoDefect, ivMissCoat;
    View v;
    Button btnOk;
    Context Activity;
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
        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        tvBlockName = (TextView) findViewById(R.id.tvBlockName);
        tvBlockLocation = (TextView) findViewById(R.id.tvBlockLocation);
        tvDFTMeasurement = (TextView) findViewById(R.id.tvDFTMeasurement);
        tvApplication = (TextView) findViewById(R.id.tvApplication);
        tvComment = (TextView) findViewById(R.id.tvComment);
        tvProses = (TextView) findViewById(R.id.tvProses);
        tvStage = (TextView) findViewById(R.id.tvStage);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvInspectionDate = (TextView) findViewById(R.id.tvInspectionDate);
        tvDocumentName = (TextView) findViewById(R.id.tvDocumentName);
        tvStripeCoatCondition = (TextView) findViewById(R.id.tvStripeCoatCondition);
        tvNoDefect = (TextView) findViewById(R.id.tvNoDefect);
        tvNoMisscoat = (TextView) findViewById(R.id.tvNoMisscoat);
        tvMaxNDFT = (TextView) findViewById(R.id.tvMaxNDFT);
        tvMinNDFT = (TextView) findViewById(R.id.tvMinNDFT);
        tvAreaDFT = (TextView) findViewById(R.id.tvAreaDFT);
        tvNumberOfPoint = (TextView) findViewById(R.id.tvNumberOfPoint);
        tvMaxDFT = (TextView) findViewById(R.id.tvMaxDFT);
        tvMinDFT = (TextView) findViewById(R.id.tvMinDFT);
        tvAvgDFT = (TextView) findViewById(R.id.tvAvgDFT);
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
        tvWeather = (TextView) findViewById(R.id.tvWeather);
        tvWind = (TextView) findViewById(R.id.tvWind);
        tvMixing = (TextView) findViewById(R.id.tvMixing);
        tvPumpType = (TextView) findViewById(R.id.tvPumpType);
        tvPumpRatio = (TextView) findViewById(R.id.tvPumpRatio);

        ivStripeCoatCondition = (ImageView) findViewById(R.id.ivVisualCondition);
        ivMissCoat = (ImageView) findViewById(R.id.ivMissCoat);
        ivNoDefect = (ImageView) findViewById(R.id.ivPinholes);
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
        tvProjectName = (TextView) v.findViewById(R.id.tvProjectName);
        tvBlockName = (TextView) v.findViewById(R.id.tvBlockName);
        tvBlockLocation = (TextView) v.findViewById(R.id.tvBlockLocation);
        tvDFTMeasurement = (TextView) v.findViewById(R.id.tvDFTMeasurement);
        tvApplication = (TextView) v.findViewById(R.id.tvApplication);
        tvComment = (TextView) v.findViewById(R.id.tvComment);
        tvProses = (TextView) v.findViewById(R.id.tvProses);
        tvStage = (TextView) v.findViewById(R.id.tvStage);
        tvArea = (TextView) v.findViewById(R.id.tvArea);
        tvInspectionDate = (TextView) v.findViewById(R.id.tvInspectionDate);
        tvDocumentName = (TextView) v.findViewById(R.id.tvDocumentName);
        tvStripeCoatCondition = (TextView) v.findViewById(R.id.tvStripeCoatCondition);
        tvNoDefect = (TextView) v.findViewById(R.id.tvNoDefect);
        tvNoMisscoat = (TextView) v.findViewById(R.id.tvNoMisscoat);
        tvMaxNDFT = (TextView) v.findViewById(R.id.tvMaxNDFT);
        tvMinNDFT = (TextView) v.findViewById(R.id.tvMinNDFT);
        tvAreaDFT = (TextView) v.findViewById(R.id.tvAreaDFT);
        tvNumberOfPoint = (TextView) v.findViewById(R.id.tvNumberOfPoint);
        tvMaxDFT = (TextView) v.findViewById(R.id.tvMaxDFT);
        tvMinDFT = (TextView) v.findViewById(R.id.tvMinDFT);
        tvAvgDFT = (TextView) v.findViewById(R.id.tvAvgDFT);
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
        tvWeather = (TextView) v.findViewById(R.id.tvWeather);
        tvWind = (TextView) v.findViewById(R.id.tvWind);
        tvMixing = (TextView) v.findViewById(R.id.tvMixing);
        tvPumpType = (TextView) v.findViewById(R.id.tvPumpType);
        tvPumpRatio = (TextView) v.findViewById(R.id.tvPumpRatio);

        ivStripeCoatCondition = (ImageView) v.findViewById(R.id.ivVisualCondition);
        ivMissCoat = (ImageView) v.findViewById(R.id.ivMissCoat);
        ivNoDefect = (ImageView) v.findViewById(R.id.ivPinholes);
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
        setContentView(R.layout.activity_block_report);

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

        v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_block_report, null, true);
        initiate2();
        new getBlockDetail().execute();
        new getCoat().execute();
        new getProject().execute();
        v.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setVisibility(GONE);
        ZoomView zoomView;
        zoomView = new ZoomView(BlockReport.this);
        zoomView.addView(v);

        LinearLayout llView = (LinearLayout) findViewById(R.id.llView);
        llView.setVisibility(GONE);

        LinearLayout main_container = (LinearLayout) findViewById(R.id.llZoom);
        main_container.addView(zoomView);
    }

    private void showCoat(String s)
    {
        JSONObject jsonObject;
        //Toast.makeText(AfterErectionCoatActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            tvProjectName.setText(projectName);
            tvInspectionDate.setText(jo.getString("ins_date"));
            tvDocumentName.setText(jo.getString("doc_name"));

            if(jo.getString("stripe_coat")!=null){
                tvStripeCoatCondition.setText("Yes");
                if(!jo.getString("img_stripe_coat").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivStripeCoatCondition))
                        .execute(jo.getString("img_stripe_coat"));
            } else tvStripeCoatCondition.setText("No");
            if(jo.getString("no_defect").equals("1")){
                tvNoDefect.setText("Yes");
                if(!jo.getString("img_no_defect").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivNoDefect))
                        .execute(jo.getString("img_no_defect"));
            } else tvNoDefect.setText("No");

            if(jo.getString("no_misscoat").equals("1")){
                tvNoMisscoat.setText("Yes");
                if(!jo.getString("img_no_misscoat").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivNoMissCoat))
                        .execute(jo.getString("img_no_misscoat"));
            } else tvNoMisscoat.setText("No");

            tvMinNDFT.setText(jo.getString("std_min_dft"));
            tvMaxNDFT.setText(jo.getString("std_max_dft"));
            tvAreaDFT.setText(jo.getString("area"));
            tvNumberOfPoint.setText(jo.getString("test_point"));
            tvMinDFT.setText(jo.getString("min_dft"));
            tvMaxDFT.setText(jo.getString("max_dft"));
            tvAvgDFT.setText(jo.getString("avg_dft"));

            tvDryBulb.setText(jo.getString("dry_bulb"));
            tvWetBulb.setText(jo.getString("wet_bulb"));
            tvSurfaceTemperature.setText(jo.getString("stl_temp"));
            tvRelativeHumidity.setText(jo.getString("rel_humid"));
            tvDewPoint.setText(jo.getString("dew_point"));
            tvPaintName.setText(jo.getString("paint_name"));
            tvColourShadeNo.setText(jo.getString("color_shade"));
            tvBatchNo.setText(jo.getString("batchno"));
            tvThinnerNo.setText(jo.getString("thinnerno"));
            tvVolumeSolid.setText(jo.getString("vol_solid"));
            tvApplicationDate.setText(jo.getString("app_date"));
            tvCuringTime.setText(jo.getString("curing_time"));
            tvPotLife.setText(jo.getString("pot_life"));
            tvComment.setText(jo.getString("comment"));
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

    class getCoat extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getCoat() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Getting coat details...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            showCoat(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/ship-inspection/get_coat.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(idBlock, "UTF-8") + "&" +
                                URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(proses, "UTF-8");
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
            //tvInspectionDate.setText();

            if(proses.equals("1C1SC")){
                String string = jo.getString("1c1sc");
                if(string!=null) {
                    if (string.equals("ae")) tvStage.setText("After Erection");
                    else if (string.equals("bc")) tvStage.setText("Block Stage");
                    tvProses.setText("1st Coat and 1st Stripe Coat");
                    tvStripeCoatCondition.setText("Visual Inspection 1st Coat Condition");
                    tvDFTMeasurement.setText("1st Coat DFT Measurement");
                    tvApplication.setText("1st Stripe Coat Application");
                }
            }
            else if(proses.equals("1SC2SC")){
                String string = jo.getString("1sc2sc");
                if(string!=null) {
                    if (string.equals("ae")) tvStage.setText("After Erection");
                    else if (string.equals("bc")) tvStage.setText("Block Stage");
                    tvProses.setText("1st Stripe Coat and 2nd Stripe Coat");
                    tvStripeCoatCondition.setText("Visual Inspection 1st Stripe Coat Condition");
                    tvDFTMeasurement.setText("1st Stripe Coat DFT Measurement");
                    tvApplication.setText("2nd Stripe Coat Application");
                }
            }
            else if(proses.equals("2SC2C")){
                String string = jo.getString("2sc2c");
                if(string!=null) {
                    if (string.equals("ae")) tvStage.setText("After Erection");
                    else if (string.equals("bc")) tvStage.setText("Block Stage");
                    tvProses.setText("2nd Stripe Coat and 2nd Coat");
                    tvStripeCoatCondition.setText("Visual Inspection 2nd Stripe Coat Condition");
                    tvDFTMeasurement.setText("2nd Stripe Coat DFT Measurement");
                    tvApplication.setText("2nd Coat Application");
                }
            }
            else if(proses.equals("2C3C")){
                String string = jo.getString("2c3c");
                if(string!=null) {
                    if (string.equals("ae")) tvStage.setText("After Erection");
                    else if (string.equals("bc")) tvStage.setText("Block Stage");
                    tvProses.setText("2nd Coat and 3rd Coat");
                    tvStripeCoatCondition.setText("Visual Inspection 2nd Coat Condition");
                    tvDFTMeasurement.setText("2nd Coat DFT Measurement");
                    tvApplication.setText("3rd Coat Application");
                }
            }
            else if(proses.equals("3C4C")){
                String string = jo.getString("3c4c");
                if(string!=null) {
                    if (string.equals("ae")) tvStage.setText("After Erection");
                    else if (string.equals("bc")) tvStage.setText("Block Stage");
                    tvProses.setText("3rd Coat and 4th Coat");
                    tvStripeCoatCondition.setText("Visual Inspection 3rd Coat Condition");
                    tvDFTMeasurement.setText("3rd Coat DFT Measurement");
                    tvApplication.setText("4th Coat Application");
                }
            }
            else if(proses.equals("4C5C")){
                String string = jo.getString("4c5c");
                if(string!=null) {
                    if (string.equals("ae")) tvStage.setText("After Erection");
                    else if (string.equals("bc")) tvStage.setText("Block Stage");
                    tvProses.setText("4th Coat and 5th Coat");
                    tvStripeCoatCondition.setText("Visual Inspection 4th Coat Condition");
                    tvDFTMeasurement.setText("4th Coat DFT Measurement");
                    tvApplication.setText("5th Coat Application");
                }
            }

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

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        btnOk = (Button) v.findViewById(R.id.btnOk);
        btnOk.setVisibility(GONE);
        LinearLayout content = (LinearLayout) findViewById(R.id.activity_raw_material_report);
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
            Toast.makeText(BlockReport.this, "Your report has been saved", Toast.LENGTH_SHORT).show();
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
