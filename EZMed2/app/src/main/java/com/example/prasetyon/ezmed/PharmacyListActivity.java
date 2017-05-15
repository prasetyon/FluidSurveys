package com.example.prasetyon.ezmed;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PharmacyListActivity extends AppCompatActivity {

    ListView lvPharmacy;
    Context activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_list);
        setTitle("Pharmacy");

        activity = this;
        lvPharmacy = (ListView) findViewById(R.id.lvPharmacy);

        setResource();
    }

    private void setResource()
    {
        final ArrayList<String> fileName = new ArrayList<>();
        final ArrayList<String> fileLocation = new ArrayList<>();
        final ArrayList<String> fileOperational = new ArrayList<>();
        final ArrayList<String> fileImage = new ArrayList<>();
        ListAdapter adapter;

        fileName.add("Apotek Sehat Bersama");
        fileName.add("Apotek Kifia Marma");
        fileName.add("Apotek Gajah Tunggal");
        fileName.add("Apotek Sehat Indah Berseri");
        fileName.add("Apotek Waluyo");

        fileLocation.add("Jl. Mulyosari 123, Surabaya");
        fileLocation.add("Jl. Kertajaya 1, Surabaya");
        fileLocation.add("Jl. Wonokromo 112, Surabaya");
        fileLocation.add("Sidokare Indah XX-20, Sidoarjo");
        fileLocation.add("Jl. Pantai Malang Selatan 22, Malang");

        fileOperational.add("09:00 - 21:00");
        fileOperational.add("07:00 - 22:00");
        fileOperational.add("17:00 - 21:00");
        fileOperational.add("06:00 - 21:00");
        fileOperational.add("06:00 - 08:00");

        fileImage.add("@drawable/apotek1");
        fileImage.add("@drawable/apotek2");
        fileImage.add("@drawable/apotek3");
        fileImage.add("@drawable/apotek4");
        fileImage.add("@drawable/apotek5");

        adapter = new Adapter(this, fileName, fileLocation, fileOperational, fileImage);
        lvPharmacy.setAdapter(adapter);
        lvPharmacy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.pharmacyview);
                dialog.setTitle("Pharmacy Description");
                dialog.setCancelable(true);
                ImageView img = (ImageView) dialog.findViewById(R.id.ivImage);
                TextView nama = (TextView) dialog.findViewById(R.id.tvNama);
                TextView lokasi = (TextView) dialog.findViewById(R.id.tvLokasi);
                TextView jam = (TextView) dialog.findViewById(R.id.tvOperasional);

                String image = fileImage.get(position);
                String namas = fileName.get(position);
                String lokasis = fileLocation.get(position);
                String jams = fileOperational.get(position);

                img.setImageResource(activity.getResources().getIdentifier(image, null, activity.getPackageName()));
                nama.setText(namas);
                lokasi.setText(lokasis);
                jam.setText(jams);

                dialog.show();
            }
        });
    }

    public class Adapter extends BaseAdapter {

        Context context;
        ArrayList<String> fileName;
        ArrayList<String> fileLocation;
        ArrayList<String> fileOperational;
        ArrayList<String> fileImage;

        private LayoutInflater inflater=null;
        public Adapter(Context activity, ArrayList<String> fileName, ArrayList<String> fileLocation, ArrayList<String> fileOperational, ArrayList<String> fileImage) {
            // TODO Auto-generated constructor stub
            context=activity;
            this.fileImage = fileImage;
            this.fileName = fileName;
            this.fileLocation = fileLocation;
            this.fileOperational = fileOperational;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fileImage.size();
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
            TextView tvNama, tvLokasi, tvOperasional;
            ImageView ivImage;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.listview, null);
            holder.tvNama = (TextView) rowView.findViewById(R.id.tvNama);
            holder.tvLokasi = (TextView) rowView.findViewById(R.id.tvLokasi);
            holder.tvOperasional = (TextView) rowView.findViewById(R.id.tvOperasional);
            holder.ivImage = (ImageView) rowView.findViewById(R.id.ivImage);

            holder.tvNama.setText(fileName.get(position));
            holder.tvLokasi.setText(fileLocation.get(position));
            holder.tvOperasional.setText(fileOperational.get(position));
            holder.ivImage.setImageResource(activity.getResources().getIdentifier(fileImage.get(position), null, activity.getPackageName()));

            return rowView;
        }
    }
}
