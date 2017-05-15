package com.mobile4day.ezmedical.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class OrderMedicineActivity extends AppCompatActivity {

    ListView lvMedicine;
    Button btnOrder;
    TextView tvName;

    String name, email, address, phone, photo, pharmacyId, pharmacyName;
    Bundle extras;
    Integer totalCost=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_medicine);

        Intent intent = getIntent();
        extras = intent.getExtras();
        name = extras.getString("NAME");
        email = extras.getString("EMAIL");
        address = extras.getString("ADDRESS");
        phone = extras.getString("PHONE");
        photo = extras.getString("PHOTO");
        pharmacyId = extras.getString("PHAR_ID");
        pharmacyName = extras.getString("PHAR_NAME");

        lvMedicine = (ListView) findViewById(R.id.lvMedicine);
        btnOrder = (Button) findViewById(R.id.btnOrder);
        tvName = (TextView) findViewById(R.id.tvName);

        tvName.setText(name);
        new getMedicine().execute(pharmacyId);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View vv;

                for(int i=0; i<lvMedicine.getCount(); i++){
                    vv=lvMedicine.getChildAt(i);
                    TextView tvQty, tvPrice;

                    if(vv!=null) {
//                        Toast.makeText(OrderMedicineActivity.this, "View is not null", Toast.LENGTH_LONG).show();
                        tvQty = (TextView) vv.findViewById(R.id.tvQty);
                        tvPrice = (TextView) vv.findViewById(R.id.tvPrice);
                        String prc = tvPrice.getText().toString().substring(8);
                        Integer price = (Integer.parseInt(tvQty.getText().toString())*Integer.parseInt(prc));
                        totalCost+=price;
                    }
                    else{
//                        Toast.makeText(OrderMedicineActivity.this, "View null", Toast.LENGTH_LONG).show();
                    }
                }
                Dialog dialog = new Dialog(OrderMedicineActivity.this);
                dialog.setContentView(R.layout.price_dialog);
                dialog.setTitle("Total Cost");
                TextView tvPrice = (TextView) dialog.findViewById(R.id.tvPrice);
                Button btnOrder = (Button) dialog.findViewById(R.id.btnOrder);
                tvPrice.setText(totalCost.toString());
                totalCost=0;
                dialog.show();
                dialog.setCancelable(true);
            }
        });
    }

    class getMedicine extends AsyncTask<String, String, String> {
        //android.app.ProgressDialog ProgressDialog;

        public getMedicine() {
            //ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(OrderMedicineActivity.this, "Reading list of medicine", Toast.LENGTH_LONG).show();
            //ProgressDialog.setMessage("Reading list of medicine...");
            //ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);
                JSONArray result = jsonObject.getJSONArray("result");

                ArrayList<String> id = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> stock = new ArrayList<>();
                ArrayList<String> price = new ArrayList<>();
                ArrayList<String> photo = new ArrayList<>();

                for(int i = 0; i<result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);

                    id.add(jo.getString("id"));
                    name.add(jo.getString("name"));
                    stock.add(jo.getString("stock"));
                    price.add(jo.getString("price"));
                    photo.add(jo.getString("photo"));
                }

                MedicineAdapter listAdapter = new MedicineAdapter(OrderMedicineActivity.this, id, name, stock, price, photo);
                lvMedicine.setAdapter(listAdapter);
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

            String id = params[0];

            try {
                URL url = new URL(getString(R.string.get_medicine_list_url));
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

    public class MedicineAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> id;
        ArrayList<String> name;
        ArrayList<String> stock;
        ArrayList<String> price;
        ArrayList<String> photo;

        private LayoutInflater inflater=null;

        public MedicineAdapter(Context activity, ArrayList<String> id, ArrayList<String> name, ArrayList<String> stock, ArrayList<String> price, ArrayList<String> photo) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.photo = photo;
            this.id = id;
            this.name = name;
            this.stock = stock;
            this.price = price;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return photo.size();
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
            TextView tvName, tvStock, tvPrice, tvQty;
            ImageView ivImage;
            Button btnMin, btnPlus;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Holder holder=new Holder();
            View rowView = inflater.inflate(R.layout.medicine_list, null);
            holder.tvName = (TextView) rowView.findViewById(R.id.tvName);
            holder.tvStock = (TextView) rowView.findViewById(R.id.tvStock);
            holder.tvPrice = (TextView) rowView.findViewById(R.id.tvPrice);
            holder.tvQty = (TextView) rowView.findViewById(R.id.tvQty);
            holder.ivImage = (ImageView) rowView.findViewById(R.id.ivPhoto);
            holder.btnMin = (Button) rowView.findViewById(R.id.btnMin);
            holder.btnPlus = (Button) rowView.findViewById(R.id.btnPlus);

            holder.tvName.setText(name.get(position));
            holder.tvStock.setText("Stock : " + stock.get(position));
            holder.tvPrice.setText("Price : " + price.get(position));
            //Toast.makeText(context, holder.tvQty.getText().toString(), Toast.LENGTH_LONG).show();

            holder.btnMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int val = Integer.parseInt(holder.tvQty.getText().toString());
                    if(val>0) {
                        val--;
                        holder.tvQty.setText(String.valueOf(val));
                    }
                }
            });

            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int val = Integer.parseInt(holder.tvQty.getText().toString());
                    int max = Integer.parseInt(stock.get(position));
                    if(val<max) {
                        val++;
                        holder.tvQty.setText(String.valueOf(val));
                    }
                }
            });

            //new DownloadImageTask(holder.ivImage).execute(photo.get(position));

//        holder.ivImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new OpenDialog(context, name.get(position), photo.get(position), stock.get(position));
//            }
//        });

            return rowView;
        }
    }
}
