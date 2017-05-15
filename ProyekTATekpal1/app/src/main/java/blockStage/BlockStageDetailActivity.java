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

public class BlockStageDetailActivity extends AppCompatActivity {

    Button btnBack, btnUpdate;
    Spinner spinnerLokasi;
    EditText edNamaBlok, edPrimerCoat, edTightCoat, edFinishCoat, edArea;
    CheckBox cbSwp1, cbSsps, cbSwp2, cbSwp1ae, cbSwp2ae, cbSsp1stCoat, cb1stCoat1stStripe, cbFirstStripeSecondStripe, cbSecondStripeSecondCoat, cbSecondCoatThirdCoat, cbThirdCoatForthCoat, cbForthCoatFifthCoat, cbStandard;
    CheckBox cbPullOff;
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

    private void initiate()
    {
        Activity = this;
        edNamaBlok = (EditText) findViewById(R.id.edNamaBlok);
        cbPullOff = (CheckBox) findViewById(R.id.cbPullOff);
        cbSwp2 = (CheckBox) findViewById(R.id.cbSwp2);
        cbSwp1ae = (CheckBox) findViewById(R.id.cbSwp1ae);
        cbSwp2ae = (CheckBox) findViewById(R.id.cbSwp2ae);
        cbStandard = (CheckBox) findViewById(R.id.cbStandard);
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
        cbSsps = (CheckBox) findViewById(R.id.cbSsps);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        spinnerLokasi = (Spinner) findViewById(R.id.spinnerLokasi);

        btnBack.setOnClickListener(activity);
        btnUpdate.setOnClickListener(activity);
    }

    private void setEnable(String role)
    {
        if(role.equals("user") || role.equals("userNo"))
        {
            cbStandard.setEnabled(false);
            cbPullOff.setEnabled(false);
            cbSwp2.setEnabled(false);
            cbSsps.setEnabled(false);
            cbSwp1.setEnabled(false);
            cbSsp1stCoat.setEnabled(false);
            cb1stCoat1stStripe.setEnabled(false);
            cbFirstStripeSecondStripe.setEnabled(false);
            cbSecondStripeSecondCoat.setEnabled(false);
            cbSecondCoatThirdCoat.setEnabled(false);
            cbThirdCoatForthCoat.setEnabled(false);
            cbForthCoatFifthCoat.setEnabled(false);
            edNamaBlok.setEnabled(false);
            edFinishCoat.setEnabled(false);
            edPrimerCoat.setEnabled(false);
            edTightCoat.setEnabled(false);
            edArea.setEnabled(false);
            btnUpdate.setVisibility(View.GONE);
            spinnerLokasi.setEnabled(false);
        }
        else if(role.equals("admin"))
        {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_stage_detail);
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

        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.location_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerLokasi.setAdapter(spinnerAdapter);
        spinnerLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lokasi = spinnerLokasi.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new getBlock().execute();
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
                    else if(cbSwp1.getVisibility()!=View.GONE) swp1="";
                    else swp1="ae";
                    if(cbSsps.isChecked())  sspsp = "bc";
                    else if(cbSsps.getVisibility()!=View.GONE) sspsp="";
                    else sspsp="ae";
                    if(cbSwp2.isChecked())  swp2 = "bc";
                    else if(cbSwp2.getVisibility()!=View.GONE) swp2="";
                    else swp2="ae";
                    if(cbSwp1ae.isChecked())  swp1ae = "bc";
                    else if(cbSwp1ae.getVisibility()!=View.GONE) swp1ae="";
                    else swp1ae="ae";
                    if(cbSwp2ae.isChecked())  swp2ae = "bc";
                    else if(cbSwp2ae.getVisibility()!=View.GONE) swp2ae="";
                    else swp2ae="ae";
                    if(cbSsp1stCoat.isChecked())  ssp1c = "bc";
                    else if(cbSsp1stCoat.getVisibility()!=View.GONE) ssp1c="";
                    else ssp1c="ae";
                    if(cb1stCoat1stStripe.isChecked())  b1c1sc = "bc";
                    else if(cb1stCoat1stStripe.getVisibility()!=View.GONE) b1c1sc="";
                    else b1c1sc="ae";
                    if(cbFirstStripeSecondStripe.isChecked())  b1sc2sc = "bc";
                    else if(cbFirstStripeSecondStripe.getVisibility()!=View.GONE) b1sc2sc="";
                    else b1sc2sc="ae";
                    if(cbSecondStripeSecondCoat.isChecked())  b2sc2c = "bc";
                    else if(cbSecondStripeSecondCoat.getVisibility()!=View.GONE) b2sc2c="";
                    else b2sc2c="ae";
                    if(cbSecondCoatThirdCoat.isChecked())  b2c3c = "bc";
                    else if(cbSecondCoatThirdCoat.getVisibility()!=View.GONE) b2c3c="";
                    else b2c3c="ae";
                    if(cbThirdCoatForthCoat.isChecked())  b3c4c = "bc";
                    else if(cbThirdCoatForthCoat.getVisibility()!=View.GONE) b3c4c="";
                    else b3c4c="ae";
                    if(cbForthCoatFifthCoat.isChecked())  b4c5c = "bc";
                    else if(cbForthCoatFifthCoat.getVisibility()!=View.GONE) b4c5c="";
                    else b4c5c="ae";
                    String pulloff;
                    if(cbPullOff.isChecked())  pulloff = "bc";
                    else if(cbPullOff.getVisibility()!=View.GONE) pulloff="";
                    else pulloff="ae";
                    String area;
                    String primer = edPrimerCoat.getText().toString();
                    String tight = edTightCoat.getText().toString();
                    String finish = edFinishCoat.getText().toString();
                    area = edArea.getText().toString();

                    new updateBlock().execute(projectID, namaBlok, lokasi, comply, swp1, sspsp, swp2, ssp1c, b1c1sc, b1sc2sc, b2sc2c, b2c3c, b3c4c, b4c5c, area, primer, tight, finish, swp1ae, swp2ae, pulloff);
                    break;
                case R.id.btnBack:
                    intent = new Intent(BlockStageDetailActivity.this, BlockStageListActivity.class);
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

            edNamaBlok.setText(jo.getString("nama_blok"));
            spinnerLokasi.setSelection(getSpinnerLocation(jo.getString("lokasi")));
            if(jo.getString("swp1").equals("ae")) cbSwp1.setVisibility(View.GONE);
            else if(jo.getString("swp1").equals("bc")) cbSwp1.setChecked(true);
            if(jo.getString("sspsp").equals("ae")) cbSsps.setVisibility(View.GONE);
            else if(jo.getString("sspsp").equals("bc")) cbSsps.setChecked(true);
            if(jo.getString("swp2").equals("ae")) cbSwp2.setVisibility(View.GONE);
            else if(jo.getString("swp2").equals("bc")) cbSwp2.setChecked(true);
            if(jo.getString("swp1ae").equals("ae")) cbSwp1ae.setVisibility(View.GONE);
            else if(jo.getString("swp1ae").equals("bc")) cbSwp1ae.setChecked(true);
            if(jo.getString("swp2ae").equals("ae")) cbSwp2ae.setVisibility(View.GONE);
            else if(jo.getString("swp2ae").equals("bc")) cbSwp2ae.setChecked(true);
            if(jo.getString("ssp1c").equals("ae")) cbSsp1stCoat.setVisibility(View.GONE);
            else if(jo.getString("ssp1c").equals("bc")) cbSsp1stCoat.setChecked(true);
            if(jo.getString("standard").equals("1")) cbStandard.setChecked(true);
            else cbStandard.setChecked(false);
            if(jo.getString("1c1sc").equals("ae")) cb1stCoat1stStripe.setVisibility(View.GONE);
            else if(jo.getString("1c1sc").equals("bc")) cb1stCoat1stStripe.setChecked(true);
            if(jo.getString("1sc2sc").equals("ae")) cbFirstStripeSecondStripe.setVisibility(View.GONE);
            else if(jo.getString("1sc2sc").equals("bc")) cbFirstStripeSecondStripe.setChecked(true);
            if(jo.getString("2sc2c").equals("ae")) cbSecondStripeSecondCoat.setVisibility(View.GONE);
            else if(jo.getString("2sc2c").equals("bc")) cbSecondStripeSecondCoat.setChecked(true);
            if(jo.getString("2c3c").equals("ae")) cbSecondCoatThirdCoat.setVisibility(View.GONE);
            else if(jo.getString("2c3c").equals("bc")) cbSecondCoatThirdCoat.setChecked(true);
            if(jo.getString("3c4c").equals("ae")) cbThirdCoatForthCoat.setVisibility(View.GONE);
            else if(jo.getString("3c4c").equals("bc")) cbThirdCoatForthCoat.setChecked(true);
            if(jo.getString("4c5c").equals("ae")) cbForthCoatFifthCoat.setVisibility(View.GONE);
            else if(jo.getString("4c5c").equals("bc")) cbForthCoatFifthCoat.setChecked(true);
            if(jo.getString("pulloff").equals("ae")) cbPullOff.setVisibility(View.GONE);
            else if(jo.getString("pulloff").equals("bc")) cbPullOff.setChecked(true);
            edPrimerCoat.setText(jo.getString("primer"));
            edTightCoat.setText(jo.getString("tight"));
            edFinishCoat.setText(jo.getString("finish"));
            edArea.setText(jo.getString("area"));
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

            create_url = "http://mobile4day.com/ship-inspection/get_blok_detail.php";

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
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(created && create.equals("1")){
                Toast.makeText(BlockStageDetailActivity.this, "You block has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BlockStageDetailActivity.this, BlockStageListActivity.class);
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

            String create_url = "http://mobile4day.com/ship-inspection/update_block.php";
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
                                URLEncoder.encode("id_blok","UTF-8")+"="+URLEncoder.encode(blockID,"UTF-8")+"&"+
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

    private int getSpinnerLocation(String position)
    {
        if(position.equals("Bottom (flat part)")) return 0;
        else if(position.equals("Bottom (except above)")) return 1;
        else if(position.equals("Boot-top")) return 2;
        else if(position.equals("Top side")) return 3;
        else if(position.equals("Seachest")) return 4;
        else if(position.equals("Weather Deck")) return 5;
        else if(position.equals("Structure Exterior")) return 6;
        else if(position.equals("Structure Interior")) return 7;
        else if(position.equals("Water Ballast Tank")) return 8;
        else if(position.equals("Cargo Oil Tank")) return 9;
        else if(position.equals("Cargo Hatch Coaming")) return 10;
        else if(position.equals("Slop Tank")) return 11;
        else if(position.equals("Fresh Water Tank")) return 12;
        else if(position.equals("F.O, D.O, L.O Tanks")) return 13;
        else if(position.equals("Waste Oil Tank")) return 14;
        else if(position.equals("After Peak Tank")) return 15;
        else if(position.equals("Fore Peak Tank")) return 16;
        else if(position.equals("Engine Room")) return 17;
        else if(position.equals("Pump Room")) return 18;
        else return 19;
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
