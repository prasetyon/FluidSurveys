package activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal1.R;

import afterErection.AfterErectionActivity;
import blockStage.BlockStageActivity;
import dokumen.UploadFileActivity;
import inspeksiFinal.InspeksiFinalListActivity;
import rawMaterial.RawMaterialActivity;

public class ProjectMainMenuActivity extends AppCompatActivity {
    Button btnDokumen, btnRawMaterial, btnBlockStage, btnAfterErection, btnInspeksiFinal, btnBack;
    TextView tvProjectName;
    private Bundle extras;
    private String username;
    private String role;
    private String from;
    private String projectName;
    private String projectID;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_main_menu);
        setTitle("PROJECT MENU");

        Intent intent = getIntent();
        extras = intent.getExtras();
        username = extras.getString("USERNAME");
        role = extras.getString("ROLE");
        from = extras.getString("FROM");
        projectName = extras.getString("PROJECT_NAME");
        projectID = extras.getString("PROJECT_ID");

        initiate();

    }

    View.OnClickListener inspeksi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch(v.getId()){
                case R.id.btnDokumen:
                    intent = new Intent(ProjectMainMenuActivity.this, UploadFileActivity.class);
                    //intent = new Intent(ProjectMainMenuActivity.this, DokumenActivity.class);
                    break;
                case R.id.btnRawMaterial:
                    intent = new Intent(ProjectMainMenuActivity.this, RawMaterialActivity.class);
                    break;
                case R.id.btnBlockStage:
                    intent = new Intent(ProjectMainMenuActivity.this, BlockStageActivity.class);
                    break;
                case R.id.btnAfterErection:
                    intent = new Intent(ProjectMainMenuActivity.this, AfterErectionActivity.class);
                    break;
                case R.id.btnInspeksiFinal:
                    intent = new Intent(ProjectMainMenuActivity.this, InspeksiFinalListActivity.class);
                    break;
                case R.id.btnBack:
                    intent = new Intent(ProjectMainMenuActivity.this, MainPageActivity.class);
                    break;
            }
            intent.putExtras(extras);
            startActivity(intent);
            if(v.getId()!=R.id.btnDokumen)
                finish();
        }
    };

    private void initiate()
    {
        btnDokumen = (Button) findViewById(R.id.btnDokumen);
        btnDokumen.setOnClickListener(inspeksi);
        btnRawMaterial = (Button) findViewById(R.id.btnRawMaterial);
        btnRawMaterial.setOnClickListener(inspeksi);
        btnBlockStage = (Button) findViewById(R.id.btnBlockStage);
        btnBlockStage.setOnClickListener(inspeksi);
        btnAfterErection = (Button) findViewById(R.id.btnAfterErection);
        btnAfterErection.setOnClickListener(inspeksi);
        btnInspeksiFinal = (Button) findViewById(R.id.btnInspeksiFinal);
        btnInspeksiFinal.setOnClickListener(inspeksi);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(inspeksi);
        tvProjectName = (TextView) findViewById(R.id.tvProjectName);
        tvProjectName.setText(projectName);
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
