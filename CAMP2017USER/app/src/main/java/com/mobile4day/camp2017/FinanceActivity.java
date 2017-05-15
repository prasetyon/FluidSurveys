package com.mobile4day.camp2017;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class FinanceActivity extends AppCompatActivity {

    TextView tvInflow, tvOutflow, tvBalance;
    TextView tvBankInflow, tvBankOutflow, tvBankBalance;
    TextView tvCashInflow, tvCashOutflow, tvCashBalance;
    boolean doubleBackToExitPressedOnce = false;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        tvInflow = (TextView) findViewById(R.id.tvInflow);
        tvOutflow = (TextView) findViewById(R.id.tvOutflow);
        tvBalance = (TextView) findViewById(R.id.tvBalance);;
        tvBankInflow = (TextView) findViewById(R.id.tvBankInflow);
        tvBankOutflow = (TextView) findViewById(R.id.tvBankOutflow);
        tvBankBalance = (TextView) findViewById(R.id.tvBankBalance);;
        tvCashInflow = (TextView) findViewById(R.id.tvCashInflow);
        tvCashOutflow = (TextView) findViewById(R.id.tvCashOutflow);
        tvCashBalance = (TextView) findViewById(R.id.tvCashBalance);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinanceActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        new getBalance().execute();
    }

    private void showBalance(String s)
    {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            String output;
            output = "Rp " + jo.getString("balance");
            tvBalance.setText(output);
            output = "Rp " + jo.getString("inflow");
            tvInflow.setText(output);
            output = "Rp " + jo.getString("outflow");
            tvOutflow.setText(output);
            output = "Rp " + jo.getString("cashbal");
            tvCashBalance.setText(output);
            output = "Rp " + jo.getString("cashin");
            tvCashInflow.setText(output);
            output = "Rp " + jo.getString("cashout");
            tvCashOutflow.setText(output);
            output = "Rp " + jo.getString("bankbal");
            tvBankBalance.setText(output);
            output = "Rp " + jo.getString("bankin");
            tvBankInflow.setText(output);
            output = "Rp " + jo.getString("bankout");
            tvBankOutflow.setText(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getBalance extends AsyncTask<String, String, String> {
        ProgressDialog ProgressDialog;

        public getBalance(){
            ProgressDialog = new ProgressDialog(FinanceActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading finance...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(MainPageActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showBalance(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/pkmbk-finance/get_balance.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("info", "UTF-8")+"="+URLEncoder.encode("","UTF-8");
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
