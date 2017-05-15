package com.mobile4day.ezmedical.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile4day.ezmedical.R;

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

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    TextView tvEmail, tvPassword, tvRePassword, tvUsername, tvFullName, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        tvRePassword = (TextView) findViewById(R.id.tvRePassword);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, repassword, address, fullname, email;
                email = tvEmail.getText().toString();
                username = tvUsername.getText().toString();
                password = tvPassword.getText().toString();
                repassword = tvRePassword.getText().toString();
                address = tvAddress.getText().toString();
                fullname = tvFullName.getText().toString();

                if(email.equals(null) || username.equals(null) || password.equals(null) || repassword.equals(null) || fullname.equals(null)){
                    Toast.makeText(RegisterActivity.this, "Please fill in the blank!", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(repassword)){
                    Toast.makeText(RegisterActivity.this, "Password did not match!", Toast.LENGTH_SHORT).show();
                }
                else{
                    new Register(RegisterActivity.this).execute(username, email, password, fullname, address);
                }
            }
        });
    }
    class Register extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;
        Context activity;
        String username;

        public Register(Context activity) {
            this.activity = activity;
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Creating account...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1")) {
                Toast.makeText(activity, "Account sucessfully created!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                finish();
            }
            else
                Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();

            ProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String username = params[0];
            String email = params[1];
            String password = params[2];
            String name = params[3];
            String address = params[4];

            this.username = username;

            try {
                URL url = new URL(activity.getString(R.string.register_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                        URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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

