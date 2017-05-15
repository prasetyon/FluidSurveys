package com.mobile4day.uipnusra.Activity;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile4day.uipnusra.Function.DownloadImageTask;
import com.mobile4day.uipnusra.R;

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

public class ImageActivity extends AppCompatActivity {

    Bundle extras;
    String id, detail, url, created, username, username_image;
    ImageView ivImage;
    TextView tvImageDetail;
    ListView lvComment;
    EditText edComment;
    Button btnComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        extras = intent.getExtras();
        id = extras.getString("id_image");
        detail = extras.getString("detail_image");
        url = extras.getString("url_image");
        created = extras.getString("created_image");
        username = extras.getString("username");
        username_image = extras.getString("username_image");

        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvImageDetail = (TextView) findViewById(R.id.tvImageDetail);
        lvComment = (ListView) findViewById(R.id.lvComment);
        edComment = (EditText) findViewById(R.id.edComment);
        btnComment = (Button) findViewById(R.id.btnComment);

        new DownloadImageTask(ivImage).execute(url);
        tvImageDetail.setText(username_image + " : " + detail + " (" + created + ")");

        new GetComment(lvComment).execute(id);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edComment.getText().toString();
                if(!comment.equals(null))
                    new NewComment().execute(id, comment);
            }
        });
    }

    class NewComment extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;
        String id;

        public NewComment() {
            ProgressDialog = new ProgressDialog(ImageActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Creating comment...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!s.equals("1"))
                Toast.makeText(ImageActivity.this, s, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ImageActivity.this, ImageActivity.class);
            extras.putString("id_image", id);
            finish();
            intent.putExtras(extras);
            startActivity(intent);

            ProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id = params[0];
            String comment = params[1];
            String username = extras.getString("username");

            this.id = id;

            try {
                URL url = new URL(getString(R.string.url_comment));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(comment, "UTF-8");
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

    public class GetComment extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;
        CommentAdapter commentAdapter;
        ListView lvList;

        public GetComment(ListView lvList) {
            this.lvList = lvList;
            ProgressDialog = new ProgressDialog(ImageActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading comment...");
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

                ArrayList<String> id = new ArrayList<>();
                ArrayList<String> username = new ArrayList<>();
                ArrayList<String> comment = new ArrayList<>();
                ArrayList<String> created = new ArrayList<>();

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);

                    id.add(jo.getString("id"));
                    username.add(jo.getString("username"));
                    comment.add(jo.getString("comment"));
                    created.add(jo.getString("created"));
                }
                commentAdapter = new CommentAdapter(ImageActivity.this, username, comment, created, id);
                lvList.setAdapter(commentAdapter);
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
                URL url = new URL(getString(R.string.url_get_comment));

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

    class CommentAdapter extends BaseAdapter {
        Context context;
        ArrayList<String> id;
        ArrayList<String> username;
        ArrayList<String> comment;
        ArrayList<String> created;

        private LayoutInflater inflater=null;

        public CommentAdapter(Context activity, ArrayList<String> username, ArrayList<String> comment, ArrayList<String> created,
                              ArrayList<String> id) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.username = username;
            this.comment = comment;
            this.created = created;
            this.id = id;

            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return username.size();
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
            TextView tvUsername, tvCreated, tvComment;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listview_comment, null);
            holder.tvUsername = (TextView) rowView.findViewById(R.id.tvUsername);
            holder.tvCreated = (TextView) rowView.findViewById(R.id.tvCreated);
            holder.tvComment = (TextView) rowView.findViewById(R.id.tvComment);

            holder.tvComment.setText(comment.get(position));
            holder.tvCreated.setText(created.get(position));
            holder.tvUsername.setText(username.get(position));

            return rowView;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ImageActivity.this, ImageListActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
