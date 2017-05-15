package com.mobile4day.uipnusra.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.mobile4day.uipnusra.CRUD.GetTaskList;
import com.mobile4day.uipnusra.CRUD.NewDialog;
import com.mobile4day.uipnusra.R;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    ListView lvTask;
    String username;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        //username = "prasetyon";
        extras = intent.getExtras();
        //extras.putString("username", username);
        if(extras!=null && extras.containsKey("username") && extras.getString("username")!=null)
            username = extras.getString("username");
        else{
            Intent goBack = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goBack);
            finish();
        }
        Toast.makeText(this, "Welcome, " + username, Toast.LENGTH_LONG).show();
        lvTask = (ListView) findViewById(R.id.lvTask);

        new GetTaskList(this, lvTask, 1, extras).execute(username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_new){
            //Toast.makeText(MainActivity.this, "Create Task", Toast.LENGTH_LONG).show();
            new NewDialog(this, 1, username, extras);
        }
        else if(id == R.id.action_logout){
            SharedPreferences preferences = getSharedPreferences("login", 0);
            preferences.edit().clear().commit();
            Intent goBack = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goBack);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

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
