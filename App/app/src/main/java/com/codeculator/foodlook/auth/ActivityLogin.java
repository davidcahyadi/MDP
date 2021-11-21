package com.codeculator.foodlook.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.home.ActivityHome;
import com.codeculator.foodlook.services.AuthService;
import com.codeculator.foodlook.services.ResponseInterface;

import org.json.JSONObject;

public class ActivityLogin extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;
    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        authService = new AuthService(this);

        btnLogin.setOnClickListener(v->{
            login();
        });
    }


    public void login(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        authService.setResponse(new ResponseInterface() {
            @Override
            public void onSuccess(JSONObject res) {
                try{
                    PrefHelper prefHelper = new PrefHelper(ActivityLogin.this);
                    prefHelper.setAccess(res.getString("access"));
                    prefHelper.setAccess(res.getString("refresh"));

                    Intent i = new Intent(ActivityLogin.this, ActivityHome.class);
                    startActivity(i);
                }
                catch (Exception e){
                    Log.e("error",e.toString());
                    Toast.makeText(ActivityLogin.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(ActivityLogin.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        authService.login(email,password);
    }
}