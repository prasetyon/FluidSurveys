package layout;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IncomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView tvInflow, tvOutflow, tvBalance;
    TextView tvBankInflow, tvBankOutflow, tvBankBalance;
    TextView tvCashInflow, tvCashOutflow, tvCashBalance;
    RelativeLayout rlBank, rlCash;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        tvInflow = (TextView) rootView.findViewById(R.id.tvInflow);
        tvOutflow = (TextView) rootView.findViewById(R.id.tvOutflow);
        tvBalance = (TextView) rootView.findViewById(R.id.tvBalance);;
        tvBankInflow = (TextView) rootView.findViewById(R.id.tvBankInflow);
        tvBankOutflow = (TextView) rootView.findViewById(R.id.tvBankOutflow);
        tvBankBalance = (TextView) rootView.findViewById(R.id.tvBankBalance);;
        tvCashInflow = (TextView) rootView.findViewById(R.id.tvCashInflow);
        tvCashOutflow = (TextView) rootView.findViewById(R.id.tvCashOutflow);
        tvCashBalance = (TextView) rootView.findViewById(R.id.tvCashBalance);
        rlBank = (RelativeLayout) rootView.findViewById(R.id.rlBank);
        rlCash = (RelativeLayout) rootView.findViewById(R.id.rlCash);
        rlBank.setOnClickListener(this);
        rlCash.setOnClickListener(this);
        new getBalance().execute();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(getActivity(), "Go To!", Toast.LENGTH_SHORT).show();
        //MainActivity main = (MainActivity) getActivity();
        //if(v == rlBank) main.goTo("BANK");
        //else if(v == rlCash) main.goTo("CASH");
        /*FragmentTransaction fragmentTransaction = null;
        if(v == rlBank) {
            BankFragment bankFragment = new BankFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, bankFragment);
        }
        else if(v == rlCash) {
            CashFragment cashFragment = new CashFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, cashFragment);
        }

        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
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

    private void showBalance(String s)
    {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            JSONObject jo = result.getJSONObject(0);

            String output;
            output = "Rp " + jo.getString("balance");
            tvBalance.setText(output);
            output = "Rp " + jo.getString("inflow");
            tvInflow.setText(output);
            output = "Rp " + jo.getString("outflow");
            tvOutflow.setText(output);
            output = "Rp " + jo.getString("cashbal");
            tvCashBalance.setText(output);
            output = "Rp " + jo.getString("cashin");
            tvCashInflow.setText(output);
            output = "Rp " + jo.getString("cashout");
            tvCashOutflow.setText(output);
            output = "Rp " + jo.getString("bankbal");
            tvBankBalance.setText(output);
            output = "Rp " + jo.getString("bankin");
            tvBankInflow.setText(output);
            output = "Rp " + jo.getString("bankout");
            tvBankOutflow.setText(output);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class getBalance extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public getBalance(){
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

            showBalance(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String create_url = "http://mobile4day.com/pkmbk-finance/get_balance.php";

            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("info", "UTF-8")+"="+URLEncoder.encode("","UTF-8");
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
