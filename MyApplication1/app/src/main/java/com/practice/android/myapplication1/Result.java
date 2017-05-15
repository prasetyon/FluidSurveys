package com.practice.android.myapplication1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by prasetyon on 11/1/2015.
 */
public class Result extends AppCompatActivity {
    TextView selection1, selection2, selection3;
    String userName, userNrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        userName = intent.getStringExtra("nama");
        userNrp = intent.getStringExtra("nrp");

        selection1 = (TextView) findViewById(R.id.selectionID);
        selection2 = (TextView) findViewById(R.id.selectionName);
        selection3 = (TextView) findViewById(R.id.selectionNRP);

        selection1.append("NAMA");
        selection2.append(userName);
        selection3.append(userNrp);
    }
}
