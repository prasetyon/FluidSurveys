package com.mobile4day.absensicamp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnAssign, btnRegis, btnBerangkat, btnCamp, btnPulang, btnManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiate();
    }

    private void initiate()
    {
        btnAssign = (Button) findViewById(R.id.btnAssign);
        btnRegis = (Button) findViewById(R.id.btnRegis);
        btnBerangkat = (Button) findViewById(R.id.btnBerangkat);
        btnCamp = (Button) findViewById(R.id.btnCamp);
        btnPulang = (Button) findViewById(R.id.btnPulang);
        btnManual = (Button) findViewById(R.id.btnManual);
        btnAssign.setOnClickListener(activity);
        btnRegis.setOnClickListener(activity);
        btnBerangkat.setOnClickListener(activity);
        btnCamp.setOnClickListener(activity);
        btnPulang.setOnClickListener(activity);
        btnManual.setOnClickListener(activity);
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch(v.getId()){
                case R.id.btnAssign:
                    intent = new Intent(MainActivity.this, AssignBus.class);
                    break;
                case R.id.btnRegis:
                    intent = new Intent(MainActivity.this, Regis.class);
                    break;
                case R.id.btnBerangkat:
                    intent = new Intent(MainActivity.this, AbsenBus.class);
                    intent.putExtra("EVENT", "BERANGKAT");
                    break;
                case R.id.btnCamp:
                    intent = new Intent(MainActivity.this, AbsenSesi.class);
                    intent.putExtra("EVENT", "CAMP");
                    break;
                case R.id.btnPulang:
                    intent = new Intent(MainActivity.this, AbsenBus.class);
                    intent.putExtra("EVENT", "PULANG");
                    break;
                case R.id.btnManual:
                    intent = new Intent(MainActivity.this, AbsenManual.class);
                    break;
            }
            startActivity(intent);
        }
    };
}