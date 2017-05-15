package com.mobile4day.camp2017;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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
import android.widget.ListAdapter;
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
import java.util.List;

public class PesertaActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    ListAdapter listAdapter;
    ListView lvPeserta;
    ImageView ivBack, ivSearch;
    EditText edSearch;
    String search="", status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peserta);

        edSearch = (EditText) findViewById(R.id.edSearch);
        lvPeserta = (ListView) findViewById(R.id.lvPeserta);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = edSearch.getText().toString();
                new getList().execute(search);
                listAdapter.notifyDataSetChanged();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PesertaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        new getList().execute(search);
    }

    private void showDialog(String statusS, String namaS, String nrpS, String sexS, String hpS, String penyakitS, String alergiS, String idS)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.peserta);
        dialog.setTitle("Update Participant");
        dialog.setCancelable(true);
        final EditText edNRP = (EditText) dialog.findViewById(R.id.edNRP);
        edNRP.setEnabled(false);
        final EditText edName = (EditText) dialog.findViewById(R.id.edName);
        final EditText edSex = (EditText) dialog.findViewById(R.id.edSex);
        final EditText edHp = (EditText) dialog.findViewById(R.id.edHp);
        final EditText edPenyakit = (EditText) dialog.findViewById(R.id.edPenyakit);
        final EditText edAlergi = (EditText) dialog.findViewById(R.id.edAlergi);
        final TextView tvID = (TextView) dialog.findViewById(R.id.tvID);
        final Spinner spinnerStatus = (Spinner) dialog.findViewById(R.id.spinnerStatus);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        Button btnScanQR = (Button) dialog.findViewById(R.id.btnScanQR);
        btnScanQR.setVisibility(View.GONE);

        edName.setText(namaS);
        edNRP.setText(nrpS);
        edSex.setText(sexS);
        edHp.setText(hpS);
        edPenyakit.setText(penyakitS);
        edAlergi.setText(alergiS);
        tvID.setText(idS);
        final ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this, R.array.status_list, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setSelection(getPosition(statusS));
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = spinnerStatus.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nrp, nama, sex, hp, penyakit, alergi, id;
                nrp = edNRP.getText().toString();
                nama = edName.getText().toString();
                sex = edSex.getText().toString();
                hp = edHp.getText().toString();
                penyakit = edPenyakit.getText().toString();
                alergi = edAlergi.getText().toString();
                id = tvID.getText().toString();

                if(nrp.length()!=10)
                    Toast.makeText(PesertaActivity.this, "Check your nrp", Toast.LENGTH_SHORT).show();
                new updatePeserta().execute(nrp, status, nama, sex, hp, penyakit, alergi, id);
                new getList().execute(search);
                listAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private int getPosition(String status)
    {
        if(status.equals("SWORD")) return 0;
        else if(status.equals("SHIELD")) return 1;
        else if(status.equals("CALON_SHIELD")) return 2;
        else if(status.equals("PANITIA")) return 3;
        else if(status.equals("KONSELOR")) return 4;
        else if(status.equals("ALUMNI")) return 5;
        else return 6;
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
            final ArrayList<String> sex = new ArrayList<>();
            final ArrayList<String> hp = new ArrayList<>();
            final ArrayList<String> penyakit = new ArrayList<>();
            final ArrayList<String> alergi = new ArrayList<>();
            final ArrayList<String> ids = new ArrayList<>();

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                nrp.add(jo.getString("nrp"));
                status.add(jo.getString("status"));
                name.add(jo.getString("nama"));
                sex.add(jo.getString("sex"));
                hp.add(jo.getString("hp"));
                penyakit.add(jo.getString("penyakit"));
                alergi.add(jo.getString("alergi"));
                ids.add(jo.getString("id"));
            }

            listAdapter = new ListAdapter(this, nrp, name, status, sex, hp, penyakit, alergi, ids);
            lvPeserta.setAdapter(listAdapter);
            lvPeserta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final int pos = position;
                    final Dialog dialog = new Dialog(PesertaActivity.this);
                    dialog.setContentView(R.layout.select);
                    dialog.setCancelable(true);
                    final Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
                    final Button btnDelete = (Button) dialog.findViewById(R.id.btnDelete);
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(status.get(pos), name.get(pos), nrp.get(pos), sex.get(pos), hp.get(pos), penyakit.get(pos), alergi.get(pos), ids.get(pos));
                            dialog.dismiss();
                        }
                    });
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new deleteTransaction().execute(ids.get(pos));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getList extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getList(){
            ProgressDialog = new ProgressDialog(PesertaActivity.this);
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
            String proses = params[0];

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("find", "UTF-8")+"="+URLEncoder.encode(proses,"UTF-8");
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

    class updatePeserta extends AsyncTask<String, String, String> {
        ProgressDialog ProgressDialog;

        public updatePeserta(){
            ProgressDialog = new ProgressDialog(PesertaActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Updating participant...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")){
                Toast.makeText(PesertaActivity.this, "Participant has been updated!", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(PesertaActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String nrp = params[0];
            String status = params[1];
            String nama = params[2];
            String sex = params[3];
            String hp = params[4];
            String penyakit = params[5];
            String alergi = params[6];
            String id = params[7];

            String create_url = "http://mobile4day.com/pkmbk-camp/update.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("nrp", "UTF-8")+"="+URLEncoder.encode(nrp,"UTF-8")+"&"+
                                URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8")+"&"+
                                URLEncoder.encode("nama","UTF-8")+"="+URLEncoder.encode(nama,"UTF-8")+"&"+
                                URLEncoder.encode("sex","UTF-8")+"="+URLEncoder.encode(sex,"UTF-8")+"&"+
                                URLEncoder.encode("hp","UTF-8")+"="+URLEncoder.encode(hp,"UTF-8")+"&"+
                                URLEncoder.encode("penyakit","UTF-8")+"="+URLEncoder.encode(penyakit,"UTF-8")+"&"+
                                URLEncoder.encode("alergi","UTF-8")+"="+URLEncoder.encode(alergi,"UTF-8")+"&"+
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

    class deleteTransaction extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public deleteTransaction(){
            ProgressDialog = new ProgressDialog(PesertaActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Deleting data...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")){
                Toast.makeText(PesertaActivity.this, "Participant has been deleted!", Toast.LENGTH_SHORT).show();
                new getList().execute(search);
                listAdapter.notifyDataSetChanged();
            }
            else Toast.makeText(PesertaActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id = params[0];

            String create_url = "http://mobile4day.com/pkmbk-camp/delete.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
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
        ArrayList<String> sex;
        ArrayList<String> hp;
        ArrayList<String> penyakit;
        ArrayList<String> alergi;
        ArrayList<String> id;
        private LayoutInflater inflater=null;
        public ListAdapter(Activity activity, ArrayList<String> nrp, ArrayList<String> name, ArrayList<String> status,
                           ArrayList<String> sex, ArrayList<String> hp, ArrayList<String> penyakit, ArrayList<String> alergi,
                           ArrayList<String> id) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.nrp = nrp;
            this.name = name;
            this.status = status;
            this.sex = sex;
            this.hp = hp;
            this.penyakit = penyakit;
            this.alergi = alergi;
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
            TextView tvSex;
            TextView tvHp;
            TextView tvPenyakit;
            TextView tvAlergi;
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
            holder.tvSex =(TextView) rowView.findViewById(R.id.tvSex);
            holder.tvHp =(TextView) rowView.findViewById(R.id.tvHp);
            holder.tvPenyakit =(TextView) rowView.findViewById(R.id.tvPenyakit);
            holder.tvAlergi =(TextView) rowView.findViewById(R.id.tvAlergi);

            holder.tvNama.setText(name.get(position));
            holder.tvNrp.setText(nrp.get(position));
            holder.tvStatus.setText(status.get(position));
            holder.tvSex.setText(sex.get(position));
            holder.tvHp.setText(hp.get(position));
            holder.tvPenyakit.setText(penyakit.get(position));
            holder.tvAlergi.setText(alergi.get(position));

            return rowView;
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
