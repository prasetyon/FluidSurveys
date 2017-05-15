package com.mobile4day.absensicamp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import java.util.ArrayList;

public class AbsenSesi extends AppCompatActivity {

    Button btnOpening, btnSesi1, btnSesi23, btnSesi45, btnMinggu, btnPenutup;
    Spinner spinnerListSesi;
    ListView lvListSesi;
    ListAdapter listAdapter;
    TextView tvUser, tvHeader;
    String event="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_sesi);

        initiate();
    }


    private void initiate()
    {
        spinnerListSesi = (Spinner) findViewById(R.id.spinnerListSesi);
        tvUser = (TextView) findViewById(R.id.tvUser);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        btnSesi1 = (Button) findViewById(R.id.btnSesi1);
        btnSesi23 = (Button) findViewById(R.id.btnSesi23);
        btnSesi45 = (Button) findViewById(R.id.btnSesi45);
        btnMinggu = (Button) findViewById(R.id.btnMinggu);
        btnPenutup = (Button) findViewById(R.id.btnPenutup);
        btnOpening = (Button) findViewById(R.id.btnOpening);
        lvListSesi = (ListView) findViewById(R.id.lvListSesi);
        btnOpening.setOnClickListener(activity);
        btnSesi1.setOnClickListener(activity);
        btnSesi23.setOnClickListener(activity);
        btnSesi45.setOnClickListener(activity);
        btnMinggu.setOnClickListener(activity);
        btnPenutup.setOnClickListener(activity);
        new getSesi().execute();
        spinnerListSesi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //if(position>0) Toast.makeText(AbsenSesi.this, getEvent(spinnerListSesi.getSelectedItem().toString()), Toast.LENGTH_LONG).show();
                String x = spinnerListSesi.getSelectedItem().toString();
                event = getEvent(x.substring(0, x.indexOf(" ")));
                if(position>0){
                    new getList().execute(event);
                    lvListSesi.setVisibility(View.VISIBLE);
                }
                else{
                    lvListSesi.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String getEvent(String event)
    {
        if(event.equals("Opening")) return "OPENING";
        else if(event.equals("Sesi1")) return "SESI1";
        else if(event.equals("Sesi2-3")) return "SESI23";
        else if(event.equals("Sesi4-5")) return "SESI45";
        else if(event.equals("IbadahMinggu")) return "IBADAH_MINGGU";
        else return "CLOSING";
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnOpening:
                    event = "OPENING";
                    Toast.makeText(AbsenSesi.this, event, Toast.LENGTH_LONG).show();
                    scanQR(v);
                    break;
                case R.id.btnSesi1:
                    event = "SESI1";
                    Toast.makeText(AbsenSesi.this, event, Toast.LENGTH_LONG).show();
                    scanQR(v);
                    break;
                case R.id.btnSesi23:
                    event = "SESI23";
                    Toast.makeText(AbsenSesi.this, event, Toast.LENGTH_LONG).show();
                    scanQR(v);
                    break;
                case R.id.btnSesi45:
                    event = "SESI45";
                    Toast.makeText(AbsenSesi.this, event, Toast.LENGTH_LONG).show();
                    scanQR(v);
                    break;
                case R.id.btnMinggu:
                    event = "IBADAH_MINGGU";
                    Toast.makeText(AbsenSesi.this, event, Toast.LENGTH_LONG).show();
                    scanQR(v);
                    break;
                case R.id.btnPenutup:
                    event = "CLOSING";
                    Toast.makeText(AbsenSesi.this, event, Toast.LENGTH_LONG).show();
                    scanQR(v);
                    break;
            }
        }
    };

    public void scanQR(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(String.valueOf("SCAN"));
        integrator.setResultDisplayDuration(0);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // update database
            new absen().execute(event, scanContent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void showList(String s, String event)
    {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            final ArrayList<String> nrp = new ArrayList<>();
            final ArrayList<String> name = new ArrayList<>();
            final ArrayList<String> status = new ArrayList<>();
            final ArrayList<String> ids = new ArrayList<>();

            nrp.add("NRP");
            status.add("");
            name.add("NAMA");
            ids.add("ID");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                nrp.add(jo.getString("nrp"));
                if(event.equals("OPENING")) status.add(jo.getString("opening"));
                else if(event.equals("CLOSING")) status.add(jo.getString("closing"));
                else if(event.equals("SESI1")) status.add(jo.getString("sesi1"));
                else if(event.equals("SESI23")) status.add(jo.getString("sesi23"));
                else if(event.equals("SESI45")) status.add(jo.getString("sesi45"));
                else if(event.equals("IBADAH_MINGGU")) status.add(jo.getString("ibadah_minggu"));
                name.add(jo.getString("nama"));
                ids.add(jo.getString("id"));
            }

            listAdapter = new ListAdapter(this, nrp, name, status, ids);
            listAdapter.notifyDataSetChanged();
            lvListSesi.setAdapter(listAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getList extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getList(){
            //ProgressDialog = new ProgressDialog(AbsenSesi.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ProgressDialog.setMessage("Reading participant list...");
            //ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            //ProgressDialog.dismiss();
            //Toast.makeText(PesertaActivity.this, s, Toast.LENGTH_SHORT).show();

            //Toast.makeText(AbsenSesi.this, s, Toast.LENGTH_LONG).show();
            showList(s, event);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/pkmbk-camp/get_absensi.php";
            String event = params[0];

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("event", "UTF-8")+"="+URLEncoder.encode(event,"UTF-8");
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
    class getSesi extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getSesi(){
            //ProgressDialog = new ProgressDialog(AbsenSesi.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ProgressDialog.setMessage("Reading participant list...");
            //ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            //ProgressDialog.dismiss();
            //Toast.makeText(PesertaActivity.this, s, Toast.LENGTH_SHORT).show();

            //Toast.makeText(AbsenSesi.this, s, Toast.LENGTH_LONG).show();
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                ArrayList<String> list = new ArrayList<>();

                JSONObject jo = result.getJSONObject(0);

                list.add("Click here for info");
                list.add("Opening "+jo.getString("opening")+"/"+jo.getString("all"));
                list.add("Sesi1 "+jo.getString("sesi1")+"/"+jo.getString("all"));
                list.add("Sesi2-3 "+jo.getString("sesi23")+"/"+jo.getString("all"));
                list.add("Sesi4-5 "+jo.getString("sesi45")+"/"+jo.getString("all"));
                list.add("IbadahMinggu "+jo.getString("ibadah_minggu")+"/"+jo.getString("all"));
                list.add("Closing "+jo.getString("closing")+"/"+jo.getString("all"));

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AbsenSesi.this,android.R.layout.simple_spinner_item, list);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerListSesi.setAdapter(spinnerAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/pkmbk-camp/get_sesi_ok.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("event", "UTF-8")+"="+URLEncoder.encode("ASSIGN","UTF-8");
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

    class absen extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public absen(){
            ProgressDialog = new ProgressDialog(AbsenSesi.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ProgressDialog.setMessage("Scanning participant id...");
            //ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            //ProgressDialog.dismiss();
            Toast.makeText(AbsenSesi.this, s, Toast.LENGTH_SHORT).show();

            JSONObject jsonObject;
            try{
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");
                JSONObject jo = result.getJSONObject(0);

                tvUser.setText(jo.getString("nama")+" --> "+event);
                lvListSesi.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/pkmbk-camp/absensi.php";
            String event = params[0];
            String id = params[1];

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("event", "UTF-8")+"="+URLEncoder.encode(event,"UTF-8")+"&"+
                        URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
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

    public class ListAdapter extends BaseAdapter {
        ArrayList<String> nrp;
        Context context;
        ArrayList<String> name;
        ArrayList<String> status;
        ArrayList<String> id;
        private LayoutInflater inflater=null;
        public ListAdapter(Activity activity, ArrayList<String> nrp, ArrayList<String> name, ArrayList<String> status,
                           ArrayList<String> id) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.nrp = nrp;
            this.name = name;
            this.status = status;
            this.id = id;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return nrp.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView tvNrp;
            TextView tvNama;
            TextView tvStatus;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.list_sesi, null);
            holder.tvNrp =(TextView) rowView.findViewById(R.id.tvNrp);
            holder.tvNama =(TextView) rowView.findViewById(R.id.tvNama);
            holder.tvStatus =(TextView) rowView.findViewById(R.id.tvStatus);

            holder.tvNama.setText(name.get(position));
            holder.tvNrp.setText(nrp.get(position));
            holder.tvStatus.setText(status.get(position));

            if(status.get(position).equals("OK")) holder.tvStatus.setTextColor(getResources().getColor(R.color.colorGreen));
            else if(status.get(position).equals("NO")) holder.tvStatus.setTextColor(getResources().getColor(R.color.colorRed));

            return rowView;
        }
    }
}
