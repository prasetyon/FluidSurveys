package login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.prasetyon.proyektatekpal1.R;

public class SignUpActivity extends AppCompatActivity {

    Button btnBack, btnRegAdmin, btnRegUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(R.string.register);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnRegAdmin = (Button) findViewById(R.id.btnRegAdmin);
        btnRegUser = (Button) findViewById(R.id.btnRegUser);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, AdminSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, UserSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
