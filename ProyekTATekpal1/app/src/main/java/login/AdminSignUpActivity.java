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

public class AdminSignUpActivity extends AppCompatActivity {

    Button btnBack, btnRegister, btnReset;
    EditText edUsername, edPassword, edVerPassword, edFullName, edEmail, edCompany, edPhoneNumber, edActivationCode;
    final String KODE_AKTIVASI = "ADMIN";
    Context activity;
    boolean signedUp=false;
    String signed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_admin);
        setTitle(R.string.admin_reg);
        activity=this;

        btnBack = (Button) findViewById(R.id.btnBack);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnReset = (Button) findViewById(R.id.btnReset);
        edUsername = (EditText) findViewById(R.id.edUsername);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edVerPassword = (EditText) findViewById(R.id.edVerPassword);
        edFullName = (EditText) findViewById(R.id.edFullName);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edPhoneNumber = (EditText) findViewById(R.id.edPhoneNumber);
        edActivationCode = (EditText) findViewById(R.id.edActivationCode);
        edCompany = (EditText) findViewById(R.id.edCompany);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminSignUpActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUsername.setText("");
                edPassword.setText("");
                edVerPassword.setText("");
                edFullName.setText("");
                edEmail.setText("");
                edPhoneNumber.setText("");
                edCompany.setText("");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Register new account here, then go to HOME (Role: Admin)
                /* have to chek whether the password is the same or not,
                   and whether the account is succesfully registered or not*/
                if(edPassword.getText().toString().equals(edVerPassword.getText().toString()) && edActivationCode.getText().toString().equals(KODE_AKTIVASI)) {
                    String username = edUsername.getText().toString();
                    String name = edFullName.getText().toString();
                    String password = edPassword.getText().toString();
                    String email = edEmail.getText().toString();
                    String phone = edPhoneNumber.getText().toString();
                    String company = edCompany.getText().toString();
                    new BackgroundTask().execute(username, password, name, email, phone, company);
                    //Toast.makeText(AdminSignUpActivity.this, "Signed = " + signed, Toast.LENGTH_SHORT).show();
                }
                else if(!edPassword.getText().toString().equals(edVerPassword.getText().toString())){
                    Toast.makeText(AdminSignUpActivity.this, "Login Failed, Password didn't match!", Toast.LENGTH_SHORT).show();
                }
                else if(!edActivationCode.getText().toString().equals(KODE_AKTIVASI)){
                    Toast.makeText(AdminSignUpActivity.this, "Login Failed, Wrong activation code!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class BackgroundTask extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public BackgroundTask(){
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Signing up...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            signedUp = true;
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(signedUp && signed.equals("1")){
                Toast.makeText(AdminSignUpActivity.this, "You have been signed up! Login to continue", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminSignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String username = params[0];
            String password = params[1];
            String name = params[2];
            String email = params[3];
            String phone = params[4];
            String company = params[5];

            String create_url = "http://mobile4day.com/ship-inspection/new_admin.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                                URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                                URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                                URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                                URLEncoder.encode("number","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                                URLEncoder.encode("company","UTF-8")+"="+URLEncoder.encode(company,"UTF-8");
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
                signed = response;
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
