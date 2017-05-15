package com.example.prasetyon.birthdaycardmis1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by prasetyon on 9/30/2016.
 */

public class login extends AppCompatActivity {

    private Button btnLogin;
    private EditText edFirstName, edLastName;
    public static String firstName="", lastName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        edFirstName = (EditText) findViewById(R.id.edFirstName);
        edLastName = (EditText) findViewById(R.id.edLastName);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("firstName", edFirstName.getText().toString());
                extras.putString("lastName", edLastName.getText().toString());
                intent.putExtras(extras);
                startActivity(intent);
                finish();
            }
        });
    }
}
