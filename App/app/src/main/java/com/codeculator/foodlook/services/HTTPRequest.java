package com.codeculator.foodlook.services;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {
    public static int GET = Request.Method.GET;
    public static int POST = Request.Method.POST;
    
    AppCompatActivity activity;
    
    public HTTPRequest(AppCompatActivity activity){
        this.activity = activity;
    }

    public void get(String url, HashMap<String,String> data,Response<String> resp){
        request(GET,url,data,resp);
    }

    public void post(String url, HashMap<String,String> data,Response<String> resp){
        request(POST,url,data,resp);
    }

    public void request(int code, String url, HashMap<String,String> data,Response<String> resp){
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

    public void image(String url,Response<Bitmap> resp){
        ImageRequest imageRequest = new ImageRequest(url,
                response -> {
                    resp.getSuccessResponse().onSuccess(response);
                },
                0, 0, ImageView.ScaleType.FIT_XY, null,
                error -> {
                    resp.getErrorResponse().onError(error);
                });
        RequestQueue queue = Volley.newRequestQueue(this.activity);
        queue.add(imageRequest);
        queue.start();
    }

    public static class Response<T>{
        ResponseError rError;
        ResponseSuccess<T> rSuccess;


        public void onSuccess(ResponseSuccess<T> response){
            rSuccess = response;
        }

        public void onError(ResponseError response){
            rError =  response;
        }

        public ResponseError getErrorResponse(){
            return rError;
        }

        public ResponseSuccess<T> getSuccessResponse(){
            return rSuccess;
        }
    }
}



