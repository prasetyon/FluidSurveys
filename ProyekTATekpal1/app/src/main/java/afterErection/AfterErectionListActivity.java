package afterErection;

import android.app.Activity;
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

import blockStage.BlockStageCheckSecondaryActivity;
import blockStage.BlockStageCheckSteelActivity;

public class AfterErectionListActivity extends AppCompatActivity {

    Spinner spinnerLokasi;
    Button btnBack;
    boolean doubleBackToExitPressedOnce = false;
    Context Activity;
    ListView lvAfterErection;
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
        setContentView(R.layout.activity_after_erection_list);
        setTitle("AFTER ERECTION");

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
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.location_list, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerLokasi.setAdapter(spinnerAdapter);
        spinnerLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lokasi = spinnerLokasi.getSelectedItem().toString();
                new getBlock().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnBack:
                    intent = new Intent(AfterErectionListActivity.this, AfterErectionActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
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
            ArrayList<String> blockSwp1 = new ArrayList<>();
            ArrayList<String> blockSspsp = new ArrayList<>();
            ArrayList<String> blockSwp2 = new ArrayList<>();
            ArrayList<String> blockSwp1ae = new ArrayList<>();
            ArrayList<String> blockSwp2ae = new ArrayList<>();
            ArrayList<String> blockSsp1c = new ArrayList<>();
            ArrayList<String> block1c1sc = new ArrayList<>();
            ArrayList<String> block1sc2sc = new ArrayList<>();
            ArrayList<String> block2sc2c = new ArrayList<>();
            ArrayList<String> block2c3c = new ArrayList<>();
            ArrayList<String> block3c4c = new ArrayList<>();
            ArrayList<String> block4c5c = new ArrayList<>();
            ArrayList<String> blockpulloff = new ArrayList<>();

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                if(jo.getString("lokasi").equals(lokasi)) {
                    blockID.add(jo.getString("id_blok"));
                    blockName.add(jo.getString("nama_blok"));
                    blockLocation.add(jo.getString("lokasi"));
                    blockSwp1.add(jo.getString("swp1"));
                    blockSspsp.add(jo.getString("sspsp"));
                    blockSwp2.add(jo.getString("swp2"));
                    blockSwp1ae.add(jo.getString("swp1ae"));
                    blockSwp2ae.add(jo.getString("swp2ae"));
                    blockSsp1c.add(jo.getString("ssp1c"));
                    block1c1sc.add(jo.getString("1c1sc"));
                    block1sc2sc.add(jo.getString("1sc2sc"));
                    block2sc2c.add(jo.getString("2sc2c"));
                    block2c3c.add(jo.getString("2c3c"));
                    block3c4c.add(jo.getString("3c4c"));
                    block4c5c.add(jo.getString("4c5c"));
                    blockpulloff.add(jo.getString("pulloff"));
                }
            }

            lvAfterErection = (ListView) findViewById(R.id.lvAfterErection);
            ListAdapter listAdapter = new ListAdapter(this, blockID, blockName, blockLocation, blockSwp1, blockSspsp, blockSwp2, blockSsp1c, block1c1sc, block1sc2sc, block2sc2c, block2c3c, block3c4c, block4c5c, blockSwp1ae, blockSwp2ae, blockpulloff);
            lvAfterErection.setAdapter(listAdapter);
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

            create_url = "http://mobile4day.com/ship-inspection/get_blok.php";

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
        ArrayList<String> blockSwp1;
        ArrayList<String> blockSspsp;
        ArrayList<String> blockSwp2;
        ArrayList<String> blockSwp1ae;
        ArrayList<String> blockSwp2ae;
        ArrayList<String> blockSsp1c;
        ArrayList<String> block1c1sc;
        ArrayList<String> block1sc2sc;
        ArrayList<String> block2sc2c;
        ArrayList<String> block2c3c;
        ArrayList<String> block3c4c;
        ArrayList<String> block4c5c;
        ArrayList<String> blockPullOff;
        private LayoutInflater inflater = null;

        public ListAdapter(Activity activity, ArrayList<String> blockID, ArrayList<String> blockName, ArrayList<String> blockLocation, ArrayList<String> blockSwp1, ArrayList<String> blockSspsp,ArrayList<String> blockSwp2, ArrayList<String> blockSsp1c,
                           ArrayList<String> block1c1sc, ArrayList<String> block1sc2sc, ArrayList<String> block2sc2c ,ArrayList<String> block2c3c, ArrayList<String> block3c4c, ArrayList<String> block4c5c
                ,ArrayList<String> blockSwp1ae, ArrayList<String> blockSwp2ae, ArrayList<String> blockPullOff) {
            // TODO Auto-generated constructor stub
            context = activity;
            this.blockID = blockID;
            this.blockName = blockName;
            this.blockLocation = blockLocation;
            this.blockSwp1 = blockSwp1;
            this.blockSspsp = blockSspsp;
            this.blockSwp2 = blockSwp2;
            this.blockSwp1ae = blockSwp1ae;
            this.blockSwp2ae = blockSwp2ae;
            this.blockSsp1c = blockSsp1c;
            this.block1c1sc = block1c1sc;
            this.block1sc2sc = block1sc2sc;
            this.block2sc2c = block2sc2c;
            this.block2c3c = block2c3c;
            this.block3c4c = block3c4c;
            this.block4c5c = block4c5c;
            this.blockPullOff = blockPullOff;
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
            Button btnHapus;
            Button btnSwp1;
            Button btnSspsp;
            Button btnSwp2;
            Button btnSwp1ae;
            Button btnSwp2ae;
            Button btnSsp1c;
            Button btn1c1sc;
            Button btn1sc2sc;
            Button btn2sc2c;
            Button btn2c3c;
            Button btn3c4c;
            Button btn4c5c;
            Button btnPullOff;
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
            holder.btnSspsp = (Button) rowView.findViewById(R.id.btnSspsp);
            holder.btnSwp1 = (Button) rowView.findViewById(R.id.btnSwp1);
            holder.btnSsp1c = (Button) rowView.findViewById(R.id.btnSsp1c);
            holder.btnSwp2 = (Button) rowView.findViewById(R.id.btnSwp2);
            holder.btnSwp1ae = (Button) rowView.findViewById(R.id.btnSwp1ae);
            holder.btnSwp2ae = (Button) rowView.findViewById(R.id.btnSwp2ae);
            holder.btn1c1sc = (Button) rowView.findViewById(R.id.btn1c1sc);
            holder.btn1sc2sc = (Button) rowView.findViewById(R.id.btn1sc2sc);
            holder.btn2sc2c = (Button) rowView.findViewById(R.id.btn2sc2c);
            holder.btn2c3c = (Button) rowView.findViewById(R.id.btn2c3c);
            holder.btn3c4c = (Button) rowView.findViewById(R.id.btn3c4c);
            holder.btn4c5c = (Button) rowView.findViewById(R.id.btn4c5c);
            holder.btnPullOff = (Button) rowView.findViewById(R.id.btnPullOff);

            holder.tvKomponen.setText(blockName.get(position) + "(" + blockLocation.get(position) + ")");

            if(!blockSwp1.get(position).equals("ae")) holder.btnSwp1.setVisibility(View.GONE);
            if(!blockSspsp.get(position).equals("ae")) holder.btnSspsp.setVisibility(View.GONE);
            if(!blockSwp2.get(position).equals("ae")) holder.btnSwp2.setVisibility(View.GONE);
            if(!blockSwp1ae.get(position).equals("ae")) holder.btnSwp1ae.setVisibility(View.GONE);
            if(!blockSwp2ae.get(position).equals("ae")) holder.btnSwp2ae.setVisibility(View.GONE);
            if(!blockSsp1c.get(position).equals("ae")) holder.btnSsp1c.setVisibility(View.GONE);
            if(!block1c1sc.get(position).equals("ae")) holder.btn1c1sc.setVisibility(View.GONE);
            if(!block1sc2sc.get(position).equals("ae")) holder.btn1sc2sc.setVisibility(View.GONE);
            if(!block2sc2c.get(position).equals("ae")) holder.btn2sc2c.setVisibility(View.GONE);
            if(!block2c3c.get(position).equals("ae")) holder.btn2c3c.setVisibility(View.GONE);
            if(!block3c4c.get(position).equals("ae")) holder.btn3c4c.setVisibility(View.GONE);
            if(!block4c5c.get(position).equals("ae")) holder.btn4c5c.setVisibility(View.GONE);
            if(!blockPullOff.get(position).equals("ae")) holder.btnPullOff.setVisibility(View.GONE);

            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionDetailActivity.class);
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnSspsp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, BlockStageCheckSecondaryActivity.class);
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnSwp1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, BlockStageCheckSteelActivity.class);
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnSsp1c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionSecondaryActivity.class);
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnSwp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionSteelActivity.class);
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnSwp1ae.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionSWP1Activity.class);
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnSwp2ae.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionSWP2Activity.class);
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btn1c1sc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionCoatActivity.class);
                    extras.putString("FROM", "1C1SC");
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btn1sc2sc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionCoatActivity.class);
                    extras.putString("FROM", "1SC2SC");
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btn2sc2c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionCoatActivity.class);
                    extras.putString("FROM", "2SC2C");
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btn2c3c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionCoatActivity.class);
                    extras.putString("FROM", "2C3C");
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btn3c4c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionCoatActivity.class);
                    extras.putString("FROM", "3C4C");
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btn4c5c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionCoatActivity.class);
                    extras.putString("FROM", "4C5C");
                    extras.putString("BLOCK_ID", blockID.get(position));
                    extras.putString("BLOCK_NAME", blockName.get(position));
                    extras.putString("BLOCK_LOCATION", blockLocation.get(position));
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
            });
            holder.btnPullOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionPullOffActivity.class);
                    extras.putString("FROM", "PULLOFF");
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
                Toast.makeText(AfterErectionListActivity.this, "You block has been deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AfterErectionListActivity.this, AfterErectionListActivity.class);
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

            String create_url = "http://mobile4day.com/ship-inspection/delete_block.php";
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
