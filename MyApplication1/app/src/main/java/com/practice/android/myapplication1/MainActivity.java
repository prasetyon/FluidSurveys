package com.practice.android.myapplication1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText ed1, ed2;
    String userName, userNrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        ed1 = (EditText) findViewById(R.id.nama);
        ed2 = (EditText) findViewById(R.id.nrp);
    }

    public void inputUser(View view)
    {
        userName = ed1.getText().toString();
        userNrp = ed2.getText().toString();

        Intent myIntent = new Intent(this, Result.class);
        myIntent.putExtra("name", userName);
        myIntent.putExtra("nrp", userNrp);
        startActivity(myIntent);
    }
}
