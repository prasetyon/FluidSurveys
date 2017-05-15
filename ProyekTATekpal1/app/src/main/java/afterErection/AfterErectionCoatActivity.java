package afterErection;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal1.R;

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

import dokumen.UploadFileActivity;

public class AfterErectionCoatActivity extends AppCompatActivity {

    Button btnBack, btnSubmit;
    boolean doubleBackToExitPressedOnce = false;
    TextView tvNamaBlok, tvVisualInspection, tvApplicationMethod, tvMinDFT, tvMaxDFT;
    EditText edProsesBlok, edPosisiBlok, edStandard, edSteelTemperature, edRelativeHumidity, edStandardRelativeHumidity, edDewPoint, edStandard1stCAM, edDryBulb, edWetBulb,
            edPaintName, edStandardPaintName, edColourShade, edStandardColourShade, edBatchNo, edStandardBatchNo, edThinnerNo, edStandardThinnerNo,
            edVolumeSolid, edStandardVolumeSolid, edApplicationDate, edTime, edCuringTime, edStandardCuringTime, edPotLife, edBlockComment, edNamaDokumen,
            edArea, edNumberOfTestPoints, edMinDFT, edStandardMinDFT, edMaxDFT, edStandardMaxDFT, edAvgDFT, edInsDate;
    CheckBox cbStripeCoatCondition, cbNoDefect, cbNoMissCoat, cbSteelTemperature, cbRelativeHumidity, cbDewPoint, cbAirlessSpray, cbConventionalSprayer, cbRollBrush;
    ImageView ivStripeCoatCondition, ivNoDefect, ivNoMissCoat;
    EditText edWind, edPumpType, edPumpRatio;
    Spinner spinnerWeather, spinnerMixing;
    private String weather="", mixing="";
    Context Activity;
    private Bundle extras;
    private boolean created=false;
    private String create="";
    private String projectID;
    private String blockID;
    private String blockName;
    private String blockLocation;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private DatePickerDialog dpProyek;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_erection_coat);
        setTitle("AFTER ERECTION");
        Activity = this;

        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");
        blockID = extras.getString("BLOCK_ID");
        blockName = extras.getString("BLOCK_NAME");
        blockLocation = extras.getString("BLOCK_LOCATION");

        initiate();
        setEnable(role);

        tvVisualInspection.setText("Visual Inspection on "+ getProcess(from));
        tvApplicationMethod.setText(getProcess(from) +" Application Method");
        edProsesBlok.setText(from);

        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.weather_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerWeather.setAdapter(spinnerAdapter);
        spinnerWeather.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weather = spinnerWeather.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(this, R.array.mixing_list, android.R.layout.simple_spinner_item);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerMixing.setAdapter(spinnerAdapter1);
        spinnerMixing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mixing = spinnerMixing.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new getCoat().execute();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnUpdate:
                    String stripe_coat="", no_defect="", no_misscoat="";
                    if(cbStripeCoatCondition.isChecked()) stripe_coat = "1";
                    else stripe_coat = "0";
                    if(cbNoDefect.isChecked()) no_defect = "1";
                    else no_defect = "0";
                    if(cbNoMissCoat.isChecked()) no_misscoat = "1";
                    else no_misscoat = "0";
                    String img_stripe_coat="", img_no_defect="", img_no_misscoat="";

                    String standard = edStandard.getText().toString();
                    String area = edArea.getText().toString();
                    String test_point = edNumberOfTestPoints.getText().toString();
                    String min_dft = edMinDFT.getText().toString();
                    String std_min_dft = edStandardMinDFT.getText().toString();
                    String max_dft = edMaxDFT.getText().toString();
                    String std_max_dft = edStandardMaxDFT.getText().toString();
                    String avg_dft = edAvgDFT.getText().toString();

                    String stl_temp = edSteelTemperature.getText().toString();
                    String rel_humid = edRelativeHumidity.getText().toString();
                    String std_rel_humid = edStandardRelativeHumidity.getText().toString();
                    String dew_point = edDewPoint.getText().toString();

                    String airless="", conv_spray="", rollbrush="";
                    if(cbAirlessSpray.isChecked()) airless = "1";
                    else airless = "0";
                    if(cbConventionalSprayer.isChecked()) conv_spray = "1";
                    else conv_spray = "0";
                    if(cbRollBrush.isChecked()) rollbrush = "1";
                    else rollbrush = "0";
                    String std_spam = edStandard1stCAM.getText().toString();
                    String ins_date = edInsDate.getText().toString();
                    String dry_bulb = edDryBulb.getText().toString();
                    String wet_bulb = edWetBulb.getText().toString();
                    String paint_name = edPaintName.getText().toString();
                    String std_paint_name = edStandardPaintName.getText().toString();
                    String colorshade = edColourShade.getText().toString();
                    String std_colorshade = edStandardColourShade.getText().toString();
                    String batchno = edBatchNo.getText().toString();
                    String std_batchno = edStandardBatchNo.getText().toString();
                    String thinnerno = edThinnerNo.getText().toString();
                    String std_thinnerno = edStandardThinnerNo.getText().toString();
                    String volume_solid = edVolumeSolid.getText().toString();
                    String std_volume_solid = edStandardVolumeSolid.getText().toString();
                    String app_date = edApplicationDate.getText().toString();
                    String time = edTime.getText().toString();
                    String curingtime = edCuringTime.getText().toString();
                    String std_curingtime = edStandardCuringTime.getText().toString();
                    String pot_life = edPotLife.getText().toString();
                    String document = "";
                    String comment = edBlockComment.getText().toString();
                    String doc_name = edNamaDokumen.getText().toString();
                    String wind, pumptype, pumpratio;
                    wind = edWind.getText().toString();
                    pumptype = edPumpType.getText().toString();
                    pumpratio = edPumpRatio.getText().toString();

                    new updateCoat().execute(stripe_coat, no_defect, no_misscoat, img_stripe_coat, img_no_defect, img_no_misscoat, area, test_point,
                            min_dft, std_min_dft, max_dft, std_max_dft, avg_dft, stl_temp, rel_humid, std_rel_humid, dew_point, airless, conv_spray,
                            rollbrush, std_spam, dry_bulb, wet_bulb, paint_name, std_paint_name, colorshade, std_colorshade, batchno, std_batchno,
                            thinnerno, std_thinnerno, volume_solid, std_volume_solid, app_date, time, curingtime, std_curingtime, pot_life,
                            document, comment, doc_name, standard, ins_date, weather, wind, mixing, pumptype, pumpratio);
                    break;
                case R.id.btnBack:
                    intent = new Intent(AfterErectionCoatActivity.this, AfterErectionListActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.edInsDate:
                    showDate(v);
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
                    case R.id.edInsDate:
                        edInsDate.setText(dateFormatter.format(newDate.getTime()));
                        break;
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dpProyek.show();
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

    private void showCoat(String s)
    {
        JSONObject jsonObject;
        //Toast.makeText(AfterErectionCoatActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            edProsesBlok.setText(getProcess(from));
            tvNamaBlok.setText(blockName);
            edPosisiBlok.setText(blockLocation);

            edStandard.setText(jo.getString("standard"));
            edInsDate.setText(jo.getString("ins_date"));
            if(jo.getString("stripe_coat").equals("1")) {
                cbStripeCoatCondition.setChecked(true);
                ivStripeCoatCondition.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivStripeCoatCondition))
                        .execute(jo.getString("img_stripe_coat"));
            }
            else cbStripeCoatCondition.setChecked(false);
            if(jo.getString("no_defect").equals("1")){
                cbNoDefect.setChecked(true);
                ivNoDefect.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivNoDefect))
                        .execute(jo.getString("img_no_defect"));
            }
            else cbNoDefect.setChecked(false);
            if(jo.getString("no_misscoat").equals("1")){
                cbNoMissCoat.setChecked(true);
                ivNoMissCoat.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivNoMissCoat))
                        .execute(jo.getString("img_no_misscoat"));
            }
            else cbNoMissCoat.setChecked(false);

            if(jo.getString("area")!=null) edArea.setText(jo.getString("area"));
            if(jo.getString("test_point")!=null) edNumberOfTestPoints.setText(jo.getString("test_point"));
            if(jo.getString("min_dft")!=null) edMinDFT.setText(jo.getString("min_dft"));
            if(jo.getString("std_min_dft")!=null) edStandardMinDFT.setText(jo.getString("std_min_dft"));
            if(jo.getString("std_min_dft")!=null) tvMinDFT.setText(jo.getString("std_min_dft"));
            if(jo.getString("max_dft")!=null) edMaxDFT.setText(jo.getString("max_dft"));
            if(jo.getString("std_max_dft")!=null) edStandardMaxDFT.setText(jo.getString("std_max_dft"));
            if(jo.getString("std_max_dft")!=null) tvMaxDFT.setText(jo.getString("std_max_dft"));
            if(jo.getString("avg_dft")!=null) edAvgDFT.setText(jo.getString("avg_dft"));

            if(jo.getString("stl_temp")!=null){
                cbSteelTemperature.setChecked(true);
                edSteelTemperature.setText(jo.getString("stl_temp"));
            }
            if(jo.getString("rel_humid")!=null){
                cbRelativeHumidity.setChecked(true);
                edRelativeHumidity.setText(jo.getString("rel_humid"));
            }if(jo.getString("dew_point")!=null){
                cbDewPoint.setChecked(true);
                edDewPoint.setText(jo.getString("dew_point"));
            }
            if(jo.getString("std_rel_humid")!=null) edStandardRelativeHumidity.setText(jo.getString("std_rel_humid"));

            if(jo.getString("airless_spray").equals("1")) cbAirlessSpray.setChecked(true);
            else cbAirlessSpray.setChecked(false);
            if(jo.getString("conv_spray").equals("1")) cbConventionalSprayer.setChecked(true);
            else cbConventionalSprayer.setChecked(false);
            if(jo.getString("rollbrush").equals("1")) cbRollBrush.setChecked(true);
            else cbRollBrush.setChecked(false);
            edStandard1stCAM.setText(jo.getString("area"));

            if(jo.getString("dry_bulb")!=null) edDryBulb.setText(jo.getString("dry_bulb"));
            if(jo.getString("wet_bulb")!=null) edWetBulb.setText(jo.getString("wet_bulb"));
            if(jo.getString("paint_name")!=null) edPaintName.setText(jo.getString("paint_name"));
            if(jo.getString("std_paint_name")!=null) edStandardPaintName.setText(jo.getString("std_paint_name"));
            if(jo.getString("color_shade")!=null) edColourShade.setText(jo.getString("color_shade"));
            if(jo.getString("std_color_shade")!=null) edStandardColourShade.setText(jo.getString("std_color_shade"));
            if(jo.getString("batchno")!=null) edBatchNo.setText(jo.getString("batchno"));
            if(jo.getString("std_batchno")!=null) edStandardBatchNo.setText(jo.getString("std_batchno"));
            if(jo.getString("thinnerno")!=null) edThinnerNo.setText(jo.getString("thinnerno"));
            if(jo.getString("std_thinnerno")!=null) edStandardThinnerNo.setText(jo.getString("std_thinnerno"));
            if(jo.getString("vol_solid")!=null) edVolumeSolid.setText(jo.getString("vol_solid"));
            if(jo.getString("std_vol_solid")!=null) edStandardVolumeSolid.setText(jo.getString("std_vol_solid"));
            if(jo.getString("app_date")!=null) edApplicationDate.setText(jo.getString("app_date"));
            if(jo.getString("time")!=null) edTime.setText(jo.getString("time"));
            if(jo.getString("curing_time")!=null) edCuringTime.setText(jo.getString("curing_time"));
            if(jo.getString("std_curing_time")!=null) edStandardCuringTime.setText(jo.getString("std_curing_time"));
            if(jo.getString("pot_life")!=null) edPotLife.setText(jo.getString("pot_life"));

            if(jo.getString("doc1")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation1)).execute(jo.getString("doc1"));
            if(jo.getString("doc2")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation2)).execute(jo.getString("doc2"));
            if(jo.getString("doc3")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation3)).execute(jo.getString("doc3"));
            if(jo.getString("doc4")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation4)).execute(jo.getString("doc4"));
            if(jo.getString("doc5")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation5)).execute(jo.getString("doc5"));
            if(jo.getString("doc6")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation6)).execute(jo.getString("doc6"));

            if(jo.getString("comment")!=null) edBlockComment.setText(jo.getString("comment"));
            if(jo.getString("doc_name")!=null) edNamaDokumen.setText(jo.getString("doc_name"));

            if(jo.getString("weather").equals("Sunshine")) spinnerWeather.setSelection(0);
            else if(jo.getString("weather").equals("Cloudy")) spinnerWeather.setSelection(1);
            else if(jo.getString("weather").equals("Overcast")) spinnerWeather.setSelection(2);
            else if(jo.getString("weather").equals("Raining")) spinnerWeather.setSelection(3);
            edWind.setText(jo.getString("wind"));
            if(jo.getString("mixing").equals("Mechanical Agitator")) spinnerMixing.setSelection(0);
            else if(jo.getString("mixing").equals("Stick")) spinnerMixing.setSelection(1);
            edPumpType.setText(jo.getString("pump_type"));
            edPumpRatio.setText(jo.getString("pump_ratio"));

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
                        URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(blockID, "UTF-8") + "&" +
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

    class updateCoat extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updateCoat() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("updating coat detail...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            created = true;
            //Toast.makeText(Activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if (created && create.equals("1")) {
                Toast.makeText(AfterErectionCoatActivity.this, "You coat detail has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AfterErectionCoatActivity.this, AfterErectionListActivity.class);
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

            String stripe_coat = params[0];
            String no_defect = params[1];
            String no_misscoat = params[2];
            String img_stripe_coat = params[3];
            String img_no_defect = params[4];
            String img_no_misscoat = params[5];
            String area = params[6];
            String test_point = params[7];
            String min_dft = params[8];
            String std_min_dft = params[9];
            String max_dft = params[10];
            String std_max_dft = params[11];
            String avg_dft = params[12];
            String stl_temp = params[13];
            String rel_humid = params[14];
            String std_rel_humid = params[15];
            String dew_point = params[16];
            String airless = params[17];
            String conv_spray = params[18];
            String rollbrush = params[19];
            String std_spam = params[20];
            String dry_bulb = params[21];
            String wet_bulb = params[22];
            String paint_name = params[23];
            String std_paint_name = params[24];
            String colorshade = params[25];
            String std_colorshade = params[26];
            String batchno = params[27];
            String std_batchno = params[28];
            String thinnerno = params[29];
            String std_thinnerno = params[30];
            String volume_solid = params[31];
            String std_volume_solid = params[32];
            String app_date = params[33];
            String time = params[34];
            String curingtime = params[35];
            String std_curingtime = params[36];
            String pot_life = params[37];
            String document = params[38];
            String comment = params[39];
            String doc_name = params[40];
            String standard = params[41];
            String ins_date = params[42];
            String weather = params[43];
            String wind = params[44];
            String mixing = params[45];
            String pumptype = params[46];
            String pumpratio = params[47];

            String create_url = "http://mobile4day.com/ship-inspection/update_coat.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(blockID, "UTF-8") + "&" +
                                URLEncoder.encode("proses", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8")+ "&" +
                                URLEncoder.encode("stripe_coat", "UTF-8") + "=" + URLEncoder.encode(stripe_coat, "UTF-8")+ "&" +
                                URLEncoder.encode("img_stripe_coat", "UTF-8") + "=" + URLEncoder.encode(img_stripe_coat, "UTF-8")+ "&" +
                                URLEncoder.encode("no_defect", "UTF-8") + "=" + URLEncoder.encode(no_defect, "UTF-8")+ "&" +
                                URLEncoder.encode("img_no_defect", "UTF-8") + "=" + URLEncoder.encode(img_no_defect, "UTF-8")+ "&" +
                                URLEncoder.encode("no_misscoat", "UTF-8") + "=" + URLEncoder.encode(no_misscoat, "UTF-8")+ "&" +
                                URLEncoder.encode("img_no_misscoat", "UTF-8") + "=" + URLEncoder.encode(img_no_misscoat, "UTF-8")+ "&" +
                                URLEncoder.encode("area", "UTF-8") + "=" + URLEncoder.encode(area, "UTF-8")+ "&" +
                                URLEncoder.encode("test_point", "UTF-8") + "=" + URLEncoder.encode(test_point, "UTF-8")+ "&" +
                                URLEncoder.encode("min_dft", "UTF-8") + "=" + URLEncoder.encode(min_dft, "UTF-8")+ "&" +
                                URLEncoder.encode("std_min_dft", "UTF-8") + "=" + URLEncoder.encode(std_min_dft, "UTF-8")+ "&" +
                                URLEncoder.encode("max_dft", "UTF-8") + "=" + URLEncoder.encode(max_dft, "UTF-8")+ "&" +
                                URLEncoder.encode("std_max_dft", "UTF-8") + "=" + URLEncoder.encode(std_max_dft, "UTF-8")+ "&" +
                                URLEncoder.encode("avg_dft", "UTF-8") + "=" + URLEncoder.encode(avg_dft, "UTF-8")+ "&" +
                                URLEncoder.encode("steel_temperature", "UTF-8") + "=" + URLEncoder.encode(stl_temp, "UTF-8")+ "&" +
                                URLEncoder.encode("relative_humidity", "UTF-8") + "=" + URLEncoder.encode(rel_humid, "UTF-8")+ "&" +
                                URLEncoder.encode("std_relative_humidity", "UTF-8") + "=" + URLEncoder.encode(std_rel_humid, "UTF-8")+ "&" +
                                URLEncoder.encode("dew_point", "UTF-8") + "=" + URLEncoder.encode(dew_point, "UTF-8")+ "&" +
                                URLEncoder.encode("airless_spray", "UTF-8") + "=" + URLEncoder.encode(airless, "UTF-8")+ "&" +
                                URLEncoder.encode("conventional_sprayer", "UTF-8") + "=" + URLEncoder.encode(conv_spray, "UTF-8")+ "&" +
                                URLEncoder.encode("roll_brush", "UTF-8") + "=" + URLEncoder.encode(rollbrush, "UTF-8")+ "&" +
                                URLEncoder.encode("std_spam", "UTF-8") + "=" + URLEncoder.encode(std_spam, "UTF-8")+ "&" +
                                URLEncoder.encode("dry_bulb", "UTF-8") + "=" + URLEncoder.encode(dry_bulb, "UTF-8")+ "&" +
                                URLEncoder.encode("wet_bulb", "UTF-8") + "=" + URLEncoder.encode(wet_bulb, "UTF-8")+ "&" +
                                URLEncoder.encode("paint_name", "UTF-8") + "=" + URLEncoder.encode(paint_name, "UTF-8")+ "&" +
                                URLEncoder.encode("std_paint_name", "UTF-8") + "=" + URLEncoder.encode(std_paint_name, "UTF-8")+ "&" +
                                URLEncoder.encode("colorshade", "UTF-8") + "=" + URLEncoder.encode(colorshade, "UTF-8")+ "&" +
                                URLEncoder.encode("std_colorshade", "UTF-8") + "=" + URLEncoder.encode(std_colorshade, "UTF-8")+ "&" +
                                URLEncoder.encode("batchno", "UTF-8") + "=" + URLEncoder.encode(batchno, "UTF-8")+ "&" +
                                URLEncoder.encode("std_batchno", "UTF-8") + "=" + URLEncoder.encode(std_batchno, "UTF-8")+ "&" +
                                URLEncoder.encode("thinnerno", "UTF-8") + "=" + URLEncoder.encode(thinnerno, "UTF-8")+ "&" +
                                URLEncoder.encode("std_thinnerno", "UTF-8") + "=" + URLEncoder.encode(std_thinnerno, "UTF-8")+ "&" +
                                URLEncoder.encode("volume_solid", "UTF-8") + "=" + URLEncoder.encode(volume_solid, "UTF-8")+ "&" +
                                URLEncoder.encode("std_volume_solid", "UTF-8") + "=" + URLEncoder.encode(std_volume_solid, "UTF-8")+ "&" +
                                URLEncoder.encode("app_date", "UTF-8") + "=" + URLEncoder.encode(app_date, "UTF-8")+ "&" +
                                URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8")+ "&" +
                                URLEncoder.encode("curingtime", "UTF-8") + "=" + URLEncoder.encode(curingtime, "UTF-8")+ "&" +
                                URLEncoder.encode("std_curingtime", "UTF-8") + "=" + URLEncoder.encode(std_curingtime, "UTF-8")+ "&" +
                                URLEncoder.encode("pot_life", "UTF-8") + "=" + URLEncoder.encode(pot_life, "UTF-8")+ "&" +
                                URLEncoder.encode("document", "UTF-8") + "=" + URLEncoder.encode(document, "UTF-8")+ "&" +
                                URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8")+ "&" +
                                URLEncoder.encode("doc_name", "UTF-8") + "=" + URLEncoder.encode(doc_name, "UTF-8")+ "&" +
                                URLEncoder.encode("standard", "UTF-8") + "=" + URLEncoder.encode(standard, "UTF-8")+ "&" +
                                URLEncoder.encode("ins_date", "UTF-8") + "=" + URLEncoder.encode(ins_date, "UTF-8") + "&" +
                                URLEncoder.encode("weather", "UTF-8") + "=" + URLEncoder.encode(weather, "UTF-8") + "&" +
                                URLEncoder.encode("wind", "UTF-8") + "=" + URLEncoder.encode(wind, "UTF-8") + "&" +
                                URLEncoder.encode("mixing", "UTF-8") + "=" + URLEncoder.encode(mixing, "UTF-8") + "&" +
                                URLEncoder.encode("pumptype", "UTF-8") + "=" + URLEncoder.encode(pumptype, "UTF-8") + "&" +
                                URLEncoder.encode("pumpratio", "UTF-8") + "=" + URLEncoder.encode(pumpratio, "UTF-8");
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

    private String getProcess(String from)
    {
        if(from.equals("1C1SC")) return "1st Coat and 1st Stripe Coat";
        else if(from.equals("1SC2SC")) return "1st Stripe Coat and 2nd Stripe Coat";
        else if(from.equals("2SC2C")) return "2nd Stripe Coat and 2nd Coat";
        else if(from.equals("2C3C")) return "2nd Coat and 3rd Coat";
        else if(from.equals("3C4C")) return "3rd Coat and 4th Coat";
        else if(from.equals("4C5C")) return "4th Coat and 5th Coat";
        else return "";
    }

    private void setEnable(String role)
    {
        tvNamaBlok.setEnabled(false);
        edProsesBlok.setEnabled(false);
        edPosisiBlok.setEnabled(false);
        if(role.equals("admin")){
            spinnerMixing.setEnabled(false);
            edWind.setEnabled(false);
            spinnerWeather.setEnabled(false);
            edPumpType.setEnabled(false);
            edPumpRatio.setEnabled(false);
            edSteelTemperature.setEnabled(false);
            edRelativeHumidity.setEnabled(false);
            edDewPoint.setEnabled(false);
            edDryBulb.setEnabled(false);
            edWetBulb.setEnabled(false);
            edPaintName.setEnabled(false);
            edColourShade.setEnabled(false);
            edBatchNo.setEnabled(false);
            edThinnerNo.setEnabled(false);
            edVolumeSolid.setEnabled(false);
            edApplicationDate.setEnabled(false);
            edTime.setEnabled(false);
            edCuringTime.setEnabled(false);
            edPotLife.setEnabled(false);
            edArea.setEnabled(false);
            edNumberOfTestPoints.setEnabled(false);
            edMinDFT.setEnabled(false);
            edMaxDFT.setEnabled(false);
            edAvgDFT.setEnabled(false);
            cbStripeCoatCondition.setEnabled(false);
            cbNoDefect.setEnabled(false);
            cbNoMissCoat.setEnabled(false);
            cbSteelTemperature.setEnabled(false);
            cbRelativeHumidity.setEnabled(false);
            cbDewPoint.setEnabled(false);
            cbAirlessSpray.setEnabled(false);
            cbConventionalSprayer.setEnabled(false);
            cbRollBrush.setEnabled(false);
        }
        else if(role.equals("user")){
            edStandard.setEnabled(false);
            edStandardRelativeHumidity.setEnabled(false);
            edStandard1stCAM.setEnabled(false);
            edStandardPaintName.setEnabled(false);
            edStandardColourShade.setEnabled(false);
            edStandardBatchNo.setEnabled(false);
            edStandardThinnerNo.setEnabled(false);
            edStandardVolumeSolid.setEnabled(false);
            edStandardCuringTime.setEnabled(false);
            edStandardMaxDFT.setEnabled(false);
            edStandardMinDFT.setEnabled(false);
        }
        else if(role.equals("userNo"))
        {
            spinnerWeather.setEnabled(false);
            edWind.setEnabled(false);
            spinnerMixing.setEnabled(false);
            edPumpType.setEnabled(false);
            edPumpRatio.setEnabled(false);
            edSteelTemperature.setEnabled(false);
            edRelativeHumidity.setEnabled(false);
            edDewPoint.setEnabled(false);
            edDryBulb.setEnabled(false);
            edWetBulb.setEnabled(false);
            edPaintName.setEnabled(false);
            edColourShade.setEnabled(false);
            edBatchNo.setEnabled(false);
            edThinnerNo.setEnabled(false);
            edVolumeSolid.setEnabled(false);
            edApplicationDate.setEnabled(false);
            edTime.setEnabled(false);
            edCuringTime.setEnabled(false);
            edPotLife.setEnabled(false);
            edArea.setEnabled(false);
            edNumberOfTestPoints.setEnabled(false);
            edMinDFT.setEnabled(false);
            edMaxDFT.setEnabled(false);
            edAvgDFT.setEnabled(false);
            cbStripeCoatCondition.setEnabled(false);
            cbNoDefect.setEnabled(false);
            cbNoMissCoat.setEnabled(false);
            cbSteelTemperature.setEnabled(false);
            cbRelativeHumidity.setEnabled(false);
            cbDewPoint.setEnabled(false);
            cbAirlessSpray.setEnabled(false);
            cbConventionalSprayer.setEnabled(false);
            cbRollBrush.setEnabled(false);
            edStandard.setEnabled(false);
            edStandardRelativeHumidity.setEnabled(false);
            edStandard1stCAM.setEnabled(false);
            edStandardPaintName.setEnabled(false);
            edStandardColourShade.setEnabled(false);
            edStandardBatchNo.setEnabled(false);
            edStandardThinnerNo.setEnabled(false);
            edStandardVolumeSolid.setEnabled(false);
            edStandardCuringTime.setEnabled(false);
            edStandardMaxDFT.setEnabled(false);
            edStandardMinDFT.setEnabled(false);
        }
    }

    private void initiate()
    {
        ivStripeCoatCondition = (ImageView) findViewById(R.id.ivStripeCoatCondition);
        ivStripeCoatCondition.setVisibility(View.GONE);
        ivNoDefect = (ImageView) findViewById(R.id.ivNoDefect);
        ivNoDefect.setVisibility(View.GONE);
        ivNoMissCoat = (ImageView) findViewById(R.id.ivNoMissCoat);
        ivNoMissCoat.setVisibility(View.GONE);

        tvNamaBlok = (TextView) findViewById(R.id.tvNamaBlok);
        tvVisualInspection = (TextView) findViewById(R.id.tvVisualInspection);
        tvApplicationMethod = (TextView) findViewById(R.id.tvApplicationMethod);
        tvMinDFT = (TextView) findViewById(R.id.tvMinDFT);
        tvMaxDFT = (TextView) findViewById(R.id.tvMaxDFT);
        edProsesBlok = (EditText) findViewById(R.id.edProsesBlok);
        edPosisiBlok = (EditText) findViewById(R.id.edPosisiBlok);
        edSteelTemperature = (EditText) findViewById(R.id.edSteelTemperature);
        edRelativeHumidity = (EditText) findViewById(R.id.edRelativeHumidity);
        edStandardRelativeHumidity = (EditText) findViewById(R.id.edStandardRelativeHumidity);
        edDewPoint = (EditText) findViewById(R.id.edDewPoint);
        edPotLife = (EditText) findViewById(R.id.edPotLife);
        edStandard1stCAM = (EditText) findViewById(R.id.edStandard1stCAM);
        edDryBulb = (EditText) findViewById(R.id.edDryBulb);
        edWetBulb = (EditText) findViewById(R.id.edWetBulb);
        edPaintName = (EditText) findViewById(R.id.edPaintName);
        edStandardPaintName = (EditText) findViewById(R.id.edStandardPaintName);
        edColourShade = (EditText) findViewById(R.id.edColourShade);
        edStandardColourShade = (EditText) findViewById(R.id.edStandardColourShade);
        edBatchNo = (EditText) findViewById(R.id.edBatchNo);
        edStandardBatchNo = (EditText) findViewById(R.id.edStandardBatchNo);
        edThinnerNo = (EditText) findViewById(R.id.edThinnerNo);
        edStandardThinnerNo = (EditText) findViewById(R.id.edStandardThinnerNo);
        edVolumeSolid = (EditText) findViewById(R.id.edVolumeSolid);
        edStandardVolumeSolid = (EditText) findViewById(R.id.edStandardVolumeSolid);
        edApplicationDate = (EditText) findViewById(R.id.edApplicationDate);
        edTime = (EditText) findViewById(R.id.edTime);
        edCuringTime = (EditText) findViewById(R.id.edCuringTime);
        edStandardCuringTime = (EditText) findViewById(R.id.edStandardCuringTime);
        edBlockComment = (EditText) findViewById(R.id.edBlockComment);
        edNamaDokumen = (EditText) findViewById(R.id.edNamaDokumen);
        edInsDate = (EditText) findViewById(R.id.edInsDate);

        spinnerWeather = (Spinner) findViewById(R.id.spinnerWeather);
        edWind = (EditText) findViewById(R.id.edWind);
        spinnerMixing = (Spinner) findViewById(R.id.spinnerMixing);
        edPumpType = (EditText) findViewById(R.id.edPumpType);
        edPumpRatio = (EditText) findViewById(R.id.edPumpRatio);

        edStandard = (EditText) findViewById(R.id.edStandard);
        edArea = (EditText) findViewById(R.id.edArea);
        edNumberOfTestPoints = (EditText) findViewById(R.id.edNumberOfTestPoints);
        edMinDFT = (EditText) findViewById(R.id.edMinDFT);
        edStandardMinDFT = (EditText) findViewById(R.id.edStandardMinDFT);
        edMaxDFT = (EditText) findViewById(R.id.edMaxDFT);
        edStandardMaxDFT = (EditText) findViewById(R.id.edStandardMaxDFT);
        edAvgDFT = (EditText) findViewById(R.id.edAvgDFT);

        cbStripeCoatCondition = (CheckBox) findViewById(R.id.cbStripeCoatCondition);
        cbNoDefect = (CheckBox) findViewById(R.id.cbNoDefect);
        cbNoMissCoat = (CheckBox) findViewById(R.id.cbNoMissCoat);
        cbSteelTemperature = (CheckBox) findViewById(R.id.cbSteelTemperature);
        cbRelativeHumidity = (CheckBox) findViewById(R.id.cbRelativeHumidity);
        cbDewPoint = (CheckBox) findViewById(R.id.cbDewPoint);
        cbAirlessSpray = (CheckBox) findViewById(R.id.cbAirlessSpray);
        cbConventionalSprayer = (CheckBox) findViewById(R.id.cbConventionalSprayer);
        cbRollBrush = (CheckBox) findViewById(R.id.cbRollBrush);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnUpdate);

        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);
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

    public void help(View view)
    {
        Intent intent = new Intent(AfterErectionCoatActivity.this, UploadFileActivity.class);
        extras.putString("FROM", "HELP");
        intent.putExtras(extras);
        startActivity(intent);
    }
}
