package com.example.prasetyon.birthdaycardmis1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView btnPrev, btnNext;
    TextView tvFirstName, tvLastName;
    private String firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        firstName = extras.getString("firstName");
        lastName = extras.getString("lastName");

        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);

        tvFirstName.setText(firstName);
        tvLastName.setText(lastName);

        btnPrev = (ImageView) findViewById(R.id.btnBack);
        btnNext = (ImageView) findViewById(R.id.btnNext);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Memories.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Biodata.class);
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });
    }
}
