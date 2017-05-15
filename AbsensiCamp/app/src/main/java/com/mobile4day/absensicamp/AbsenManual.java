package com.mobile4day.absensicamp;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AbsenManual extends AppCompatActivity {

    ListView lvList;
    ListAdapter listAdapter;
    EditText edFind;
    ImageView ivSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_manual);

        lvList = (ListView) findViewById(R.id.lvList);
        edFind = (EditText) findViewById(R.id.edFind);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);

        new getList().execute("");

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getList().execute(edFind.getText().toString());
            }
        });
    }

    private void showList(String s)
    {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");
            final ArrayList<String> nrp = new ArrayList<>();
            final ArrayList<String> name = new ArrayList<>();
            final ArrayList<String> ids = new ArrayList<>();
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                nrp.add(jo.getString("nrp"));
                name.add(jo.getString("nama"));
                ids.add(jo.getString("id"));
            }

            listAdapter = new ListAdapter(this, ids, nrp, name);
            lvList.setAdapter(listAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getList extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getList(){
            ProgressDialog = new ProgressDialog(AbsenManual.this);
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

            String create_url = "http://mobile4day.com/pkmbk-camp/get_data.php";
            String find = params[0];

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("find", "UTF-8")+"="+URLEncoder.encode(find,"UTF-8");
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

    private void absen(final String id)
    {
        final Dialog dialog = new Dialog(this);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = li.inflate(R.layout.sesi, null);
        dialog.setContentView(dialogView);
        dialog.setTitle("Pilih Sesi");
        Button btnRegis, btnBerangkat, btnPulang, btnOpening, btnSesi1, btnSesi23, btnSesi45, btnMinggu, btnPenutup;
        btnRegis = (Button) dialog.findViewById(R.id.btnRegis);
        btnBerangkat = (Button) dialog.findViewById(R.id.btnBerangkat);
        btnPulang = (Button) dialog.findViewById(R.id.btnPulang);
        btnOpening = (Button) dialog.findViewById(R.id.btnOpening);
        btnSesi1 = (Button) dialog.findViewById(R.id.btnSesi1);
        btnSesi23 = (Button) dialog.findViewById(R.id.btnSesi23);
        btnSesi45 = (Button) dialog.findViewById(R.id.btnSesi45);
        btnMinggu = (Button) dialog.findViewById(R.id.btnMinggu);
        btnPenutup = (Button) dialog.findViewById(R.id.btnPenutup);
        dialog.show();
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new absen().execute("REGISTRASI", id);
                edFind.setText("");
                new getList().execute("");
                dialog.dismiss();
            }
        });
        btnBerangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new absen().execute("BERANGKAT", id);
                edFind.setText("");
                new getList().execute("");
                dialog.dismiss();
            }
        });
        btnPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new absen().execute("PULANG", id);
                edFind.setText("");
                new getList().execute("");
                dialog.dismiss();
            }
        });
        btnOpening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new absen().execute("OPENING", id);
                edFind.setText("");
                new getList().execute("");
                dialog.dismiss();
            }
        });
        btnSesi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new absen().execute("SESI1", id);
                edFind.setText("");
                new getList().execute("");
                dialog.dismiss();
            }
        });
        btnSesi23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new absen().execute("SESI23", id);
                edFind.setText("");
                new getList().execute("");
                dialog.dismiss();
            }
        });
        btnSesi45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new absen().execute("SESI45", id);
                edFind.setText("");
                new getList().execute("");
                dialog.dismiss();
            }
        });
        btnMinggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new absen().execute("IBADAH_MINGGU", id);
                edFind.setText("");
                new getList().execute("");
                dialog.dismiss();
            }
        });
        btnPenutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new absen().execute("CLOSING", id);
                edFind.setText("");
                new getList().execute("");
                dialog.dismiss();
            }
        });
    }

    class absen extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public absen(){
            ProgressDialog = new ProgressDialog(AbsenManual.this);
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

            JSONObject jsonObject;
            try{
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");
                JSONObject jo = result.getJSONObject(0);

                Toast.makeText(AbsenManual.this, jo.getString("nama"), Toast.LENGTH_SHORT).show();
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
        ArrayList<String> Id;
        ArrayList<String> nrp;
        Context context;
        ArrayList<String> name;

        private LayoutInflater inflater=null;
        public ListAdapter(Activity activity, ArrayList<String> Id, ArrayList<String> nrp, ArrayList<String> name) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.Id = Id;
            this.nrp = nrp;
            this.name = name;
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
            TextView tvNrp;
            TextView tvNama;
            Button btnAbsen;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.list_manual, null);

            holder.tvNrp =(TextView) rowView.findViewById(R.id.tvNrp);
            holder.tvNama =(TextView) rowView.findViewById(R.id.tvNama);
            holder.btnAbsen =(Button) rowView.findViewById(R.id.btnAbsen);

            holder.tvNama.setText(name.get(position));
            holder.tvNrp.setText(nrp.get(position));

            holder.btnAbsen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    absen(Id.get(position));
                }
            });

            return rowView;
        }
    }
}
