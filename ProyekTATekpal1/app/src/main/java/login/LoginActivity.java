package login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal1.R;

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

import activity.MainPageActivity;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnSignUp, btnForgot;
    Bundle extras;
    String role="", username;
    boolean login=false;
    boolean loginSuccess = false;
    EditText edUsername, edPassword;
    Context activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.login_page);

        activity = this;
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnForgot = (Button) findViewById(R.id.btnForgot);
        edUsername = (EditText) findViewById(R.id.edUsername);
        edPassword = (EditText) findViewById(R.id.edPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                verifyLogin(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void verifyLogin(String username, String password)
    {
        login=false;
        this.username = username;
        new BackgroundTask().execute(username, password);
        //Toast.makeText(LoginActivity.this, "Role: " + role, Toast.LENGTH_SHORT).show();
    }

    class BackgroundTask extends AsyncTask<String, String, String> {
        ProgressDialog ProgressDialog;

        public BackgroundTask(){
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Logging in...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            login=true;
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if (role.equals("Coating Inspector") || role.equals("Shipyard") || role.equals("Class") || role.equals("Owner") || role.equals("admin")) loginSuccess = true;
            if (loginSuccess) {
                extras = new Bundle();
                extras.putString("USERNAME", username);
                if(role.equals("admin"))
                    extras.putString("ROLE", role);
                else if(role.equals("Coating Inspector") || role.equals("Shipyard"))
                    extras.putString("ROLE", "user");
                else extras.putString("ROLE", "userNo");
                extras.putString("FROM", "login");
                Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Login Failed, Check Your Username and Password" + s, Toast.LENGTH_SHORT).show();
                edPassword.setText("");
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String var1 = params[0];
            String var2 = params[1];

            String create_url = "http://mobile4day.com/ship-inspection/verify_login.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(var1,"UTF-8")+"&"+
                                URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(var2,"UTF-8");
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
                role = response;
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
