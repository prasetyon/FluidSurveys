package com.mobile4day.ezmedical.Function;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.mobile4day.ezmedical.Activity.OrderMedicineActivity;
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

/**
 * Created by prasetyon on 2/23/2017.
 */

public class OpenDialog {
    Context activity;
    TextView tvAntrian;
    String email;
    Dialog dialog;
    Button btnAntrian;
    Button btnOrder;
    Bundle bundle;

    public OpenDialog(final Context activity, final String fileName, String urls, String locations, final String id, final String email, final Bundle bundle)
    {
        this.email = email;
        this.activity = activity;
        this.bundle = bundle;
        String contextTemp = String.valueOf(activity).substring(34);
        String context = contextTemp.substring(0, contextTemp.indexOf("@"));
        //Toast.makeText(activity, "Context is "+ context, Toast.LENGTH_LONG).show();

        if(activity!=null) {
            dialog = new Dialog(activity);
            if (context.equals("PharmacyActivity")) {
                dialog.setContentView(R.layout.pharmacy_image_dialog);
                btnOrder = (Button) dialog.findViewById(R.id.btnOrder);
                new getOrderRecipe().execute(id);
                btnOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, OrderMedicineActivity.class);
                        bundle.putString("PHAR_NAME", fileName);
                        bundle.putString("PHAR_ID", id);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        ((Activity)activity).finish();
                        dialog.dismiss();
                    }
                });
            } else if (context.equals("DoctorActivity")) {
                dialog.setContentView(R.layout.doctor_image_dialog);
                tvAntrian = (TextView) dialog.findViewById(R.id.tvAntrian);
                btnAntrian = (Button) dialog.findViewById(R.id.btnAntrian);
                btnAntrian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new insertQueue().execute(id);
                    }
                });
                new getBooking().execute(id);
                new getQueue().execute(id);
            }

            dialog.setTitle(fileName);
            dialog.setCancelable(true);
            ImageView img = (ImageView) dialog.findViewById(R.id.ivImage);
            TextView tvLocation = (TextView) dialog.findViewById(R.id.tvLocation);
            tvLocation.setText(locations);
            new DownloadImageTask(img).execute(urls);

            dialog.show();
        }
        else{
            Toast.makeText(activity, "Context not found!", Toast.LENGTH_LONG).show();
        }
    }

    class getQueue extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getQueue() {
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            ProgressDialog.setMessage("Reading list of doctor...");
//            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                JSONObject jo = result.getJSONObject(0);
                tvAntrian.setText(jo.getString("num"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            ProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                String sip = params[0];

                URL url = new URL(activity.getString(R.string.get_queue_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("SIP", "UTF-8") + "=" + URLEncoder.encode(sip , "UTF-8");
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

    class insertQueue extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public insertQueue() {
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            ProgressDialog.setMessage("Reading list of doctor...");
//            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject;
            dialog.dismiss();
//            ProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                String sip = params[0];

                URL url = new URL(activity.getString(R.string.insert_queue_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("SIP", "UTF-8") + "=" + URLEncoder.encode(sip , "UTF-8") + "&" +
                        URLEncoder.encode("EMAIL", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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

    class getBooking extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getBooking() {
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            ProgressDialog.setMessage("Reading list of doctor...");
//            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                JSONObject jo = result.getJSONObject(0);
                if(jo.getString("num")!=null){
                    btnAntrian.setVisibility(View.INVISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            ProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                String sip = params[0];

                URL url = new URL(activity.getString(R.string.get_booking_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("SIP", "UTF-8") + "=" + URLEncoder.encode(sip , "UTF-8") + "&" +
                        URLEncoder.encode("EMAIL", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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

    class getOrderRecipe extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getOrderRecipe() {
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            ProgressDialog.setMessage("Reading list of doctor...");
//            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                JSONObject jo = result.getJSONObject(0);
                if(jo.getString("num")!=null){
                    btnOrder.setVisibility(View.INVISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            ProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                String sia = params[0];

                URL url = new URL(activity.getString(R.string.get_recipe_order_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("SIA", "UTF-8") + "=" + URLEncoder.encode(sia , "UTF-8") + "&" +
                        URLEncoder.encode("EMAIL", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
