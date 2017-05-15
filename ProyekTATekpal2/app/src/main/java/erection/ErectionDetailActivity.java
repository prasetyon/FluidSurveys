package erection;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
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

public class ErectionDetailActivity extends AppCompatActivity {

    Button btnBack, btnSubmit;
    boolean doubleBackToExitPressedOnce = false;
    EditText edTime, edDate, edMaterialSpec, edWeldingProcess, edFlux, edShieldingGas, edNamaDokumen, edTglDokumen, edComment, edProses;
    EditText edSpatters, edPorosity,edSurfaceConcavity, edPinhole, edSurfaceColdLap, edSurfaceUndercut, edSurfaceUnderfill, edCrack,
            edExcessive, edStopStart, edWideBead, edHighLow;
    EditText edStdSpatters, edStdPorosity,edStdSurfaceConcavity, edStdPinhole, edStdSurfaceColdLap, edStdSurfaceUndercut, edStdSurfaceUnderfill, edStdCrack,
            edStdExcessive, edStdStopStart, edStdWideBead, edStdHighLow;
    private DatePickerDialog dpProyek;
    private SimpleDateFormat dateFormatter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erection_detail);
        setTitle("ERECTION");

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
        setRole(role);
        new getProcess().execute();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnSubmit:
                    String time, date, material, wp, flux, shielding, doc, comment, doc_name, doc_time;
                    time = edTime.getText().toString();
                    date = edDate.getText().toString();
                    material = edMaterialSpec.getText().toString();
                    wp = edWeldingProcess.getText().toString();
                    flux = edFlux.getText().toString();
                    shielding = edShieldingGas.getText().toString();
                    doc = "";
                    comment = edComment.getText().toString();
                    doc_name = edNamaDokumen.getText().toString();
                    doc_time = edTglDokumen.getText().toString();
                    String x1 = edSpatters.getText().toString();
                    String x2 = edStdSpatters.getText().toString();
                    String x3 = edPorosity.getText().toString();
                    String x4 = edStdPorosity.getText().toString();
                    String x5 = edSurfaceConcavity.getText().toString();
                    String x6 = edStdSurfaceConcavity.getText().toString();
                    String x7 = edPinhole.getText().toString();
                    String x8 = edStdPinhole.getText().toString();
                    String x9 = edSurfaceColdLap.getText().toString();
                    String x10 = edStdSurfaceColdLap.getText().toString();
                    String x11 = edSurfaceUndercut.getText().toString();
                    String x12 = edStdSurfaceUndercut.getText().toString();
                    String x13 = edSurfaceUnderfill.getText().toString();
                    String x14 = edStdSurfaceUnderfill.getText().toString();
                    String x15 = edCrack.getText().toString();
                    String x16 = edStdCrack.getText().toString();
                    String x17 = edExcessive.getText().toString();
                    String x18 = edStdExcessive.getText().toString();
                    String x19 = edStopStart.getText().toString();
                    String x20 = edStdStopStart.getText().toString();
                    String x21 = edWideBead.getText().toString();
                    String x22 = edStdWideBead.getText().toString();
                    String x23 = edHighLow.getText().toString();
                    String x24 = edStdHighLow.getText().toString();

                    new updateBlock().execute(time, date, material, wp, flux, shielding, doc, comment, doc_name, doc_time, x1, x2, x3, x4, x5, x6, x7, x8, x9,
                            x10, x11, x12, x13, x14, x15, x16, x17, x18, x19, x20, x21, x22, x23, x24);
                    break;
                case R.id.btnBack:
                    intent = new Intent(ErectionDetailActivity.this, ErectionListActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private void showBlock(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            if(!jo.getString("spatters").equals("")) edSpatters.setText(jo.getString("spatters"));
            if(!jo.getString("std_spatters").equals("")) edStdSpatters.setText(jo.getString("std_spatters"));
            if(!jo.getString("porosity").equals("")) edPorosity.setText(jo.getString("porosity"));
            if(!jo.getString("std_porosity").equals("")) edStdPorosity.setText(jo.getString("std_porosity"));
            if(!jo.getString("sc").equals("")) edSurfaceConcavity.setText(jo.getString("sc"));
            if(!jo.getString("std_sc").equals("")) edStdSurfaceConcavity.setText(jo.getString("std_sc"));
            if(!jo.getString("pinhole").equals("")) edPinhole.setText(jo.getString("pinhole"));
            if(!jo.getString("std_pinhole").equals("")) edStdPinhole.setText(jo.getString("std_pinhole"));
            if(!jo.getString("scl").equals("")) edSurfaceColdLap.setText(jo.getString("scl"));
            if(!jo.getString("std_scl").equals("")) edStdSurfaceColdLap.setText(jo.getString("std_scl"));
            if(!jo.getString("undercut").equals("")) edSurfaceUndercut.setText(jo.getString("undercut"));
            if(!jo.getString("std_undercut").equals("")) edStdSurfaceUndercut.setText(jo.getString("std_undercut"));
            if(!jo.getString("underfill").equals("")) edSurfaceUnderfill.setText(jo.getString("underfill"));
            if(!jo.getString("std_underfill").equals("")) edStdSurfaceUnderfill.setText(jo.getString("std_underfill"));
            if(!jo.getString("crack").equals("")) edCrack.setText(jo.getString("crack"));
            if(!jo.getString("std_crack").equals("")) edStdCrack.setText(jo.getString("std_crack"));
            if(!jo.getString("excessive").equals("")) edExcessive.setText(jo.getString("excessive"));
            if(!jo.getString("std_excessive").equals("")) edStdExcessive.setText(jo.getString("std_excessive"));
            if(!jo.getString("stop").equals("")) edStopStart.setText(jo.getString("stop"));
            if(!jo.getString("std_stop").equals("")) edStdStopStart.setText(jo.getString("std_stop"));
            if(!jo.getString("wb").equals("")) edWideBead.setText(jo.getString("wb"));
            if(!jo.getString("std_wb").equals("")) edStdWideBead.setText(jo.getString("std_wb"));
            if(!jo.getString("hl").equals("")) edHighLow.setText(jo.getString("hl"));
            if(!jo.getString("std_hl").equals("")) edStdHighLow.setText(jo.getString("std_hl"));

            if(!jo.getString("time").equals("")) edTime.setText(jo.getString("time"));
            if(!jo.getString("date").equals("")) edDate.setText(jo.getString("date"));
            if(!jo.getString("material_spec").equals("")) edMaterialSpec.setText(jo.getString("material_spec"));
            if(!jo.getString("welding_process").equals("")) edWeldingProcess.setText(jo.getString("welding_process"));
            if(!jo.getString("flux").equals("")) edFlux.setText(jo.getString("flux"));
            if(!jo.getString("shielding").equals("")) edShieldingGas.setText(jo.getString("shielding"));

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
            String from = "swpp";

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
                Toast.makeText(ErectionDetailActivity.this, "You block has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ErectionDetailActivity.this, ErectionListActivity.class);
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

            String time = params[0];
            String date = params[1];
            String material = params[2];
            String wp = params[3];
            String flux = params[4];
            String shielding = params[5];
            String doc = params[6];
            String comment = params[7];
            String doc_name = params[8];
            String doc_date = params[9];
            String spatters = params[10];
            String std_spatters = params[11];
            String porosity = params[12];
            String std_porosity = params[13];
            String sc = params[14];
            String std_sc = params[15];
            String pinhole = params[16];
            String std_pinhole = params[17];
            String scl = params[18];
            String std_scl = params[19];
            String undercut = params[20];
            String std_undercut = params[21];
            String underfill = params[22];
            String std_underfill = params[23];
            String crack = params[24];
            String std_crack = params[25];
            String excessive = params[26];
            String std_excessive = params[27];
            String stop = params[28];
            String std_stop = params[29];
            String wb = params[30];
            String std_wb = params[31];
            String high = params[32];
            String std_high = params[33];
            String from = "swpp";

            String create_url = "http://mobile4day.com/welding-inspection/update_process.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("spatters","UTF-8")+"="+URLEncoder.encode(spatters,"UTF-8")+"&"+
                                URLEncoder.encode("std_spatters","UTF-8")+"="+URLEncoder.encode(std_spatters,"UTF-8")+"&"+
                                URLEncoder.encode("porosity","UTF-8")+"="+URLEncoder.encode(porosity,"UTF-8")+"&"+
                                URLEncoder.encode("std_porosity","UTF-8")+"="+URLEncoder.encode(std_porosity,"UTF-8")+"&"+
                                URLEncoder.encode("sc","UTF-8")+"="+URLEncoder.encode(sc,"UTF-8")+"&"+
                                URLEncoder.encode("std_sc","UTF-8")+"="+URLEncoder.encode(std_sc,"UTF-8")+"&"+
                                URLEncoder.encode("pinhole","UTF-8")+"="+URLEncoder.encode(pinhole,"UTF-8")+"&"+
                                URLEncoder.encode("std_pinhole","UTF-8")+"="+URLEncoder.encode(std_pinhole,"UTF-8")+"&"+
                                URLEncoder.encode("scl","UTF-8")+"="+URLEncoder.encode(scl,"UTF-8")+"&"+
                                URLEncoder.encode("std_scl","UTF-8")+"="+URLEncoder.encode(std_scl,"UTF-8")+"&"+
                                URLEncoder.encode("undercut","UTF-8")+"="+URLEncoder.encode(undercut,"UTF-8")+"&"+
                                URLEncoder.encode("std_undercut","UTF-8")+"="+URLEncoder.encode(std_undercut,"UTF-8")+"&"+
                                URLEncoder.encode("underfill","UTF-8")+"="+URLEncoder.encode(underfill,"UTF-8")+"&"+
                                URLEncoder.encode("std_underfill","UTF-8")+"="+URLEncoder.encode(std_underfill,"UTF-8")+"&"+
                                URLEncoder.encode("crack","UTF-8")+"="+URLEncoder.encode(crack,"UTF-8")+"&"+
                                URLEncoder.encode("std_crack","UTF-8")+"="+URLEncoder.encode(std_crack,"UTF-8")+"&"+
                                URLEncoder.encode("excessive","UTF-8")+"="+URLEncoder.encode(excessive,"UTF-8")+"&"+
                                URLEncoder.encode("std_excessive","UTF-8")+"="+URLEncoder.encode(std_excessive,"UTF-8")+"&"+
                                URLEncoder.encode("stop","UTF-8")+"="+URLEncoder.encode(stop,"UTF-8")+"&"+
                                URLEncoder.encode("std_stop","UTF-8")+"="+URLEncoder.encode(std_stop,"UTF-8")+"&"+
                                URLEncoder.encode("wb","UTF-8")+"="+URLEncoder.encode(wb,"UTF-8")+"&"+
                                URLEncoder.encode("std_wb","UTF-8")+"="+URLEncoder.encode(std_wb,"UTF-8")+"&"+
                                URLEncoder.encode("high","UTF-8")+"="+URLEncoder.encode(high,"UTF-8")+"&"+
                                URLEncoder.encode("std_high","UTF-8")+"="+URLEncoder.encode(std_high,"UTF-8")+"&"+
                                URLEncoder.encode("time", "UTF-8")+"="+URLEncoder.encode(time,"UTF-8")+"&"+
                                URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"+
                                URLEncoder.encode("material","UTF-8")+"="+URLEncoder.encode(material,"UTF-8")+"&"+
                                URLEncoder.encode("wp","UTF-8")+"="+URLEncoder.encode(wp,"UTF-8")+"&"+
                                URLEncoder.encode("flux","UTF-8")+"="+URLEncoder.encode(flux,"UTF-8")+"&"+
                                URLEncoder.encode("shielding","UTF-8")+"="+URLEncoder.encode(shielding,"UTF-8")+"&"+
                                URLEncoder.encode("doc","UTF-8")+"="+URLEncoder.encode(doc,"UTF-8")+"&"+
                                URLEncoder.encode("comment","UTF-8")+"="+URLEncoder.encode(comment,"UTF-8")+"&"+
                                URLEncoder.encode("doc_name","UTF-8")+"="+URLEncoder.encode(doc_name,"UTF-8")+"&"+
                                URLEncoder.encode("doc_date","UTF-8")+"="+URLEncoder.encode(doc_date,"UTF-8")+"&"+
                                URLEncoder.encode("from","UTF-8")+"="+URLEncoder.encode(from,"UTF-8")+"&"+
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
                    case R.id.edDate:
                        edDate.setText(dateFormatter.format(newDate.getTime()));
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
        Intent intent = new Intent(ErectionDetailActivity.this, UploadFileActivity.class);
        extras.putString("FROM", "HELP");
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void initiate()
    {
        edProses = (EditText) findViewById(R.id.edProses);
        edProses.setEnabled(false);
        edNamaDokumen = (EditText) findViewById(R.id.edNamaDokumen);
        edTglDokumen = (EditText) findViewById(R.id.edTglDokumen);
        edComment = (EditText) findViewById(R.id.edComment);
        edTime = (EditText) findViewById(R.id.edTime);
        edDate = (EditText) findViewById(R.id.edDate);
        edMaterialSpec = (EditText) findViewById(R.id.edMaterialSpec);
        edWeldingProcess = (EditText) findViewById(R.id.edWeldingProcess);
        edFlux = (EditText) findViewById(R.id.edFlux);
        edShieldingGas = (EditText) findViewById(R.id.edShieldingGas);

        edSpatters = (EditText) findViewById(R.id.edSpatters);
        edPorosity = (EditText) findViewById(R.id.edPorosity);
        edSurfaceConcavity = (EditText) findViewById(R.id.edSurfaceConcavity);
        edPinhole = (EditText) findViewById(R.id.edPinhole);
        edSurfaceColdLap = (EditText) findViewById(R.id.edSurfaceColdLap);
        edSurfaceUndercut = (EditText) findViewById(R.id.edSurfaceUndercut);
        edSurfaceUnderfill = (EditText) findViewById(R.id.edSurfaceUnderfill);
        edCrack = (EditText) findViewById(R.id.edCrack);
        edExcessive = (EditText) findViewById(R.id.edExcessive);
        edStopStart = (EditText) findViewById(R.id.edStopStart);
        edWideBead = (EditText) findViewById(R.id.edWideBead);
        edHighLow = (EditText) findViewById(R.id.edHighLow);

        edStdSpatters = (EditText) findViewById(R.id.edStdSpatters);
        edStdPorosity = (EditText) findViewById(R.id.edStdPorosity);
        edStdSurfaceConcavity = (EditText) findViewById(R.id.edStdSurfaceConcavity);
        edStdPinhole = (EditText) findViewById(R.id.edStdPinhole);
        edStdSurfaceColdLap = (EditText) findViewById(R.id.edStdSurfaceColdLap);
        edStdSurfaceUndercut = (EditText) findViewById(R.id.edStdSurfaceUndercut);
        edStdSurfaceUnderfill = (EditText) findViewById(R.id.edStdSurfaceUnderfill);
        edStdCrack = (EditText) findViewById(R.id.edStdCrack);
        edStdExcessive = (EditText) findViewById(R.id.edStdExcessive);
        edStdStopStart = (EditText) findViewById(R.id.edStdStopStart);
        edStdWideBead = (EditText) findViewById(R.id.edStdWideBead);
        edStdHighLow = (EditText) findViewById(R.id.edStdHighLow);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        edDate.setOnClickListener(tampil_tanggal);
        edTglDokumen.setOnClickListener(tampil_tanggal);
        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    }

    private void setRole(String role)
    {
        if(role.equals("admin"))
        {
            edSpatters.setEnabled(false);
            edPorosity.setEnabled(false);
            edSurfaceConcavity.setEnabled(false);
            edPinhole.setEnabled(false);
            edSurfaceColdLap.setEnabled(false);
            edSurfaceUndercut.setEnabled(false);
            edSurfaceUnderfill.setEnabled(false);
            edCrack.setEnabled(false);
            edExcessive.setEnabled(false);
            edStopStart.setEnabled(false);
            edWideBead.setEnabled(false);
            edHighLow.setEnabled(false);

            edTime.setEnabled(false);
            edDate.setEnabled(false);
            edMaterialSpec.setEnabled(false);
            edWeldingProcess.setEnabled(false);
            edFlux.setEnabled(false);
            edShieldingGas.setEnabled(false);
            edNamaDokumen.setEnabled(false);
            edTglDokumen.setEnabled(false);
            edComment.setEnabled(false);
            edProses.setEnabled(false);
        }
        else{
            edStdSpatters.setEnabled(false);
            edStdPorosity.setEnabled(false);
            edStdSurfaceConcavity.setEnabled(false);
            edStdPinhole.setEnabled(false);
            edStdSurfaceColdLap.setEnabled(false);
            edStdSurfaceUndercut.setEnabled(false);
            edStdSurfaceUnderfill.setEnabled(false);
            edStdCrack.setEnabled(false);
            edStdExcessive.setEnabled(false);
            edStdStopStart.setEnabled(false);
            edStdWideBead.setEnabled(false);
            edStdHighLow.setEnabled(false);

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


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
