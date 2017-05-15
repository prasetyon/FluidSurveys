package com.mobile4day.soltemon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnGraphic, btnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGraphic = (Button) findViewById(R.id.btnGraphic);
        btnInfo = (Button) findViewById(R.id.btnInfo);
        btnGraphic.setOnClickListener(activity);
        btnInfo.setOnClickListener(activity);
    }

    View.OnClickListener activity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId())
            {
                case R.id.btnGraphic:
                    intent = new Intent(MainActivity.this, GraphicMonitorActivity.class);
                    break;
                case R.id.btnInfo:
                    intent = new Intent(MainActivity.this, InfoMonitorActivity.class);
                    break;
            }
            startActivity(intent);
            //finish();
        }
    };
}
