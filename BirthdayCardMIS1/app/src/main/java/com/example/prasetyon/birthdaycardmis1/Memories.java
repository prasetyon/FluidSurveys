package com.example.prasetyon.birthdaycardmis1;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by prasetyon on 9/30/2016.
 */

public class Memories extends Activity{

    ImageView btnPrev, btnNext;
    TextView tvKeterangan;
    String firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memories);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        firstName = extras.getString("firstName");
        lastName = extras.getString("lastName");

        tvKeterangan = (TextView) findViewById(R.id.tvKeterangan);
        btnPrev = (ImageView) findViewById(R.id.btnBack);
        btnNext = (ImageView) findViewById(R.id.btnNext);

        tvKeterangan.setText(firstName + " " + lastName + "'s Memories");

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Memories.this, Biodata.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Memories.this, MainActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });
    }
}
