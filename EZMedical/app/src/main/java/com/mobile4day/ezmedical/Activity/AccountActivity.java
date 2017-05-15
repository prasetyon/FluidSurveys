package com.mobile4day.ezmedical.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile4day.ezmedical.Function.DownloadImageTask;
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

public class AccountActivity extends AppCompatActivity {

    ListView lvMedicine;
    Context activity;
    String name, email, address, phone, photo;
    TextView tvName, tvLocation, tvEmail, tvPhone, tvPesanObat, tvBookingDokter;
    ImageView ivProfilePicture;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle(R.string.account_info);

        activity = this.getApplicationContext();
        Intent intent = getIntent();
        extras = intent.getExtras();
        name = extras.getString("NAME");
        email = extras.getString("EMAIL");
        address = extras.getString("ADDRESS");
        phone = extras.getString("PHONE");
        photo = extras.getString("PHOTO");

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        tvName = (TextView) findViewById(R.id.tvName);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvBookingDokter = (TextView) findViewById(R.id.tvBookingDokter);
        tvPesanObat = (TextView) findViewById(R.id.tvPesanObat);

        if(photo.equals("")){
            ivProfilePicture.setImageResource(R.drawable.thumbnail);
        }
        else{
            new DownloadImageTask(ivProfilePicture).execute(photo);
        }
        tvName.setText(name);
        tvLocation.setText(address);
        tvEmail.setText(email);
        tvPhone.setText(phone);

        lvMedicine = (ListView) findViewById(R.id.lvMedicine);
        setResource();
        new getBooking().execute();
    }

    public class Adapter extends BaseAdapter {

        Context context;
        ArrayList<String> fileName;
        ArrayList<String> fileConsume;
        ArrayList<String> fileAlarm1;
        ArrayList<String> fileAlarm2;
        ArrayList<String> fileAlarm3;
        ArrayList<String> fileStartDate;
        ArrayList<String> fileEndDate;

        private LayoutInflater inflater=null;
        public Adapter(Context activity, ArrayList<String> fileName, ArrayList<String> fileConsume, ArrayList<String> fileAlarm1, ArrayList<String> fileAlarm2, ArrayList<String> fileAlarm3,
                       ArrayList<String> fileStartDate, ArrayList<String> fileEndDate) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.fileName = fileName;
            this.fileConsume = fileConsume;
            this.fileAlarm1 = fileAlarm1;
            this.fileAlarm2 = fileAlarm2;
            this.fileAlarm3 = fileAlarm3;
            this.fileStartDate = fileStartDate;
            this.fileEndDate = fileEndDate;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fileEndDate.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public class Holder
        {
            TextView tvNama, tvConsume, tvAlarm1, tvAlarm2, tvAlarm3, tvStartDate, tvEndDate;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.alarm_list, null);
            holder.tvNama = (TextView) rowView.findViewById(R.id.tvNama);
            holder.tvConsume = (TextView) rowView.findViewById(R.id.tvConsume);
            holder.tvAlarm1 = (TextView) rowView.findViewById(R.id.tvAlarm1);
            holder.tvAlarm2 = (TextView) rowView.findViewById(R.id.tvAlarm2);
            holder.tvAlarm3 = (TextView) rowView.findViewById(R.id.tvAlarm3);
            holder.tvStartDate = (TextView) rowView.findViewById(R.id.tvStartDate);
            holder.tvEndDate = (TextView) rowView.findViewById(R.id.tvEndDate);

            holder.tvNama.setText(fileName.get(position));
            holder.tvConsume.setText(fileConsume.get(position));
            holder.tvAlarm1.setText(fileAlarm1.get(position));
            holder.tvAlarm2.setText(fileAlarm2.get(position));
            holder.tvAlarm3.setText(fileAlarm3.get(position));
            holder.tvStartDate.setText(fileStartDate.get(position));
            holder.tvEndDate.setText(fileEndDate.get(position));

            return rowView;
        }
    }

    private void setResource()
    {
        android.widget.ListAdapter adapter;
        ArrayList<String> fileName = new ArrayList<>();
        ArrayList<String> fileConsume = new ArrayList<>();
        ArrayList<String> fileAlarm1 = new ArrayList<>();
        ArrayList<String> fileAlarm2 = new ArrayList<>();
        ArrayList<String> fileAlarm3 = new ArrayList<>();
        ArrayList<String> fileStartDate = new ArrayList<>();
        ArrayList<String> fileEndDate = new ArrayList<>();

        fileName.add("Glukopack");
        fileName.add("Amoxicilin");
        fileName.add("Cefadroxil");
        fileName.add("Ester C");
        fileName.add("Vape");

        fileConsume.add("3");
        fileConsume.add("2");
        fileConsume.add("2");
        fileConsume.add("1");
        fileConsume.add("1");

        fileAlarm1.add("07:00");
        fileAlarm1.add("09:00");
        fileAlarm1.add("12:00");
        fileAlarm1.add("14:00");
        fileAlarm1.add("16:00");

        fileAlarm2.add("15:00");
        fileAlarm2.add("19:00");
        fileAlarm2.add("00:00");
        fileAlarm2.add("");
        fileAlarm2.add("");

        fileAlarm3.add("23:00");
        fileAlarm3.add("");
        fileAlarm3.add("");
        fileAlarm3.add("");
        fileAlarm3.add("");

        fileStartDate.add("23/12/2016");
        fileStartDate.add("22/12/2016");
        fileStartDate.add("23/12/2016");
        fileStartDate.add("-");
        fileStartDate.add("-");

        fileEndDate.add("26/12/2016");
        fileEndDate.add("27/12/2016");
        fileEndDate.add("31/12/2016");
        fileEndDate.add("-");
        fileEndDate.add("-");

        adapter = new Adapter(activity, fileName, fileConsume, fileAlarm1, fileAlarm2, fileAlarm3, fileStartDate, fileEndDate);
        lvMedicine.setAdapter(adapter);
    }

    class getBooking extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getBooking() {
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading Information...");
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
                if(jo.getString("num")!=null){
                    tvBookingDokter.setText(jo.getString("doctor"));
                }
                else{
                    tvBookingDokter.setText("-");
                }

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

            try {

                URL url = new URL(activity.getString(R.string.get_booking_detail_url));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("EMAIL", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
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
