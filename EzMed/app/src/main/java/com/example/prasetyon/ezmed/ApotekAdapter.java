package com.example.prasetyon.ezmed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prasetyon.ezmed.R;
/**
 * Created by prasetyon on 9/28/2016.
 */

public class ApotekAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ApotekAdapter(Context context, String[] values) {
        super(context, R.layout.list_apotek, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_apotek, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.namaApotek);
        TextView lokasi = (TextView) rowView.findViewById(R.id.lokasiApotek);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listImage);
        textView.setText(values[position]);

        // Change icon based on name
        String s = values[position];

        System.out.println(s);

        if (s.equals("Apotek Tujuh")) {
            imageView.setImageResource(R.drawable.download_1);
            lokasi.setText("Jalan Mulyosari no 52, Surabaya");
        } else if (s.equals("Apotek Kimia Farma")) {
            imageView.setImageResource(R.drawable.download2);
            lokasi.setText("Jalan Teknik Kimia ITS, Surabaya");
        } else if (s.equals("Apotek Haha")) {
            imageView.setImageResource(R.drawable.download3);
            lokasi.setText("Jalan Terus tapi Gak Jadian, Surabaya");
        }

        return rowView;
    }
}
