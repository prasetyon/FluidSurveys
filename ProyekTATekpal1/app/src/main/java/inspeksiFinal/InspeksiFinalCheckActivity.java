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
import java.util.NavigableMap;

import dokumen.UploadFileActivity;

public class InspeksiFinalCheckActivity extends AppCompatActivity {
    TextView tvNamaBlok;
    ImageView ivVisualCondition, ivMissCoat, ivPinholes, ivBubbles, ivVoids, ivHoliday, ivOtherDefect;
    CheckBox cbVisualCondition, cbMissCoat, cbPinholes, cbBubbles, cbVoids, cbHoliday, cbOtherDefect;
    EditText edBlockComment, edNamaDokumen, edMinNDFT, edMaxNDFT, edArea, edTestPoint, edMinDFT, edMaxDFT, edAvgDFT, edProsesBlok, edPosisiBlok, edTypeOfDefect, edRepairMethod, edRepairConfirmDate;
    EditText edStandard, edInsDate;
    Button btnSubmit, btnBack;
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


    private void initiate()
    {
        ivVisualCondition = (ImageView) findViewById(R.id.ivVisualCondition);
        ivVisualCondition.setVisibility(View.GONE);
        ivMissCoat = (ImageView) findViewById(R.id.ivMissCoat);
        ivMissCoat.setVisibility(View.GONE);
        ivPinholes = (ImageView) findViewById(R.id.ivPinholes);
        ivPinholes.setVisibility(View.GONE);
        ivBubbles = (ImageView) findViewById(R.id.ivBubbles);
        ivBubbles.setVisibility(View.GONE);
        ivVoids = (ImageView) findViewById(R.id.ivVoids);
        ivVoids.setVisibility(View.GONE);
        ivHoliday = (ImageView) findViewById(R.id.ivHoliday);
        ivHoliday.setVisibility(View.GONE);
        ivOtherDefect = (ImageView) findViewById(R.id.ivOtherDefect);
        ivOtherDefect.setVisibility(View.GONE);

        cbVisualCondition = (CheckBox) findViewById(R.id.cbVisualCondition);
        cbMissCoat = (CheckBox) findViewById(R.id.cbMissCoat);
        cbPinholes = (CheckBox) findViewById(R.id.cbPinholes);
        cbBubbles = (CheckBox) findViewById(R.id.cbBubbles);
        cbVoids = (CheckBox) findViewById(R.id.cbVoids);
        cbHoliday = (CheckBox) findViewById(R.id.cbHoliday);
        cbOtherDefect = (CheckBox) findViewById(R.id.cbOtherDefect);
        edInsDate = (EditText) findViewById(R.id.edInsDate);
        edTypeOfDefect = (EditText) findViewById(R.id.edTypeOfDefect);
        edRepairMethod = (EditText) findViewById(R.id.edRepairMethod);
        edRepairConfirmDate = (EditText) findViewById(R.id.edRepairConfirmDate);
        edBlockComment = (EditText) findViewById(R.id.edBlockComment);
        edNamaDokumen = (EditText) findViewById(R.id.edNamaDokumen);
        edMinNDFT = (EditText) findViewById(R.id.edMinNDFT);
        edMaxNDFT = (EditText) findViewById(R.id.edMaxNDFT);
        edArea = (EditText) findViewById(R.id.edArea);
        edTestPoint = (EditText) findViewById(R.id.edTestPoint);
        edMinDFT = (EditText) findViewById(R.id.edMinDFT);
        edMaxDFT = (EditText) findViewById(R.id.edMaxDFT);
        edAvgDFT = (EditText) findViewById(R.id.edAvgDFT);
        edPosisiBlok = (EditText) findViewById(R.id.edPosisiBlok);
        edProsesBlok = (EditText) findViewById(R.id.edProsesBlok);
        edStandard = (EditText) findViewById(R.id.edStandard);
        tvNamaBlok = (TextView) findViewById(R.id.tvNamaBlok);

        btnSubmit = (Button) findViewById(R.id.btnUpdate);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit.setOnClickListener(activity);
        btnBack.setOnClickListener(activity);
    }

    private void setEnable(String role)
    {
        tvNamaBlok.setEnabled(false);
        edProsesBlok.setEnabled(false);
        edPosisiBlok.setEnabled(false);
        if(role.equals("admin"))
        {
            cbVisualCondition.setEnabled(false);
            cbMissCoat.setEnabled(false);
            cbPinholes.setEnabled(false);
            cbBubbles.setEnabled(false);
            cbVoids.setEnabled(false);
            cbHoliday.setEnabled(false);
            cbOtherDefect.setEnabled(false);
            edInsDate.setEnabled(false);
            edTypeOfDefect.setEnabled(false);
            edRepairConfirmDate.setEnabled(false);
            edRepairMethod.setEnabled(false);
            edNamaDokumen.setEnabled(false);
            edArea.setEnabled(false);
            edTestPoint.setEnabled(false);
            edMinDFT.setEnabled(false);
            edMaxDFT.setEnabled(false);
            edAvgDFT.setEnabled(false);
            btnSubmit.setVisibility(View.GONE);
        }
        else if(role.equals("user"))
        {
            edMinNDFT.setEnabled(false);
            edMaxNDFT.setEnabled(false);
            edStandard.setEnabled(false);
        }
        else if(role.equals("userNo"))
        {
            cbVisualCondition.setEnabled(false);
            cbMissCoat.setEnabled(false);
            cbPinholes.setEnabled(false);
            cbBubbles.setEnabled(false);
            cbVoids.setEnabled(false);
            cbHoliday.setEnabled(false);
            cbOtherDefect.setEnabled(false);
            edInsDate.setEnabled(false);
            edTypeOfDefect.setEnabled(false);
            edRepairConfirmDate.setEnabled(false);
            edRepairMethod.setEnabled(false);
            edBlockComment.setEnabled(false);
            edNamaDokumen.setEnabled(false);
            edArea.setEnabled(false);
            edTestPoint.setEnabled(false);
            edMinDFT.setEnabled(false);
            edMaxDFT.setEnabled(false);
            edAvgDFT.setEnabled(false);
            edMinNDFT.setEnabled(false);
            edMaxNDFT.setEnabled(false);
            edStandard.setEnabled(false);
        }
    }

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
        blockID = extras.getString("BLOCK_ID");
        blockName = extras.getString("BLOCK_NAME");
        blockLocation = extras.getString("BLOCK_LOCATION");

        initiate();
        setEnable(role);

        new getFinal().execute();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnUpdate:
                    String vc = "", misscoat="", pinholes="", bubbles="", voids="", od="", doc="";
                    String min_ndft = edMinNDFT.getText().toString();
                    String max_ndft = edMaxNDFT.getText().toString();
                    String area = edArea.getText().toString();
                    String test = edTestPoint.getText().toString();
                    String min_dft = edMinDFT.getText().toString();
                    String max_dft = edMaxDFT.getText().toString();
                    String avg_dft = edAvgDFT.getText().toString();
                    String comment = edBlockComment.getText().toString();
                    String doc_name = edNamaDokumen.getText().toString();
                    String type_of_defect = edTypeOfDefect.getText().toString();
                    String repair_date = edRepairConfirmDate.getText().toString();
                    String repair_method = edRepairMethod.getText().toString();
                    String standard = edStandard.getText().toString();
                    String ins_date = edInsDate.getText().toString();
                    new updateFinal().execute(blockID, vc, misscoat, pinholes, bubbles, voids, od, min_ndft, max_ndft, area, test, min_dft, max_dft, avg_dft, doc, comment, doc_name, type_of_defect, repair_date, repair_method, standard, ins_date);
                    break;
                case R.id.btnBack:
                    intent = new Intent(InspeksiFinalCheckActivity.this, InspeksiFinalListActivity.class);
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

    private void showBlock(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            tvNamaBlok.setText(blockName);
            edPosisiBlok.setText(blockLocation);
            edInsDate.setText(jo.getString("ins_date"));

            if(jo.getString("visual_condition")!=null){
                cbVisualCondition.setChecked(true);
                ivVisualCondition.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivVisualCondition))
                        .execute(jo.getString("visual_condition"));
            }
            else cbVisualCondition.setChecked(false);
            if(jo.getString("misscoat")!=null){
                cbMissCoat.setChecked(true);
                ivMissCoat.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivMissCoat))
                        .execute(jo.getString("misscoat"));
            }
            else cbMissCoat.setChecked(false);
            if(jo.getString("pinholes")!=null){
                cbPinholes.setChecked(true);
                ivPinholes.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivPinholes))
                        .execute(jo.getString("pinholes"));
            }
            else cbPinholes.setChecked(false);
            if(jo.getString("bubbles")!=null){
                cbBubbles.setChecked(true);
                ivBubbles.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivBubbles))
                        .execute(jo.getString("bubbles"));
            }
            else cbBubbles.setChecked(false);
            if(jo.getString("voids")!=null){
                cbVoids.setChecked(true);
                ivVoids.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivVoids))
                        .execute(jo.getString("voids"));
            }
            else cbVoids.setChecked(false);
            if(jo.getString("holiday")!=null){
                cbHoliday.setChecked(true);
                ivHoliday.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivHoliday))
                        .execute(jo.getString("holiday"));
            }
            else cbHoliday.setChecked(false);
            if(jo.getString("other_defect")!=null){
                cbOtherDefect.setChecked(true);
                ivOtherDefect.setVisibility(View.VISIBLE);
                new DownloadImageTask((ImageView) findViewById(R.id.ivOtherDefect))
                        .execute(jo.getString("other_defect"));
            }
            else cbOtherDefect.setChecked(false);

            if(jo.getString("min_ndft")!=null) edMinNDFT.setText(jo.getString("min_ndft"));
            if(jo.getString("max_ndft")!=null) edMaxNDFT.setText(jo.getString("max_ndft"));
            if(jo.getString("area")!=null) edArea.setText(jo.getString("area"));
            if(jo.getString("test_point")!=null) edTestPoint.setText(jo.getString("test_point"));
            if(jo.getString("min_dft")!=null) edMinDFT.setText(jo.getString("min_dft"));
            if(jo.getString("max_dft")!=null) edMaxDFT.setText(jo.getString("max_dft"));
            if(jo.getString("avg_dft")!=null) edAvgDFT.setText(jo.getString("avg_dft"));
            if(jo.getString("typedefect")!=null) edTypeOfDefect.setText(jo.getString("typedefect"));
            if(jo.getString("repair_method")!=null) edRepairMethod.setText(jo.getString("repair_method"));
            if(jo.getString("repair_confirm")!=null) edRepairConfirmDate.setText(jo.getString("repair_confirm"));
            if(jo.getString("standard")!=null) edStandard.setText(jo.getString("standard"));

            if(jo.getString("doc1")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation1)).execute(jo.getString("doc1"));
            if(jo.getString("doc2")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation2)).execute(jo.getString("doc2"));
            if(jo.getString("doc3")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation3)).execute(jo.getString("doc3"));
            if(jo.getString("doc4")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation4)).execute(jo.getString("doc4"));
            if(jo.getString("doc5")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation5)).execute(jo.getString("doc5"));
            if(jo.getString("doc6")!=null) new DownloadImageTask((ImageView) findViewById(R.id.ivDocumentation6)).execute(jo.getString("doc6"));

            if(jo.getString("comment")!=null) edBlockComment.setText(jo.getString("comment"));
            if(jo.getString("doc_name")!=null) edNamaDokumen.setText(jo.getString("doc_name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void help(View view)
    {
        Intent intent = new Intent(InspeksiFinalCheckActivity.this, UploadFileActivity.class);
        extras.putString("FROM", "HELP");
        intent.putExtras(extras);
        startActivity(intent);
    }

    class getFinal extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getFinal() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading final inspection...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(InspeksiFinalCheckActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

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

            create_url = "http://mobile4day.com/ship-inspection/get_final.php";

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

    class updateFinal extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updateFinal(){
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
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(created && create.equals("1")){
                Toast.makeText(InspeksiFinalCheckActivity.this, "You Final Inspection has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InspeksiFinalCheckActivity.this, InspeksiFinalListActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(InspeksiFinalCheckActivity.this, "failed! s= " + s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id_blok = params[0];
            String visual_condition = params[1];
            String misscoat = params[2];
            String pinholes = params[3];
            String bubbles = params[4];
            String voids = params[5];
            String other_defect = params[6];
            String min_ndft = params[7];
            String max_ndft = params[8];
            String area = params[9];
            String test_point = params[10];
            String min_dft = params[11];
            String max_dft = params[12];
            String avg_dft = params[13];
            String doc = params[14];
            String comment = params[15];
            String doc_name = params[16];
            String typedefect = params[17];
            String repair_date = params[18];
            String repair_method = params[19];
            String standard = params[20];
            String ins_date = params[21];

            String create_url = "http://mobile4day.com/ship-inspection/update_final.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id_blok", "UTF-8")+"="+URLEncoder.encode(id_blok,"UTF-8")+"&"+
                                URLEncoder.encode("visual_condition","UTF-8")+"="+URLEncoder.encode(visual_condition,"UTF-8")+"&"+
                                URLEncoder.encode("misscoat","UTF-8")+"="+URLEncoder.encode(misscoat,"UTF-8")+"&"+
                                URLEncoder.encode("pinholes","UTF-8")+"="+URLEncoder.encode(pinholes,"UTF-8")+"&"+
                                URLEncoder.encode("bubbles","UTF-8")+"="+URLEncoder.encode(bubbles,"UTF-8")+"&"+
                                URLEncoder.encode("voids","UTF-8")+"="+URLEncoder.encode(voids,"UTF-8")+"&"+
                                URLEncoder.encode("other_defect","UTF-8")+"="+URLEncoder.encode(other_defect,"UTF-8")+"&"+
                                URLEncoder.encode("min_ndft","UTF-8")+"="+URLEncoder.encode(min_ndft,"UTF-8")+"&"+
                                URLEncoder.encode("max_ndft","UTF-8")+"="+URLEncoder.encode(max_ndft,"UTF-8")+"&"+
                                URLEncoder.encode("area","UTF-8")+"="+URLEncoder.encode(area,"UTF-8")+"&"+
                                URLEncoder.encode("test_point","UTF-8")+"="+URLEncoder.encode(test_point,"UTF-8")+"&"+
                                URLEncoder.encode("min_dft","UTF-8")+"="+URLEncoder.encode(min_dft,"UTF-8")+"&"+
                                URLEncoder.encode("max_dft","UTF-8")+"="+URLEncoder.encode(max_dft,"UTF-8")+"&"+
                                URLEncoder.encode("avg_dft","UTF-8")+"="+URLEncoder.encode(avg_dft,"UTF-8")+"&"+
                                URLEncoder.encode("doc","UTF-8")+"="+URLEncoder.encode(doc,"UTF-8")+"&"+
                                URLEncoder.encode("comment","UTF-8")+"="+URLEncoder.encode(comment,"UTF-8")+"&"+
                                URLEncoder.encode("doc_name","UTF-8")+"="+URLEncoder.encode(doc_name,"UTF-8")+"&"+
                                URLEncoder.encode("typedefect","UTF-8")+"="+URLEncoder.encode(typedefect,"UTF-8")+"&"+
                                URLEncoder.encode("repair_confirm","UTF-8")+"="+URLEncoder.encode(repair_date,"UTF-8")+"&"+
                                URLEncoder.encode("repair_method","UTF-8")+"="+URLEncoder.encode(repair_method,"UTF-8")+"&"+
                                URLEncoder.encode("standard","UTF-8")+"="+URLEncoder.encode(standard,"UTF-8")+"&"+
                                URLEncoder.encode("ins_date","UTF-8")+"="+URLEncoder.encode(ins_date,"UTF-8");
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
