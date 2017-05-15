package com.example.prasetyon.birthdaycardmis1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by prasetyon on 9/30/2016.
 */

public class Biodata extends AppCompatActivity{

    ImageView btnPrev, btnNext;
    String firstName, lastName;
    TextView tvFirstName, tvLastName, tvTempatLahir, tvTanggalLahir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biodata);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        firstName = extras.getString("firstName");
        lastName = extras.getString("lastName");

        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvTempatLahir = (TextView) findViewById(R.id.tvTempatLahir);
        tvTanggalLahir = (TextView) findViewById(R.id.tvTanggalLahir);

        tvFirstName.setText(" " + firstName);
        tvLastName.setText(" " + lastName);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        tvTempatLahir.setText(" Surabaya");
        tvTanggalLahir.setText(" " + formattedDate);

        btnPrev = (ImageView) findViewById(R.id.btnBack);
        btnNext = (ImageView) findViewById(R.id.btnNext);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Biodata.this, MainActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Biodata.this, Memories.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });
    }
}
