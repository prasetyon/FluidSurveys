package com.mobile4day.uipnusra.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile4day.uipnusra.CRUD.GetTaskList;
import com.mobile4day.uipnusra.CRUD.NewDialog;
import com.mobile4day.uipnusra.R;

public class SubTaskActivity extends AppCompatActivity {

    ListView lvTask;
    TextView tvName;
    String id;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_task);

        Intent intent = getIntent();
        extras = intent.getExtras();
        id = extras.getString("id_sub_task");
        String name = extras.getString("sub_task_name");
        lvTask = (ListView) findViewById(R.id.lvTask);
        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(name);

        new GetTaskList(this, lvTask, 3, extras).execute(id);
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
            new NewDialog(this, 3, this.id, extras);
        }
        else if(id == R.id.action_logout){
            SharedPreferences preferences = getSharedPreferences("login", 0);
            preferences.edit().clear().commit();
            Intent goBack = new Intent(SubTaskActivity.this, LoginActivity.class);
            startActivity(goBack);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(SubTaskActivity.this, TaskActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
