package com.mobile4day.ezmedical.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

public class LoginActivity extends AppCompatActivity {

    TextView tvEmail, tvPassword;
    Button btnSignIn, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        getSharedPreference();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tvEmail.getText().toString();
                String password = tvPassword.getText().toString();
                new Verify().execute(email, password);
                //move("prasetyon");
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void move(String name, String phone, String email, String address, String photo)
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putString("NAME", name);
        extras.putString("PHONE", phone);
        extras.putString("EMAIL", email);
        extras.putString("ADDRESS", address);
        extras.putString("PHOTO", photo);
        intent.putExtras(extras);
        finish();
        startActivity(intent);
    }

    private void NewSharedPreferences(String name, String phone, String email, String address, String photo)
    {
        // select your mode to be either private or public.
        int mode= Activity.MODE_PRIVATE;
        // get the sharedPreference of your context.
        SharedPreferences mySharedPreferences;
        mySharedPreferences=getSharedPreferences("login",mode);
        // retrieve an editor to modify the shared preferences
        SharedPreferences.Editor editor= mySharedPreferences.edit();
        /* now store your primitive type values. In this case it is true, 1f and Hello! World  */
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("address", address);
        editor.putString("photo", photo);
        //save the changes that you made
        editor.commit();

        move(name, phone, email, address, photo);
    }

    private void getSharedPreference()
    {
        /* Now we will try to retrieve the data saved i.e. true, 1f and Hello! World */
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences  mySharedPreferences;
        mySharedPreferences=getSharedPreferences("login",mode);

        // Retrieve the saved values.
        String name = mySharedPreferences.getString("name",null);
        if(name!=null) {
            String address = mySharedPreferences.getString("address",null);
            String phone = mySharedPreferences.getString("phone",null);
            String photo = mySharedPreferences.getString("photo",null);
            String email = mySharedPreferences.getString("email",null);
            //Toast.makeText(LoginActivity.this, "Get the logged in " + username, Toast.LENGTH_LONG).show();
            move(name, phone, email, address, photo);
        }
        else
            return;
    }

    public class Verify extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public Verify() {
            ProgressDialog = new ProgressDialog(LoginActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Logging in...");
            //ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_LONG).show();

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                JSONObject jo = result.getJSONObject(0);

                String email = (jo.getString("email"));
                String name = (jo.getString("name"));
                String address = (jo.getString("address"));
                String photo = (jo.getString("photo"));
                String phone = (jo.getString("phone"));
                if(name.equals(null))
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                else NewSharedPreferences(name, phone, email, address, photo);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //ProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String email = params[0];
            String password = params[1];

            try {
                URL url = new URL(getString(R.string.login_url));

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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