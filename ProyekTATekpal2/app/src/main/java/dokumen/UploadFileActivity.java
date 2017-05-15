package dokumen;

import android.Manifest;
import android.app.Dialog;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    Spinner spinnerProses, spinnerBagian, spinnerID;
    ListView lvProject;
    private EditText editText;
    Context Activity;
    public static final String UPLOAD_URL = "http://mobile4day.com/welding-inspection/upload.php";

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
    private String proses="", bagian="", IDs="";
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        setTitle("FILE");

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
        spinnerBagian = (Spinner) findViewById(R.id.spinnerBagian);
        spinnerID = (Spinner) findViewById(R.id.spinnerID);
        spinnerBagian.setVisibility(View.GONE);
        spinnerID.setVisibility(View.GONE);
        editText = (EditText) findViewById(R.id.editTextName);

        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        btnShowFile.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.proses_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerProses.setAdapter(spinnerAdapter);
        spinnerProses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String project = spinnerProses.getSelectedItem().toString();

                spinnerBagian.setVisibility(View.GONE);
                spinnerID.setVisibility(View.GONE);

                if(project.equals("SUB-ASSEMBLY")){
                    Toast.makeText(UploadFileActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                    proses = "pvp";

                    spinnerBagian.setVisibility(View.VISIBLE);

                    final ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(UploadFileActivity.this, R.array.subassembly_list, android.R.layout.simple_spinner_item);
                    spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinnerBagian.setAdapter(spinnerAdapter1);
                    spinnerBagian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String project1 = spinnerBagian.getSelectedItem().toString();
                            bagian = project1;

                            new getID().execute("komponen");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if(project.equals("ASSEMBLY") || project.equals("ERECTION")){
                    spinnerBagian.setVisibility(View.VISIBLE);
                    Toast.makeText(UploadFileActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                    if(project.equals("ASSEMBLY")) proses = "swsp";
                    else proses = "swpp";

                    final ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(UploadFileActivity.this, R.array.assembly_list, android.R.layout.simple_spinner_item);
                    spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinnerBagian.setAdapter(spinnerAdapter1);
                    spinnerBagian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String project1 = spinnerBagian.getSelectedItem().toString();
                            bagian = project1;

                            new getID().execute("blok");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if(project.equals("FINAL INSPECTION")){
                    Toast.makeText(UploadFileActivity.this, "You are going to add file into " + project, Toast.LENGTH_SHORT).show();
                    spinnerBagian.setVisibility(View.VISIBLE);
                    proses = "final";

                    final ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(UploadFileActivity.this, R.array.final_list, android.R.layout.simple_spinner_item);
                    spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinnerBagian.setAdapter(spinnerAdapter1);
                    spinnerBagian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String project1 = spinnerBagian.getSelectedItem().toString();
                            bagian = project1;

                            new getID().execute("blok");
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if(project.equals("Select a proses...")){
                    buttonUpload.setVisibility(View.INVISIBLE);
                }
                else if(project.equals("HELP")){
                    buttonUpload.setVisibility(View.VISIBLE);
                    proses = "help";
                }
                else{
                    Toast.makeText(UploadFileActivity.this, "You are going to add file into dokumen", Toast.LENGTH_SHORT).show();
                    proses = "etc";
                    buttonUpload.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void uploadMultipart() {
        //getting name for the image
        String name = editText.getText().toString().trim();

        //getting the actual path of the image
        String path = FilePath.getPath(this, filePath);

        if (path == null) {

            Toast.makeText(this, "Please move your file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("name", name) //Adding text parameter to the request
                        .addParameter("id_proyek", projectID) //Adding text parameter to the request
                        .addParameter("proses", proses) //Adding text parameter to the request
                        .addParameter("bagian", bagian) //Adding text parameter to the request
                        .addParameter("id", IDs) //Adding text parameter to the request
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
        if(proses.equals("help") || proses.equals("etc"))
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
            lvProject.setAdapter(listAdapter);
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

            String create_url = "http://mobile4day.com/welding-inspection/get_file.php";
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
                    if(fileProses.get(position).equals("help") || fileProses.get(position).equals("etc")) {
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

    private void showIDSpinner(String list)
    {
        spinnerID.setVisibility(View.VISIBLE);
        buttonUpload.setVisibility(View.VISIBLE);

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
                Toast.makeText(UploadFileActivity.this, "Proses=" + proses + "Bagian=" + bagian + "ID =" + IDs, Toast.LENGTH_SHORT).show();
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

            String create_url = "http://mobile4day.com/welding-inspection/get_id.php";
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

            String create_url = "http://mobile4day.com/welding-inspection/delete_file.php";
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
