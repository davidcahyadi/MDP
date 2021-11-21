package com.codeculator.foodlook.services;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeculator.foodlook.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    AppCompatActivity activity;
    ResponseInterface response;

    public AuthService(AppCompatActivity activity){
        this.activity = activity;
    }

    public void login(String email, String password){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                this.activity.getResources().getString(R.string.APP_URL)+"/auth/login",
                response -> {
                    try {
                        AuthService.this.response.onSuccess(new JSONObject(response));
                    } catch (Exception e){
                        Log.e("error",e.toString());
                        AuthService.this.response.onError();
                    }
                },
                error -> {
                    AuthService.this.response.onError();
                    Log.e("error",error.toString());
                }
        ){
            @Override
            protected Map getParams(){
                Map params = new HashMap();
                params.put("email",email);
                params.put("password",password);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this.activity);
        queue.add(stringRequest);
        queue.start();
    }

    public void setResponse(ResponseInterface response){
        this.response = response;
    }
}

