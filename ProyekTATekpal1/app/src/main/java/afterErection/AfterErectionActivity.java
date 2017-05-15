package afterErection;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal1.R;

import activity.ProjectMainMenuActivity;

public class AfterErectionActivity extends AppCompatActivity {

    Button btnBack, btnAddData, btnListData;
    TextView tvProjectAfterErection;
    boolean doubleBackToExitPressedOnce = false;
    private Bundle extras;
    private String projectID;
    private String username;
    private String role;
    private String from;
    private String projectName;

    private void initiate()
    {
        tvProjectAfterErection = (TextView) findViewById(R.id.tvProjectAfterErection);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnAddData = (Button) findViewById(R.id.btnAddData);
        btnListData= (Button) findViewById(R.id.btnListData);

        btnBack.setOnClickListener(activity);
        btnAddData.setOnClickListener(activity);
        btnListData.setOnClickListener(activity);
    }

    private void setEnable(String role)
    {
        if(role.equals("user") || role.equals("userNo"))
        {
            btnAddData.setVisibility(View.GONE);
        }
        else if(role.equals("admin")) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_erection);
        setTitle("AFTER ERECTION");

        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");

        initiate();
        setEnable(role);
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.btnBack: intent = new Intent(AfterErectionActivity.this, ProjectMainMenuActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btnAddData: intent = new Intent(AfterErectionActivity.this, AfterErectionAddActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btnListData: intent = new Intent(AfterErectionActivity.this, AfterErectionListActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                    break;
            }
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