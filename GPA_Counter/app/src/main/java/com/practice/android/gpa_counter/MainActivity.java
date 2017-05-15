package com.practice.android.gpa_counter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {

    EditText ed1, ed2;
    String userSks, userSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1 = (EditText) findViewById(R.id.sks);
        ed2 = (EditText) findViewById(R.id.subject);
    }

    public void inputUser(View view)
    {
        userSks = ed1.getText().toString();
        userSubject = ed2.getText().toString();

        Intent myIntent = new Intent(this, Result.class);
        Bundle bundle = new Bundle();
        bundle.putString("sks", userSks);
        bundle.putString("subject", userSubject);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }

    public void goData(View view)
    {
        userSks = ed1.getText().toString();
        userSubject = ed2.getText().toString();

        Intent myIntent = new Intent(this, Result.class);
        Bundle bundle = new Bundle();
        bundle.putString("sks", userSks);
        bundle.putString("subject", userSubject);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }
}
