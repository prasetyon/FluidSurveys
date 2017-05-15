package rawMaterial;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class RawMaterialCheckActivity extends AppCompatActivity {

    Button btnBack, btnSubmit, btnCountResult;
    TextView tvKomponen;
    EditText edProses, edIDKomponen, edStandardRelativeHumidity, edSteelTemperature, edRelativeHumidity, edDewPoint, edSurfaceContaminant,
            edAbrasiveSize, edGrade, edStandardAbrasiveSize, edStandardGrade, edTimeDate, edStandardTimeDate, edRoughness, edStandardMechanicalTool,
            edStandardSSF, edResultSSF, edInputC, edInputV, edInputA, edInputY1, edInputY2, edStandardSPAM, edDryBulb, edWetBulb, edInsDate,
            edPaintName, edStandardPaintName, edColourShade, edStandardColourShade, edBatchNo, edStandardBatchNo, edThinnerNo, edStandardThinnerNo,
            edVolumeSolid, edStandardVolumeSolid, edApplicationDate, edTime, edCuringTime, edStandardCuringTime, edPotLife, edStandardPotLife, edBlockComment, edNamaDokumen;
    CheckBox cbSteelTemperature, cbRelativeHumidity, cbDewPoint, cbSurfaceContaminant, cbMechanicalTool, cbBlasting, cbSt2, cbSt3, cbSa1, cbSa2,
            cbSa212, cbSa3, cbRollBrush, cbConventionalSprayer, cbAirlessSpray;
    EditText edWind, edPumpType, edPumpRatio;
    Context Activity;
    Spinner spinnerTypeOfAbrasive, spinnerRustGrade, spinnerCleanliness, spinnerWeather, spinnerMixing;
    private boolean created;
    private String create;
    private Bundle extras;
    private String projectID;
    private String komponenID;
    private String komponenName;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String abrasive="", rust_grade="";
    private String cleanliness="", weather="", mixing="";
    private DatePickerDialog dpProyek;
    private SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material_check);
        setTitle("RAW MATERIALS");

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");
        komponenID = extras.getString("COMPONENT_ID");
        komponenName = extras.getString("COMPONENT_NAME");

        initiate();
        setEnable(role);

        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.abrasive_type_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerTypeOfAbrasive.setAdapter(spinnerAdapter);
        spinnerTypeOfAbrasive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                abrasive = spinnerTypeOfAbrasive.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(this, R.array.rust_grade_list, android.R.layout.simple_spinner_item);
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerRustGrade.setAdapter(spinnerAdapter1);
        spinnerRustGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rust_grade = spinnerRustGrade.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(this, R.array.cleanliness_list, android.R.layout.simple_spinner_item);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCleanliness.setAdapter(spinnerAdapter2);
        spinnerCleanliness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cleanliness = spinnerCleanliness.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<CharSequence> spinnerAdapter3 = ArrayAdapter.createFromResource(this, R.array.weather_list, android.R.layout.simple_spinner_item);
        spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerWeather.setAdapter(spinnerAdapter3);
        spinnerWeather.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weather = spinnerWeather.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<CharSequence> spinnerAdapter4 = ArrayAdapter.createFromResource(this, R.array.mixing_list, android.R.layout.simple_spinner_item);
        spinnerAdapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerMixing.setAdapter(spinnerAdapter4);
        spinnerMixing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mixing = spinnerMixing.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new getPspsp().execute();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnUpdate:
                    String stl_temp = edSteelTemperature.getText().toString();
                    String rel_humid = edRelativeHumidity.getText().toString();
                    String std_rel_humid = edStandardRelativeHumidity.getText().toString();
                    String dew_point = edDewPoint.getText().toString();
                    String surface_cont = edSurfaceContaminant.getText().toString();
                    String mechanical_tool, st2, st3, blasting, sa1, sa2, sa212, sa3, airless, conv_spray, rollbrush;
                    if(cbMechanicalTool.isChecked()) mechanical_tool = "1";
                    else mechanical_tool = "0";
                    if(cbSt2.isChecked()) st2 = "1";
                    else st2 = "0";
                    if(cbSt3.isChecked()) st3 = "1";
                    else st3 = "0";
                    String std_mechanical = edStandardMechanicalTool.getText().toString();
                    if(cbBlasting.isChecked()) blasting = "1";
                    else blasting = "0";
                    if(cbSa1.isChecked()) sa1 = "1";
                    else sa1 = "0";
                    if(cbSa2.isChecked()) sa2 = "1";
                    else sa2 = "0";
                    if(cbSa212.isChecked()) sa212 = "1";
                    else sa212 = "0";
                    if(cbSa3.isChecked()) sa3 = "1";
                    else sa3 = "0";
                    String abrasive_size = edAbrasiveSize.getText().toString();
                    String std_abrasive_size = edStandardAbrasiveSize.getText().toString();
                    String grade = edGrade.getText().toString();
                    String std_grade = edStandardGrade.getText().toString();
                    String timedate = edTimeDate.getText().toString();
                    String std_timedate = edStandardTimeDate.getText().toString();
                    String roughness = edRoughness.getText().toString();
                    String C = edInputC.getText().toString();
                    String V = edInputV.getText().toString();
                    String A = edInputA.getText().toString();
                    String y1 = edInputY1.getText().toString();
                    String y2 = edInputY2.getText().toString();
                    String std_ssf = edStandardSSF.getText().toString();
                    String res_ssf = edResultSSF.getText().toString();
                    if(cbAirlessSpray.isChecked()) airless = "1";
                    else airless = "0";
                    if(cbConventionalSprayer.isChecked()) conv_spray = "1";
                    else conv_spray = "0";
                    if(cbRollBrush.isChecked()) rollbrush = "1";
                    else rollbrush = "0";
                    String std_spam = edStandardSPAM.getText().toString();
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
                    String std_pot_life = edStandardPotLife.getText().toString();
                    String comment = edBlockComment.getText().toString();
                    String doc_name = edNamaDokumen.getText().toString();
                    String ins_date = edInsDate.getText().toString();
                    String wind, pumptype, pumpratio;
                    wind = edWind.getText().toString();
                    pumptype = edPumpType.getText().toString();
                    pumpratio = edPumpRatio.getText().toString();

                    if(role.equals("user") || role.equals("userNo"))
                        new updatePspspUser().execute(komponenID, stl_temp, rel_humid, dew_point, surface_cont, mechanical_tool, st2, st3, blasting,
                                sa1, sa2, sa212, sa3, abrasive, abrasive_size, grade, timedate, roughness,
                                rust_grade, C, V, A, y1, y2, res_ssf, airless, conv_spray, rollbrush, dry_bulb, wet_bulb, paint_name,
                                colorshade, batchno, thinnerno, volume_solid, app_date, time, curingtime, pot_life, comment, doc_name, ins_date,
                                cleanliness, weather, wind, mixing, pumptype, pumpratio);
                    else if(role.equals("admin"))
                        new updatePspspAdmin().execute(komponenID, std_rel_humid, std_mechanical, std_abrasive_size, std_grade, std_timedate, std_ssf, std_spam,
                                std_paint_name, std_colorshade, std_batchno, std_thinnerno, std_volume_solid, std_curingtime, std_pot_life);
                    break;
                case R.id.btnBack:
                    intent = new Intent(RawMaterialCheckActivity.this, RawMaterialListActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.edInsDate:
                case R.id.edApplicationDate:
                    showDate(v);
                    break;
                case R.id.btnCountResult:
                    float cc = Float.parseFloat(edInputC.getText().toString());
                    float vv = Float.parseFloat(edInputV.getText().toString());
                    float aa = Float.parseFloat(edInputA.getText().toString());
                    float yy1 = Float.parseFloat(edInputY1.getText().toString());
                    float yy2 = Float.parseFloat(edInputY2.getText().toString());
                    float result = cc*vv*(yy2-yy1)/aa;
                    edResultSSF.setText(Float.toString(result));
                    break;
            }
        }
    };

    CheckBox.OnClickListener checked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.cbSteelTemperature:
                    if(((CheckBox)v).isChecked()){
                        edSteelTemperature.setEnabled(true);
                    }
                    else edSteelTemperature.setEnabled(false);
                    break;
                case R.id.cbRelativeHumidity:
                    if(((CheckBox)v).isChecked()){
                        edRelativeHumidity.setEnabled(true);
                    }
                    else edRelativeHumidity.setEnabled(false);
                    break;
                case R.id.cbDewPoint:
                    if(((CheckBox)v).isChecked()){
                        edDewPoint.setEnabled(true);
                    }
                    else edDewPoint.setEnabled(false);
                    break;
                case R.id.cbSurfaceContaminant:
                    if(((CheckBox)v).isChecked()){
                        edSurfaceContaminant.setEnabled(true);
                    }
                    else edSurfaceContaminant.setEnabled(false);
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
                    case R.id.edApplicationDate:
                        edApplicationDate.setText(dateFormatter.format(newDate.getTime()));
                        break;
                    case R.id.edInsDate:
                        edInsDate.setText(dateFormatter.format(newDate.getTime()));
                        break;
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dpProyek.show();
    }

    private void initiate()
    {
        tvKomponen = (TextView) findViewById(R.id.tvNamaBlok);
        edInsDate = (EditText) findViewById(R.id.edInsDate);
        edSurfaceContaminant = (EditText) findViewById(R.id.edSurfaceContaminant);
        edAbrasiveSize = (EditText) findViewById(R.id.edAbrasiveSize);
        edGrade = (EditText) findViewById(R.id.edGrade);
        edStandardAbrasiveSize = (EditText) findViewById(R.id.edStandardAbrasiveSize);
        edStandardGrade = (EditText) findViewById(R.id.edStandardGrade);
        edTimeDate = (EditText) findViewById(R.id.edTimeDate);
        edStandardTimeDate = (EditText) findViewById(R.id.edStandardTimeDate);
        edRoughness = (EditText) findViewById(R.id.edRoughness);
        edStandardMechanicalTool = (EditText) findViewById(R.id.edStandardMechanicalTool);
        edStandardSSF = (EditText) findViewById(R.id.edStandardSSF);
        edResultSSF = (EditText) findViewById(R.id.edResultSSF);
        edInputC = (EditText) findViewById(R.id.edInputC);
        edInputV = (EditText) findViewById(R.id.edInputV);
        edInputA = (EditText) findViewById(R.id.edInputA);
        edInputY1 = (EditText) findViewById(R.id.edInputY1);
        edInputY2 = (EditText) findViewById(R.id.edInputY2);
        edProses = (EditText) findViewById(R.id.edProses);
        edIDKomponen = (EditText) findViewById(R.id.edIDKomponen);
        edSteelTemperature = (EditText) findViewById(R.id.edSteelTemperature);
        edRelativeHumidity = (EditText) findViewById(R.id.edRelativeHumidity);
        edStandardRelativeHumidity = (EditText) findViewById(R.id.edStandardRelativeHumidity);
        edDewPoint = (EditText) findViewById(R.id.edDewPoint);
        edStandardSPAM = (EditText) findViewById(R.id.edStandardSPAM);
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
        edPotLife = (EditText) findViewById(R.id.edPotLife);
        edStandardPotLife = (EditText) findViewById(R.id.edStandardPotLife);
        edBlockComment = (EditText) findViewById(R.id.edBlockComment);
        edNamaDokumen = (EditText) findViewById(R.id.edNamaDokumen);
        cbSteelTemperature = (CheckBox) findViewById(R.id.cbSteelTemperature);
        cbRelativeHumidity = (CheckBox) findViewById(R.id.cbRelativeHumidity);
        cbDewPoint = (CheckBox) findViewById(R.id.cbDewPoint);
        cbAirlessSpray = (CheckBox) findViewById(R.id.cbAirlessSpray);
        cbConventionalSprayer = (CheckBox) findViewById(R.id.cbConventionalSprayer);
        cbRollBrush = (CheckBox) findViewById(R.id.cbRollBrush);
        cbSa3 = (CheckBox) findViewById(R.id.cbSa3);
        cbSa212 = (CheckBox) findViewById(R.id.cbSa212);
        cbSa2 = (CheckBox) findViewById(R.id.cbSa2);
        cbSa1 = (CheckBox) findViewById(R.id.cbSa1);
        cbSt3 = (CheckBox) findViewById(R.id.cbSt3);
        cbSt2 = (CheckBox) findViewById(R.id.cbSt2);
        cbBlasting = (CheckBox) findViewById(R.id.cbBlasting);
        cbMechanicalTool = (CheckBox) findViewById(R.id.cbMechanicalTool);
        cbSurfaceContaminant = (CheckBox) findViewById(R.id.cbSurfaceContaminant);

        spinnerCleanliness = (Spinner) findViewById(R.id.spinnerCleanliness);
        spinnerWeather = (Spinner) findViewById(R.id.spinnerWeather);
        edWind = (EditText) findViewById(R.id.edWind);
        spinnerMixing = (Spinner) findViewById(R.id.spinnerMixing);
        edPumpType = (EditText) findViewById(R.id.edPumpType);
        edPumpRatio = (EditText) findViewById(R.id.edPumpRatio);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnUpdate);
        btnCountResult = (Button) findViewById(R.id.btnCountResult);

        cbSteelTemperature.setOnClickListener(checked);
        cbRelativeHumidity.setOnClickListener(checked);
        cbDewPoint.setOnClickListener(checked);
        cbSurfaceContaminant.setOnClickListener(checked);
        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);
        btnCountResult.setOnClickListener(activity);
        edApplicationDate.setOnClickListener(activity);
        edInsDate.setOnClickListener(activity);

        tvKomponen.setText(komponenName);
        edIDKomponen.setText(komponenID);
        edProses.setText("Primary Surface Preparation to Shop Primer");
        spinnerTypeOfAbrasive = (Spinner) findViewById(R.id.spinnerTypeOfAbrasive);
        spinnerRustGrade = (Spinner) findViewById(R.id.spinnerRustGrade);
    }

    private void setEnable(String role)
    {
        edProses.setEnabled(false);
        edIDKomponen.setEnabled(false);
        edSteelTemperature.setEnabled(false);
        edRelativeHumidity.setEnabled(false);
        edSurfaceContaminant.setEnabled(false);
        edDewPoint.setEnabled(false);
        edResultSSF.setEnabled(false);

        if(role.equals("user"))
        {
            edStandardSPAM.setEnabled(false);
            edStandardRelativeHumidity.setEnabled(false);
            edStandardMechanicalTool.setEnabled(false);
            edStandardAbrasiveSize.setEnabled(false);
            edStandardGrade.setEnabled(false);
            edStandardTimeDate.setEnabled(false);
            edStandardSSF.setEnabled(false);
            edStandardPaintName.setEnabled(false);
            edStandardColourShade.setEnabled(false);
            edStandardBatchNo.setEnabled(false);
            edStandardThinnerNo.setEnabled(false);
            edStandardVolumeSolid.setEnabled(false);
            edStandardCuringTime.setEnabled(false);
            edStandardPotLife.setEnabled(false);
        }
        else if (role.equals("admin"))
        {
            edNamaDokumen.setEnabled(false);
            edInsDate.setEnabled(false);
            cbSteelTemperature.setEnabled(false);
            cbRelativeHumidity.setEnabled(false);
            cbDewPoint.setEnabled(false);
            cbSurfaceContaminant.setEnabled(false);
            cbMechanicalTool.setEnabled(false);
            cbBlasting.setEnabled(false);
            cbSt2.setEnabled(false);
            cbSt3.setEnabled(false);
            cbSa1.setEnabled(false);
            cbSa2.setEnabled(false);
            cbSa212.setEnabled(false);
            cbSa3.setEnabled(false);
            cbRollBrush.setEnabled(false);
            cbConventionalSprayer.setEnabled(false);
            cbAirlessSpray.setEnabled(false);
            edAbrasiveSize.setEnabled(false);
            edGrade.setEnabled(false);
            edTimeDate.setEnabled(false);
            edRoughness.setEnabled(false);
            edInputA.setEnabled(false);
            edInputC.setEnabled(false);
            edInputV.setEnabled(false);
            edInputY1.setEnabled(false);
            edInputY2.setEnabled(false);
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
            edNamaDokumen.setEnabled(false);
            spinnerRustGrade.setEnabled(false);
            spinnerTypeOfAbrasive.setEnabled(false);
            btnCountResult.setEnabled(false);
            spinnerCleanliness.setEnabled(false);
            spinnerWeather.setEnabled(false);
            edWind.setEnabled(false);
            spinnerMixing.setEnabled(false);
            edPumpType.setEnabled(false);
            edPumpRatio.setEnabled(false);
        }
        else if(role.equals("userNo"))
        {
            edNamaDokumen.setEnabled(false);
            edInsDate.setEnabled(false);
            cbSteelTemperature.setEnabled(false);
            cbRelativeHumidity.setEnabled(false);
            cbDewPoint.setEnabled(false);
            cbSurfaceContaminant.setEnabled(false);
            cbMechanicalTool.setEnabled(false);
            cbBlasting.setEnabled(false);
            cbSt2.setEnabled(false);
            cbSt3.setEnabled(false);
            cbSa1.setEnabled(false);
            cbSa2.setEnabled(false);
            cbSa212.setEnabled(false);
            cbSa3.setEnabled(false);
            cbRollBrush.setEnabled(false);
            cbConventionalSprayer.setEnabled(false);
            cbAirlessSpray.setEnabled(false);
            edAbrasiveSize.setEnabled(false);
            edGrade.setEnabled(false);
            edTimeDate.setEnabled(false);
            edRoughness.setEnabled(false);
            edInputA.setEnabled(false);
            edInputC.setEnabled(false);
            edInputV.setEnabled(false);
            edInputY1.setEnabled(false);
            edInputY2.setEnabled(false);
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
            edNamaDokumen.setEnabled(false);
            spinnerRustGrade.setEnabled(false);
            spinnerTypeOfAbrasive.setEnabled(false);
            btnCountResult.setEnabled(false);
            edStandardSPAM.setEnabled(false);
            edStandardRelativeHumidity.setEnabled(false);
            edStandardMechanicalTool.setEnabled(false);
            edStandardAbrasiveSize.setEnabled(false);
            edStandardGrade.setEnabled(false);
            edStandardTimeDate.setEnabled(false);
            edStandardSSF.setEnabled(false);
            edStandardPaintName.setEnabled(false);
            edStandardColourShade.setEnabled(false);
            edStandardBatchNo.setEnabled(false);
            edStandardThinnerNo.setEnabled(false);
            edStandardVolumeSolid.setEnabled(false);
            edStandardCuringTime.setEnabled(false);
            edStandardPotLife.setEnabled(false);
            spinnerCleanliness.setEnabled(false);
            spinnerWeather.setEnabled(false);
            edWind.setEnabled(false);
            spinnerMixing.setEnabled(false);
            edPumpType.setEnabled(false);
            edPumpRatio.setEnabled(false);
        }
    }

    class updatePspspAdmin extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updatePspspAdmin() {
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
                Toast.makeText(RawMaterialCheckActivity.this, "You component detail has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RawMaterialCheckActivity.this, RawMaterialListActivity.class);
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
            String id_komponen = params[0];
            String standard_relative_humidity = params[1];
            String std_mechanical_tool = params[2];
            String std_abrasive_size = params[3];
            String std_grade = params[4];
            String std_timedate = params[5];
            String std_ssf = params[6];
            String std_spam = params[7];
            String std_paint_name = params[8];
            String std_colorshade = params[9];
            String std_batchno = params[10];
            String std_thinnerno = params[11];
            String std_volume_solid = params[12];
            String std_curingtime = params[13];
            String std_pot_life = params[14];

            String create_url = "http://mobile4day.com/ship-inspection/update_pspsp_admin.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_komponen", "UTF-8") + "=" + URLEncoder.encode(id_komponen, "UTF-8") + "&" +
                                URLEncoder.encode("standard_relative_humidity", "UTF-8") + "=" + URLEncoder.encode(standard_relative_humidity, "UTF-8") + "&" +
                                URLEncoder.encode("std_mechanical_tool", "UTF-8") + "=" + URLEncoder.encode(std_mechanical_tool, "UTF-8") + "&" +
                                URLEncoder.encode("std_abrasive_size", "UTF-8") + "=" + URLEncoder.encode(std_abrasive_size, "UTF-8") + "&" +
                                URLEncoder.encode("std_grade", "UTF-8") + "=" + URLEncoder.encode(std_grade, "UTF-8") + "&" +
                                URLEncoder.encode("std_timedate", "UTF-8") + "=" + URLEncoder.encode(std_timedate, "UTF-8")+ "&" +
                                URLEncoder.encode("std_ssf ", "UTF-8") + "=" + URLEncoder.encode(std_ssf , "UTF-8") + "&" +
                                URLEncoder.encode("std_spam ", "UTF-8") + "=" + URLEncoder.encode(std_spam , "UTF-8") + "&" +
                                URLEncoder.encode("std_paint_name", "UTF-8") + "=" + URLEncoder.encode(std_paint_name, "UTF-8") + "&" +
                                URLEncoder.encode("std_colorshade", "UTF-8") + "=" + URLEncoder.encode(std_colorshade, "UTF-8")+ "&" +
                                URLEncoder.encode("std_batchno", "UTF-8") + "=" + URLEncoder.encode(std_batchno, "UTF-8") + "&" +
                                URLEncoder.encode("std_thinnerno", "UTF-8") + "=" + URLEncoder.encode(std_thinnerno, "UTF-8") + "&" +
                                URLEncoder.encode("std_volume_solid", "UTF-8") + "=" + URLEncoder.encode(std_volume_solid, "UTF-8") + "&" +
                                URLEncoder.encode("std_curingtime", "UTF-8") + "=" + URLEncoder.encode(std_curingtime, "UTF-8")+ "&" +
                                URLEncoder.encode("std_pot_life", "UTF-8") + "=" + URLEncoder.encode(std_pot_life, "UTF-8");
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

    class updatePspspUser extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updatePspspUser() {
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
                Toast.makeText(RawMaterialCheckActivity.this, "You component detail has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RawMaterialCheckActivity.this, RawMaterialListActivity.class);
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
            String id_komponen = params[0];
            String steel_temperature = params[1];
            String relative_humidity = params[2];
            String dew_point = params[3];
            String surface_contaminant = params[4];
            String mechanical_tool = params[5];
            String st2 = params[6];
            String st3 = params[7];
            String blasting = params[8];
            String sa1 = params[9];
            String sa2 = params[10];
            String sa212 = params[11];
            String sa3 = params[12];
            String abrasive = params[13];
            String abrasive_size = params[14];
            String grade = params[15];
            String timedate = params[16];
            String roughness = params[17];
            String rust_grade = params[18];
            String c = params[19];
            String v = params[20];
            String a = params[21];
            String y1 = params[22];
            String y2 = params[23];
            String result_ssf = params[24];
            String airless_spray = params[25];
            String conventional_sprayer = params[26];
            String roll_brush = params[27];
            String dry_bulb = params[28];
            String wet_bulb = params[29];
            String paint_name = params[30];
            String colorshade = params[31];
            String batchno = params[32];
            String thinnerno = params[33];
            String volume_solid = params[34];
            String app_date = params[35];
            String time = params[36];
            String curingtime = params[37];
            String pot_life = params[38];
            String comment = params[39];
            String doc_name = params[40];
            String ins_date = params[41];
            String cleanliness = params[42];
            String weather = params[43];
            String wind = params[44];
            String mixing = params[45];
            String pumptype = params[46];
            String pumpratio = params[47];

            String create_url = "http://mobile4day.com/ship-inspection/update_pspsp_user.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_komponen", "UTF-8") + "=" + URLEncoder.encode(id_komponen, "UTF-8") + "&" +
                                URLEncoder.encode("steel_temperature", "UTF-8") + "=" + URLEncoder.encode(steel_temperature, "UTF-8") + "&" +
                                URLEncoder.encode("relative_humidity", "UTF-8") + "=" + URLEncoder.encode(relative_humidity, "UTF-8") + "&" +
                                URLEncoder.encode("dew_point", "UTF-8") + "=" + URLEncoder.encode(dew_point, "UTF-8") + "&" +
                                URLEncoder.encode("surface_contaminant", "UTF-8") + "=" + URLEncoder.encode(surface_contaminant, "UTF-8")+ "&" +
                                URLEncoder.encode("mechanical_tool", "UTF-8") + "=" + URLEncoder.encode(mechanical_tool, "UTF-8") + "&" +
                                URLEncoder.encode("st2", "UTF-8") + "=" + URLEncoder.encode(st2, "UTF-8") + "&" +
                                URLEncoder.encode("st3", "UTF-8") + "=" + URLEncoder.encode(st3, "UTF-8") + "&" +
                                URLEncoder.encode("blasting", "UTF-8") + "=" + URLEncoder.encode(blasting, "UTF-8")+ "&" +
                                URLEncoder.encode("sa1", "UTF-8") + "=" + URLEncoder.encode(sa1, "UTF-8") + "&" +
                                URLEncoder.encode("sa2", "UTF-8") + "=" + URLEncoder.encode(sa2, "UTF-8") + "&" +
                                URLEncoder.encode("sa212", "UTF-8") + "=" + URLEncoder.encode(sa212, "UTF-8") + "&" +
                                URLEncoder.encode("sa3", "UTF-8") + "=" + URLEncoder.encode(sa3, "UTF-8") + "&" +
                                URLEncoder.encode("abrasive", "UTF-8") + "=" + URLEncoder.encode(abrasive, "UTF-8")+ "&" +
                                URLEncoder.encode("abrasive_size", "UTF-8") + "=" + URLEncoder.encode(abrasive_size, "UTF-8")+ "&" +
                                URLEncoder.encode("grade", "UTF-8") + "=" + URLEncoder.encode(grade, "UTF-8") + "&" +
                                URLEncoder.encode("timedate", "UTF-8") + "=" + URLEncoder.encode(timedate, "UTF-8") + "&" +
                                URLEncoder.encode("roughness", "UTF-8") + "=" + URLEncoder.encode(roughness, "UTF-8") + "&" +
                                URLEncoder.encode("rust_grade", "UTF-8") + "=" + URLEncoder.encode(rust_grade, "UTF-8") + "&" +
                                URLEncoder.encode("c", "UTF-8") + "=" + URLEncoder.encode(c, "UTF-8") + "&" +
                                URLEncoder.encode("v", "UTF-8") + "=" + URLEncoder.encode(v, "UTF-8") + "&" +
                                URLEncoder.encode("a", "UTF-8") + "=" + URLEncoder.encode(a, "UTF-8")+ "&" +
                                URLEncoder.encode("y1", "UTF-8") + "=" + URLEncoder.encode(y1, "UTF-8") + "&" +
                                URLEncoder.encode("y2", "UTF-8") + "=" + URLEncoder.encode(y2, "UTF-8") + "&" +
                                URLEncoder.encode("result_ssf", "UTF-8") + "=" + URLEncoder.encode(result_ssf, "UTF-8") + "&" +
                                URLEncoder.encode("airless_spray", "UTF-8") + "=" + URLEncoder.encode(airless_spray, "UTF-8")+ "&" +
                                URLEncoder.encode("conventional_sprayer", "UTF-8") + "=" + URLEncoder.encode(conventional_sprayer, "UTF-8") + "&" +
                                URLEncoder.encode("roll_brush", "UTF-8") + "=" + URLEncoder.encode(roll_brush, "UTF-8") + "&" +
                                URLEncoder.encode("dew_point", "UTF-8") + "=" + URLEncoder.encode(dew_point, "UTF-8") + "&" +
                                URLEncoder.encode("dry_bulb", "UTF-8") + "=" + URLEncoder.encode(dry_bulb, "UTF-8")+ "&" +
                                URLEncoder.encode("wet_bulb", "UTF-8") + "=" + URLEncoder.encode(wet_bulb, "UTF-8") + "&" +
                                URLEncoder.encode("paint_name", "UTF-8") + "=" + URLEncoder.encode(paint_name, "UTF-8") + "&" +
                                URLEncoder.encode("colorshade", "UTF-8") + "=" + URLEncoder.encode(colorshade, "UTF-8") + "&" +
                                URLEncoder.encode("batchno", "UTF-8") + "=" + URLEncoder.encode(batchno, "UTF-8") + "&" +
                                URLEncoder.encode("thinnerno", "UTF-8") + "=" + URLEncoder.encode(thinnerno, "UTF-8") + "&" +
                                URLEncoder.encode("volume_solid", "UTF-8") + "=" + URLEncoder.encode(volume_solid, "UTF-8") + "&" +
                                URLEncoder.encode("app_date", "UTF-8") + "=" + URLEncoder.encode(app_date, "UTF-8")+ "&" +
                                URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") + "&" +
                                URLEncoder.encode("curingtime", "UTF-8") + "=" + URLEncoder.encode(curingtime, "UTF-8") + "&" +
                                URLEncoder.encode("pot_life", "UTF-8") + "=" + URLEncoder.encode(pot_life, "UTF-8") + "&" +
                                URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8") + "&" +
                                URLEncoder.encode("doc_name", "UTF-8") + "=" + URLEncoder.encode(doc_name, "UTF-8") + "&" +
                                URLEncoder.encode("ins_date", "UTF-8") + "=" + URLEncoder.encode(ins_date, "UTF-8") + "&" +
                                URLEncoder.encode("cleanliness", "UTF-8") + "=" + URLEncoder.encode(cleanliness, "UTF-8") + "&" +
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

    private void showPspsp(String s)
    {
        JSONObject jsonObject;
        //Toast.makeText(RawMaterialCheckActivity.this, "showProject with s " + s, Toast.LENGTH_SHORT).show();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            JSONObject jo = result.getJSONObject(0);

            edInsDate.setText(jo.getString("ins_date"));

            if(jo.getString("stl_temp")!=null){
                cbSteelTemperature.setChecked(true);
                edSteelTemperature.setText(jo.getString("stl_temp"));
            }
            if(jo.getString("rel_humid")!=null){
                cbRelativeHumidity.setChecked(true);
                edRelativeHumidity.setText(jo.getString("rel_humid"));
            }
            if(jo.getString("dew_point")!=null){
                cbDewPoint.isChecked();
                edDewPoint.setText(jo.getString("dew_point"));
            }
            if(jo.getString("surface_cont")!=null){
                cbSurfaceContaminant.setChecked(true);
                edSurfaceContaminant.setText(jo.getString("surface_cont"));
            }
            if(jo.getString("std_rel_humid")!=null) edStandardRelativeHumidity.setText(jo.getString("std_rel_humid"));
            if(jo.getString("std_mechanical_tool")!=null) edStandardMechanicalTool.setText(jo.getString("std_mechanical_tool"));
            if(jo.getString("mechanical_tool")!=null)  cbMechanicalTool.setChecked(true);
            if(jo.getString("st2").equals("1"))  cbSt2.setChecked(true);
            if(jo.getString("st3").equals("1"))  cbSt3.setChecked(true);
            if(jo.getString("blasting").equals("1"))  cbBlasting.setChecked(true);
            if(jo.getString("sa1").equals("1"))  cbSa1.setChecked(true);
            if(jo.getString("sa2").equals("1"))  cbSa2.setChecked(true);
            if(jo.getString("sa212").equals("1"))  cbSa212.setChecked(true);
            if(jo.getString("sa3").equals("1"))  cbSa3.setChecked(true);
            if(jo.getString("abrasive")!=null){
                String abr = jo.getString("abrasive");
                if(abr.equals("Steel Grit")) spinnerTypeOfAbrasive.setSelection(0);
                else spinnerTypeOfAbrasive.setSelection(1);
            }
            if(jo.getString("rust_grade")!=null){
                String abr = jo.getString("rust_grade");
                if(abr.equals("A")) spinnerRustGrade.setSelection(0);
                else if(abr.equals("B")) spinnerRustGrade.setSelection(1);
                else if(abr.equals("C")) spinnerRustGrade.setSelection(2);
                else if(abr.equals("D")) spinnerRustGrade.setSelection(3);
            }
            if(jo.getString("abrasive_size")!=null) edAbrasiveSize.setText(jo.getString("abrasive_size"));
            if(jo.getString("std_abrasive_size")!=null) edStandardAbrasiveSize.setText(jo.getString("std_abrasive_size"));
            if(jo.getString("grade")!=null) edGrade.setText(jo.getString("grade"));
            if(jo.getString("std_grade")!=null) edStandardGrade.setText(jo.getString("std_grade"));
            if(jo.getString("timedate")!=null) edTimeDate.setText(jo.getString("timedate"));
            if(jo.getString("std_timedate")!=null) edStandardTimeDate.setText(jo.getString("std_timedate"));
            if(jo.getString("roughness")!=null) edRoughness.setText(jo.getString("roughness"));
            if(jo.getString("c")!=null) edInputC.setText(jo.getString("c"));
            if(jo.getString("v")!=null) edInputV.setText(jo.getString("v"));
            if(jo.getString("a")!=null) edInputA.setText(jo.getString("a"));
            if(jo.getString("y1")!=null) edInputY1.setText(jo.getString("y1"));
            if(jo.getString("y2")!=null) edInputY2.setText(jo.getString("y2"));
            if(jo.getString("std_ssf")!=null) edStandardSSF.setText(jo.getString("std_ssf"));
            if(jo.getString("result_ssf")!=null) edResultSSF.setText(jo.getString("result_ssf"));
            if(jo.getString("airless_spray").equals("1"))  cbAirlessSpray.setChecked(true);
            if(jo.getString("conv_spray").equals("1"))  cbConventionalSprayer.setChecked(true);
            if(jo.getString("rollbrush").equals("1"))  cbRollBrush.setChecked(true);
            if(jo.getString("std_spam")!=null) edStandardSPAM.setText(jo.getString("std_spam"));
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
            if(jo.getString("std_pot_life")!=null) edStandardPotLife.setText(jo.getString("std_pot_life"));

            if(jo.getString("doc1")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation1)).execute(jo.getString("doc1"));
            if(jo.getString("doc2")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation2)).execute(jo.getString("doc2"));
            if(jo.getString("doc3")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation3)).execute(jo.getString("doc3"));
            if(jo.getString("doc4")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation4)).execute(jo.getString("doc4"));
            if(jo.getString("doc5")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation5)).execute(jo.getString("doc5"));
            if(jo.getString("doc6")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation6)).execute(jo.getString("doc6"));

            if(jo.getString("comment")!=null) edBlockComment.setText(jo.getString("comment"));
            if(jo.getString("doc_name")!=null) edNamaDokumen.setText(jo.getString("doc_name"));

            if(jo.getString("cleanliness").equals("Good")) spinnerCleanliness.setSelection(0);
            else if(jo.getString("cleanliness").equals("Medium")) spinnerCleanliness.setSelection(1);
            else if(jo.getString("cleanliness").equals("Fair")) spinnerCleanliness.setSelection(2);
            else if(jo.getString("cleanliness").equals("Poor")) spinnerCleanliness.setSelection(3);
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

    public void help(View view)
    {
        Intent intent = new Intent(RawMaterialCheckActivity.this, UploadFileActivity.class);
        extras.putString("FROM", "HELP");
        intent.putExtras(extras);
        startActivity(intent);
    }

    class getPspsp extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getPspsp() {
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
            showPspsp(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/ship-inspection/get_pspsp.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =
                        URLEncoder.encode("id_komponen", "UTF-8") + "=" + URLEncoder.encode(komponenID, "UTF-8");
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
}
