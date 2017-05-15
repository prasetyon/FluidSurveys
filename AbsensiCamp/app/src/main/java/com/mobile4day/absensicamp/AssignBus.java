package com.mobile4day.absensicamp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class AssignBus extends AppCompatActivity {

    TextView tvBus1, tvBus2, tvBus3, tvBus4, tvBus5, tvPioneer, tvNoBus, tvUnassigned;
    ListView lvAssignBus;
    ListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_bus);

        initiate();
        new getBus().execute();
        new getList().execute();
    }

    private void initiate()
    {
        tvBus1 = (TextView) findViewById(R.id.tvBus1);
        tvBus2 = (TextView) findViewById(R.id.tvBus2);
        tvBus3 = (TextView) findViewById(R.id.tvBus3);
        tvBus4 = (TextView) findViewById(R.id.tvBus4);
        tvBus5 = (TextView) findViewById(R.id.tvBus5);
        tvPioneer = (TextView) findViewById(R.id.tvPioneer);
        tvNoBus = (TextView) findViewById(R.id.tvNoBus);
        tvUnassigned = (TextView) findViewById(R.id.tvUnassigned);
        lvAssignBus = (ListView) findViewById(R.id.lvAssignBus);
    }

    private int getBus(String bus)
    {
        if(bus.equals("BUS 1")) return 1;
        else if(bus.equals("BUS 2")) return 2;
        else if(bus.equals("BUS 3")) return 3;
        else if(bus.equals("BUS 4")) return 4;
        else if(bus.equals("BUS 5")) return 5;
        else if(bus.equals("PIONEER")) return 6;
        else if(bus.equals("-")) return 7;
        else return 0;
    }

    private String getBuss(int bus)
    {
        switch(bus){
            case 1: return "BUS 1";
            case 2: return "BUS 2";
            case 3: return "BUS 3";
            case 4: return "BUS 4";
            case 5: return "BUS 5";
            case 6: return "PIONEER";
            case 7: return "-";
            default: return "";
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
            final ArrayList<String> ids = new ArrayList<>();
            final ArrayList<String> bus = new ArrayList<>();
            final ArrayList<ArrayAdapter<CharSequence>> data = new ArrayList<>();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bus_list, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                status.add(jo.getString("status"));
                nrp.add(jo.getString("nrp"));
                name.add(jo.getString("nama"));
                ids.add(jo.getString("id"));
                bus.add(jo.getString("bus"));
                data.add(adapter);
            }

            listAdapter = new ListAdapter(this, ids, nrp, name, status, bus, data);
            lvAssignBus.setAdapter(listAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getList extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getList(){
            ProgressDialog = new ProgressDialog(AssignBus.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading participant list...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(PesertaActivity.this, s, Toast.LENGTH_SHORT).show();

            //Toast.makeText(AbsenSesi.this, s, Toast.LENGTH_LONG).show();
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

    class getBus extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getBus(){
            ProgressDialog = new ProgressDialog(AssignBus.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ProgressDialog.setMessage("Reading participant list...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(PesertaActivity.this, s, Toast.LENGTH_SHORT).show();

            //Toast.makeText(AbsenSesi.this, s, Toast.LENGTH_LONG).show();
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                JSONObject jo = result.getJSONObject(0);

                tvBus1.setText(jo.getString("bus1"));
                tvBus2.setText(jo.getString("bus2"));
                tvBus3.setText(jo.getString("bus3"));
                tvBus4.setText(jo.getString("bus4"));
                tvBus5.setText(jo.getString("bus5"));
                tvPioneer.setText(jo.getString("pioneer"));
                tvNoBus.setText(jo.getString("nobus"));
                tvUnassigned.setText(jo.getString("unassigned"));
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

            String create_url = "http://mobile4day.com/pkmbk-camp/get_bus.php";

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

    class updateBus extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updateBus(){
            ProgressDialog = new ProgressDialog(AssignBus.this);
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

            if(s.equals("1")){
                new getBus().execute();
            }
            else Toast.makeText(AssignBus.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/pkmbk-camp/assign_bus.php";
            String id = params[0];
            String bus = params[1];

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("bus", "UTF-8")+"="+URLEncoder.encode(bus,"UTF-8")+"&"+
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
        ArrayList<String> Id;
        ArrayList<String> nrp;
        Context context;
        ArrayList<String> name;
        ArrayList<String> status;
        ArrayList<String> bus;
        ArrayList<ArrayAdapter<CharSequence>> data;

        private LayoutInflater inflater=null;
        public ListAdapter(Activity activity, ArrayList<String> Id, ArrayList<String> nrp, ArrayList<String> name, ArrayList<String> status, ArrayList<String> bus, ArrayList<ArrayAdapter<CharSequence>> data) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.Id = Id;
            this.nrp = nrp;
            this.name = name;
            this.status = status;
            this.bus = bus;
            this.data = data;
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

        private class Holder
        {
            String dataBus;
            TextView tvNrp;
            TextView tvNama;
            TextView tvStatus;
            Spinner spinnerBus;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.list_assign, null);

            holder.tvNrp =(TextView) rowView.findViewById(R.id.tvNrp);
            holder.tvNama =(TextView) rowView.findViewById(R.id.tvNama);
            holder.tvStatus =(TextView) rowView.findViewById(R.id.tvStatus);
            holder.spinnerBus =(Spinner) rowView.findViewById(R.id.spinnerBus);

            holder.tvNama.setText(name.get(position));
            holder.tvNrp.setText(nrp.get(position));
            holder.tvStatus.setText(status.get(position));
            holder.spinnerBus.setAdapter(data.get(position));
            holder.spinnerBus.setSelection(getBus(bus.get(position)));
            holder.spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int positions, long id) {
                    update(Id.get(position), name.get(position), bus.get(position), getBuss(positions));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            return rowView;
        }
    }

    private void update(String id, String name, String busbef, String busnow)
    {
        if(!busbef.equals(busnow))
        {
            Toast.makeText(this, "Set "+name+" into "+busnow, Toast.LENGTH_LONG).show();
            new updateBus().execute(id, busnow);
            new getList().execute();
            listAdapter.notifyDataSetChanged();
        }
    }
}