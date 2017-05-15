package subAssembly;

import android.app.ProgressDialog;
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
import android.widget.EditText;
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

public class SubAssemblyListActivity extends AppCompatActivity {

    Button btnBack;
    TextView edBagianSub;
    ListView lvKomponen;
    Context Activity;
    private Bundle extras;
    private String projectID;
    private String username;
    private String role;
    private String from;
    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_assembly_list);
        setTitle("SUB ASSEMBLY");

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

        edBagianSub = (EditText) findViewById(R.id.edBagianSub);
        edBagianSub.setEnabled(false);

        new getKomponen().execute();
    }

    private void showKomponen(String s) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            ArrayList<String> komponenID = new ArrayList<>();
            ArrayList<String> komponenName = new ArrayList<>();
            ArrayList<String> komponenType = new ArrayList<>();

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                komponenID.add(jo.getString("id_komponen"));
                komponenName.add(jo.getString("nama_komponen"));
                komponenType.add(jo.getString("jenis_komponen"));
            }

            lvKomponen = (ListView) findViewById(R.id.lvKomponen);
            ListAdapter listAdapter = new ListAdapter(this, komponenID, komponenName, komponenType);
            lvKomponen.setAdapter(listAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getKomponen extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getKomponen() {
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading all component...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(MainPageActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showKomponen(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url;

            create_url = "http://mobile4day.com/welding-inspection/get_component.php";

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

        ArrayList<String> komponenID;
        Context context;
        ArrayList<String> komponenName;
        ArrayList<String> komponenType;
        private LayoutInflater inflater = null;

        public ListAdapter(android.app.Activity activity, ArrayList<String> komponenID, ArrayList<String> komponenName, ArrayList<String> komponenType) {
            // TODO Auto-generated constructor stub
            context = activity;
            this.komponenID = komponenID;
            this.komponenName = komponenName;
            this.komponenType = komponenType;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return komponenID.size();
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
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.sub_assembly_list, null);
            holder.tvKomponen = (TextView) rowView.findViewById(R.id.tvNamaBlok);
            holder.btnDetail = (Button) rowView.findViewById(R.id.btnDetail);
            holder.btnEdit = (Button) rowView.findViewById(R.id.btnEdit);
            holder.btnHapus = (Button) rowView.findViewById(R.id.btnHapus);
            holder.tvKomponen.setText(komponenName.get(position) + "(" + komponenType.get(position) + ")");
            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SubAssemblyListActivity.this, SubAssemblyProsesActivity.class);
                    extras.putString("FROM", "detail");
                    extras.putString("COMPONENT_ID", komponenID.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SubAssemblyListActivity.this, SubAssemblyCheckActivity.class);
                    extras.putString("FROM", "edit");
                    extras.putString("COMPONENT_ID", komponenID.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new deleteComponent().execute(komponenID.get(position));
                }
            });
            if(role.equals("user")) holder.btnHapus.setVisibility(View.GONE);
            return rowView;
        }
    }

    class deleteComponent extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public deleteComponent(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Deleting component...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")) {
                Toast.makeText(SubAssemblyListActivity.this, "You component detail has been deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SubAssemblyListActivity.this, SubAssemblyListActivity.class);
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

            String id_komponen = params[0];

            String create_url = "http://mobile4day.com/welding-inspection/delete_component.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id_komponen", "UTF-8")+"="+URLEncoder.encode(id_komponen,"UTF-8");
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

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.btnBack:
                    intent = new Intent(SubAssemblyListActivity.this, SubAssemblyActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };
}