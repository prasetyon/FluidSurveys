package report;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import dokumen.FilePath;
import pl.polidea.view.ZoomView;

import static android.view.View.GONE;
import static dokumen.UploadFileActivity.UPLOAD_URL;

public class InspeksiFinalReportActivity extends AppCompatActivity {

    TextView tvProjectName, tvBlockName, tvBlockLocation, tvArea, tvHoliday, tvInspectionDate, tvDocumentName,
            tvVisualCondition, tvMissCoat, tvPinholes, tvBubbles, tvVoids, tvOtherDefect,
            tvMaxNDFT, tvMinNDFT, tvAreaDFT, tvNumberOfPoint, tvMaxDFT, tvMinDFT, tvAvgDFT,
            tvTypeOfDefect, tvRepairMethod, tvRepairConfirmDate, tvComment;
    ImageView ivVisualCondition, ivMissCoat, ivPinholes, ivBubbles, ivVoids, ivHoliday, ivOtherDefect;
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
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;

    private void initiate()
    {
        btnOk = (Button) findViewById(R.id.btnOk);
        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        tvBlockName = (TextView) findViewById(R.id.tvBlockName);
        tvBlockLocation = (TextView) findViewById(R.id.tvBlockLocation);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvInspectionDate = (TextView) findViewById(R.id.tvInspectionDate);
        tvDocumentName = (TextView) findViewById(R.id.tvDocumentName);
        tvVisualCondition = (TextView) findViewById(R.id.tvVisualCondition);
        tvMissCoat = (TextView) findViewById(R.id.tvMissCoat);
        tvPinholes = (TextView) findViewById(R.id.tvPinholes);
        tvBubbles = (TextView) findViewById(R.id.tvBubbles);
        tvVoids = (TextView) findViewById(R.id.tvVoids);
        tvHoliday = (TextView) findViewById(R.id.tvHoliday);
        tvOtherDefect = (TextView) findViewById(R.id.tvOtherDefect);
        tvMaxNDFT = (TextView) findViewById(R.id.tvMaxNDFT);
        tvMinNDFT = (TextView) findViewById(R.id.tvMinNDFT);
        tvAreaDFT = (TextView) findViewById(R.id.tvAreaDFT);
        tvNumberOfPoint = (TextView) findViewById(R.id.tvNumberOfPoint);
        tvMaxDFT = (TextView) findViewById(R.id.tvMaxDFT);
        tvMinDFT = (TextView) findViewById(R.id.tvMinDFT);
        tvAvgDFT = (TextView) findViewById(R.id.tvAvgDFT);
        tvTypeOfDefect = (TextView) findViewById(R.id.tvTypeOfDefect);
        tvRepairMethod = (TextView) findViewById(R.id.tvRepairMethod);
        tvRepairConfirmDate = (TextView) findViewById(R.id.tvRepairConfirmDate);
        tvComment = (TextView) findViewById(R.id.tvComment);
        //ivDocumentation = (ImageView) findViewById(R.id.ivDocumentation);
        ivVisualCondition = (ImageView) findViewById(R.id.ivVisualCondition);
        ivMissCoat = (ImageView) findViewById(R.id.ivMissCoat);
        ivPinholes = (ImageView) findViewById(R.id.ivPinholes);
        ivBubbles = (ImageView) findViewById(R.id.ivBubbles);
        ivVoids = (ImageView) findViewById(R.id.ivVoids);
        ivHoliday = (ImageView) findViewById(R.id.ivHoliday);
        ivOtherDefect = (ImageView) findViewById(R.id.ivOtherDefect);
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
        tvArea = (TextView) v.findViewById(R.id.tvArea);
        tvInspectionDate = (TextView) v.findViewById(R.id.tvInspectionDate);
        tvDocumentName = (TextView) v.findViewById(R.id.tvDocumentName);
        tvVisualCondition = (TextView) v.findViewById(R.id.tvVisualCondition);
        tvMissCoat = (TextView) v.findViewById(R.id.tvMissCoat);
        tvPinholes = (TextView) v.findViewById(R.id.tvPinholes);
        tvBubbles = (TextView) v.findViewById(R.id.tvBubbles);
        tvVoids = (TextView) v.findViewById(R.id.tvVoids);
        tvHoliday = (TextView) v.findViewById(R.id.tvHoliday);
        tvOtherDefect = (TextView) v.findViewById(R.id.tvOtherDefect);
        tvMaxNDFT = (TextView) v.findViewById(R.id.tvMaxNDFT);
        tvMinNDFT = (TextView) v.findViewById(R.id.tvMinNDFT);
        tvAreaDFT = (TextView) v.findViewById(R.id.tvAreaDFT);
        tvNumberOfPoint = (TextView) v.findViewById(R.id.tvNumberOfPoint);
        tvMaxDFT = (TextView) v.findViewById(R.id.tvMaxDFT);
        tvMinDFT = (TextView) v.findViewById(R.id.tvMinDFT);
        tvAvgDFT = (TextView) v.findViewById(R.id.tvAvgDFT);
        tvTypeOfDefect = (TextView) v.findViewById(R.id.tvTypeOfDefect);
        tvRepairMethod = (TextView) v.findViewById(R.id.tvRepairMethod);
        tvRepairConfirmDate = (TextView) v.findViewById(R.id.tvRepairConfirmDate);
        tvComment = (TextView) v.findViewById(R.id.tvComment);
        //ivDocumentation = (ImageView) findViewById(R.id.ivDocumentation);
        ivVisualCondition = (ImageView) v.findViewById(R.id.ivVisualCondition);
        ivMissCoat = (ImageView) v.findViewById(R.id.ivMissCoat);
        ivPinholes = (ImageView) v.findViewById(R.id.ivPinholes);
        ivBubbles = (ImageView) v.findViewById(R.id.ivBubbles);
        ivVoids = (ImageView) v.findViewById(R.id.ivVoids);
        ivHoliday = (ImageView) v.findViewById(R.id.ivHoliday);
        ivOtherDefect = (ImageView) v.findViewById(R.id.ivOtherDefect);
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
        setContentView(R.layout.activity_inspeksi_final_report);
        setTitle("FINAL INSPECTION");

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

        //initiate();

        v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_inspeksi_final_report, null, true);
        initiate2();
        new getBlockDetail().execute();
        new getFinal().execute();
        new getProject().execute();
        v.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setVisibility(GONE);
        ZoomView zoomView;
        zoomView = new ZoomView(InspeksiFinalReportActivity.this);
        zoomView.addView(v);

        LinearLayout llView = (LinearLayout) findViewById(R.id.llView);
        llView.setVisibility(GONE);

        LinearLayout main_container = (LinearLayout) findViewById(R.id.llZoom);
        main_container.addView(zoomView);
    }

    private void showBlock(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            tvProjectName.setText(projectName);
            tvInspectionDate.setText(jo.getString("ins_date"));

            if(!jo.getString("visual_condition").equals("")){
                tvVisualCondition.setText("Yes");
                new DownloadImageTask((ImageView) findViewById(R.id.ivVisualCondition))
                        .execute(jo.getString("visual_condition"));
            } else tvVisualCondition.setText("No");

            if(!jo.getString("misscoat").equals("")){
                tvMissCoat.setText("Yes");
                ivMissCoat.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivMissCoat))
                        .execute(jo.getString("misscoat"));
            } else tvMissCoat.setText("No");

            if(!jo.getString("pinholes").equals("")){
                tvPinholes.setText("Yes");
                ivPinholes.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivPinholes))
                        .execute(jo.getString("pinholes"));
            }  else tvPinholes.setText("No");

            if(!jo.getString("bubbles").equals("")){
                tvBubbles.setText("Yes");
                ivBubbles.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivBubbles))
                        .execute(jo.getString("bubbles"));
            } tvBubbles.setText("No");

            if(!jo.getString("voids").equals("")){
                tvVoids.setText("Yes");
                ivVoids.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivVoids))
                        .execute(jo.getString("voids"));
            } else tvVoids.setText("No");

            if(!jo.getString("holiday").equals("")){
                tvHoliday.setText("Yes");
                ivHoliday.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivHoliday))
                        .execute(jo.getString("holiday"));
            } else tvHoliday.setText("No");

            if(!jo.getString("other_defect").equals("")){
                tvOtherDefect.setText("Yes");
                ivOtherDefect.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivOtherDefect))
                        .execute(jo.getString("other_defect"));
            } else tvOtherDefect.setText("No");

            tvMinNDFT.setText(jo.getString("min_ndft"));
            tvMaxNDFT.setText(jo.getString("max_ndft"));
            tvAreaDFT.setText(jo.getString("area"));
            tvNumberOfPoint.setText(jo.getString("test_point"));
            tvMinDFT.setText(jo.getString("min_dft"));
            tvMaxDFT.setText(jo.getString("max_dft"));
            tvAvgDFT.setText(jo.getString("avg_dft"));
            tvTypeOfDefect.setText(jo.getString("typedefect"));
            tvRepairMethod.setText(jo.getString("repair_method"));
            tvRepairConfirmDate.setText(jo.getString("repair_confirm"));
            tvComment.setText(jo.getString("comment"));

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

    class getFinal extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getFinal() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading final inspection...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(InspeksiFinalReportActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

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

            create_url = "http://mobile4day.com/ship-inspection/get_final.php";

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

    private void showBlockDetail(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            tvBlockName.setText(jo.getString("nama_blok"));
            tvBlockLocation.setText(jo.getString("lokasi"));
            tvArea.setText(jo.getString("area"));
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
            if(!jo.getString("signature").equals("")) {
                if (dep.equals("Coating Inspector"))
                    new DownloadImageTask((ImageView) findViewById(R.id.ivSignInspector)).execute(jo.getString("signature"));
                else if (dep.equals("Owner"))
                    new DownloadImageTask((ImageView) findViewById(R.id.ivSignOwner)).execute(jo.getString("signature"));
                else if (dep.equals("Shipyard"))
                    new DownloadImageTask((ImageView) findViewById(R.id.ivSignShipyard)).execute(jo.getString("signature"));
                else if (dep.equals("Class"))
                    new DownloadImageTask((ImageView) findViewById(R.id.ivSignClass)).execute(jo.getString("signature"));
            }
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
        //System.out.println("Current time => " + c.getTime());

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
            Toast.makeText(InspeksiFinalReportActivity.this, "Your report has been saved", Toast.LENGTH_SHORT).show();
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