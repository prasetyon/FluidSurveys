package com.example.prasetyon.ezmed;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    ImageView ivPharmacy, ivDoctor, ivAccount, ivAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivAbout = (ImageView) findViewById(R.id.ivAbout);
        ivAccount = (ImageView) findViewById(R.id.ivAccount);
        ivDoctor = (ImageView) findViewById(R.id.ivDoctor);
        ivPharmacy = (ImageView) findViewById(R.id.ivPharmacy);

        ivPharmacy.setOnClickListener(activity);
        ivDoctor.setOnClickListener(activity);
        ivAccount.setOnClickListener(activity);
        ivAbout.setOnClickListener(activity);
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch(v.getId()){
                case R.id.ivAbout:
                    intent = new Intent(MainActivity.this, AboutActivity.class);
                    break;
                case R.id.ivAccount:
                    intent = new Intent(MainActivity.this, AccountPageActivity.class);
                    break;
                case R.id.ivDoctor:
                    intent = new Intent(MainActivity.this, DoctorListActivity.class);
                    break;
                case R.id.ivPharmacy:
                    intent = new Intent(MainActivity.this, PharmacyListActivity.class);
                    break;
            }
            startActivity(intent);
        }
    };


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
