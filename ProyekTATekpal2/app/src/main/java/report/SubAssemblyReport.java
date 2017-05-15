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
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import subAssembly.SubAssemblyCheckActivity;

import static dokumen.UploadFileActivity.UPLOAD_URL;

public class SubAssemblyReport extends AppCompatActivity {

    TextView tvProjectName, tvBlockName, tvComponentName, tvInspectionDate;
    TextView tvMaterial, tvWeldingProcess, tvManual, tvPosition, tvFillerMetalSpec, tvFillerMetalClass,
            tvWeldingCurrent, tvPolarity, tvElectrode, tvSpeed, tvDocumentName;
    TextView tvSharpEdges, tvGap, tvImperfectWelding, tvMissWeld, tvImperfectSteel;
    TextView tvStdSharpEdges, tvStdGap, tvStdImperfectWelding, tvStdMissWeld, tvStdImperfectSteel;
    TextView tvResSharpEdges, tvResGap, tvResImperfectWelding, tvResMissWeld, tvResImperfectSteel;
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
    private String proses;
    private String componentID;
    private Uri filePath;

    private void initiate()
    {
        btnOk = (Button) findViewById(R.id.btnOk);
        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        tvBlockName = (TextView) findViewById(R.id.tvBlockName);
        tvComponentName = (TextView) findViewById(R.id.tvComponentName);
        tvInspectionDate = (TextView) findViewById(R.id.tvInspectionDate);
        tvDocumentName = (TextView) findViewById(R.id.tvDocumentName);
        tvMaterial = (TextView) findViewById(R.id.tvMaterial);
        tvWeldingProcess = (TextView) findViewById(R.id.tvWeldingProcess);
        tvManual = (TextView) findViewById(R.id.tvManual);
        tvPosition = (TextView) findViewById(R.id.tvPosition);
        tvFillerMetalSpec = (TextView) findViewById(R.id.tvFillerMetalSpec);
        tvFillerMetalClass = (TextView) findViewById(R.id.tvFillerMetalClass);
        tvWeldingCurrent = (TextView) findViewById(R.id.tvWeldingCurrent);
        tvPolarity = (TextView) findViewById(R.id.tvPolarity);
        tvElectrode = (TextView) findViewById(R.id.tvElectrode);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvComment = (TextView) findViewById(R.id.tvComment);

        tvSharpEdges = (TextView) findViewById(R.id.tvSharpEdges);
        tvGap = (TextView) findViewById(R.id.tvGap);
        tvImperfectWelding = (TextView) findViewById(R.id.tvImperfectWelding);
        tvMissWeld = (TextView) findViewById(R.id.tvMissWeld);
        tvImperfectSteel = (TextView) findViewById(R.id.tvImperfectSteel);

        tvStdSharpEdges = (TextView) findViewById(R.id.tvStdSharpEdges);
        tvStdGap = (TextView) findViewById(R.id.tvStdGap);
        tvStdImperfectWelding = (TextView) findViewById(R.id.tvStdImperfectWelding);
        tvStdMissWeld = (TextView) findViewById(R.id.tvStdMissWeld);
        tvStdImperfectSteel = (TextView) findViewById(R.id.tvStdImperfectSteel);

        tvResSharpEdges = (TextView) findViewById(R.id.tvResSharpEdges);
        tvResGap = (TextView) findViewById(R.id.tvResGap);
        tvResImperfectWelding = (TextView) findViewById(R.id.tvResImperfectWelding);
        tvResMissWeld = (TextView) findViewById(R.id.tvResMissWeld);
        tvResImperfectSteel = (TextView) findViewById(R.id.tvResImperfectSteel);

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
        setContentView(R.layout.activity_sub_assembly_report);
        setTitle("Sub Assembly Report");

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");
        proses = extras.getString("PROSES");
        componentID = extras.getString("ID");

        initiate();

        new getProject().execute();
        new getComponent().execute(componentID);
    }

    private void showComponent(String s)
    {
        JSONObject jsonObject;
        //Toast.makeText(DetailProjectActivity.this, "showComponent with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            boolean check = false;
            String norm, std;
            Integer val, std1, std2;

            tvComponentName.setText(jo.getString("nama_komponen"));
            tvBlockName.setText(jo.getString("nama_blok"));

            tvMaterial.setText(jo.getString("material_spec"));
            tvWeldingProcess.setText(jo.getString("wp"));
            tvManual.setText(jo.getString("mm"));
            tvPosition.setText(jo.getString("pow"));
            tvFillerMetalSpec.setText(jo.getString("fms"));
            tvFillerMetalClass.setText(jo.getString("fmc"));
            tvWeldingCurrent.setText(jo.getString("wc"));
            tvPolarity.setText(jo.getString("polar"));
            tvElectrode.setText(jo.getString("es"));
            tvSpeed.setText(jo.getString("sot"));

            norm = jo.getString("se");
            std = jo.getString("stdse");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") || !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvSharpEdges.setText(jo.getString("se"));
            tvStdSharpEdges.setText(jo.getString("stdse"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResSharpEdges.setText("Accept");
            else tvResSharpEdges.setText("Reject");

            norm = jo.getString("gap");
            std = jo.getString("stdgap");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") && !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvGap.setText(jo.getString("gap"));
            tvStdGap.setText(jo.getString("stdgap"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResGap.setText("Accept");
            else tvResGap.setText("Reject");

            norm = jo.getString("iw");
            std = jo.getString("stdiw");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") && !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvImperfectWelding.setText(jo.getString("iw"));
            tvStdImperfectWelding.setText(jo.getString("stdiw"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResImperfectWelding.setText("Accept");
            else tvResImperfectWelding.setText("Reject");

            norm = jo.getString("mw");
            std = jo.getString("stdmw");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") && !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvMissWeld.setText(jo.getString("mw"));
            tvStdMissWeld.setText(jo.getString("stdmw"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResMissWeld.setText("Accept");
            else tvResMissWeld.setText("Reject");

            norm = jo.getString("deform");
            std = jo.getString("stddeform");
            check = false;
            val = Integer.parseInt(norm);
            std1 = Integer.parseInt(std.substring(0, std.indexOf("-")));
            std2 = Integer.parseInt(std.substring(std.indexOf("-")+1, std.length()));
            if((!norm.equalsIgnoreCase("NO") && !norm.equals("0")) && (val>=std1 && val<=std2)) check = true;
            tvImperfectSteel.setText(norm);
            tvStdImperfectSteel.setText(jo.getString("stddeform"));
            if(norm.equals("") || norm.equalsIgnoreCase("NO") || norm.equals("0") || check) tvResImperfectSteel.setText("Accept");
            else tvResImperfectSteel.setText("Reject");

            tvComment.setText(jo.getString("comment"));

            if(jo.getString("doc1")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation1)).execute(jo.getString("doc1"));
            if(jo.getString("doc2")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation2)).execute(jo.getString("doc2"));
            if(jo.getString("doc3")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation3)).execute(jo.getString("doc3"));
            if(jo.getString("doc4")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation4)).execute(jo.getString("doc4"));
            if(jo.getString("doc5")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation5)).execute(jo.getString("doc5"));
            if(jo.getString("doc6")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation6)).execute(jo.getString("doc6"));

            tvInspectionDate.setText(jo.getString("doc_date"));
            tvDocumentName.setText(jo.getString("doc_name"));
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
            showComponent(s);
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
        String x = "Sub_Assembly" + projectName + "_" + proses + "_" + "_" + formattedDate +".png";
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
            Toast.makeText(SubAssemblyReport.this, "Your report has been saved", Toast.LENGTH_SHORT).show();
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
