package com.codeculator.foodlook.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityLoginBinding;
import com.codeculator.foodlook.databinding.ActivityRegisterBinding;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.helper.Validator;
import com.codeculator.foodlook.home.ActivityHome;
import com.codeculator.foodlook.services.HTTPRequest;

import org.json.JSONObject;

import java.util.HashMap;


public class ActivityRegister extends AppCompatActivity {


    private ActivityRegisterBinding binding;
    HTTPRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        request = new HTTPRequest(this);

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(i);
                finish();
            }
        });


        binding.register.setOnClickListener(v->{
            register();
        });
    }

    private void register(){
        Validator validator = new Validator();
        validator.validate(binding.email,binding.emailLayout).required();
        validator.validate(binding.name,binding.namelayout).required();
        validator.validate(binding.password,binding.passwordLayout).required().min(6);
        validator.validate(binding.confirm,binding.confirmLayout).required().min(6);

        if(validator.isValid()){
            HashMap<String,String> data = new HashMap<>();
            data.put("email",binding.email.getText().toString());
            data.put("password",binding.password.getText().toString());
            data.put("confirm_password",binding.confirm.getText().toString());
            data.put("name",binding.name.getText().toString());

            HTTPRequest.Response response = new HTTPRequest.Response();

            response.onError(e->{
                Toast.makeText(this, "Register Error", Toast.LENGTH_SHORT).show();
            });

            response.onSuccess(res->{
                try{
                    Toast.makeText(ActivityRegister.this, "Success Register", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
                    startActivity(i);
                }
                catch (Exception e){}
            });

            request.post(getString(R.string.APP_URL)+"/auth/register",data,response);
        }
    }
}