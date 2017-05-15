package com.mobile4day.ezmedical.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobile4day.ezmedical.Function.DownloadImageTask;
import com.mobile4day.ezmedical.R;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    ImageView ivPharmacy, ivDoctor, ivAccount, ivAbout;

    String name, email, address, phone, photo;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        Intent intent = getIntent();
        extras = intent.getExtras();
        name = extras.getString("NAME");
        email = extras.getString("EMAIL");
        address = extras.getString("ADDRESS");
        phone = extras.getString("PHONE");
        photo = extras.getString("PHOTO");

        Toast.makeText(MainActivity.this, "Welcome " + name, Toast.LENGTH_LONG).show();

        ivAbout = (ImageView) findViewById(R.id.ivAbout);
        ivAccount = (ImageView) findViewById(R.id.ivAccount);
        ivDoctor = (ImageView) findViewById(R.id.ivDoctor);
        ivPharmacy = (ImageView) findViewById(R.id.ivPharmacy);

        new DownloadImageTask(ivAbout).execute(getString(R.string.about_background_url));
        new DownloadImageTask(ivAccount).execute(getString(R.string.account_background_url));
        new DownloadImageTask(ivDoctor).execute(getString(R.string.doctor_background_url));
        new DownloadImageTask(ivPharmacy).execute(getString(R.string.pharmacy_background_url));

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
                    intent = new Intent(MainActivity.this, AccountActivity.class);
                    break;
                case R.id.ivDoctor:
                    intent = new Intent(MainActivity.this, DoctorActivity.class);
                    break;
                case R.id.ivPharmacy:
                    intent = new Intent(MainActivity.this, PharmacyActivity.class);
                    break;
            }
            intent.putExtras(extras);
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
