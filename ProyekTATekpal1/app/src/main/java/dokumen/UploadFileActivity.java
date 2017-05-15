package dokumen;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UploadFileActivity extends AppCompatActivity implements View.OnClickListener {

    //Declaring views
    LinearLayout llUpload, llListFile;
    ListAdapter listAdapter;
    private Button buttonChoose;
    private Button buttonUpload;
    private Button btnShowFile;
    private Button btnSearch;
    EditText edSearch;
    Spinner spinnerProses, spinnerBagian, spinnerID, spinnerSubProses;
    ListView lvProject;
    private EditText editText;
    Context Activity;
    public static final String UPLOAD_URL = "http://mobile4day.com/ship-inspection/upload.php";

    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private Uri filePath;

    private Bundle extras;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String projectID;
    private String proses="", bagian="", IDs="", blok="";
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        //Requesting storage permission
        requestStoragePermission();

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");

        //Initializing views
        llUpload = (LinearLayout) findViewById(R.id.llUpload);
        llListFile = (LinearLayout) findViewById(R.id.llListFile);
        llListFile.setVisibility(View.GONE);
        edSearch = (EditText) findViewById(R.id.edSearch);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        btnShowFile = (Button) findViewById(R.id.btnShowFile);
        spinnerProses = (Spinner) findViewById(R.id.spinnerProses);
        spinnerSubProses = (Spinner) findViewById(R.id.spinnerSubProses);
        spinnerBagian = (Spinner) findViewById(R.id.spinnerBagian);
        spinnerID = (Spinner) findViewById(R.id.spinnerID);
        spinnerSubProses.setVisibility(View.GONE);
        spinnerBagian.setVisibility(View.GONE);
        spinnerID.setVisibility(View.GONE);
        editText = (EditText) findViewById(R.id.editTextName);

        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        btnShowFile.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        if(!from.equals("HELP")) {
            final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.proses_list, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinnerProses.setAdapter(spinnerAdapter);
            spinnerProses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String project = spinnerProses.getSelectedItem().toString();

                    if (project.equals("Raw Material")) {
                        Toast.makeText(UploadFileActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                        proses = "PSPSP";

                        spinnerID.setVisibility(View.GONE);
                        spinnerSubProses.setVisibility(View.GONE);

                        new getID().execute("komponen");
                    } else if (project.equals("Block")) {
                        Toast.makeText(UploadFileActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();

                        new getID().execute("blok");
                    } else if (project.equals("Select a proses...")) {
                        spinnerBagian.setVisibility(View.GONE);
                        spinnerID.setVisibility(View.GONE);
                        spinnerSubProses.setVisibility(View.GONE);
                        buttonUpload.setVisibility(View.INVISIBLE);
                    } else {
                        spinnerBagian.setVisibility(View.GONE);
                        spinnerID.setVisibility(View.GONE);
                        spinnerSubProses.setVisibility(View.GONE);
                        Toast.makeText(UploadFileActivity.this, "You are going to add file into dokumen", Toast.LENGTH_SHORT).show();
                        if (project.equals("Etc")) proses = "ETC";
                        else if (project.equals("Help")) proses = "HELP";
                        buttonUpload.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else {
            spinnerBagian.setVisibility(View.GONE);
            spinnerID.setVisibility(View.GONE);
            spinnerProses.setVisibility(View.GONE);
            spinnerSubProses.setVisibility(View.GONE);
            buttonUpload.setVisibility(View.GONE);
            buttonChoose.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            new getProject().execute("");
        }
    }

    /*
    * This is the method responsible for pdf upload
    * We need the full pdf path and the name for the pdf in this method
    * */

    public void uploadMultipart() {
        //getting name for the image
        String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = FilePath.getPath(this, filePath);
        //Toast.makeText(UploadFileActivity.this, "Path: " + path, Toast.LENGTH_LONG).show();

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
                        .addParameter("proses", proses) //Adding text parameter to the request
                        .addParameter("name", editText.getText().toString()) //Adding text parameter to the request
                        .addParameter("bagian", bagian) //Adding text parameter to the request
                        .addParameter("id", IDs) //Adding text parameter to the request
                        .addParameter("blok", blok) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        if(proses.equals("HELP"))
            intent.setType("application/pdf");
        else
            intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
            buttonUpload.setVisibility(View.VISIBLE);
        }
        if (v == buttonUpload) {
            uploadMultipart();
        }
        if (v == btnShowFile){
            if(flag) {
                llUpload.setVisibility(View.GONE);
                llListFile.setVisibility(View.VISIBLE);
                btnShowFile.setText("Show Upload File");
                flag=false;
                new getProject().execute("");
            }
            else{
                llUpload.setVisibility(View.VISIBLE);
                llListFile.setVisibility(View.GONE);
                btnShowFile.setText("Show File List");
                flag=true;
            }
        }
        if (v == btnSearch){
            new getProject().execute(edSearch.getText().toString());
            listAdapter.notifyDataSetChanged();
        }
    }

    private void showIDSpinner(String list)
    {
        spinnerID.setVisibility(View.VISIBLE);
        List<String> listBlok = new ArrayList<>();

        JSONObject jsonObject;
        //Toast.makeText(DetailProjectActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(list);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                listBlok.add(jo.getString("id") + " " + jo.getString("name"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listBlok);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerID.setAdapter(spinnerAdapter);
        spinnerID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String project1 = spinnerID.getSelectedItem().toString().substring(0, spinnerID.getSelectedItem().toString().indexOf(" "));
                IDs = project1;
                buttonUpload.setVisibility(View.VISIBLE);

                if(proses.equals("PSPSP")){
                    spinnerBagian.setVisibility(View.VISIBLE);
                    final ArrayAdapter<CharSequence> spinnerAdapter1;
                    spinnerAdapter1 = ArrayAdapter.createFromResource(UploadFileActivity.this, R.array.pspsp_list, android.R.layout.simple_spinner_item);
                    spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinnerBagian.setAdapter(spinnerAdapter1);
                    spinnerBagian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String project = spinnerBagian.getSelectedItem().toString();
                            bagian = project;
                            Toast.makeText(UploadFileActivity.this, "Proses = " + proses + "Bagian = " + bagian + "ID = " + IDs + "Blok = " + blok, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else
                new getProses().execute(IDs);

                Toast.makeText(UploadFileActivity.this, "Proses = " + proses + "Bagian = " + bagian + "ID = " + IDs + "Blok = " + blok, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class getID extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getID() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Getting details...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            showIDSpinner(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String from = params[0];

            String create_url = "http://mobile4day.com/ship-inspection/get_id.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_proyek", "UTF-8") + "=" + URLEncoder.encode(projectID, "UTF-8") + "&" +
                                URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8");
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

    private void showProsesSpinner(String list)
    {
        spinnerSubProses.setVisibility(View.VISIBLE);
        List<String> listBlok = new ArrayList<>();

        JSONObject jsonObject;
        //Toast.makeText(DetailProjectActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(list);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            String swp1 = jo.getString("swp1");
            if(swp1.equals("ae") || swp1.equals("bc")) listBlok.add("Steel Work Preparation 1");
            String sspsp = jo.getString("sspsp");
            if(sspsp.equals("ae") || sspsp.equals("bc")) listBlok.add("Secondary Surface Preparation and Shop Primer");
            String swp2 = jo.getString("swp2");
            if(swp2.equals("ae") || swp2.equals("bc")) listBlok.add("Steel Work Preparation 2");
            String swp1ae = jo.getString("swp1ae");
            if(swp1ae.equals("ae") || swp1ae.equals("bc")) listBlok.add("Steel Work Preparation 1 After Erection");
            String swp2ae = jo.getString("swp2ae");
            if(swp2ae.equals("ae") || swp2ae.equals("bc")) listBlok.add("Steel Work Preparation 2 After Erection");
            String ssp1c = jo.getString("ssp1c");
            if(ssp1c.equals("ae") || ssp1c.equals("bc")) listBlok.add("Secondary Surface Preparation and 1st Coat");
            String b1c1sc = jo.getString("1c1sc");
            if(b1c1sc.equals("ae") || b1c1sc.equals("bc")) listBlok.add("1st Coat and 1st Stripe Coat");
            String b1sc2sc = jo.getString("1sc2sc");
            if(b1sc2sc.equals("ae") || b1sc2sc.equals("bc")) listBlok.add("1st Stripe Coat and 2nd Stripe Coat");
            String b2sc2c = jo.getString("2sc2c");
            if(b2sc2c.equals("ae") || b2sc2c.equals("bc")) listBlok.add("2nd Stripe Coat and 2nd Coat");
            String b2c3c = jo.getString("2c3c");
            if(b2c3c.equals("ae") || b2c3c.equals("bc")) listBlok.add("2nd Coat and 3rd Coat");
            String b3c4c = jo.getString("3c4c");
            if(b3c4c.equals("ae") || b3c4c.equals("bc")) listBlok.add("3rd Coat and 4th Coat");
            String b4c5c = jo.getString("4c5c");
            if(b4c5c.equals("ae") || b4c5c.equals("bc")) listBlok.add("4th Coat and 5th Coat");
            String pulloff = jo.getString("pulloff");
            if(pulloff.equals("ae") || pulloff.equals("bc")) listBlok.add("Pull Off Strength Test");
            listBlok.add("Final Inspection");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listBlok);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSubProses.setAdapter(spinnerAdapter);
        spinnerSubProses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from = spinnerSubProses.getSelectedItem().toString();
                spinnerBagian.setVisibility(View.VISIBLE);
                final ArrayAdapter<CharSequence> spinnerAdapter1;
                if(from.equals("Steel Work Preparation 1") || from.equals("Steel Work Preparation 2") || from.equals("Steel Work Preparation 1 After Erection") || from.equals("Steel Work Preparation 2 After Erection")) {
                    if(from.equals("Steel Work Preparation 1")) proses = "SWP1";
                    else if(from.equals("Steel Work Preparation 2")) proses = "SWP2";
                    else if(from.equals("Steel Work Preparation 1 After Erection")) proses = "SWP1AE";
                    else if(from.equals("Steel Work Preparation 2 After Erection")) proses = "SWP2AE";
                    spinnerAdapter1 = ArrayAdapter.createFromResource(UploadFileActivity.this, R.array.swp_list, android.R.layout.simple_spinner_item);
                }
                else if(from.equals("Final Inspection")) {
                    proses = "FINAL";
                    spinnerAdapter1 = ArrayAdapter.createFromResource(UploadFileActivity.this, R.array.final_list, android.R.layout.simple_spinner_item);
                }
                else if(from.equals("Pull Off Strength Test")) {
                    proses = "PULLOFF";
                    spinnerAdapter1 = ArrayAdapter.createFromResource(UploadFileActivity.this, R.array.pulloff_list, android.R.layout.simple_spinner_item);
                }
                else if(from.equals("Secondary Surface Preparation and Shop Primer") || from.equals("Secondary Surface Preparation and 1st Coat")) {
                    if(from.equals("Secondary Surface Preparation and Shop Primer")) proses = "SSPSP";
                    else if(from.equals("Secondary Surface Preparation and 1st Coat")) proses = "SSP1C";
                    spinnerAdapter1 = ArrayAdapter.createFromResource(UploadFileActivity.this, R.array.ssp_list, android.R.layout.simple_spinner_item);
                }
                else {
                    if(from.equals("1st Coat and 1st Stripe Coat")) blok = "1C1SC";
                    else if(from.equals("1st Stripe Coat and 2nd Stripe Coat")) blok = "1SC2SC";
                    else if(from.equals("2nd Stripe Coat and 2nd Coat")) blok = "2SC2C";
                    else if(from.equals("2nd Coat and 3rd Coat")) blok = "2C3C";
                    else if(from.equals("3rd Coat and 4th Coat")) blok = "3C4C";
                    else if(from.equals("4th Coat and 5th Coat")) blok = "4C5C";
                    spinnerAdapter1 = ArrayAdapter.createFromResource(UploadFileActivity.this, R.array.coat_list, android.R.layout.simple_spinner_item);
                }

                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinnerBagian.setAdapter(spinnerAdapter1);
                spinnerBagian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String project = spinnerBagian.getSelectedItem().toString();
                        bagian = project;
                        Toast.makeText(UploadFileActivity.this, "Proses = " + proses + "Bagian = " + bagian + "ID = " + IDs + "Blok = " + blok, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class getProses extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getProses() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Getting details...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            showProsesSpinner(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id_blok = params[0];

            String create_url = "http://mobile4day.com/ship-inspection/get_proses.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(id_blok, "UTF-8");
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

            ArrayList<String> fileID = new ArrayList<>();
            ArrayList<String> fileName = new ArrayList<>();
            ArrayList<String> fileProses = new ArrayList<>();
            ArrayList<String> fileUrl = new ArrayList<>();

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                if(from.equals("HELP")) {
                    if(jo.getString("proses").equals("HELP")) {
                        fileID.add(jo.getString("id"));
                        fileName.add(jo.getString("nama"));
                        fileProses.add(jo.getString("proses"));
                        fileUrl.add(jo.getString("url"));
                    }
                }
                else{
                    fileID.add(jo.getString("id"));
                    fileName.add(jo.getString("nama"));
                    fileProses.add(jo.getString("proses"));
                    fileUrl.add(jo.getString("url"));
                }
            }

            lvProject = (ListView) findViewById(R.id.lvListFile);
            listAdapter = new ListAdapter(this, fileID, fileName, fileProses, fileUrl);
            listAdapter.notifyDataSetChanged();
            lvProject.setAdapter(listAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getProject extends AsyncTask<String, String, String> {
        ProgressDialog ProgressDialog;

        public getProject(){
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
            //Toast.makeText(MainPageActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showProject(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/ship-inspection/get_file.php";
            String name = params[0];

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("id_project", "UTF-8")+"="+URLEncoder.encode(projectID,"UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
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

    public class ListAdapter extends BaseAdapter {

        ArrayList<String> fileID;
        Context context;
        ArrayList<String> fileName;
        ArrayList<String> fileProses;
        ArrayList<String> fileUrl;
        private LayoutInflater inflater=null;
        public ListAdapter(Context activity, ArrayList<String> fileID, ArrayList<String> fileName, ArrayList<String> fileProses, ArrayList<String> fileUrl) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.fileID = fileID;
            this.fileName = fileName;
            this.fileProses = fileProses;
            this.fileUrl = fileUrl;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fileID.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView tvNamaFile;
            Button btnLihat, btnHapus;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.file_list, null);
            holder.tvNamaFile = (TextView) rowView.findViewById(R.id.tvNamaFile);
            holder.btnHapus = (Button) rowView.findViewById(R.id.btnHapus);
            holder.btnLihat = (Button) rowView.findViewById(R.id.btnLihat);

            holder.tvNamaFile.setText(fileName.get(position) + " (" + fileProses.get(position) + ") ");

            holder.btnLihat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fileProses.get(position).equals("HELP")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(fileUrl.get(position)));
                        startActivity(intent);
                    }
                    else{
                        showImage(fileName.get(position), fileUrl.get(position));
                    }
                }
            });

            holder.btnHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new deleteFile().execute(fileID.get(position));
                }
            });

            return rowView;
        }
    }

    private void showImage(String fileName, String urls)
    {
        Dialog dialog = new Dialog(Activity);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle(fileName);
        dialog.setCancelable(true);
        ImageView img = (ImageView) dialog.findViewById(R.id.ivImage);

        new DownloadImageTask(img).execute(urls);

        dialog.show();
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

    class deleteFile extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public deleteFile(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Deleting file...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")) {
                Toast.makeText(UploadFileActivity.this, "You file has been deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UploadFileActivity.this, UploadFileActivity.class);
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

            String id_dokumen = params[0];

            String create_url = "http://mobile4day.com/ship-inspection/delete_file.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id_dokumen", "UTF-8")+"="+URLEncoder.encode(id_dokumen,"UTF-8");
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
}
