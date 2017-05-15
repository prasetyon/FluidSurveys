package inspeksiFinal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal2.R;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dokumen.UploadFileActivity;

public class InspeksiFinalCheckActivity extends AppCompatActivity {

    Button btnBack, btnSubmit;
    boolean doubleBackToExitPressedOnce = false;
    EditText edNamaDokumen, edTglDokumen, edComment;
    Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6, spinner7, spinner8, spinner9;
    Context Activity;
    private Bundle extras;
    private boolean created=false;
    private String create="";
    private DatePickerDialog dpProyek;
    private SimpleDateFormat dateFormatter;
    private String projectID;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String blockID;
    private String blockLocation;
    private String question1, question2, question3, question4, question5, question6, question7, question8, question9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspeksi_final_check);
        setTitle("FINAL INSPECTION");

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");
        blockLocation = extras.getString("BLOCK_LOCATION");
        blockID = extras.getString("BLOCK_ID");

        initiate();

        new getProcess().execute();
    }

    private void showBlock(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            spinner1.setSelection(getPosition(jo.getString("q1")));
            spinner2.setSelection(getPosition(jo.getString("q2")));
            spinner3.setSelection(getPosition(jo.getString("q3")));
            spinner4.setSelection(getPosition(jo.getString("q4")));
            spinner5.setSelection(getPosition(jo.getString("q5")));
            spinner6.setSelection(getPosition(jo.getString("q6")));
            spinner7.setSelection(getPosition(jo.getString("q7")));
            spinner8.setSelection(getPosition(jo.getString("q8")));
            spinner9.setSelection(getPosition(jo.getString("q9")));

            if(jo.getString("doc1")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation1)).execute(jo.getString("doc1"));
            if(jo.getString("doc2")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation2)).execute(jo.getString("doc2"));
            if(jo.getString("doc3")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation3)).execute(jo.getString("doc3"));
            if(jo.getString("doc4")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation4)).execute(jo.getString("doc4"));
            if(jo.getString("doc5")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation5)).execute(jo.getString("doc5"));
            if(jo.getString("doc6")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation6)).execute(jo.getString("doc6"));

            if(!jo.getString("comment").equals("")) edComment.setText(jo.getString("comment"));
            if(!jo.getString("doc_date").equals("")) edTglDokumen.setText(jo.getString("doc_date"));
            if(!jo.getString("doc_name").equals("")) edNamaDokumen.setText(jo.getString("doc_name"));

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

    class updateBlock extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updateBlock(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Updating block...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            created = true;
            //Toast.makeText(Activity, "s = " + s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(created && create.equals("1")){
                Toast.makeText(InspeksiFinalCheckActivity.this, "You block has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InspeksiFinalCheckActivity.this, InspeksiFinalListActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(InspeksiFinalCheckActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String doc = params[0];
            String comment = params[1];
            String doc_name = params[2];
            String doc_date = params[3];

            String create_url = "http://mobile4day.com/welding-inspection/update_final.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("doc","UTF-8")+"="+URLEncoder.encode(doc,"UTF-8")+"&"+
                                URLEncoder.encode("q1","UTF-8")+"="+URLEncoder.encode(question1,"UTF-8")+"&"+
                                URLEncoder.encode("q2","UTF-8")+"="+URLEncoder.encode(question2,"UTF-8")+"&"+
                                URLEncoder.encode("q3","UTF-8")+"="+URLEncoder.encode(question3,"UTF-8")+"&"+
                                URLEncoder.encode("q4","UTF-8")+"="+URLEncoder.encode(question4,"UTF-8")+"&"+
                                URLEncoder.encode("q5","UTF-8")+"="+URLEncoder.encode(question5,"UTF-8")+"&"+
                                URLEncoder.encode("q6","UTF-8")+"="+URLEncoder.encode(question6,"UTF-8")+"&"+
                                URLEncoder.encode("q7","UTF-8")+"="+URLEncoder.encode(question7,"UTF-8")+"&"+
                                URLEncoder.encode("q8","UTF-8")+"="+URLEncoder.encode(question8,"UTF-8")+"&"+
                                URLEncoder.encode("q9","UTF-8")+"="+URLEncoder.encode(question9,"UTF-8")+"&"+
                                URLEncoder.encode("comment","UTF-8")+"="+URLEncoder.encode(comment,"UTF-8")+"&"+
                                URLEncoder.encode("doc_name","UTF-8")+"="+URLEncoder.encode(doc_name,"UTF-8")+"&"+
                                URLEncoder.encode("doc_date","UTF-8")+"="+URLEncoder.encode(doc_date,"UTF-8")+"&"+
                                URLEncoder.encode("id_blok","UTF-8")+"="+URLEncoder.encode(blockID,"UTF-8");
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

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnSubmit:
                    String doc, comment, doc_name, doc_time;

                    doc = "";
                    comment = edComment.getText().toString();
                    doc_name = edNamaDokumen.getText().toString();
                    doc_time = edTglDokumen.getText().toString();
                    new updateBlock().execute(doc, comment, doc_name, doc_time);

                    break;
                case R.id.btnBack:
                    intent = new Intent(InspeksiFinalCheckActivity.this, InspeksiFinalListActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
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
                    case R.id.edTglDokumen:
                        edTglDokumen.setText(dateFormatter.format(newDate.getTime()));
                        break;
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dpProyek.show();
    }

    View.OnClickListener tampil_tanggal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDate(view);
        }
    };

    public void help(View view)
    {
        Intent intent = new Intent(InspeksiFinalCheckActivity.this, UploadFileActivity.class);
        extras.putString("FROM", "HELP");
        intent.putExtras(extras);
        startActivity(intent);
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

    private int getPosition(String position)
    {
        if(position.equals("NO")) return 0;
        else if(position.equals("YES WITH REPAIR")) return 1;
        else return 2;
    }

    private void initiate()
    {
        edNamaDokumen = (EditText) findViewById(R.id.edNamaDokumen);
        edTglDokumen = (EditText) findViewById(R.id.edTglDokumen);
        edTglDokumen.setOnClickListener(tampil_tanggal);
        edComment = (EditText) findViewById(R.id.edComment);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(this, R.array.final_choice_list, android.R.layout.simple_spinner_item);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(spinnerAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question1 = spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(this, R.array.final_choice_list, android.R.layout.simple_spinner_item);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(spinnerAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question2 = spinner2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> spinnerAdapter3 = ArrayAdapter.createFromResource(this, R.array.final_choice_list, android.R.layout.simple_spinner_item);
        spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner3.setAdapter(spinnerAdapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question3 = spinner3.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner4 = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> spinnerAdapter4 = ArrayAdapter.createFromResource(this, R.array.final_choice_list, android.R.layout.simple_spinner_item);
        spinnerAdapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner4.setAdapter(spinnerAdapter4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question4 = spinner4.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner5 = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> spinnerAdapter5 = ArrayAdapter.createFromResource(this, R.array.final_choice_list, android.R.layout.simple_spinner_item);
        spinnerAdapter5.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner5.setAdapter(spinnerAdapter5);
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question5 = spinner5.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner6 = (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter<CharSequence> spinnerAdapter6 = ArrayAdapter.createFromResource(this, R.array.final_choice_list, android.R.layout.simple_spinner_item);
        spinnerAdapter6.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner6.setAdapter(spinnerAdapter6);
        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question6 = spinner6.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner7 = (Spinner) findViewById(R.id.spinner7);
        ArrayAdapter<CharSequence> spinnerAdapter7 = ArrayAdapter.createFromResource(this, R.array.final_choice_list, android.R.layout.simple_spinner_item);
        spinnerAdapter7.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner7.setAdapter(spinnerAdapter7);
        spinner7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question7 = spinner7.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner8 = (Spinner) findViewById(R.id.spinner8);
        ArrayAdapter<CharSequence> spinnerAdapter8 = ArrayAdapter.createFromResource(this, R.array.final_choice_list, android.R.layout.simple_spinner_item);
        spinnerAdapter8.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner8.setAdapter(spinnerAdapter8);
        spinner8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question8 = spinner8.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner9 = (Spinner) findViewById(R.id.spinner9);
        ArrayAdapter<CharSequence> spinnerAdapter9 = ArrayAdapter.createFromResource(this, R.array.final_choice_list, android.R.layout.simple_spinner_item);
        spinnerAdapter9.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner9.setAdapter(spinnerAdapter9);
        spinner9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question9 = spinner9.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
