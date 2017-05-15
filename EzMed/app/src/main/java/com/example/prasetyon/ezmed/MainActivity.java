package com.example.prasetyon.ezmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnApotek, btnDokter, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnApotek = (Button) findViewById(R.id.btnApotek);
        btnDokter = (Button) findViewById(R.id.btnDokter);

        btnApotek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, apotek.class);
                startActivity(intent);
                //finish();
            }
        });

        btnDokter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, dokter.class);
                startActivity(intent);
                //finish();
            }
        });
    }
}
