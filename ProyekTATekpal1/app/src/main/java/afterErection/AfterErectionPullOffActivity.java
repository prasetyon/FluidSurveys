package afterErection;

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
import android.widget.LinearLayout;
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
import java.util.Locale;

import dokumen.UploadFileActivity;
import report.PullOffReport;

import static android.view.View.GONE;

public class AfterErectionPullOffActivity extends AppCompatActivity {

    Button btnBack, btnSubmit;
    TextView tvNamaBlok;
    ImageView ivFail1, ivFail2, ivFail3;
    LinearLayout llAdmin, llUser;
    EditText edProsesBlok, edPosisiBlok, edInsDate, edNamaDokumen, edBlockComment, edRequired, edSpot;
    EditText edAdhesion, edEquipment, edLastCoat, edDateAttachment, edShopPrimer, ed1stCoat, ed2ndCoat, ed3rdCoat, ed4thCoat, ed5thCoat,
            edDFTBef1, edDFTAf1, edPullOff1, edFail1, edDFTBef2, edDFTAf2, edPullOff2, edFail2, edDFTBef3, edDFTAf3, edPullOff3, edFail3;
    CheckBox cbShopPrimer, cb1stCoat, cb2ndCoat, cb3rdCoat, cb4thCoat, cb5thCoat;
    boolean doubleBackToExitPressedOnce = false;
    Context Activity;
    private Bundle extras;
    private boolean created=false;
    private String lokasi;
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
        setContentView(R.layout.activity_after_erection_pull_off);
        setTitle("PULL OFF ADHESION TEST");

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

        new getBlock().execute();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnUpdate:
                    if(role.equals("admin")){
                        String shop, first, second, third, forth, fifth, required;
                        if(cbShopPrimer.isChecked()) shop = "1";
                        else shop = "0";
                        if(cb1stCoat.isChecked()) first = "1";
                        else first = "0";
                        if(cb2ndCoat.isChecked()) second = "1";
                        else second = "0";
                        if(cb3rdCoat.isChecked()) third = "1";
                        else third = "0";
                        if(cb4thCoat.isChecked()) forth = "1";
                        else forth = "0";
                        if(cb5thCoat.isChecked()) fifth = "1";
                        else fifth = "0";
                        required = edRequired.getText().toString();
                        String comment = edBlockComment.getText().toString();
                        new updateProcess().execute(shop, first, second, third, forth, fifth, required, comment);
                    }
                    else if(role.equals("user") || role.equals("userNo")){
                        String ins_date = edInsDate.getText().toString();
                        String adhesion, equipment, lastcoat, dateattach, shop, first, second, third, forth, fifth, spot,
                                dftbe1, dftaf1, pulloff1, fail1, dftbe2, dftaf2, pulloff2, fail2, dftbe3, dftaf3, pulloff3, fail3;
                        adhesion = edAdhesion.getText().toString();
                        equipment = edEquipment.getText().toString();
                        lastcoat = edLastCoat.getText().toString();
                        dateattach = edDateAttachment.getText().toString();
                        shop = edShopPrimer.getText().toString();
                        first = ed1stCoat.getText().toString();
                        second = ed2ndCoat.getText().toString();
                        third = ed3rdCoat.getText().toString();
                        forth =ed4thCoat.getText().toString();
                        fifth = ed5thCoat.getText().toString();
                        spot = edSpot.getText().toString();
                        dftbe1 = edDFTBef1.getText().toString();
                        dftaf1 = edDFTAf1.getText().toString();
                        pulloff1 = edPullOff1.getText().toString();
                        fail1 = edFail1.getText().toString();
                        dftbe2 = edDFTBef2.getText().toString();
                        dftaf2 = edDFTAf2.getText().toString();
                        pulloff2 = edPullOff2.getText().toString();
                        fail2 = edFail2.getText().toString();
                        dftbe3 = edDFTBef3.getText().toString();
                        dftaf3 = edDFTAf3.getText().toString();
                        pulloff3 = edPullOff3.getText().toString();
                        fail3 = edFail3.getText().toString();

                        String comment = edBlockComment.getText().toString();
                        String doc_name = edNamaDokumen.getText().toString();

                        new updateProcess().execute(ins_date, adhesion, equipment, lastcoat, dateattach, shop, first, second, third, forth, fifth, spot,
                                dftbe1, dftaf1, pulloff1, fail1, dftbe2, dftaf2, pulloff2, fail2, dftbe3, dftaf3, pulloff3, fail3, comment, doc_name);
                    }
                    break;
                case R.id.btnBack:
                    intent = new Intent(AfterErectionPullOffActivity.this, AfterErectionListActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.edDateAttachment:
                case R.id.edInsDate:
                    showDate(v);
                    break;
            }
        }
    };

    class updateProcess extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updateProcess(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("updating process...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            created = true;
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(created && create.equals("1")){
                Toast.makeText(AfterErectionPullOffActivity.this, "You process has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AfterErectionPullOffActivity.this, AfterErectionListActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(AfterErectionPullOffActivity.this, "You process has not been updated! s=" + s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String from = "pulloff";
            String shop="", first="", second="", third="", forth="", fifth="", required="";
            String ins_date="", adhesion="", equipment="", lastcoat="", dateattach="", spot="",
                    dftbe1="", dftaf1="", pulloff1="", fail1="", dftbe2="", dftaf2="", pulloff2="", fail2="",
                    dftbe3="", dftaf3="", pulloff3="", fail3="", comment="", doc_name="";
            if(role.equals("admin")) {
                shop = params[0];
                first = params[1];
                second = params[2];
                third = params[3];
                forth = params[4];
                fifth = params[5];
                required = params[6];
                comment = params[7];
            }
            else if(role.equals("user") || role.equals("userNo")){
                ins_date = params[0];
                adhesion = params[1];
                equipment = params[2];
                lastcoat = params[3];
                dateattach = params[4];
                shop = params[5];
                first = params[6];
                second = params[7];
                third = params[8];
                forth = params[9];
                fifth = params[10];
                spot = params[11];

                dftbe1 = params[12];
                dftaf1 = params[13];
                pulloff1 = params[14];
                fail1 = params[15];
                dftbe2 = params[16];
                dftaf2 = params[17];
                pulloff2 = params[18];
                fail2 = params[19];
                dftbe3 = params[20];
                dftaf3 = params[21];
                pulloff3 = params[22];
                fail3 = params[23];
                comment = params[24];
                doc_name = params[25];
            }

            String create_url = "http://mobile4day.com/ship-inspection/update_pulloff.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data="";
                if(role.equals("admin")) {
                    data =
                            URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(blockID, "UTF-8") + "&" +
                                    URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8") + "&" +
                                    URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8") + "&" +
                                    URLEncoder.encode("shop", "UTF-8") + "=" + URLEncoder.encode(shop, "UTF-8") + "&" +
                                    URLEncoder.encode("first", "UTF-8") + "=" + URLEncoder.encode(first, "UTF-8") + "&" +
                                    URLEncoder.encode("second", "UTF-8") + "=" + URLEncoder.encode(second, "UTF-8") + "&" +
                                    URLEncoder.encode("third", "UTF-8") + "=" + URLEncoder.encode(third, "UTF-8") + "&" +
                                    URLEncoder.encode("forth", "UTF-8") + "=" + URLEncoder.encode(forth, "UTF-8") + "&" +
                                    URLEncoder.encode("fifth", "UTF-8") + "=" + URLEncoder.encode(fifth, "UTF-8") + "&" +
                                    URLEncoder.encode("required", "UTF-8") + "=" + URLEncoder.encode(required, "UTF-8") + "&" +
                                    URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8");
                }
                else if(role.equals("user") || role.equals("userNo")) {
                    data =
                            URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(blockID, "UTF-8") + "&" +
                                    URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8") + "&" +
                                    URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8") + "&" +
                                    URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8") + "&" +
                                    URLEncoder.encode("adhesion", "UTF-8") + "=" + URLEncoder.encode(adhesion, "UTF-8") + "&" +
                                    URLEncoder.encode("equipment", "UTF-8") + "=" + URLEncoder.encode(equipment, "UTF-8") + "&" +
                                    URLEncoder.encode("lastcoat", "UTF-8") + "=" + URLEncoder.encode(lastcoat, "UTF-8") + "&" +
                                    URLEncoder.encode("dateattach", "UTF-8") + "=" + URLEncoder.encode(dateattach, "UTF-8") + "&" +
                                    URLEncoder.encode("shop", "UTF-8") + "=" + URLEncoder.encode(shop, "UTF-8") + "&" +
                                    URLEncoder.encode("first", "UTF-8") + "=" + URLEncoder.encode(first, "UTF-8") + "&" +
                                    URLEncoder.encode("second", "UTF-8") + "=" + URLEncoder.encode(second, "UTF-8") + "&" +
                                    URLEncoder.encode("third", "UTF-8") + "=" + URLEncoder.encode(third, "UTF-8") + "&" +
                                    URLEncoder.encode("forth", "UTF-8") + "=" + URLEncoder.encode(forth, "UTF-8") + "&" +
                                    URLEncoder.encode("fifth", "UTF-8") + "=" + URLEncoder.encode(fifth, "UTF-8") + "&" +
                                    URLEncoder.encode("spot", "UTF-8") + "=" + URLEncoder.encode(spot, "UTF-8") + "&" +
                                    URLEncoder.encode("dftbe1", "UTF-8") + "=" + URLEncoder.encode(dftbe1, "UTF-8") + "&" +
                                    URLEncoder.encode("dftbe2", "UTF-8") + "=" + URLEncoder.encode(dftbe2, "UTF-8") + "&" +
                                    URLEncoder.encode("dftbe3", "UTF-8") + "=" + URLEncoder.encode(dftbe3, "UTF-8") + "&" +
                                    URLEncoder.encode("dftaf1", "UTF-8") + "=" + URLEncoder.encode(dftaf1, "UTF-8") + "&" +
                                    URLEncoder.encode("dftaf2", "UTF-8") + "=" + URLEncoder.encode(dftaf2, "UTF-8") + "&" +
                                    URLEncoder.encode("dftaf3", "UTF-8") + "=" + URLEncoder.encode(dftaf3, "UTF-8") + "&" +
                                    URLEncoder.encode("pulloff1", "UTF-8") + "=" + URLEncoder.encode(pulloff1, "UTF-8") + "&" +
                                    URLEncoder.encode("pulloff2", "UTF-8") + "=" + URLEncoder.encode(pulloff2, "UTF-8") + "&" +
                                    URLEncoder.encode("pulloff3", "UTF-8") + "=" + URLEncoder.encode(pulloff3, "UTF-8") + "&" +
                                    URLEncoder.encode("fail1", "UTF-8") + "=" + URLEncoder.encode(fail1, "UTF-8") + "&" +
                                    URLEncoder.encode("fail2", "UTF-8") + "=" + URLEncoder.encode(fail2, "UTF-8") + "&" +
                                    URLEncoder.encode("fail3", "UTF-8") + "=" + URLEncoder.encode(fail3, "UTF-8") + "&" +
                                    URLEncoder.encode("doc_name", "UTF-8") + "=" + URLEncoder.encode(doc_name, "UTF-8") + "&" +
                                    URLEncoder.encode("ins_date", "UTF-8") + "=" + URLEncoder.encode(ins_date, "UTF-8");
                }
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

    private void showBlock(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            tvNamaBlok.setText(blockName);
            edPosisiBlok.setText(blockLocation);
            edInsDate.setText(jo.getString("ins_date"));

            edAdhesion.setText(jo.getString("adhesion"));
            edEquipment.setText(jo.getString("equipment"));
            edLastCoat.setText(jo.getString("duration"));
            edDateAttachment.setText(jo.getString("date"));

            String shop = jo.getString("shop");
            if(shop.equals("1")){
                cbShopPrimer.setChecked(true);
                edShopPrimer.setVisibility(View.VISIBLE);
            }
            else{
                cbShopPrimer.setChecked(false);
                edShopPrimer.setVisibility(View.GONE);
            }
            String first = jo.getString("first");
            if(first.equals("1")){
                cb1stCoat.setChecked(true);
                ed1stCoat.setVisibility(View.VISIBLE);
            }
            else{
                cb1stCoat.setChecked(false);
                ed1stCoat.setVisibility(View.GONE);
            }
            String second = jo.getString("second");
            if(second.equals("1")){
                cb2ndCoat.setChecked(true);
                ed2ndCoat.setVisibility(View.VISIBLE);
            }
            else{
                cb2ndCoat.setChecked(false);
                ed2ndCoat.setVisibility(View.GONE);
            }
            String third = jo.getString("third");
            if(third.equals("1")){
                cb3rdCoat.setChecked(true);
                ed3rdCoat.setVisibility(View.VISIBLE);
            }
            else{
                cb3rdCoat.setChecked(false);
                ed3rdCoat.setVisibility(View.GONE);
            }
            String forth = jo.getString("forth");
            if(forth.equals("1")){
                cb4thCoat.setChecked(true);
                ed4thCoat.setVisibility(View.VISIBLE);
            }
            else{
                cb4thCoat.setChecked(false);
                ed4thCoat.setVisibility(View.GONE);
            }
            String fifth = jo.getString("fifth");
            if(fifth.equals("1")){
                cb5thCoat.setChecked(true);
                ed5thCoat.setVisibility(View.VISIBLE);
            }
            else{
                cb5thCoat.setChecked(false);
                ed5thCoat.setVisibility(View.GONE);
            }

            edSpot.setText(jo.getString("spot"));
            edRequired.setText(jo.getString("required"));

            edDFTBef1.setText(jo.getString("dftbef1"));
            edDFTBef2.setText(jo.getString("dftbef2"));
            edDFTBef3.setText(jo.getString("dftbef3"));
            edDFTAf1.setText(jo.getString("dftaf1"));
            edDFTAf2.setText(jo.getString("dftaf2"));
            edDFTAf3.setText(jo.getString("dftaf3"));
            edPullOff1.setText(jo.getString("pulloff1"));
            edPullOff2.setText(jo.getString("pulloff2"));
            edPullOff3.setText(jo.getString("pulloff3"));

            edFail1.setText(jo.getString("fail1"));
            edFail2.setText(jo.getString("fail2"));
            edFail3.setText(jo.getString("fail3"));

            if(!jo.getString("imgfail1").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivFail1)).execute(jo.getString("imgfail1"));
            if(!jo.getString("imgfail2").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivFail2)).execute(jo.getString("imgfail2"));
            if(!jo.getString("imgfail3").equals("")) new DownloadImageTask((ImageView) findViewById(R.id.ivFail3)).execute(jo.getString("imgfail3"));

            if(jo.getString("doc1")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation1)).execute(jo.getString("doc1"));
            if(jo.getString("doc2")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation2)).execute(jo.getString("doc2"));
            if(jo.getString("doc3")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation3)).execute(jo.getString("doc3"));
            if(jo.getString("doc4")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation4)).execute(jo.getString("doc4"));
            if(jo.getString("doc5")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation5)).execute(jo.getString("doc5"));
            if(jo.getString("doc6")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation6)).execute(jo.getString("doc6"));

            edBlockComment.setText(jo.getString("comment"));
            edNamaDokumen.setText(jo.getString("doc_name"));

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
            ProgressDialog.setMessage("Reading information...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(AfterErectionSteelActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

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

    private void initiate()
    {
        Activity = this;

        tvNamaBlok = (TextView) findViewById(R.id.tvNamaBlok);
        edProsesBlok = (EditText) findViewById(R.id.edProsesBlok);
        edPosisiBlok = (EditText) findViewById(R.id.edPosisiBlok);
        edNamaDokumen = (EditText) findViewById(R.id.edNamaDokumen);
        edBlockComment = (EditText) findViewById(R.id.edBlockComment);
        edInsDate = (EditText) findViewById(R.id.edInsDate);
        edRequired = (EditText) findViewById(R.id.edRequired);
        edSpot = (EditText) findViewById(R.id.edSpot);
        edAdhesion = (EditText) findViewById(R.id.edAdhesion);
        edEquipment = (EditText) findViewById(R.id.edEquipment);
        edLastCoat = (EditText) findViewById(R.id.edLastCoat);
        edDateAttachment = (EditText) findViewById(R.id.edDateAttachment);
        edShopPrimer = (EditText) findViewById(R.id.edShopPrimer);
        ed1stCoat = (EditText) findViewById(R.id.ed1stCoat);
        ed2ndCoat = (EditText) findViewById(R.id.ed2ndCoat);
        ed3rdCoat = (EditText) findViewById(R.id.ed3rdCoat);
        ed4thCoat = (EditText) findViewById(R.id.ed4thCoat);
        ed5thCoat = (EditText) findViewById(R.id.ed5thCoat);
        edDFTBef1 = (EditText) findViewById(R.id.edDFTBef1);
        edDFTAf1 = (EditText) findViewById(R.id.edDFTAf1);
        edPullOff1 = (EditText) findViewById(R.id.edPullOff1);
        edFail1 = (EditText) findViewById(R.id.edFail1);
        edDFTBef2 = (EditText) findViewById(R.id.edDFTBef2);
        edDFTAf2 = (EditText) findViewById(R.id.edDFTAf2);
        edPullOff2 = (EditText) findViewById(R.id.edPullOff2);
        edFail2 = (EditText) findViewById(R.id.edFail2);
        edDFTBef3 = (EditText) findViewById(R.id.edDFTBef3);
        edDFTAf3 = (EditText) findViewById(R.id.edDFTAf3);
        edPullOff3 = (EditText) findViewById(R.id.edPullOff3);
        edFail3 = (EditText) findViewById(R.id.edFail3);

        cbShopPrimer = (CheckBox) findViewById(R.id.cbShopPrimer);
        cb1stCoat = (CheckBox) findViewById(R.id.cb1stCoat);
        cb2ndCoat = (CheckBox) findViewById(R.id.cb2ndCoat);
        cb3rdCoat = (CheckBox) findViewById(R.id.cb3rdCoat);
        cb4thCoat = (CheckBox) findViewById(R.id.cb4thCoat);
        cb5thCoat = (CheckBox) findViewById(R.id.cb5thCoat);

        llAdmin = (LinearLayout) findViewById(R.id.llAdmin);
        llUser = (LinearLayout) findViewById(R.id.llUser);

        ivFail1 = (ImageView) findViewById(R.id.ivFail1);
        ivFail2 = (ImageView) findViewById(R.id.ivFail2);
        ivFail3 = (ImageView) findViewById(R.id.ivFail3);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnUpdate);

        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    }

    private void setEnable(String role)
    {
        tvNamaBlok.setEnabled(false);
        edPosisiBlok.setEnabled(false);
        edProsesBlok.setEnabled(false);
        if(role.equals("user"))
        {
            llAdmin.setVisibility(GONE);
            edRequired.setEnabled(false);
        }
        else if(role.equals("admin"))
        {
            llUser.setVisibility(GONE);
            edSpot.setEnabled(false);
            edInsDate.setEnabled(false);
            edNamaDokumen.setEnabled(false);
            edAdhesion.setEnabled(false);
            edEquipment.setEnabled(false);
            edLastCoat.setEnabled(false);
            edDateAttachment.setEnabled(false);
            edShopPrimer.setEnabled(false);
            ed1stCoat.setEnabled(false);
            ed2ndCoat.setEnabled(false);
            ed3rdCoat.setEnabled(false);
            ed4thCoat.setEnabled(false);
            ed5thCoat.setEnabled(false);
            edDFTBef1.setEnabled(false);
            edDFTAf1.setEnabled(false);
            edPullOff1.setEnabled(false);
            edFail1.setEnabled(false);
            edDFTBef2.setEnabled(false);
            edDFTAf2.setEnabled(false);
            edPullOff2.setEnabled(false);
            edFail2.setEnabled(false);
            edDFTBef3.setEnabled(false);
            edDFTAf3.setEnabled(false);
            edPullOff3.setEnabled(false);
            edFail3.setEnabled(false);
        }
        else if(role.equals("userNo"))
        {
            llAdmin.setVisibility(GONE);
            edInsDate.setEnabled(false);
            edRequired.setEnabled(false);
            edSpot.setEnabled(false);
            edInsDate.setEnabled(false);
            edNamaDokumen.setEnabled(false);
            edAdhesion.setEnabled(false);
            edEquipment.setEnabled(false);
            edLastCoat.setEnabled(false);
            edDateAttachment.setEnabled(false);
            edShopPrimer.setEnabled(false);
            ed1stCoat.setEnabled(false);
            ed2ndCoat.setEnabled(false);
            ed3rdCoat.setEnabled(false);
            ed4thCoat.setEnabled(false);
            ed5thCoat.setEnabled(false);
            edDFTBef1.setEnabled(false);
            edDFTAf1.setEnabled(false);
            edPullOff1.setEnabled(false);
            edFail1.setEnabled(false);
            edDFTBef2.setEnabled(false);
            edDFTAf2.setEnabled(false);
            edPullOff2.setEnabled(false);
            edFail2.setEnabled(false);
            edDFTBef3.setEnabled(false);
            edDFTAf3.setEnabled(false);
            edPullOff3.setEnabled(false);
            edFail3.setEnabled(false);
        }
    }

    public void help(View view)
    {
        Intent intent = new Intent(AfterErectionPullOffActivity.this, UploadFileActivity.class);
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
                    case R.id.edDateAttachment:
                        edDateAttachment.setText(dateFormatter.format(newDate.getTime()));
                        break;
                    case R.id.edInsDate:
                        edInsDate.setText(dateFormatter.format(newDate.getTime()));
                        break;
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dpProyek.show();
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
