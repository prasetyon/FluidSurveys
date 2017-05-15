package com.mobile4day.ezmedical.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobile4day.ezmedical.Function.ListAdapter;
import com.mobile4day.ezmedical.Function.OpenDialog;
import com.mobile4day.ezmedical.R;

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

public class PharmacyActivity extends AppCompatActivity {

    ListView lvPharmacy;
    ListAdapter listAdapter;
    Context activity;
    String name, email, address, phone, photo;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        setTitle(R.string.pharmacy);

        activity = PharmacyActivity.this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        name = extras.getString("NAME");
        email = extras.getString("EMAIL");
        address = extras.getString("ADDRESS");
        phone = extras.getString("PHONE");
        photo = extras.getString("PHOTO");

        lvPharmacy = (ListView) findViewById(R.id.lvPharmacy);
        new getList().execute();
    }

    class getList extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getList() {
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading list of pharmacy...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                final ArrayList<String> id = new ArrayList<>();
                final ArrayList<String> name = new ArrayList<>();
                final ArrayList<String> location = new ArrayList<>();
                final ArrayList<String> phone = new ArrayList<>();
                final ArrayList<String> info = new ArrayList<>();
                final ArrayList<String> operational = new ArrayList<>();
                final ArrayList<String> photo = new ArrayList<>();

                for(int i = 0; i<result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);

                    id.add(jo.getString("id"));
                    name.add(jo.getString("name"));
                    location.add(jo.getString("location"));
                    phone.add(jo.getString("phone"));
                    info.add(jo.getString("info"));
                    operational.add(jo.getString("operational"));
                    photo.add(jo.getString("photo"));
                }

                listAdapter = new ListAdapter(activity, id, name, location, phone, info, operational, photo);
                lvPharmacy.setAdapter(listAdapter);
                lvPharmacy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                        Toast.makeText(PharmacyActivity.this, "you choose "+id.get(position), Toast.LENGTH_LONG).show();
                        new OpenDialog(PharmacyActivity.this, name.get(position), photo.get(position), location.get(position), id.get(position), email, extras);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String x = "";

            try {
                URL url = new URL(getString(R.string.get_pharmacy_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("x", "UTF-8") + "=" + URLEncoder.encode(x , "UTF-8");
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
}