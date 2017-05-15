package blockStage;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

public class BlockStageCheckSteelActivity extends AppCompatActivity {

    Button btnBack, btnSubmit;
    TextView tvNamaBlok;
    ImageView ivSharpEdges, ivGap, ivImperfectWelding, ivMissWeld, ivImperfectSteelSurface;
    EditText edProsesBlok, edPosisiBlok, edInputPrepGrade, edStandardPrepGrade, edInsDate,
            edTypeOfDefect, edRepairMethod, edRepairConfirmDate, edNamaDokumen, edBlockComment;
    CheckBox cbSharpEdges, cbGap, cbImperfectWelding, cbMissWeld, cbImperfectSteelSurface;
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
        setContentView(R.layout.activity_block_stage_check_steel);
        setTitle("BLOCK STAGE");

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
                        String std_prep_grade = edStandardPrepGrade.getText().toString();
                        new updateProcess().execute(std_prep_grade);
                    }
                    else if(role.equals("user") || role.equals("userNo")){
                        String prep_grade = edInputPrepGrade.getText().toString();
                        String sharp_edges, gap, imp_welding, miss_weld, imp_Steel;
                        if(cbSharpEdges.isChecked()) sharp_edges="1";
                        else sharp_edges="0";
                        if(cbGap.isChecked()) gap="1";
                        else gap="0";
                        if(cbImperfectWelding.isChecked()) imp_welding="1";
                        else imp_welding="0";
                        if(cbMissWeld.isChecked()) miss_weld="1";
                        else miss_weld="0";
                        if(cbImperfectSteelSurface.isChecked()) imp_Steel="1";
                        else imp_Steel="0";
                        String img_sharp_edges="", img_gap="", img_imp_welding="", img_miss_weld="", img_imp_steel="", doc="";

                        String type_defect = edTypeOfDefect.getText().toString();
                        String repair_method = edRepairMethod.getText().toString();
                        String repair_confirm = edRepairConfirmDate.getText().toString();
                        String comment = edBlockComment.getText().toString();
                        String doc_name = edNamaDokumen.getText().toString();
                        String ins_date = edInsDate.getText().toString();

                        new updateProcess().execute(prep_grade, sharp_edges, img_sharp_edges, gap, img_gap, imp_welding, img_imp_welding, miss_weld, img_miss_weld, imp_Steel,
                                                    img_imp_steel, type_defect, repair_method, repair_confirm, doc, comment, doc_name, ins_date);
                    }
                    break;
                case R.id.btnBack:
                    intent = new Intent(BlockStageCheckSteelActivity.this, BlockStageListActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.edInsDate:
                case R.id.edRepairConfirmDate:
                    showDate(v);
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

            tvNamaBlok.setText(blockName);
            edPosisiBlok.setText(blockLocation);
            edInsDate.setText(jo.getString("ins_date"));
            edInputPrepGrade.setText(jo.getString("prep_grade"));
            edStandardPrepGrade.setText(jo.getString("std_prep_grade"));
            if(jo.getString("sharp_edges").equals("1")){
                cbSharpEdges.setChecked(true);
                ivSharpEdges.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivSharpEdges))
                        .execute(jo.getString("img_sharp_edges"));
            }
            else cbSharpEdges.setChecked(false);
            if(jo.getString("gap").equals("1")){
                cbGap.setChecked(true);
                ivGap.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivGap))
                        .execute(jo.getString("img_gap"));
            }
            else cbGap.setChecked(false);
            if(jo.getString("imp_welding").equals("1")){
                cbImperfectWelding.setChecked(true);
                ivImperfectWelding.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivImperfectWelding))
                        .execute(jo.getString("img_imp_welding"));
            }
            else cbImperfectWelding.setChecked(false);
            if(jo.getString("miss_weld").equals("1")){
                cbMissWeld.setChecked(true);
                ivMissWeld.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivMissWeld))
                        .execute(jo.getString("img_miss_weld"));
            }
            else cbMissWeld.setChecked(false);
            if(jo.getString("imp_steel").equals("1")){
                cbImperfectSteelSurface.setChecked(true);
                ivImperfectSteelSurface.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivImperfectSteelSurface))
                        .execute(jo.getString("img_imp_steel"));
            }
            else cbImperfectSteelSurface.setChecked(false);
            edTypeOfDefect.setText(jo.getString("type_of_defect"));
            edRepairMethod.setText(jo.getString("repair_method"));
            edRepairConfirmDate.setText(jo.getString("repair_confirm"));

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

    public void help(View view)
    {
        Intent intent = new Intent(BlockStageCheckSteelActivity.this, UploadFileActivity.class);
        extras.putString("FROM", "HELP");
        intent.putExtras(extras);
        startActivity(intent);
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
            //Toast.makeText(MainPageActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

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

            create_url = "http://mobile4day.com/ship-inspection/get_swp1.php";

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
                Toast.makeText(BlockStageCheckSteelActivity.this, "You process has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BlockStageCheckSteelActivity.this, BlockStageListActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(BlockStageCheckSteelActivity.this, "You process has not been updated! s=" + s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            String std_prep_grade="", prep_grade="", sharp_edges="", img_sharp_edges="", gap="", img_gap="", imp_welding="", img_imp_welding="",
                    miss_weld="", img_miss_weld="", imp_steel="", img_imp_steel="", type_defect="", repair_method="", repair_confirm="", doc="",
                    comment="", doc_name="", ins_date="";
            String from = "swp1";

            if(role.equals("admin")) {
                std_prep_grade = params[0];
            }
            else if(role.equals("user") || role.equals("userNo")){
                prep_grade = params[0];
                sharp_edges = params[1];
                img_sharp_edges = params[2];
                gap = params[3];
                img_gap = params[4];
                imp_welding = params[5];
                img_imp_welding = params[6];
                miss_weld = params[7];
                img_miss_weld = params[8];
                imp_steel = params[9];
                img_imp_steel = params[10];
                type_defect = params[11];
                repair_method = params[12];
                repair_confirm = params[13];
                doc = params[14];
                comment = params[15];
                doc_name = params[16];
                ins_date = params[17];
            }

            String create_url = "http://mobile4day.com/ship-inspection/update_swp.php";
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
                                    URLEncoder.encode("std_prep_grade", "UTF-8") + "=" + URLEncoder.encode(std_prep_grade, "UTF-8");
                }
                else if(role.equals("user") || role.equals("userNo")) {
                    data =
                            URLEncoder.encode("id_blok", "UTF-8") + "=" + URLEncoder.encode(blockID, "UTF-8") + "&" +
                                    URLEncoder.encode("role", "UTF-8") + "=" + URLEncoder.encode(role, "UTF-8") + "&" +
                                    URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8") + "&" +
                                    URLEncoder.encode("prep_grade", "UTF-8") + "=" + URLEncoder.encode(prep_grade, "UTF-8") + "&" +
                                    URLEncoder.encode("sharp_edges", "UTF-8") + "=" + URLEncoder.encode(sharp_edges, "UTF-8") + "&" +
                                    URLEncoder.encode("gap", "UTF-8") + "=" + URLEncoder.encode(gap, "UTF-8") + "&" +
                                    URLEncoder.encode("imp_welding", "UTF-8") + "=" + URLEncoder.encode(imp_welding, "UTF-8") + "&" +
                                    URLEncoder.encode("miss_weld", "UTF-8") + "=" + URLEncoder.encode(miss_weld, "UTF-8") + "&" +
                                    URLEncoder.encode("imp_steel", "UTF-8") + "=" + URLEncoder.encode(imp_steel, "UTF-8") + "&" +
                                    URLEncoder.encode("type_defect", "UTF-8") + "=" + URLEncoder.encode(type_defect, "UTF-8") + "&" +
                                    URLEncoder.encode("repair_method", "UTF-8") + "=" + URLEncoder.encode(repair_method, "UTF-8") + "&" +
                                    URLEncoder.encode("repair_confirm", "UTF-8") + "=" + URLEncoder.encode(repair_confirm, "UTF-8") + "&" +
                                    URLEncoder.encode("doc", "UTF-8") + "=" + URLEncoder.encode(doc, "UTF-8") + "&" +
                                    URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8") + "&" +
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

    public void showDate(View v){
        final View vView = v;
        Calendar newCalendar = Calendar.getInstance();
        dpProyek = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                switch (vView.getId()) {
                    case R.id.edRepairConfirmDate:
                        edRepairConfirmDate.setText(dateFormatter.format(newDate.getTime()));
                        break;
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


    private void initiate()
    {
        Activity = this;

        ivSharpEdges = (ImageView) findViewById(R.id.ivSharpEdges);
        ivSharpEdges.setVisibility(View.GONE);
        ivGap = (ImageView) findViewById(R.id.ivGap);
        ivGap.setVisibility(View.GONE);
        ivImperfectWelding = (ImageView) findViewById(R.id.ivImperfectWelding);
        ivImperfectWelding.setVisibility(View.GONE);
        ivMissWeld = (ImageView) findViewById(R.id.ivMissWeld);
        ivMissWeld.setVisibility(View.GONE);
        ivImperfectSteelSurface = (ImageView) findViewById(R.id.ivImperfectSteelSurface);
        ivImperfectSteelSurface.setVisibility(View.GONE);

        tvNamaBlok = (TextView) findViewById(R.id.tvNamaBlok);
        edProsesBlok = (EditText) findViewById(R.id.edProsesBlok);
        edPosisiBlok = (EditText) findViewById(R.id.edPosisiBlok);
        edInputPrepGrade = (EditText) findViewById(R.id.edInputPrepGrade);
        edStandardPrepGrade = (EditText) findViewById(R.id.edStandardPrepGrade);
        edTypeOfDefect = (EditText) findViewById(R.id.edTypeOfDefect);
        edRepairMethod = (EditText) findViewById(R.id.edRepairMethod);
        edRepairConfirmDate = (EditText) findViewById(R.id.edRepairConfirmDate);
        edNamaDokumen = (EditText) findViewById(R.id.edNamaDokumen);
        edBlockComment = (EditText) findViewById(R.id.edBlockComment);
        edRepairConfirmDate.setOnClickListener(activity);
        edInsDate = (EditText) findViewById(R.id.edInsDate);
        cbSharpEdges = (CheckBox) findViewById(R.id.cbSharpEdges);
        cbGap = (CheckBox) findViewById(R.id.cbGap);
        cbImperfectWelding = (CheckBox) findViewById(R.id.cbImperfectWelding);
        cbMissWeld = (CheckBox) findViewById(R.id.cbMissWeld);
        cbImperfectSteelSurface = (CheckBox) findViewById(R.id.cbImperfectSteelSurface);

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
            edStandardPrepGrade.setEnabled(false);
        }
        else if(role.equals("admin"))
        {
            edInsDate.setEnabled(false);
            cbSharpEdges.setEnabled(false);
            cbGap.setEnabled(false);
            cbImperfectSteelSurface.setEnabled(false);
            cbImperfectWelding.setEnabled(false);
            cbMissWeld.setEnabled(false);
            edInputPrepGrade.setEnabled(false);
            edTypeOfDefect.setEnabled(false);
            edRepairConfirmDate.setEnabled(false);
            edRepairMethod.setEnabled(false);
        }
        else if(role.equals("userNo"))
        {
            edStandardPrepGrade.setEnabled(false);
            edInsDate.setEnabled(false);
            cbSharpEdges.setEnabled(false);
            cbGap.setEnabled(false);
            cbImperfectSteelSurface.setEnabled(false);
            cbImperfectWelding.setEnabled(false);
            cbMissWeld.setEnabled(false);
            edInputPrepGrade.setEnabled(false);
            edTypeOfDefect.setEnabled(false);
            edRepairConfirmDate.setEnabled(false);
            edRepairMethod.setEnabled(false);
            edNamaDokumen.setEnabled(false);
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
