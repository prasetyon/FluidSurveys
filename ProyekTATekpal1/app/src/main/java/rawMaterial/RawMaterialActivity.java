package rawMaterial;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal1.R;

import activity.ProjectMainMenuActivity;
import report.RawMaterialReportActivity;

public class RawMaterialActivity extends AppCompatActivity {

    Button btnAddData, btnListData, btnBack;
    boolean doubleBackToExitPressedOnce = false;
    private String projectID;
    private Bundle extras;
    private String username;
    private String role;
    private String from;
    private String projectName;

    private void initiate()
    {

        btnAddData = (Button) findViewById(R.id.btnAddData);
        btnListData = (Button) findViewById(R.id.btnListData);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnAddData.setOnClickListener(activity);
        btnListData.setOnClickListener(activity);
        btnBack.setOnClickListener(activity);
    }

    private void setEnable(String role)
    {
        if(role.equals("user") || role.equals("userNo"))
            btnAddData.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material);
        setTitle("RAW MATERIALS");

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
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.btnAddData:
                    intent = new Intent(RawMaterialActivity.this, RawMaterialAddActivity.class);
                    break;
                case R.id.btnListData:
                    intent = new Intent(RawMaterialActivity.this, RawMaterialListActivity.class);
                    break;
                case R.id.btnBack:
                    intent = new Intent(RawMaterialActivity.this, ProjectMainMenuActivity.class);
                    break;
            }
            intent.putExtras(extras);
            startActivity(intent);
            finish();
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
