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

public class DokterAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public DokterAdapter(Context context, String[] values) {
        super(context, R.layout.list_dokter, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_dokter, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.namaDokter);
        TextView lokasi = (TextView) rowView.findViewById(R.id.lokasiDokter);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listImageDokter);
        textView.setText(values[position]);

        // Change icon based on name
        String s = values[position];

        System.out.println(s);

        if (s.equals("Dr Prasetyo Nugrohadi")) {
            imageView.setImageResource(R.drawable.hs1);
            lokasi.setText("Jalan Mulyosari no 52, Surabaya");
        } else if (s.equals("Dr Reynaldo Johanes")) {
            imageView.setImageResource(R.drawable.hs2);
            lokasi.setText("Jalan Teknik Kimia ITS, Surabaya");
        } else if (s.equals("Dr Steven Kurkur")) {
            imageView.setImageResource(R.drawable.hs3);
            lokasi.setText("Jalan Terus tapi Gak Jadian, Surabaya");
        }

        return rowView;
    }
}
