package com.codeculator.foodlook.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codeculator.foodlook.R;

public class ActivityHome extends AppCompatActivity {

    TextView tv_home_gotoregister, tv_home_gotologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home0);

        tv_home_gotoregister = findViewById(R.id.tv_home_gotoregister);
        tv_home_gotologin = findViewById(R.id.tv_home_gotologin);

        tv_home_gotologin.setOnClickListener(view -> {
            Intent i = new Intent(ActivityHome.this, ActivityLogin.class);
            startActivity(i);
            finish();
        });

        tv_home_gotoregister.setOnClickListener(view -> {
            Intent i = new Intent(ActivityHome.this, ActivityRegister.class);
            startActivity(i);
            finish();
        });
    }
}