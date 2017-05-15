package com.practice.android.cobaganti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by prasetyon on 11/2/2015.
 */
public class Result extends AppCompatActivity{
    TextView selection1, selection2, selection3;
    String userName, userNrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        userName = bundle.getString("nama");
        userNrp = bundle.getString("nrp");

        selection1 = (TextView) findViewById(R.id.selectionID);
        selection2 = (TextView) findViewById(R.id.selectionName);
        selection3 = (TextView) findViewById(R.id.selectionNRP);

        selection1.append("1");
        selection2.append(userName);
        selection3.append(userNrp);
    }

    public void goInput(View view)
    {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
