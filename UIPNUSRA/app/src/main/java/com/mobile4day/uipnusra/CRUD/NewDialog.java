package com.mobile4day.uipnusra.CRUD;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mobile4day.uipnusra.Activity.ImageListActivity;
import com.mobile4day.uipnusra.Activity.MainActivity;
import com.mobile4day.uipnusra.Activity.SubTaskActivity;
import com.mobile4day.uipnusra.Activity.TaskActivity;
import com.mobile4day.uipnusra.Function.FilePath;
import com.mobile4day.uipnusra.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

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
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * Created by prasetyon on 3/11/2017.
 */

public class NewDialog{
    Context activity;
    int from;
    String id;
    Bundle extras;

    public NewDialog(Context activity, int from, String id, Bundle extras)
    {
        this.activity = activity;
        this.from = from;
        this.id = id;
        this.extras = extras;
        showDialog();
    }

    private void showDialog()
    {
        if(Integer.valueOf(Build.VERSION.SDK)>20) {
            final AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.new_task)
                    .setView(R.layout.dialog_new_task)
                    .setCancelable(true)
                    .create();
            dialog.show();

            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
            final EditText edName = (EditText) dialog.findViewById(R.id.edName);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new NewTask(activity, from).execute(edName.getText().toString(), id);
                    dialog.dismiss();
                }
            });
        }
        else {
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_new_task);
            dialog.setTitle(R.string.new_task);
            dialog.setCancelable(true);
            dialog.show();

            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
            final EditText edName = (EditText) dialog.findViewById(R.id.edName);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new NewTask(activity, from).execute(edName.getText().toString(), id);
                    dialog.dismiss();
                }
            });
        }

        //dialog.show();
    }

    class NewTask extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;
        Context activity;
        int from = 0;
        String name, id;

        public NewTask(Context activity, int from) {
            this.activity = activity;
            this.from = from;
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Creating task...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1"))
                Toast.makeText(activity, "Task sucessfully created!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();

            Intent intent = null;
            switch(from)
            {
                case 1:
                    intent = new Intent(activity, MainActivity.class);
                    extras.putString("username", id);
                    break;
                case 2:
                    intent = new Intent(activity, TaskActivity.class);
                    extras.putString("id_task", id);
                    break;
                case 3:
                    intent = new Intent(activity, SubTaskActivity.class);
                    extras.putString("id_sub_task", id);
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

            String name = params[0];
            String id = params[1];

            this.name = name;
            this.id = id;

            try {
                URL url = null;
                switch (from) {
                    case 0:
                        break;
                    case 1:
                        url = new URL(activity.getString(R.string.url_create_task));
                        break;
                    case 2:
                        url = new URL(activity.getString(R.string.url_create_subtask));
                        break;
                    case 3:
                        url = new URL(activity.getString(R.string.url_create_subsubtask));
                        break;
                    default:
                        break;

                }
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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
