package com.mobile4day.uipnusra.CRUD;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobile4day.uipnusra.Activity.ImageListActivity;
import com.mobile4day.uipnusra.Activity.MainActivity;
import com.mobile4day.uipnusra.Activity.SubTaskActivity;
import com.mobile4day.uipnusra.Activity.TaskActivity;
import com.mobile4day.uipnusra.R;

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
 * Created by prasetyon on 4/2/2017.
 */

public class OkDialog {
    Context activity;
    int from;
    String id;
    String name;
    Bundle extras;

    public OkDialog(Context activity, int from, String id, String name, Bundle extras)
    {
        this.activity = activity;
        this.from = from;
        this.id = id;
        this.name = name;
        this.extras = extras;
        showDialog();
    }

    private void showDialog()
    {
        if(Integer.valueOf(Build.VERSION.SDK)>20) {
            final AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.ok_task)
                    .setView(R.layout.dialog_delete_task)
                    .setCancelable(true)
                    .create();
            dialog.show();

            Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
            Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
            TextView tvAlert = (TextView) dialog.findViewById(R.id.tvAlert);
            String alert = "Are you sure " + name + " is finished?";
            tvAlert.setText(alert);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new OkTask(activity).execute(id);
                    dialog.dismiss();
                }
            });

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        else {
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_delete_task);
            dialog.setTitle(R.string.ok_task);
            dialog.setCancelable(true);
            dialog.show();

            Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
            Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
            TextView tvAlert = (TextView) dialog.findViewById(R.id.tvAlert);
            String alert = "Are you sure " + name + " is finished?";
            tvAlert.setText(alert);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new OkTask(activity).execute(id);
                    dialog.dismiss();
                }
            });

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    class OkTask extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;
        Context activity;

        public OkTask(Context activity)
        {
            this.activity = activity;
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Updating task...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Intent intent = null;
            switch(from)
            {
                case 1:
                    intent = new Intent(activity, MainActivity.class);
                    break;
                case 2:
                    intent = new Intent(activity, TaskActivity.class);
                    break;
                case 3:
                    intent = new Intent(activity, SubTaskActivity.class);
                    break;
                case 4:
                    intent = new Intent(activity, ImageListActivity.class);
                    break;
                default:
                    break;
            }
            intent.putExtras(extras);
            activity.startActivity(intent);
            ((Activity)activity).finish();

            ProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id = params[0];

            try {
                URL url = null;
                switch(from)
                {
                    case 0:
                        break;
                    case 1:
                        url = new URL(activity.getString(R.string.url_ok_task));
                        break;
                    case 2:
                        url = new URL(activity.getString(R.string.url_ok_subtask));
                        break;
                    case 3:
                        url = new URL(activity.getString(R.string.url_ok_subsubtask));
                        break;
                    default:
                        break;

                }
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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