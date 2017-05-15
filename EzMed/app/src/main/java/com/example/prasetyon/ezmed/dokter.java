package com.example.prasetyon.ezmed;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by prasetyon on 9/28/2016.
 */

public class dokter extends ListActivity {
    static final String[] DOKTER =
            new String[] { "Dr Prasetyo Nugrohadi", "Dr Reynaldo Johanes", "Dr Steven Kurkur"};
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.apotek);

        setListAdapter(new DokterAdapter(this, DOKTER));
        /*btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dokter.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

    }
}
