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
import com.codeculator.foodlook.helper.Validator;
import com.codeculator.foodlook.home.ActivityHome;
import com.codeculator.foodlook.services.HTTPRequest;

import org.json.JSONObject;

import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister;
    HTTPRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        request = new HTTPRequest(this);

        btnLogin.setOnClickListener(v->{
            login();
        });
    }


    public void login(){
        Validator validator = new Validator();
        validator.validate(etEmail).required();
        validator.validate(etPassword).required().min(6);

        if(validator.isValid()){
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            HashMap<String,String> data = new HashMap<>();
            data.put("email",email);
            data.put("password",password);

            HTTPRequest.Response response = new HTTPRequest.Response();

            response.onError(e->{});

            response.onSuccess(res->{
                try{
                    JSONObject json = new JSONObject(res);
                    PrefHelper prefHelper = new PrefHelper(ActivityLogin.this);
                    prefHelper.setAccess(json.getString("access"));
                    prefHelper.setRefresh(json.getString("refresh"));
                    Intent i = new Intent(ActivityLogin.this, ActivityHome.class);
                    startActivity(i);
                }
                catch (Exception e){}
            });

            request.post(getString(R.string.APP_URL)+"/auth/login",data,response);
        }
        else{
            Toast.makeText(this, validator.getMessage(), Toast.LENGTH_SHORT).show();
        }




    }
}