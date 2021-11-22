package com.codeculator.foodlook.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityLoginBinding;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.helper.Validator;
import com.codeculator.foodlook.home.ActivityHome;
import com.codeculator.foodlook.services.HTTPRequest;

import org.json.JSONObject;

import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity {
    HTTPRequest request;

    private ActivityLoginBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        request = new HTTPRequest(this);

        binding.button.setOnClickListener(v->{
            login();
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(i);
                finish();
            }
        });
    }


    public void login(){
        Validator validator = new Validator();
        validator.validate(binding.email,binding.emailLayout).required();
        validator.validate(binding.password,binding.passwordLayout).required().min(6);

        if(validator.isValid()){
            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();
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
    }
}