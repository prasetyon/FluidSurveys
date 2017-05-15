package com.mobile4day.uipnusra.CRUD;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile4day.uipnusra.Activity.ImageListActivity;
import com.mobile4day.uipnusra.R;
import com.mobile4day.uipnusra.Activity.SubTaskActivity;
import com.mobile4day.uipnusra.Activity.TaskActivity;

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

/**
 * Created by prasetyon on 3/10/2017.
 */

public class GetTaskList extends AsyncTask<String, String, String> {
    android.app.ProgressDialog ProgressDialog;
    Context activity;
    int from=0;
    ListAdapter listAdapter;
    ListView lvList;
    String username;
    Bundle extras;

    public GetTaskList(Context activity, ListView lvList, int from, Bundle extras) {
        this.activity = activity;
        this.lvList = lvList;
        this.from = from;
        this.extras = extras;
        this.username = extras.getString("username");
        ProgressDialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressDialog.setMessage("Reading list...");
        ProgressDialog.show();
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
            ArrayList<String> subNum = new ArrayList<>();
            ArrayList<String> status = new ArrayList<>();

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                id.add(jo.getString("id"));
                name.add(jo.getString("name"));
                subNum.add(jo.getString("subnum"));
                status.add(jo.getString("status"));
            }

            listAdapter = new ListAdapter(activity, id, name, subNum, status, from);
            lvList.setAdapter(listAdapter);
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

        String id = params[0];

        try {
            URL url = null;
            switch(from)
            {
                case 0:
                    break;
                case 1:
                    url = new URL(activity.getString(R.string.url_get_task));
                    break;
                case 2:
                    url = new URL(activity.getString(R.string.url_get_subtask));
                    break;
                case 3:
                    url = new URL(activity.getString(R.string.url_get_subsubtask));
                    break;
                default:
                    break;

            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                    URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(from), "UTF-8");
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

    class ListAdapter extends BaseAdapter {

        Context context;
        int from;
        ArrayList<String> id;
        ArrayList<String> name;
        ArrayList<String> subNumber;
        ArrayList<String> status;

        private LayoutInflater inflater=null;

        public ListAdapter(Context activity, ArrayList<String> id, ArrayList<String> name, ArrayList<String> subNumber, ArrayList<String> status, int from) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.id = id;
            this.name = name;
            this.subNumber = subNumber;
            this.from = from;
            this.status = status;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return id.size();
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
            TextView tvName, tvSubNum;
            ImageView ivDelete, ivUpdate, ivOk;
            LinearLayout llView;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listview_task, null);
            holder.llView = (LinearLayout) rowView.findViewById(R.id.llView);
            holder.tvName = (TextView) rowView.findViewById(R.id.tvName);
            holder.tvSubNum = (TextView) rowView.findViewById(R.id.tvSubNum);
            holder.ivDelete = (ImageView) rowView.findViewById(R.id.ivDelete);
            holder.ivUpdate = (ImageView) rowView.findViewById(R.id.ivUpdate);
            holder.ivOk = (ImageView) rowView.findViewById(R.id.ivOk);

            holder.tvName.setText(name.get(position));
            holder.tvSubNum.setText(subNumber.get(position));
            if(status.get(position).equals("OK")) {
                holder.ivOk.setVisibility(View.INVISIBLE);
                holder.llView.setBackgroundResource(R.drawable.select_ok);
            }

            if(!username.equals("PLN UPP LOMBOK")){
                holder.ivDelete.setVisibility(View.INVISIBLE);
                holder.ivUpdate.setVisibility(View.INVISIBLE);
                holder.ivOk.setVisibility(View.INVISIBLE);;
            }

            holder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    switch(from)
                    {
                        case 0:
                            break;
                        case 1:
                            intent = new Intent(context, TaskActivity.class);
                            extras.putString("id_task", id.get(position));
                            extras.putString("task_name", name.get(position));
                            break;
                        case 2:
                            intent = new Intent(context, SubTaskActivity.class);
                            extras.putString("id_sub_task", id.get(position));
                            extras.putString("sub_task_name", name.get(position));
                            break;
                        case 3:
                            intent = new Intent(context, ImageListActivity.class);
                            extras.putString("id_sub_sub_task", id.get(position));
                            extras.putString("sub_sub_task_name", name.get(position));
                            break;
                        default:
                            break;
                    }
                    intent.putExtras(extras);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
            holder.ivUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new UpdateDialog(context, from, id.get(position), name.get(position), extras);
                }
            });
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DeleteDialog(context, from, id.get(position), name.get(position), extras);
                }
            });
            holder.ivOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new OkDialog(context, from, id.get(position), name.get(position), extras);
                }
            });
            return rowView;
        }
    }
}