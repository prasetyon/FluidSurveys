package layout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile4day.trytabbed.MainActivity;
import com.mobile4day.trytabbed.R;

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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView lvList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String status;
    String info;
    private OnFragmentInteractionListener mListener;

    public BankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BankFragment newInstance(String param1, String param2) {
        BankFragment fragment = new BankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bank, container, false);
        lvList = (ListView) rootView.findViewById(R.id.lvList);
        new getBalanceList().execute("BANK");
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void showList(String s)
    {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            final ArrayList<String> balanceID = new ArrayList<>();
            final ArrayList<String> balanceDate = new ArrayList<>();
            final ArrayList<String> balanceInfo = new ArrayList<>();
            final ArrayList<String> balanceFlow = new ArrayList<>();
            final ArrayList<String> balanceStatus = new ArrayList<>();
            final ArrayList<String> balanceDetail = new ArrayList<>();

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                balanceID.add(jo.getString("id"));
                balanceInfo.add(jo.getString("info"));
                balanceStatus.add(jo.getString("status"));
                balanceDetail.add(jo.getString("detail"));
                String output = "Rp " + jo.getString("flow");
                balanceFlow.add(output);
                balanceDate.add(jo.getString("date"));
            }

            /*balanceID.add("");
            balanceInfo.add("");
            balanceStatus.add("");
            balanceDetail.add("");
            balanceFlow.add("");
            balanceDate.add("");*/

            ListAdapter listAdapter = new ListAdapter(getActivity(), balanceID, balanceDate, balanceInfo, balanceFlow, balanceStatus, balanceDetail);
            lvList.setAdapter(listAdapter);
            lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final int pos = position;
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.select);
                    dialog.setCancelable(true);
                    final Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
                    final Button btnDelete = (Button) dialog.findViewById(R.id.btnDelete);
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(balanceInfo.get(pos), balanceStatus.get(pos), balanceDetail.get(pos), balanceFlow.get(pos), balanceDate.get(pos), balanceID.get(pos));
                            dialog.dismiss();
                        }
                    });
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new deleteTransaction().execute(balanceID.get(pos), balanceInfo.get(pos));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String infoS, String statusS, String detailS, String flowS, String dateS, final String idS)
    {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.new_transaction);
        dialog.setTitle("Update Transaction");
        dialog.setCancelable(true);
        final EditText edDetail = (EditText) dialog.findViewById(R.id.edDetail);
        edDetail.setText(detailS);
        final EditText edFlow = (EditText) dialog.findViewById(R.id.edFlow);
        edFlow.setText(flowS);
        final EditText edDate = (EditText) dialog.findViewById(R.id.edDate);
        edDate.setText(dateS);
        final Spinner spinnerStatus = (Spinner) dialog.findViewById(R.id.spinnerStatus);
        final Spinner spinnerInfo = (Spinner) dialog.findViewById(R.id.spinnerInfo);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);

        final ArrayAdapter<CharSequence> adapterInfo = ArrayAdapter.createFromResource(getContext(), R.array.info_list, android.R.layout.simple_spinner_item);
        adapterInfo.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerInfo.setAdapter(adapterInfo);
        if(infoS.equals("BANK")) spinnerInfo.setSelection(0);
        else if(infoS.equals("PKMBK")) spinnerInfo.setSelection(1);
        else spinnerInfo.setSelection(2);
        spinnerInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), spinnerInfo.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                info = spinnerInfo.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(getContext(), R.array.status_list, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(R.layout.spinner_dropdown);
        spinnerStatus.setAdapter(adapterStatus);
        if(statusS.equals("INCOME")) spinnerStatus.setSelection(0);
        else spinnerStatus.setSelection(1);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = spinnerStatus.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detail, flow, date;
                detail = edDetail.getText().toString();
                flow = edFlow.getText().toString();
                date = edDate.getText().toString();

                new updateTransaction().execute(info, status, detail, flow, date, idS);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    class updateTransaction extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public updateTransaction(){
            ProgressDialog = new ProgressDialog(getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Updating transaction...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")){
                Toast.makeText(getContext(), "You transaction has been updated!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String info = params[0];
            String status = params[1];
            String detail = params[2];
            String flow = params[3];
            String date = params[4];
            String id = params[5];

            String create_url = "http://mobile4day.com/pkmbk-finance/update.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("info", "UTF-8")+"="+URLEncoder.encode(info,"UTF-8")+"&"+
                                URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8")+"&"+
                                URLEncoder.encode("detail","UTF-8")+"="+URLEncoder.encode(detail,"UTF-8")+"&"+
                                URLEncoder.encode("flow","UTF-8")+"="+URLEncoder.encode(flow,"UTF-8")+"&"+
                                URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"+
                                URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
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

    class deleteTransaction extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public deleteTransaction(){
            ProgressDialog = new ProgressDialog(getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Updating transaction...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(s.equals("1")){
                Toast.makeText(getContext(), "You transaction has been deleted!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String id = params[0];
            String info = params[1];

            String create_url = "http://mobile4day.com/pkmbk-finance/delete.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("info", "UTF-8")+"="+URLEncoder.encode(info,"UTF-8")+"&"+
                                URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
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
        ArrayList<String> balanceID;
        Context context;
        ArrayList<String> balanceDate;
        ArrayList<String> balanceInfo;
        ArrayList<String> balanceFlow;
        ArrayList<String> balanceStatus;
        ArrayList<String> balanceDetail;
        private LayoutInflater inflater=null;
        public ListAdapter(Activity activity, ArrayList<String> balanceID, ArrayList<String> balanceDate, ArrayList<String> balanceInfo,
                           ArrayList<String> balanceFlow, ArrayList<String> balanceStatus, ArrayList<String> balanceDetail) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.balanceID = balanceID;
            this.balanceDate = balanceDate;
            this.balanceInfo = balanceInfo;
            this.balanceFlow = balanceFlow;
            this.balanceStatus = balanceStatus;
            this.balanceDetail = balanceDetail;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return balanceID.size();
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
            TextView tvDate;
            TextView tvInfo;
            TextView tvFlow;
            TextView tvDetail;
            TextView tvStatus;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listview, null);
            holder.tvDate =(TextView) rowView.findViewById(R.id.tvDate);
            holder.tvDetail =(TextView) rowView.findViewById(R.id.tvDetail);
            holder.tvInfo =(TextView) rowView.findViewById(R.id.tvInfo);
            holder.tvFlow =(TextView) rowView.findViewById(R.id.tvFlow);
            holder.tvStatus =(TextView) rowView.findViewById(R.id.tvBalanceInfo);

            holder.tvDate.setText(balanceDate.get(position));
            holder.tvDetail.setText(balanceDetail.get(position));
            holder.tvInfo.setText(balanceInfo.get(position));
            holder.tvFlow.setText(balanceFlow.get(position));
            holder.tvStatus.setText(balanceStatus.get(position));

            if(balanceStatus.get(position).equals("INCOME")) holder.tvFlow.setTextColor(getResources().getColor(R.color.colorGreen));
            else if(balanceStatus.get(position).equals("EXPENSE")) holder.tvFlow.setTextColor(getResources().getColor(R.color.colorRed));

            return rowView;
        }
    }

    class getBalanceList extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getBalanceList(){
            ProgressDialog = new ProgressDialog(getContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Reading transactions...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            //Toast.makeText(MainPageActivity.this, "onPostExecute with s " + s, Toast.LENGTH_SHORT).show();

            showList(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/pkmbk-finance/get_data.php";
            String proses = params[0];

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("info", "UTF-8")+"="+URLEncoder.encode(proses,"UTF-8");
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
}
