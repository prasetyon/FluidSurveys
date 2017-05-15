package inspeksiFinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Andy on 01/11/2016.
 */

public class InspeksiFinalEditActivity extends AppCompatActivity{
    Button btnBack, btnSubmit;
    EditText edNamaBlok;
    Spinner spinnerLokasi, spinnerFrame, spinnerNama, spinnerWelded, spinnerType;
    boolean doubleBackToExitPressedOnce = false;
    Context Activity;
    private boolean created;
    private String create;
    private Bundle extras;
    private String lokasi, frame, nama, welded, type;
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
        setContentView(R.layout.activity_assembly_edit);
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

        edNamaBlok = (EditText) findViewById(R.id.edNamaBlok);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnBack.setOnClickListener(activity);
        btnSubmit.setOnClickListener(activity);

        spinnerLokasi = (Spinner) findViewById(R.id.spinnerLokasi);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.location_list, android.R.layout.simple_spinner_item);
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

        spinnerFrame = (Spinner) findViewById(R.id.spinnerFrame);
        ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(this, R.array.frame_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerFrame.setAdapter(spinnerAdapter1);
        spinnerFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                frame = spinnerFrame.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerNama = (Spinner) findViewById(R.id.spinnerNama);
        ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(this, R.array.nama_array, android.R.layout.simple_spinner_item);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerNama.setAdapter(spinnerAdapter2);
        spinnerNama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nama = spinnerLokasi.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerWelded = (Spinner) findViewById(R.id.spinnerWelded);
        ArrayAdapter<CharSequence> spinnerAdapter3 = ArrayAdapter.createFromResource(this, R.array.welded_array, android.R.layout.simple_spinner_item);
        spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerWelded.setAdapter(spinnerAdapter3);
        spinnerWelded.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                welded = spinnerWelded.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> spinnerAdapter4 = ArrayAdapter.createFromResource(this, R.array.type_of_weld, android.R.layout.simple_spinner_item);
        spinnerAdapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerType.setAdapter(spinnerAdapter4);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = spinnerType.getSelectedItem().toString();
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
                case R.id.btnSubmit:
                    String nama = edNamaBlok.getText().toString();

                    new updateBlock().execute(nama, lokasi);
                    break;
                case R.id.btnBack:
                    intent = new Intent(InspeksiFinalEditActivity.this, InspeksiFinalListActivity.class);
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

            spinnerLokasi.setSelection(getSpinnerLocation(jo.getString("posisi")));
            spinnerFrame.setSelection(getSpinnerFrame(jo.getString("frame")));
            spinnerNama.setSelection(getSpinnerName(jo.getString("nama_blok")));
            spinnerWelded.setSelection(getSpinnerWelded(jo.getString("welded")));
            spinnerWelded.setSelection(getSpinnerType(jo.getString("type")));

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

            create_url = "http://mobile4day.com/welding-inspection/get_blok_detail.php";

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
                Toast.makeText(InspeksiFinalEditActivity.this, "You block has been updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InspeksiFinalEditActivity.this, InspeksiFinalListActivity.class);
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

            String nama_blok = params[0];
            String lokasi_blok = params[1];

            String create_url = "http://mobile4day.com/welding-inspection/update_blok.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id_blok", "UTF-8")+"="+URLEncoder.encode(blockID,"UTF-8")+"&"+
                                URLEncoder.encode("nama_blok","UTF-8")+"="+URLEncoder.encode(nama_blok,"UTF-8")+"&"+
                                URLEncoder.encode("posisi","UTF-8")+"="+URLEncoder.encode(lokasi_blok,"UTF-8")+"&"+
                                URLEncoder.encode("frame","UTF-8")+"="+URLEncoder.encode(frame,"UTF-8")+"&"+
                                URLEncoder.encode("welded","UTF-8")+"="+URLEncoder.encode(welded,"UTF-8")+"&"+
                                URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");
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
        if(position.equals("Bottom")) return 0;
        else if(position.equals("Boottop")) return 1;
        else if(position.equals("Top Side")) return 2;
        else if(position.equals("Seachest")) return 3;
        else if(position.equals("Weather Deck")) return 4;
        else if(position.equals("Structure Exterior")) return 5;
        else if(position.equals("Structure Interior")) return 6;
        else if(position.equals("Water Ballast Tank")) return 7;
        else if(position.equals("Cargo Oil Tank")) return 8;
        else if(position.equals("Cargo Hatch Coaming")) return 9;
        else if(position.equals("Slop Tank")) return 10;
        else if(position.equals("Fresh Water Tank")) return 11;
        else if(position.equals("F.O, D.O Tanks")) return 12;
        else if(position.equals("After Peak Tank")) return 13;
        else if(position.equals("Fore Peak Tank")) return 14;
        else return 15;
    }

    private int getSpinnerFrame(String position)
    {
        if(position.equals("1-2")) return 0;
        else if(position.equals("3-4")) return 1;
        else if(position.equals("5-6")) return 2;
        else if(position.equals("7-8")) return 3;
        else if(position.equals("9-10")) return 4;
        else if(position.equals("11-12")) return 5;
        else if(position.equals("13-14")) return 6;
        else if(position.equals("15-16")) return 7;
        else if(position.equals("17-18")) return 8;
        else if(position.equals("19-20")) return 9;
        else if(position.equals("21-22")) return 10;
        else if(position.equals("23-24")) return 11;
        else if(position.equals("25-26")) return 12;
        else if(position.equals("27-28")) return 13;
        else if(position.equals("29-30")) return 14;
        else if(position.equals("31-32")) return 15;
        else if(position.equals("33-34")) return 16;
        else if(position.equals("35-36")) return 17;
        else if(position.equals("37-38")) return 18;
        else if(position.equals("39-40")) return 19;
        else return 20;
    }

    private int getSpinnerName(String position)
    {
        if(position.equals("Main Deck Plating")) return 0;
        else if(position.equals("Deck Long. Grider")) return 1;
        else if(position.equals("Deck Transverse Web")) return 2;
        else if(position.equals("Deck Girder / Web")) return 3;
        else if(position.equals("DK. Transv Beam (O.S TKS)")) return 4;
        else if(position.equals("DK. Transv Beam (I.S TKS)")) return 5;
        else if(position.equals("Side Shell")) return 6;
        else if(position.equals("Longitudinal Bhd. Plating")) return 7;
        else if(position.equals("Transverse Bhd. Plating")) return 8;
        else if(position.equals("Side Shell Plating")) return 9;
        else if(position.equals("Side Webs")) return 10;
        else if(position.equals("Side Frames (O.S Tanks)")) return 11;
        else if(position.equals("Side Frames (I.S Tanks)")) return 12;
        else if(position.equals("Bottom Shell")) return 13;
        else if(position.equals("Floor")) return 14;
        else if(position.equals("Floor in Engine Room")) return 15;
        else if(position.equals("Bottom Girder")) return 16;
        else if(position.equals("Floor Girder")) return 17;
        else if(position.equals("Engine Girder")) return 18;
        else if(position.equals("DK. Transverse Web")) return 19;
        else if(position.equals("Vert. Stiffeners (O.S Tanks)")) return 20;
        else if(position.equals("Vert. Stiffeners (I.S Tanks)")) return 21;
        else if(position.equals("Tank Top Plating")) return 22;
        else if(position.equals("Tank Top Beams")) return 23;
        else if(position.equals("Tank Top Girder")) return 24;
        else if(position.equals("Transverse Bhd.")) return 25;
        else if(position.equals("Longitudinal Bhd.")) return 26;
        else if(position.equals("Fore Peak Platform Plating")) return 27;
        else if(position.equals("Transverse Beam<")) return 28;
        else if(position.equals("Platform Girder")) return 29;
        else if(position.equals("All Brackets Parts")) return 30;
        else return 31;
    }

    private int getSpinnerWelded(String position)
    {
        if(position.equals("Main Deck Plating")) return 0;
        else if(position.equals("Face Plate")) return 1;
        else if(position.equals("Side Shell Plating")) return 2;
        else if(position.equals("Bottom Shell Plating")) return 3;
        else if(position.equals("Keel Plate")) return 4;
        else if(position.equals("Longitudinal Bhd. Plating")) return 5;
        else if(position.equals("Transverse Bhd. Plating")) return 6;
        else if(position.equals("Tank Top Plating")) return 7;
        else if(position.equals("F. Peak Platform Plating")) return 8;
        else if(position.equals("Floors/DK/Shell/Stffnrs")) return 9;
        else return 10;
    }

    private int getSpinnerType(String position)
    {
        if(position.equals("Butt")) return 0;
        else if(position.equals("Fillet")) return 1;
        else return 2;
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
