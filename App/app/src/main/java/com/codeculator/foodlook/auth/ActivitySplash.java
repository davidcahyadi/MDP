package com.codeculator.foodlook.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivitySplashBinding;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.services.RetrofitApi;

public class ActivitySplash extends AppCompatActivity {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        View view = binding.getRoot();
        setContentView(view);

        binding.progressBar.setMax(100);
        
        new CountDownTimer(1500,20) {
            @Override
            public void onTick(long l) {
                binding.progressBar.setProgress(binding.progressBar.getProgress()+2);
            }

            @Override
            public void onFinish() {
                redirect();
            }
        }.start();

        // assign retrotif api url
        RetrofitApi.BASE_URL = getString(R.string.APP_URL)+"/";
    }

    private void redirect(){
        PrefHelper prefHelper = new PrefHelper(this);
        Intent i;
        // if has refresh token redirect to welcome screen
        if(!prefHelper.getRefresh().equals("null"))
            i = new Intent(this,ActivityWelcome.class);
        // else redirect to login screen
        else i = new Intent(this,ActivityLogin.class);
        startActivity(i);
        finish();
    }
}