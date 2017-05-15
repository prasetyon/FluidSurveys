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

public class PullOffReport extends AppCompatActivity {

    TextView tvProjectName, tvBlockName, tvBlockLocation, tvArea, tvInspectionDate, tvDocumentName, tvStage,
            tvTypeOfAdhesion, tvEquipmentUsed, tvDurationOfLastCoat, tvDateOfAttachment,
            tvShopPrimer, tv1stCoat, tv2ndCoat, tv3rdCoat, tv4thCoat, tv5thCoat,
            tvDFTBef1, tvDFTBef2, tvDFTBef3, tvDFTAf1, tvDFTAf2, tvDFTAf3, tvPullOff1, tvPullOff2, tvPullOff3,
            tvFailure1, tvFailure2, tvFailure3, tvComment;
    ImageView ivFailure1, ivFailure2, ivFailure3;
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

    private void initiate2()
    {
        btnOk = (Button) v.findViewById(R.id.btnOk);
        tvProjectName = (TextView) v.findViewById(R.id.tvProjectName);
        tvBlockName = (TextView) v.findViewById(R.id.tvBlockName);
        tvBlockLocation = (TextView) v.findViewById(R.id.tvBlockLocation);
        tvArea = (TextView) v.findViewById(R.id.tvArea);
        tvStage = (TextView) v.findViewById(R.id.tvStage);
        tvInspectionDate = (TextView) v.findViewById(R.id.tvInspectionDate);
        tvDocumentName = (TextView) v.findViewById(R.id.tvDocumentName);

        tvTypeOfAdhesion = (TextView) v.findViewById(R.id.tvTypeOfAdhesion);
        tvEquipmentUsed = (TextView) v.findViewById(R.id.tvEquipmentUsed);
        tvDurationOfLastCoat = (TextView) v.findViewById(R.id.tvDurationOfLastCoat);
        tvDateOfAttachment = (TextView) v.findViewById(R.id.tvDateOfAttachment);
        tvShopPrimer = (TextView) v.findViewById(R.id.tvShopPrimer);

        tv1stCoat = (TextView) v.findViewById(R.id.tv1stCoat);
        tv2ndCoat = (TextView) v.findViewById(R.id.tv2ndCoat);
        tv3rdCoat = (TextView) v.findViewById(R.id.tv3rdCoat);
        tv4thCoat = (TextView) v.findViewById(R.id.tv4thCoat);
        tv5thCoat = (TextView) v.findViewById(R.id.tv5thCoat);
        tvDFTBef1 = (TextView) v.findViewById(R.id.tvDFTBef1);
        tvDFTBef2 = (TextView) v.findViewById(R.id.tvDFTBef2);
        tvDFTBef3 = (TextView) v.findViewById(R.id.tvDFTBef3);
        tvDFTAf1 = (TextView) v.findViewById(R.id.tvDFTAf1);
        tvDFTAf2 = (TextView) v.findViewById(R.id.tvDFTAf2);
        tvDFTAf3 = (TextView) v.findViewById(R.id.tvDFTAf3);
        tvPullOff1 = (TextView) v.findViewById(R.id.tvPullOff1);
        tvPullOff2 = (TextView) v.findViewById(R.id.tvPullOff2);
        tvPullOff3 = (TextView) v.findViewById(R.id.tvPullOff3);
        tvFailure1 = (TextView) v.findViewById(R.id.tvFailure1);
        tvFailure2 = (TextView) v.findViewById(R.id.tvFailure2);
        tvFailure3 = (TextView) v.findViewById(R.id.tvFailure3);
        ivFailure1 = (ImageView) v.findViewById(R.id.ivFailure1);
        ivFailure2 = (ImageView) v.findViewById(R.id.ivFailure2);
        ivFailure3 = (ImageView) v.findViewById(R.id.ivFailure3);
        tvComment = (TextView) v.findViewById(R.id.tvComment);
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
        setContentView(R.layout.activity_pull_off_report);
        setTitle("PULL OFF ADHESION");

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

        v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_pull_off_report, null, true);
        initiate2();
        new getBlockDetail().execute();
        new getPullOff().execute();
        new getProject().execute();
        v.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setVisibility(GONE);
        ZoomView zoomView;
        zoomView = new ZoomView(PullOffReport.this);
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

            tvTypeOfAdhesion.setText(jo.getString("adhesion"));
            tvEquipmentUsed.setText(jo.getString("equipment"));
            tvDurationOfLastCoat.setText(jo.getString("duration"));
            tvDateOfAttachment.setText(jo.getString("date"));

            tvShopPrimer.setText(jo.getString("shop_val"));
            tv1stCoat.setText(jo.getString("first_val"));
            tv2ndCoat.setText(jo.getString("second_val"));
            tv3rdCoat.setText(jo.getString("third_val"));
            tv4thCoat.setText(jo.getString("forth_val"));
            tv5thCoat.setText(jo.getString("fifth_val"));

            tvDFTBef1.setText(jo.getString("dftbef1"));
            tvDFTBef2.setText(jo.getString("dftbef2"));
            tvDFTBef3.setText(jo.getString("dftbef3"));
            tvDFTAf1.setText(jo.getString("dftaf1"));
            tvDFTAf2.setText(jo.getString("dftaf2"));
            tvDFTAf3.setText(jo.getString("dftaf3"));
            tvPullOff1.setText(jo.getString("pulloff1"));
            tvPullOff2.setText(jo.getString("pulloff2"));
            tvPullOff3.setText(jo.getString("pulloff3"));

            tvFailure1.setText(jo.getString("fail1"));
            tvFailure2.setText(jo.getString("fail2"));
            tvFailure3.setText(jo.getString("fail3"));

            if(!jo.getString("imgfail1").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivFailure1)).execute(jo.getString("imgfail1"));
            if(!jo.getString("imgfail2").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivFailure2)).execute(jo.getString("imgfail2"));
            if(!jo.getString("imgfail3").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivFailure3)).execute(jo.getString("imgfail3"));

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

    class getPullOff extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getPullOff() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading pull off...");
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

            create_url = "http://mobile4day.com/ship-inspection/get_pulloff.php";

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
            String string = jo.getString("pulloff");
            if (string.equals("ae")) tvStage.setText("After Erection");
            else if (string.equals("bc")) tvStage.setText("Block Stage");
            tvArea.setText(jo.getString("area"));
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
            Toast.makeText(PullOffReport.this, "Your report has been saved", Toast.LENGTH_SHORT).show();
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
