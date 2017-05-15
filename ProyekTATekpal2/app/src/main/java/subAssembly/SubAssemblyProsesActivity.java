package subAssembly;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class SubAssemblyProsesActivity extends AppCompatActivity {

    Button btnBack, btnSimpan;
    TextView tvNamaKomponen;
    EditText edNamaBlok, edProsesSub, edMaterialSpec, edWeldingProcess, edManualMachine, edPosition, edFilterMetalSpec, edFilterMetalClass, edComment, edTglDokumen, edNamaDokumen;
    EditText edWeldingCurrent, edPolarity, edElectrodeSize, edSpeed, edSharpEdges, edGap, edImperfectWelding, edMissWeld, edDeformation;
    EditText edStdMaterialSpec, edStdWeldingProcess, edStdManualMachine, edStdPosition, edStdFilterMetalSpec, edStdFilterMetalClass, edStdWeldingCurrent, edStdPolarity,
            edStdElectrodeSize, edStdSpeed, edStdSharpEdges, edStdGap, edStdImperfectWelding, edStdMissWeld, edStdDeformation;
    boolean doubleBackToExitPressedOnce;
    private DatePickerDialog dpProyek;
    private SimpleDateFormat dateFormatter;
    Context Activity;
    private Bundle extras;
    private boolean created;
    private String create;
    private String projectID;
    private String project;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String namaKomponen;
    private String idKomponen;

    private void initiate()
    {
        tvNamaKomponen = (TextView) findViewById(R.id.tvNamaKomponen);
        edNamaBlok = (EditText) findViewById(R.id.edNamaBlok);
        edProsesSub = (EditText) findViewById(R.id.edProsesSub);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);

        edMaterialSpec = (EditText) findViewById(R.id.edMaterialSpec);
        edWeldingProcess = (EditText) findViewById(R.id.edWeldingProcess);
        edManualMachine = (EditText) findViewById(R.id.edManualMachine);
        edPosition = (EditText) findViewById(R.id.edPosition);
        edFilterMetalSpec = (EditText) findViewById(R.id.edFilterMetalSpec);
        edFilterMetalClass = (EditText) findViewById(R.id.edFilterMetalClass);
        edWeldingCurrent = (EditText) findViewById(R.id.edWeldingCurrent);
        edPolarity = (EditText) findViewById(R.id.edPolarity);
        edElectrodeSize = (EditText) findViewById(R.id.edElectrodeSize);
        edSpeed = (EditText) findViewById(R.id.edSpeed);
        edSharpEdges = (EditText) findViewById(R.id.edSharpEdges);
        edGap = (EditText) findViewById(R.id.edGap);
        edImperfectWelding = (EditText) findViewById(R.id.edImperfectWelding);
        edMissWeld = (EditText) findViewById(R.id.edMissWeld);
        edDeformation = (EditText) findViewById(R.id.edDeformation);

        edStdMaterialSpec = (EditText) findViewById(R.id.edStdMaterialSpec);
        edStdWeldingProcess = (EditText) findViewById(R.id.edStdWeldingProcess);
        edStdManualMachine = (EditText) findViewById(R.id.edStdManualMachine);
        edStdPosition = (EditText) findViewById(R.id.edStdPosition);
        edStdFilterMetalSpec = (EditText) findViewById(R.id.edStdFilterMetalSpec);
        edStdFilterMetalClass = (EditText) findViewById(R.id.edStdFilterMetalClass);
        edStdWeldingCurrent = (EditText) findViewById(R.id.edStdWeldingCurrent);
        edStdPolarity = (EditText) findViewById(R.id.edStdPolarity);
        edStdElectrodeSize = (EditText) findViewById(R.id.edStdElectrodeSize);
        edStdSpeed = (EditText) findViewById(R.id.edStdSpeed);
        edStdSharpEdges = (EditText) findViewById(R.id.edStdSharpEdges);
        edStdGap = (EditText) findViewById(R.id.edStdGap);
        edStdImperfectWelding = (EditText) findViewById(R.id.edStdImperfectWelding);
        edStdMissWeld = (EditText) findViewById(R.id.edStdMissWeld);
        edStdDeformation = (EditText) findViewById(R.id.edStdDeformation);

        edComment = (EditText) findViewById(R.id.edComment);
        edTglDokumen = (EditText) findViewById(R.id.edTglDokumen);
        edNamaDokumen = (EditText) findViewById(R.id.edNamaDokumen);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        edTglDokumen.setOnClickListener(tampil_tanggal);
        btnBack.setOnClickListener(activity);
        btnSimpan.setOnClickListener(activity);

        edProsesSub.setEnabled(false);
        edNamaBlok.setEnabled(false);
    }

    private void setRole(String role)
    {
        edNamaBlok.setEnabled(false);
        edProsesSub.setEnabled(false);
        tvNamaKomponen.setEnabled(false);

        if(role.equals("admin"))
        {
            edMaterialSpec.setEnabled(false);
            edWeldingProcess.setEnabled(false);
            edManualMachine.setEnabled(false);
            edPosition.setEnabled(false);
            edFilterMetalSpec.setEnabled(false);
            edFilterMetalClass.setEnabled(false);
            edComment.setEnabled(false);
            edTglDokumen.setEnabled(false);
            edNamaDokumen.setEnabled(false);
            edWeldingCurrent.setEnabled(false);
            edPolarity.setEnabled(false);
            edElectrodeSize.setEnabled(false);
            edSpeed.setEnabled(false);
            edSharpEdges.setEnabled(false);
            edGap.setEnabled(false);
            edImperfectWelding.setEnabled(false);
            edMissWeld.setEnabled(false);
            edDeformation.setEnabled(false);
        }
        else{
            edStdMaterialSpec.setEnabled(false);
            edStdWeldingProcess.setEnabled(false);
            edStdManualMachine.setEnabled(false);
            edStdPosition.setEnabled(false);
            edStdFilterMetalSpec.setEnabled(false);
            edStdFilterMetalClass.setEnabled(false);
            edStdWeldingCurrent.setEnabled(false);
            edStdPolarity.setEnabled(false);
            edStdElectrodeSize.setEnabled(false);
            edStdSpeed.setEnabled(false);
            edStdSharpEdges.setEnabled(false);
            edStdGap.setEnabled(false);
            edStdImperfectWelding.setEnabled(false);
            edStdMissWeld.setEnabled(false);
            edStdDeformation.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_assembly_proses);
        setTitle("SUB ASSEMBLY");

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        namaKomponen = extras.getString("KOMPONEN_NAME");
        projectID = extras.getString("PROJECT_ID");
        idKomponen = extras.getString("COMPONENT_ID");

        initiate();
        setRole(role);

        new getComponent().execute(idKomponen);
    }

    private void showComponent(String s)
    {
        JSONObject jsonObject;
        //Toast.makeText(DetailProjectActivity.this, "showComponent with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            tvNamaKomponen.setText(jo.getString("nama_komponen"));
            if(jo.getString("proses")!=null) edProsesSub.setText(jo.getString("proses"));
            if(jo.getString("nama_blok")!=null) edNamaBlok.setText(jo.getString("nama_blok"));
            if(jo.getString("material_spec")!=null) edMaterialSpec.setText(jo.getString("material_spec"));
            if(jo.getString("std_material_spec")!=null) edStdMaterialSpec.setText(jo.getString("std_material_spec"));
            if(jo.getString("wp")!=null) edWeldingProcess.setText(jo.getString("wp"));
            if(jo.getString("stdwp")!=null) edStdWeldingProcess.setText(jo.getString("stdwp"));
            if(jo.getString("mm")!=null) edManualMachine.setText(jo.getString("mm"));
            if(jo.getString("stdmm")!=null) edStdManualMachine.setText(jo.getString("stdmm"));
            if(jo.getString("pow")!=null) edPosition.setText(jo.getString("pow"));
            if(jo.getString("stdpow")!=null) edStdPosition.setText(jo.getString("stdpow"));
            if(jo.getString("fms")!=null) edFilterMetalSpec.setText(jo.getString("fms"));
            if(jo.getString("stdfms")!=null) edStdFilterMetalSpec.setText(jo.getString("stdfms"));
            if(jo.getString("fmc")!=null) edFilterMetalClass.setText(jo.getString("fmc"));
            if(jo.getString("stdfmc")!=null) edStdFilterMetalClass.setText(jo.getString("stdfmc"));
            if(jo.getString("wc")!=null) edWeldingCurrent.setText(jo.getString("wc"));
            if(jo.getString("stdwc")!=null) edStdWeldingCurrent.setText(jo.getString("stdwc"));
            if(jo.getString("polar")!=null) edPolarity.setText(jo.getString("polar"));
            if(jo.getString("stdpolar")!=null) edStdPolarity.setText(jo.getString("stdpolar"));
            if(jo.getString("es")!=null) edElectrodeSize.setText(jo.getString("es"));
            if(jo.getString("stdes")!=null) edStdElectrodeSize.setText(jo.getString("stdes"));
            if(jo.getString("sot")!=null) edSpeed.setText(jo.getString("sot"));
            if(jo.getString("stdsot")!=null) edStdSpeed.setText(jo.getString("stdsot"));
            if(jo.getString("se")!=null) edSharpEdges.setText(jo.getString("se"));
            if(jo.getString("stdse")!=null) edStdSharpEdges.setText(jo.getString("stdse"));
            if(jo.getString("gap")!=null) edGap.setText(jo.getString("gap"));
            if(jo.getString("stdgap")!=null) edStdGap.setText(jo.getString("stdgap"));
            if(jo.getString("iw")!=null) edImperfectWelding.setText(jo.getString("iw"));
            if(jo.getString("stdiw")!=null) edStdImperfectWelding.setText(jo.getString("stdiw"));
            if(jo.getString("mw")!=null) edMissWeld.setText(jo.getString("mw"));
            if(jo.getString("stdmw")!=null) edStdMissWeld.setText(jo.getString("stdmw"));
            if(jo.getString("deform")!=null) edDeformation.setText(jo.getString("deform"));
            if(jo.getString("stddeform")!=null) edStdDeformation.setText(jo.getString("stddeform"));

            if(jo.getString("comment")!=null) edComment.setText(jo.getString("comment"));

            if(jo.getString("doc1")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation1)).execute(jo.getString("doc1"));
            if(jo.getString("doc2")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation2)).execute(jo.getString("doc2"));
            if(jo.getString("doc3")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation3)).execute(jo.getString("doc3"));
            if(jo.getString("doc4")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation4)).execute(jo.getString("doc4"));
            if(jo.getString("doc5")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation5)).execute(jo.getString("doc5"));
            if(jo.getString("doc6")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation6)).execute(jo.getString("doc6"));

            if(jo.getString("doc_date")!=null) edTglDokumen.setText(jo.getString("doc_date"));
            if(jo.getString("doc_name")!=null) edNamaDokumen.setText(jo.getString("doc_name"));

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

    class updateComponent extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updateComponent() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("updating component...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            created = true;
            //Toast.makeText(Activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if (created && create.equals("1")) {
                Toast.makeText(SubAssemblyProsesActivity.this, "You component has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SubAssemblyProsesActivity.this, SubAssemblyListActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(SubAssemblyProsesActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            String id_komponen = params[0];
            String ms = params[1];
            String stdms = params[2];
            String wp = params[3];
            String stdwp = params[4];
            String mm = params[5];
            String stdmm = params[6];
            String pow = params[7];
            String stdpow = params[8];
            String fms = params[9];
            String stdfms = params[10];
            String fmc = params[11];
            String stdfmc = params[12];
            String wc = params[13];
            String stdwc = params[14];
            String polar = params[15];
            String stdpolar = params[16];
            String es = params[17];
            String stdes = params[18];
            String sot = params[19];
            String stdsot = params[20];
            String se = params[21];
            String stdse = params[22];
            String gap = params[23];
            String stdgap = params[24];
            String iw = params[25];
            String stdiw = params[26];
            String mw = params[27];
            String stdmw = params[28];
            String defor = params[29];
            String stddefor = params[30];
            String comment = params[31];
            String doc_date = params[32];
            String doc_name = params[33];

            String create_url = "http://mobile4day.com/welding-inspection/update_component_detail.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("ms", "UTF-8") + "=" + URLEncoder.encode(ms, "UTF-8") + "&" +
                                URLEncoder.encode("stdms", "UTF-8") + "=" + URLEncoder.encode(stdms, "UTF-8") + "&" +
                                URLEncoder.encode("wp", "UTF-8") + "=" + URLEncoder.encode(wp, "UTF-8") + "&" +
                                URLEncoder.encode("stdwp", "UTF-8") + "=" + URLEncoder.encode(stdwp, "UTF-8") + "&" +
                                URLEncoder.encode("mm", "UTF-8") + "=" + URLEncoder.encode(mm, "UTF-8") + "&" +
                                URLEncoder.encode("stdmm", "UTF-8") + "=" + URLEncoder.encode(stdmm, "UTF-8") + "&" +
                                URLEncoder.encode("pow", "UTF-8") + "=" + URLEncoder.encode(pow, "UTF-8") + "&" +
                                URLEncoder.encode("stdpow", "UTF-8") + "=" + URLEncoder.encode(stdpow, "UTF-8") + "&" +
                                URLEncoder.encode("fms", "UTF-8") + "=" + URLEncoder.encode(fms, "UTF-8") + "&" +
                                URLEncoder.encode("stdfms", "UTF-8") + "=" + URLEncoder.encode(stdfms, "UTF-8") + "&" +
                                URLEncoder.encode("fmc", "UTF-8") + "=" + URLEncoder.encode(fmc, "UTF-8") + "&" +
                                URLEncoder.encode("stdfmc", "UTF-8") + "=" + URLEncoder.encode(stdfmc, "UTF-8") + "&" +
                                URLEncoder.encode("wc", "UTF-8") + "=" + URLEncoder.encode(wc, "UTF-8") + "&" +
                                URLEncoder.encode("stdwc", "UTF-8") + "=" + URLEncoder.encode(stdwc, "UTF-8") + "&" +
                                URLEncoder.encode("polar", "UTF-8") + "=" + URLEncoder.encode(polar, "UTF-8") + "&" +
                                URLEncoder.encode("stdpolar", "UTF-8") + "=" + URLEncoder.encode(stdpolar, "UTF-8") + "&" +
                                URLEncoder.encode("es", "UTF-8") + "=" + URLEncoder.encode(es, "UTF-8") + "&" +
                                URLEncoder.encode("stdes", "UTF-8") + "=" + URLEncoder.encode(stdes, "UTF-8") + "&" +
                                URLEncoder.encode("sot", "UTF-8") + "=" + URLEncoder.encode(sot, "UTF-8") + "&" +
                                URLEncoder.encode("stdsot", "UTF-8") + "=" + URLEncoder.encode(stdsot, "UTF-8") + "&" +
                                URLEncoder.encode("se", "UTF-8") + "=" + URLEncoder.encode(se, "UTF-8") + "&" +
                                URLEncoder.encode("stdse", "UTF-8") + "=" + URLEncoder.encode(stdse, "UTF-8") + "&" +
                                URLEncoder.encode("gap", "UTF-8") + "=" + URLEncoder.encode(gap, "UTF-8") + "&" +
                                URLEncoder.encode("stdgap", "UTF-8") + "=" + URLEncoder.encode(stdgap, "UTF-8") + "&" +
                                URLEncoder.encode("iw", "UTF-8") + "=" + URLEncoder.encode(iw, "UTF-8") + "&" +
                                URLEncoder.encode("stdiw", "UTF-8") + "=" + URLEncoder.encode(stdiw, "UTF-8") + "&" +
                                URLEncoder.encode("mw", "UTF-8") + "=" + URLEncoder.encode(mw, "UTF-8") + "&" +
                                URLEncoder.encode("stdmw", "UTF-8") + "=" + URLEncoder.encode(stdmw, "UTF-8") + "&" +
                                URLEncoder.encode("defor", "UTF-8") + "=" + URLEncoder.encode(defor, "UTF-8") + "&" +
                                URLEncoder.encode("stddefor", "UTF-8") + "=" + URLEncoder.encode(stddefor, "UTF-8") + "&" +
                                URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8") + "&" +
                                URLEncoder.encode("doc_date", "UTF-8") + "=" + URLEncoder.encode(doc_date, "UTF-8") + "&" +
                                URLEncoder.encode("doc_name", "UTF-8") + "=" + URLEncoder.encode(doc_name, "UTF-8")+ "&" +
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

    public void help(View view)
    {
        Intent intent = new Intent(SubAssemblyProsesActivity.this, UploadFileActivity.class);
        extras.putString("FROM", "HELP");
        intent.putExtras(extras);
        startActivity(intent);
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

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnSimpan:
                    String ms = edMaterialSpec.getText().toString();
                    String stdms = edStdMaterialSpec.getText().toString();
                    String wp = edWeldingProcess.getText().toString();
                    String stdwp = edStdWeldingProcess.getText().toString();
                    String mm = edManualMachine.getText().toString();
                    String stdmm = edStdManualMachine.getText().toString();
                    String pow = edPosition.getText().toString();
                    String stdpow = edStdPosition.getText().toString();
                    String fms = edFilterMetalSpec.getText().toString();
                    String stdfms = edStdFilterMetalSpec.getText().toString();
                    String fmc = edFilterMetalClass.getText().toString();
                    String stdfmc = edStdFilterMetalClass.getText().toString();
                    String wc = edWeldingCurrent.getText().toString();
                    String stdwc = edStdWeldingCurrent.getText().toString();
                    String polar = edPolarity.getText().toString();
                    String stdpolar = edStdPolarity.getText().toString();
                    String es = edElectrodeSize.getText().toString();
                    String stdes = edStdElectrodeSize.getText().toString();
                    String sot = edSpeed.getText().toString();
                    String stdsot = edStdSpeed.getText().toString();
                    String se = edSharpEdges.getText().toString();
                    String stdse = edStdSharpEdges.getText().toString();
                    String gap = edGap.getText().toString();
                    String stdgap = edStdGap.getText().toString();
                    String iw = edImperfectWelding.getText().toString();
                    String stdiw = edStdImperfectWelding.getText().toString();
                    String mw = edMissWeld.getText().toString();
                    String stdmw = edStdMissWeld.getText().toString();
                    String defor = edDeformation.getText().toString();
                    String stddefor = edStdDeformation.getText().toString();
                    String comment = edComment.getText().toString();
                    String doc_date = edTglDokumen.getText().toString();
                    String doc_name = edNamaDokumen.getText().toString();

                    new updateComponent().execute(idKomponen, ms, stdms, wp, stdwp, mm, stdmm, pow, stdpow, fms, stdfms, fmc, stdfmc, wc, stdwc,
                            polar, stdpolar, es, stdes, sot, stdsot, se, stdse, gap, stdgap, iw, stdiw, mw, stdmw, defor, stddefor, comment, doc_date, doc_name);
                    break;
                case R.id.btnBack:
                    intent = new Intent(SubAssemblyProsesActivity.this, SubAssemblyListActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

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
