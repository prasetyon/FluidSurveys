package com.mobile4day.absensicamp;

import android.app.Activity;
import android.app.Dialog;
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
import java.util.List;

public class Regis extends AppCompatActivity {

    Button btnRegis, btnList;
    ListView lvListPeserta;
    ListAdapter listAdapter;
    TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        initiate();
    }

    private void initiate()
    {
        tvUser = (TextView) findViewById(R.id.tvUser);
        btnList = (Button) findViewById(R.id.btnList);
        btnRegis = (Button) findViewById(R.id.btnRegis);
        lvListPeserta = (ListView) findViewById(R.id.lvListPeserta);
        lvListPeserta.setVisibility(View.GONE);
        btnList.setOnClickListener(activity);
        btnRegis.setOnClickListener(activity);
        new getRegis().execute();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnList:
                    if(btnList.getText().equals("HIDE PARTICIPANTS")){
                        new getRegis().execute();
                        lvListPeserta.setVisibility(View.GONE);
                    }
                    else{
                        btnList.setText("HIDE PARTICIPANTS");
                        lvListPeserta.setVisibility(View.VISIBLE);
                        new getList().execute();
                        //listAdapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.btnRegis:
                    scanQR(v);
                    if(btnList.getText().equals("HIDE PARTICIPANTS")){
                        new getList().execute();
                        listAdapter.notifyDataSetChanged();
                    }
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
            new absen().execute(scanContent);

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void showList(String s)
    {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            final ArrayList<String> nrp = new ArrayList<>();
            final ArrayList<String> name = new ArrayList<>();
            final ArrayList<String> status = new ArrayList<>();
            final ArrayList<String> bus = new ArrayList<>();
            final ArrayList<String> ids = new ArrayList<>();

            nrp.add("NRP");
            status.add("");
            name.add("NAMA");
            bus.add("BUS");
            ids.add("ID");

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                nrp.add(jo.getString("nrp"));
                status.add(jo.getString("regis"));
                name.add(jo.getString("nama"));
                bus.add(jo.getString("bus"));
                ids.add(jo.getString("id"));
            }

            listAdapter = new ListAdapter(this, nrp, name, status, bus, ids);
            lvListPeserta.setAdapter(listAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getList extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getList(){
            ProgressDialog = new ProgressDialog(Regis.this);
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

            showList(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/pkmbk-camp/get_absensi.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("event", "UTF-8")+"="+URLEncoder.encode("REGISTRASI","UTF-8");
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

    class getRegis extends AsyncTask<String, String, String> {
        //android.app.ProgressDialog ProgressDialog;

        public getRegis(){
            //ProgressDialog = new ProgressDialog(Regis.this);
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

                JSONObject jo = result.getJSONObject(0);

                btnList.setText("PARTICIPANT "+jo.getString("regis")+"/"+jo.getString("all"));
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

            String create_url = "http://mobile4day.com/pkmbk-camp/get_regis_ok.php";

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
            //ProgressDialog = new ProgressDialog(Regis.this);
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
            Toast.makeText(Regis.this, s, Toast.LENGTH_SHORT).show();

            JSONObject jsonObject;
            try{
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");
                JSONObject jo = result.getJSONObject(0);

                tvUser.setText(jo.getString("nama")+" --> "+jo.getString("bus"));
                //listAdapter.notifyDataSetChanged();
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
            String id = params[0];

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("event", "UTF-8")+"="+URLEncoder.encode("REGISTRASI","UTF-8")+"&"+
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
        ArrayList<String> bus;
        ArrayList<String> id;
        private LayoutInflater inflater=null;
        public ListAdapter(Activity activity, ArrayList<String> nrp, ArrayList<String> name, ArrayList<String> status,
                           ArrayList<String> bus, ArrayList<String> id) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.nrp = nrp;
            this.name = name;
            this.status = status;
            this.bus = bus;
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
            TextView tvBus;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.list_peserta, null);
            holder.tvNrp =(TextView) rowView.findViewById(R.id.tvNrp);
            holder.tvNama =(TextView) rowView.findViewById(R.id.tvNama);
            holder.tvStatus =(TextView) rowView.findViewById(R.id.tvStatus);
            holder.tvBus =(TextView) rowView.findViewById(R.id.tvBus);

            holder.tvNama.setText(name.get(position));
            holder.tvNrp.setText(nrp.get(position));
            holder.tvStatus.setText(status.get(position));
            holder.tvBus.setText(bus.get(position));

            if(status.get(position).equals("OK")) holder.tvStatus.setTextColor(getResources().getColor(R.color.colorGreen));
            else if(status.get(position).equals("NO")) holder.tvStatus.setTextColor(getResources().getColor(R.color.colorRed));

            return rowView;
        }
    }
}
