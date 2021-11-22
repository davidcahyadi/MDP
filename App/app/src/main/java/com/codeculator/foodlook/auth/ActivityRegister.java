package com.codeculator.foodlook.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codeculator.foodlook.R;


public class ActivityRegister extends AppCompatActivity {

    TextView tv_register_to_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tv_register_to_login = findViewById(R.id.tv_register_to_login);
        tv_register_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(i);
                finish();
            }
        });
    }
}