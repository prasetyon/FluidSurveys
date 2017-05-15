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

public class InspeksiFinalReport extends AppCompatActivity {

    TextView tvProjectName, tvBlockName, tvBlockLocation, tvWelded, tvType, tvInspectionDate, tvDocumentName;
    TextView tvQ1, tvQ2, tvQ3, tvQ4, tvQ5, tvQ6, tvQ7, tvQ8, tvQ9, tvResQ1, tvResQ2, tvResQ3, tvResQ4, tvResQ5, tvResQ6, tvResQ7, tvResQ8, tvResQ9;
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
        tvQ1 = (TextView) findViewById(R.id.tvQ1);
        tvQ2 = (TextView) findViewById(R.id.tvQ2);
        tvQ3 = (TextView) findViewById(R.id.tvQ3);
        tvQ4 = (TextView) findViewById(R.id.tvQ4);
        tvQ5 = (TextView) findViewById(R.id.tvQ5);
        tvQ6 = (TextView) findViewById(R.id.tvQ6);
        tvQ7 = (TextView) findViewById(R.id.tvQ7);
        tvQ8 = (TextView) findViewById(R.id.tvQ8);
        tvQ9 = (TextView) findViewById(R.id.tvQ9);
        tvResQ1 = (TextView) findViewById(R.id.tvResQ1);
        tvResQ2 = (TextView) findViewById(R.id.tvResQ2);
        tvResQ3 = (TextView) findViewById(R.id.tvResQ3);
        tvResQ4 = (TextView) findViewById(R.id.tvResQ4);
        tvResQ5 = (TextView) findViewById(R.id.tvResQ5);
        tvResQ6 = (TextView) findViewById(R.id.tvResQ6);
        tvResQ7 = (TextView) findViewById(R.id.tvResQ7);
        tvResQ8 = (TextView) findViewById(R.id.tvResQ8);
        tvResQ9 = (TextView) findViewById(R.id.tvResQ9);
        tvComment = (TextView) findViewById(R.id.tvComment);

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
        setTitle("Final Inspection Report");

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
        new getProcess().execute();
        new getProject().execute();
    }

    private void showBlock(String s) {
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
            //Toast.makeText(AssemblyEditActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

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

    private void showProcess(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            tvQ1.setText(jo.getString("q1"));
            if(jo.getString("q1").equals("NO")) tvResQ1.setText("Reject");
            else tvResQ1.setText("Accept");
            tvQ2.setText(jo.getString("q2"));
            if(jo.getString("q2").equals("NO")) tvResQ2.setText("Reject");
            else tvResQ2.setText("Accept");
            tvQ3.setText(jo.getString("q3"));
            if(jo.getString("q3").equals("NO")) tvResQ3.setText("Reject");
            else tvResQ3.setText("Accept");
            tvQ4.setText(jo.getString("q4"));
            if(jo.getString("q4").equals("NO")) tvResQ4.setText("Reject");
            else tvResQ4.setText("Accept");
            tvQ5.setText(jo.getString("q5"));
            if(jo.getString("q5").equals("NO")) tvResQ5.setText("Reject");
            else tvResQ5.setText("Accept");
            tvQ6.setText(jo.getString("q6"));
            if(jo.getString("q6").equals("NO")) tvResQ6.setText("Reject");
            else tvResQ6.setText("Accept");
            tvQ7.setText(jo.getString("q7"));
            if(jo.getString("q7").equals("NO")) tvResQ7.setText("Reject");
            else tvResQ7.setText("Accept");
            tvQ8.setText(jo.getString("q8"));
            if(jo.getString("q8").equals("NO")) tvResQ8.setText("Reject");
            else tvResQ8.setText("Accept");
            tvQ9.setText(jo.getString("q9"));
            if(jo.getString("q9").equals("NO")) tvResQ9.setText("Reject");
            else tvResQ9.setText("Accept");
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

    class getProcess extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getProcess() {
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
            //Toast.makeText(ErectionDetailActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showProcess(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url;

            create_url = "http://mobile4day.com/welding-inspection/get_final.php";

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
        String x = "Block_" + projectName + "_" + proses + "_" + blockID + "_" + formattedDate +".png";
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
            Toast.makeText(InspeksiFinalReport.this, "Your report has been saved", Toast.LENGTH_SHORT).show();
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
