package com.mobile4day.uipnusra.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile4day.uipnusra.Function.DownloadImageTask;
import com.mobile4day.uipnusra.Function.UploadActivity;
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

import static android.widget.Toast.LENGTH_LONG;

public class ImageListActivity extends AppCompatActivity {

    String id;
    ListView lvImage;
    TextView tvName;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        Intent intent = getIntent();
        extras = intent.getExtras();

        tvName = (TextView) findViewById(R.id.tvName);
        lvImage = (ListView) findViewById(R.id.lvImage);

        id = extras.getString("id_sub_sub_task");
        String name = extras.getString("sub_sub_task_name");
        tvName.setText(name);
        new GetImageList(this, lvImage, extras).execute(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_new){
            finish();
            Intent intent = new Intent(ImageListActivity.this, UploadActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
        else if(id == R.id.action_logout){
            SharedPreferences preferences = getSharedPreferences("login", 0);
            preferences.edit().clear().commit();
            Intent goBack = new Intent(ImageListActivity.this, LoginActivity.class);
            startActivity(goBack);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetImageList extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;
        Context activity;
        ImageAdapter imageAdapter;
        ListView lvList;
        Bundle extras;

        public GetImageList(Context activity, ListView lvList, Bundle extras) {
            this.activity = activity;
            this.lvList = lvList;
            this.extras = extras;
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading list...");
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

                ArrayList<String> id1 = new ArrayList<>();
                ArrayList<String> id2 = new ArrayList<>();
                ArrayList<String> id3 = new ArrayList<>();
                ArrayList<String> image1 = new ArrayList<>();
                ArrayList<String> image2 = new ArrayList<>();
                ArrayList<String> image3 = new ArrayList<>();
                ArrayList<String> detail1 = new ArrayList<>();
                ArrayList<String> detail2 = new ArrayList<>();
                ArrayList<String> detail3 = new ArrayList<>();
                ArrayList<String> created1 = new ArrayList<>();
                ArrayList<String> created2 = new ArrayList<>();
                ArrayList<String> created3 = new ArrayList<>();
                ArrayList<String> username1 = new ArrayList<>();
                ArrayList<String> username2 = new ArrayList<>();
                ArrayList<String> username3 = new ArrayList<>();

                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);

                    switch(i%3)
                    {
                        case 0:
                            id1.add(jo.getString("id"));
                            image1.add(jo.getString("url"));
                            detail1.add(jo.getString("detail"));
                            created1.add(jo.getString("created"));
                            username1.add(jo.getString("username"));
                            break;
                        case 1:
                            id2.add(jo.getString("id"));
                            image2.add(jo.getString("url"));
                            detail2.add(jo.getString("detail"));
                            created2.add(jo.getString("created"));
                            username2.add(jo.getString("username"));
                            break;
                        case 2:
                            id3.add(jo.getString("id"));
                            image3.add(jo.getString("url"));
                            detail3.add(jo.getString("detail"));
                            created3.add(jo.getString("created"));
                            username3.add(jo.getString("username"));
                            break;
                    }
                }

                if(result.length()<2 && result.length()>0){
                    image2.add(null);
                    image3.add(null);
                    id2.add(null);
                    id3.add(null);
                    created2.add(null);
                    created3.add(null);
                    detail2.add(null);
                    detail3.add(null);
                    username2.add(null);
                    username3.add(null);
                }
                else if(result.length()<3 && result.length()>0){
                    image3.add(null);
                    id3.add(null);
                    created3.add(null);
                    detail3.add(null);
                    username3.add(null);
                }
                imageAdapter = new ImageAdapter(activity, image1, image2, image3, id1, id2, id3, detail1, detail2, detail3,
                        created1, created2, created3, username1, username2, username3);
                lvList.setAdapter(imageAdapter);
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
                URL url = new URL(activity.getString(R.string.url_get_image_list));

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

    class ImageAdapter extends BaseAdapter {
        Context context;
        ArrayList<String> id1;
        ArrayList<String> id2;
        ArrayList<String> id3;
        ArrayList<String> image1;
        ArrayList<String> image2;
        ArrayList<String> image3;
        ArrayList<String> detail1;
        ArrayList<String> detail2;
        ArrayList<String> detail3;
        ArrayList<String> created1;
        ArrayList<String> created2;
        ArrayList<String> created3;
        ArrayList<String> username1;
        ArrayList<String> username2;
        ArrayList<String> username3;

        private LayoutInflater inflater=null;

        public ImageAdapter(Context activity, ArrayList<String> image1, ArrayList<String> image2, ArrayList<String> image3,
                            ArrayList<String> id1, ArrayList<String> id2, ArrayList<String> id3, ArrayList<String> detail1,
                            ArrayList<String> detail2, ArrayList<String> detail3, ArrayList<String> created1, ArrayList<String> created2,
                            ArrayList<String> created3, ArrayList<String> username1, ArrayList<String> username2, ArrayList<String> username3) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.image1 = image1;
            this.image2 = image2;
            this.image3 = image3;
            this.id1 = id1;
            this.id2 = id2;
            this.id3 = id3;
            this.created1 = created1;
            this.created2 = created2;
            this.created3 = created3;
            this.detail1 = detail1;
            this.detail2 = detail2;
            this.detail3 = detail3;
            this.username1 = username1;
            this.username2 = username2;
            this.username3 = username3;

            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return image1.size();
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
            ImageView ivImage1, ivImage2, ivImage3;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listview_image, null);
            holder.ivImage1 = (ImageView) rowView.findViewById(R.id.ivImage1);
            holder.ivImage2 = (ImageView) rowView.findViewById(R.id.ivImage2);
            holder.ivImage3 = (ImageView) rowView.findViewById(R.id.ivImage3);

            holder.ivImage1.setVisibility(View.INVISIBLE);
            holder.ivImage2.setVisibility(View.INVISIBLE);
            holder.ivImage3.setVisibility(View.INVISIBLE);

            if(id1.get(position)!=null) {
                holder.ivImage1.setVisibility(View.VISIBLE);
                new DownloadImageTask(holder.ivImage1).execute(image1.get(position));
                holder.ivImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent = new Intent(ImageListActivity.this, ImageActivity.class);
                        extras.putString("id_image", id1.get(position));
                        extras.putString("url_image", image1.get(position));
                        extras.putString("detail_image", detail1.get(position));
                        extras.putString("created_image", created1.get(position));
                        extras.putString("username_image", username1.get(position));
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
            }

            if(id2.get(position)!=null) {
                holder.ivImage2.setVisibility(View.VISIBLE);
                new DownloadImageTask(holder.ivImage2).execute(image2.get(position));
                holder.ivImage2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent = new Intent(ImageListActivity.this, ImageActivity.class);
                        extras.putString("id_image", id2.get(position));
                        extras.putString("url_image", image2.get(position));
                        extras.putString("detail_image", detail2.get(position));
                        extras.putString("created_image", created2.get(position));
                        extras.putString("username_image", username2.get(position));
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
            }

            if(id3.get(position)!=null) {
                holder.ivImage3.setVisibility(View.VISIBLE);
                new DownloadImageTask(holder.ivImage3).execute(image3.get(position));
                holder.ivImage3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        Intent intent = new Intent(ImageListActivity.this, ImageActivity.class);
                        extras.putString("id_image", id3.get(position));
                        extras.putString("url_image", image3.get(position));
                        extras.putString("detail_image", detail3.get(position));
                        extras.putString("created_image", created3.get(position));
                        extras.putString("username_image", username3.get(position));
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                });
            }

            return rowView;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ImageListActivity.this, SubTaskActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
