package blockStage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal1.R;

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

public class BlockStageAddActivity extends AppCompatActivity {

    Button btnBack, btnSubmit;
    Spinner spinnerLokasiAfterErection;
    EditText edNamaBlok, edPrimerCoat, edTightCoat, edFinishCoat, edArea;
    CheckBox cbSwp1, cbSsps, cbSwp2, cbSwp1ae, cbSwp2ae, cbSsp1stCoat, cb1stCoat1stStripe, cbFirstStripeSecondStripe, cbSecondStripeSecondCoat, cbSecondCoatThirdCoat, cbThirdCoatForthCoat, cbForthCoatFifthCoat, cbStandard;
    CheckBox cbPullOff;
    boolean doubleBackToExitPressedOnce = false;
    Context Activity;
    private boolean created=false;
    private String lokasi;
    private String create="";
    private Bundle extras;
    private String projectID;
    private String username;
    private String role;
    private String from;
    private String projectName;

    private void initiate()
    {
        edNamaBlok = (EditText) findViewById(R.id.edNamaBlok);
        cbStandard = (CheckBox) findViewById(R.id.cbStandard);
        cbPullOff = (CheckBox) findViewById(R.id.cbPullOff);
        cbSsp1stCoat = (CheckBox) findViewById(R.id.cbSsp1stCoat);
        cb1stCoat1stStripe = (CheckBox) findViewById(R.id.cb1stCoat1stStripe);
        cbFirstStripeSecondStripe = (CheckBox) findViewById(R.id.cbFirstStripeSecondStripe);
        cbSecondStripeSecondCoat = (CheckBox) findViewById(R.id.cbSecondStripeSecondCoat);
        cbSecondCoatThirdCoat = (CheckBox) findViewById(R.id.cbSecondCoatThirdCoat);
        cbThirdCoatForthCoat = (CheckBox) findViewById(R.id.cbThirdCoatForthCoat);
        cbForthCoatFifthCoat = (CheckBox) findViewById(R.id.cbForthCoatFifthCoat);
        edNamaBlok = (EditText) findViewById(R.id.edNamaBlok);
        edPrimerCoat = (EditText) findViewById(R.id.edPrimerCoat);
        edTightCoat = (EditText) findViewById(R.id.edTightCoat);
        edFinishCoat = (EditText) findViewById(R.id.edFinishCoat);
        edArea = (EditText) findViewById(R.id.edArea);
        cbSwp1 = (CheckBox) findViewById(R.id.cbSwp1);
        cbSwp2 = (CheckBox) findViewById(R.id.cbSwp2);
        cbSwp1ae = (CheckBox) findViewById(R.id.cbSwp1ae);
        cbSwp2ae = (CheckBox) findViewById(R.id.cbSwp2ae);
        cbSsps = (CheckBox) findViewById(R.id.cbSsps);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnUpdate);
        Activity = this;

        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_stage_add);
        setTitle("BLOCK STAGE");

        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");

        initiate();

        spinnerLokasiAfterErection = (Spinner) findViewById(R.id.spinnerLokasiAfterErection);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.location_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerLokasiAfterErection.setAdapter(spinnerAdapter);
        spinnerLokasiAfterErection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lokasi = spinnerLokasiAfterErection.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnUpdate:
                    String namaBlok = edNamaBlok.getText().toString();
                    String comply, swp1, sspsp, swp2, swp1ae, swp2ae, ssp1c, b1c1sc, b1sc2sc, b2sc2c, b2c3c, b3c4c, b4c5c;
                    if(cbStandard.isChecked())  comply = "1";
                    else comply="0";
                    if(cbSwp1.isChecked())  swp1 = "bc";
                    else swp1="";
                    if(cbSsps.isChecked())  sspsp = "bc";
                    else sspsp="";
                    if(cbSwp2.isChecked())  swp2 = "bc";
                    else swp2="";
                    if(cbSwp1ae.isChecked())  swp1ae = "bc";
                    else swp1ae="";
                    if(cbSwp2ae.isChecked())  swp2ae = "bc";
                    else swp2ae="";
                    if(cbSsp1stCoat.isChecked())  ssp1c = "bc";
                    else ssp1c="";
                    if(cb1stCoat1stStripe.isChecked())  b1c1sc = "bc";
                    else b1c1sc="";
                    if(cbFirstStripeSecondStripe.isChecked())  b1sc2sc = "bc";
                    else b1sc2sc="";
                    if(cbSecondStripeSecondCoat.isChecked())  b2sc2c = "bc";
                    else b2sc2c="";
                    if(cbSecondCoatThirdCoat.isChecked())  b2c3c = "bc";
                    else b2c3c="";
                    if(cbThirdCoatForthCoat.isChecked())  b3c4c = "bc";
                    else b3c4c="";
                    if(cbForthCoatFifthCoat.isChecked())  b4c5c = "bc";
                    else b4c5c="";
                    String pulloff;
                    if(cbPullOff.isChecked())  pulloff = "bc";
                    else pulloff="";
                    String area;
                    String primer = edPrimerCoat.getText().toString();
                    String tight = edTightCoat.getText().toString();
                    String finish = edFinishCoat.getText().toString();
                    area = edArea.getText().toString();

                    new NewBlock().execute(projectID, namaBlok, lokasi, comply, swp1, sspsp, swp2, ssp1c, b1c1sc, b1sc2sc, b2sc2c, b2c3c, b3c4c, b4c5c, area, primer, tight, finish, swp1ae, swp2ae, pulloff);
                    break;
                case R.id.btnBack:
                    intent = new Intent(BlockStageAddActivity.this, BlockStageActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    class NewBlock extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public NewBlock(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Creating block...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            created = true;
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(created && create.equals("1")){
                Toast.makeText(BlockStageAddActivity.this, "You block has been created!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BlockStageAddActivity.this, BlockStageListActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(BlockStageAddActivity.this, "You block has not been created! Error = " + s, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id_proyek = params[0];
            String nama_blok = params[1];
            String lokasi_blok = params[2];
            String comply = params[3];
            String swp1 = params[4];
            String sspsp = params[5];
            String swp2 = params[6];
            String ssp1c = params[7];
            String b1c1sc = params[8];
            String b1sc2sc = params[9];
            String b2sc2c = params[10];
            String b2c3c = params[11];
            String b3c4c = params[12];
            String b4c5c = params[13];
            String area = params[14];
            String primer = params[15];
            String tight = params[16];
            String finish = params[17];
            String swp1ae = params[18];
            String swp2ae = params[19];
            String pulloff = params[20];

            String create_url = "http://mobile4day.com/ship-inspection/new_block.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id_proyek", "UTF-8")+"="+URLEncoder.encode(id_proyek,"UTF-8")+"&"+
                                URLEncoder.encode("nama_blok","UTF-8")+"="+URLEncoder.encode(nama_blok,"UTF-8")+"&"+
                                URLEncoder.encode("lokasi_blok","UTF-8")+"="+URLEncoder.encode(lokasi_blok,"UTF-8")+"&"+
                                URLEncoder.encode("comply","UTF-8")+"="+URLEncoder.encode(comply,"UTF-8")+"&"+
                                URLEncoder.encode("swp1","UTF-8")+"="+URLEncoder.encode(swp1,"UTF-8")+"&"+
                                URLEncoder.encode("sspsp","UTF-8")+"="+URLEncoder.encode(sspsp,"UTF-8")+"&"+
                                URLEncoder.encode("swp2","UTF-8")+"="+URLEncoder.encode(swp2,"UTF-8")+"&"+
                                URLEncoder.encode("ssp1c","UTF-8")+"="+URLEncoder.encode(ssp1c,"UTF-8")+"&"+
                                URLEncoder.encode("1c1sc","UTF-8")+"="+URLEncoder.encode(b1c1sc,"UTF-8")+"&"+
                                URLEncoder.encode("1sc2sc","UTF-8")+"="+URLEncoder.encode(b1sc2sc,"UTF-8")+"&"+
                                URLEncoder.encode("2sc2c","UTF-8")+"="+URLEncoder.encode(b2sc2c,"UTF-8")+"&"+
                                URLEncoder.encode("2c3c","UTF-8")+"="+URLEncoder.encode(b2c3c,"UTF-8")+"&"+
                                URLEncoder.encode("3c4c","UTF-8")+"="+URLEncoder.encode(b3c4c,"UTF-8")+"&"+
                                URLEncoder.encode("4c5c","UTF-8")+"="+URLEncoder.encode(b4c5c,"UTF-8")+"&"+
                                URLEncoder.encode("primer","UTF-8")+"="+URLEncoder.encode(primer,"UTF-8")+"&"+
                                URLEncoder.encode("tight","UTF-8")+"="+URLEncoder.encode(tight,"UTF-8")+"&"+
                                URLEncoder.encode("finish","UTF-8")+"="+URLEncoder.encode(finish,"UTF-8")+"&"+
                                URLEncoder.encode("area","UTF-8")+"="+URLEncoder.encode(area,"UTF-8")+"&"+
                                URLEncoder.encode("swp1ae","UTF-8")+"="+URLEncoder.encode(swp1ae,"UTF-8")+"&"+
                                URLEncoder.encode("swp2ae","UTF-8")+"="+URLEncoder.encode(swp2ae,"UTF-8")+"&"+
                                URLEncoder.encode("pulloff","UTF-8")+"="+URLEncoder.encode(pulloff,"UTF-8");
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
