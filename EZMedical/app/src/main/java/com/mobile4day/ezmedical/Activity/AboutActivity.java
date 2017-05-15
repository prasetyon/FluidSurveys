package com.mobile4day.ezmedical.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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

public class AboutActivity extends AppCompatActivity {

    TextView tvAppsName, tvSlogan, tvInfo, tvCreator, tvYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(R.string.about_us);

        tvAppsName = (TextView) findViewById(R.id.tvAppsName);
        tvSlogan = (TextView) findViewById(R.id.tvSlogan);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvCreator = (TextView) findViewById(R.id.tvCreator);
        tvYear = (TextView) findViewById(R.id.tvYear);

        new getDetails().execute();
    }

    class getDetails extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getDetails() {
            ProgressDialog = new ProgressDialog(AboutActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading application's detail...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                JSONObject jo = result.getJSONObject(0);

                tvAppsName.setText(jo.getString("apps_name"));
                tvSlogan.setText(jo.getString("slogan"));
                tvInfo.setText(jo.getString("info"));
                tvCreator.setText(jo.getString("creator"));
                tvYear.setText(jo.getString("year"));
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

            String x = "EZ Medical";

            try {
                URL url = new URL(getString(R.string.get_about_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("apps_name", "UTF-8") + "=" + URLEncoder.encode(x , "UTF-8");
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
