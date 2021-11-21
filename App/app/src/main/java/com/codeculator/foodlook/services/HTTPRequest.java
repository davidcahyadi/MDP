package com.codeculator.foodlook.services;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeculator.foodlook.R;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    public static int GET = Request.Method.GET;
    public static int POST = Request.Method.POST;
    
    AppCompatActivity activity;
    
    public HTTPRequest(AppCompatActivity activity){
        this.activity = activity;
    }

    public void get(String url, HashMap<String,String> data,Response resp){
        request(GET,url,data,resp);
    }

    public void post(String url, HashMap<String,String> data,Response resp){
        request(POST,url,data,resp);
    }

    public void request(int code, String url, HashMap<String,String> data,Response resp){
        StringRequest stringRequest = new StringRequest(
                code, url,
                response -> {
                    resp.getSuccessResponse().onSuccess(response);

                },
                e -> {
                    resp.getErrorResponse().onError(e);
                }
        ){
            @Override
            protected Map getParams(){
                return data;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this.activity);
        queue.add(stringRequest);
        queue.start();
    }

    public static class Response {
        ResponseError rError;
        ResponseSuccess rSuccess;


        public Response onSuccess(ResponseSuccess response){
            rSuccess = response;
            return this;
        }

        public Response onError(ResponseError response){
            rError =  response;
            return this;
        }

        public ResponseError getErrorResponse(){
            return rError;
        }

        public ResponseSuccess getSuccessResponse(){
            return rSuccess;
        }
    }
}



