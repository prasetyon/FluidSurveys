package com.mobile4day.ezmedical.Function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile4day.ezmedical.R;

import java.util.ArrayList;

/**
 * Created by prasetyon on 2/23/2017.
 */

public class ListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> id;
    ArrayList<String> name;
    ArrayList<String> location;
    ArrayList<String> phone;
    ArrayList<String> info;
    ArrayList<String> operational;
    ArrayList<String> photo;

    private LayoutInflater inflater=null;

    public ListAdapter(Context activity, ArrayList<String> id, ArrayList<String> name, ArrayList<String> location, ArrayList<String> phone, ArrayList<String> info, ArrayList<String> operational, ArrayList<String> photo) {
        // TODO Auto-generated constructor stub
        context=activity;
        this.photo = photo;
        this.id = id;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.info= info;
        this.operational = operational;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return photo.size();
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
        TextView tvName, tvLocation, tvOperational, tvInfo, tvPhone;
        ImageView ivImage;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list, null);
        holder.tvName = (TextView) rowView.findViewById(R.id.tvName);
        holder.tvLocation = (TextView) rowView.findViewById(R.id.tvLocation);
        holder.tvOperational = (TextView) rowView.findViewById(R.id.tvOperational);
        holder.tvInfo = (TextView) rowView.findViewById(R.id.tvInfo);
        holder.tvPhone = (TextView) rowView.findViewById(R.id.tvPhone);
        holder.ivImage = (ImageView) rowView.findViewById(R.id.ivPhoto);

        holder.tvName.setText(name.get(position));
        holder.tvLocation.setText(location.get(position));
        holder.tvOperational.setText(operational.get(position));
        holder.tvInfo.setText(info.get(position));
        holder.tvPhone.setText(phone.get(position));
        new DownloadImageTask(holder.ivImage).execute(photo.get(position));

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new OpenDialog(context, name.get(position), photo.get(position), location.get(position));
            }
        });

        return rowView;
    }
}