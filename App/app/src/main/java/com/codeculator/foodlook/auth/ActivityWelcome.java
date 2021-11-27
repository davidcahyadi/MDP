package com.codeculator.foodlook.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityRegisterBinding;
import com.codeculator.foodlook.databinding.ActivityWelcomeBinding;
import com.codeculator.foodlook.helper.ResultLauncherHelper;

public class ActivityWelcome extends AppCompatActivity {

    ActivityWelcomeBinding binding;
    ResultLauncherHelper launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        launcher = new ResultLauncherHelper(this);

        binding.login.setOnClickListener(v -> {
            Intent i = new Intent(ActivityWelcome.this, ActivityLogin.class);
            launcher.launch(i);
        });

        binding.register.setOnClickListener(v -> {
            Intent i = new Intent(ActivityWelcome.this, ActivityRegister.class);
            launcher.launch(i);
        });
    }
}