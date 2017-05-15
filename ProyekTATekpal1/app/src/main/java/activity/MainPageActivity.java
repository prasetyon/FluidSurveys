package activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.example.prasetyon.proyektatekpal1.R;

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

import login.LoginActivity;

public class MainPageActivity extends AppCompatActivity {
    protected Button btnLogout, btnAddProject;
    private ListView lvProject;
    TextView tvUsername;
    Context Activity;
    private Bundle extras;
    private String username;
    private String role;
    private String from;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        setTitle("PROFILE");

        Activity = this;

        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUsername.setText(username);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(activity);
        btnAddProject = (Button) findViewById(R.id.btnAddProject);
        btnAddProject.setOnClickListener(activity);

        if(role.equals("user")) btnAddProject.setVisibility(View.GONE);

        new getProject().execute();
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnLogout:
                    intent = new Intent(MainPageActivity.this, LoginActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btnAddProject:
                    intent = new Intent(MainPageActivity.this, AddProjectActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private void showReport(String idProject, String nameProject)
    {
        Intent intent = new Intent(MainPageActivity.this, InspectionReportActivity.class);
        extras.putString("PROJECT_ID", idProject);
        extras.putString("PROJECT_NAME", nameProject);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    private void showProject(String s)
    {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            ArrayList<String> projectID = new ArrayList<>();
            ArrayList<String> projectNm = new ArrayList<>();

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                projectID.add(jo.getString("id_proyek"));
                projectNm.add(jo.getString("nama_proyek"));
            }

            lvProject = (ListView) findViewById(R.id.lvProject);
            ListAdapter listAdapter = new ListAdapter(this, projectID, projectNm);
            lvProject.setAdapter(listAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getProject extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getProject(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading all project...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(MainPageActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showProject(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url;

            if(role.equals("admin"))
                create_url = "http://mobile4day.com/ship-inspection/get_project_admin.php";
            else create_url = "http://mobile4day.com/ship-inspection/get_project_user.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
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

    public class ListAdapter extends BaseAdapter {

        ArrayList<String> projectID;
        Context context;
        ArrayList<String> projectName;
        private LayoutInflater inflater=null;
        public ListAdapter(Activity activity, ArrayList<String> projectID, ArrayList<String> projectName) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.projectID = projectID;
            this.projectName = projectName;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return projectID.size();
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
            TextView tvID;
            TextView tvName;
            Spinner spinnerProject;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.project_list, null);
            holder.tvID=(TextView) rowView.findViewById(R.id.tvIdProject);
            holder.tvName =(TextView) rowView.findViewById(R.id.tvProjectName);
            holder.tvID.setText(projectID.get(position));
            holder.tvName.setText(projectName.get(position));
            holder.spinnerProject = (Spinner) rowView.findViewById(R.id.spinnerProject);
            final ArrayAdapter<CharSequence> spinnerAdapter;
            if(role.equals("admin")) spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.spinner_proyek_list, android.R.layout.simple_spinner_item);
            else spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.spinner_proyek_user_list, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            holder.spinnerProject.setAdapter(spinnerAdapter);
            holder.spinnerProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int positions, long id) {
                    if(positions>1) {
                        Intent intent = new Intent(MainPageActivity.this, DetailProjectActivity.class);
                        String from="";
                        switch(positions){
                            case 2:
                                from = "detail";
                                break;
                            case 3:
                                if(role.equals("admin")) from = "edit";
                                else showReport(projectID.get(position), projectName.get(position));
                                break;
                            case 4:
                                intent = new Intent(MainPageActivity.this, UserListActivity.class);
                                extras.putString("FROM", from);
                                extras.putString("PROJECT_ID", projectID.get(position));
                                extras.putString("PROJECT_NAME", projectName.get(position));
                                intent.putExtras(extras);
                                startActivity(intent);
                                break;
                            case 5:
                                new deleteProject().execute(projectID.get(position));
                                break;
                            case 6:
                                showReport(projectID.get(position), projectName.get(position));
                                break;
                        }
                        if((positions<4 && role.equals("admin")) || (positions==2 && (role.equals("user") || role.equals("userNo")))) {
                            extras.putString("FROM", from);
                            extras.putString("PROJECT_ID", projectID.get(position));
                            extras.putString("PROJECT_NAME", projectName.get(position));
                            intent.putExtras(extras);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else if(positions==1){
                        Intent intent = new Intent(MainPageActivity.this, ProjectMainMenuActivity.class);
                        extras.putString("PROJECT_ID", projectID.get(position));
                        extras.putString("PROJECT_NAME", projectName.get(position));
                        intent.putExtras(extras);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            return rowView;
        }
    }

    class deleteProject extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public deleteProject(){
            ProgressDialog = new ProgressDialog(Activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Deleting project...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")) {
                Toast.makeText(MainPageActivity.this, "You project detail has been deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainPageActivity.this, MainPageActivity.class);
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

            String id_proyek = params[0];

            String create_url = "http://mobile4day.com/ship-inspection/delete_project.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("id_proyek", "UTF-8")+"="+URLEncoder.encode(id_proyek,"UTF-8");
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