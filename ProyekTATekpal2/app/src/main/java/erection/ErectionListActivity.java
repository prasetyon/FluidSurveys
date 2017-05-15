package erection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal2.R;

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

import activity.ProjectMainMenuActivity;

public class ErectionListActivity extends AppCompatActivity {

    Spinner spinnerLokasi;
    ListView lvBlock;
    Button btnBack;
    boolean doubleBackToExitPressedOnce = false;
    Context Activity;
    private Bundle extras;
    private String lokasi;
    private String projectID;
    private String username;
    private String role;
    private String from;
    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erection_list);
        setTitle("ERECTION");

        Activity = this;
        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(activity);

        spinnerLokasi = (Spinner) findViewById(R.id.spinnerLokasi);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.location_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerLokasi.setAdapter(spinnerAdapter);
        spinnerLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lokasi = spinnerLokasi.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new getBlock().execute();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnBack:
                    intent = new Intent(ErectionListActivity.this, ProjectMainMenuActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
            }
        }
    };

    private void showBlock(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            ArrayList<String> blockID = new ArrayList<>();
            ArrayList<String> blockName = new ArrayList<>();
            ArrayList<String> blockLocation = new ArrayList<>();

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                blockID.add(jo.getString("id_blok"));
                blockName.add(jo.getString("nama_blok"));
                blockLocation.add(jo.getString("posisi"));
            }

            lvBlock = (ListView) findViewById(R.id.lvBlock);
            ListAdapter listAdapter = new ListAdapter(this, blockID, blockName, blockLocation);
            lvBlock.setAdapter(listAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getBlock extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getBlock() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading all block...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(AfterErectionListActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showBlock(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url;

            create_url = "http://mobile4day.com/welding-inspection/get_blok.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("id_project", "UTF-8") + "=" + URLEncoder.encode(projectID, "UTF-8");
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

    public class ListAdapter extends BaseAdapter {

        ArrayList<String> blockID;
        Context context;
        ArrayList<String> blockName;
        ArrayList<String> blockLocation;
        private LayoutInflater inflater = null;

        public ListAdapter(android.app.Activity activity, ArrayList<String> blockID, ArrayList<String> blockName, ArrayList<String> blockLocation) {
            // TODO Auto-generated constructor stub
            context = activity;
            this.blockID = blockID;
            this.blockName = blockName;
            this.blockLocation = blockLocation;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return blockID.size();
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

        public class Holder {
            TextView tvKomponen;
            Button btnDetail;
            Button btnEdit;
            Button btnHapus;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //Toast.makeText(AfterErectionListActivity.this, "List View: " + blockID + " " + blockName + " " + blockLocation, Toast.LENGTH_SHORT).show();
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.block_list, null);
            holder.tvKomponen = (TextView) rowView.findViewById(R.id.tvNamaBlok);
            holder.btnDetail = (Button) rowView.findViewById(R.id.btnDetail);
            holder.btnHapus = (Button) rowView.findViewById(R.id.btnHapus);
            holder.btnEdit = (Button) rowView.findViewById(R.id.btnEdit);

            holder.tvKomponen.setText(blockID.get(position)+". "+blockName.get(position));

            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ErectionListActivity.this, ErectionDetailActivity.class);
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ErectionListActivity.this, ErectionEditActivity.class);
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new deleteBlock().execute(blockID.get(position));
                }
            });

            return rowView;
        }
    }

    class deleteBlock extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public deleteBlock(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Deleting block...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")) {
                Toast.makeText(ErectionListActivity.this, "You block has been deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ErectionListActivity.this, ErectionListActivity.class);
                intent.putExtras(extras);
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

            String id_blok = params[0];

            String create_url = "http://mobile4day.com/welding-inspection/delete_block.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id_blok", "UTF-8")+"="+URLEncoder.encode(id_blok,"UTF-8");
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
