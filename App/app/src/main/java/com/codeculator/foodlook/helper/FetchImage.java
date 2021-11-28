package com.codeculator.foodlook.helper;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.codeculator.foodlook.services.HTTPRequest;

public class FetchImage {

    private HTTPRequest httpRequest;

    public FetchImage(HTTPRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public void fetch(String url, ImageView view){
        HTTPRequest.Response<Bitmap> resp = new HTTPRequest.Response<>();
        resp.onSuccess(res -> {
            try{
                view.setImageBitmap(res);
            }
            catch (Exception e){}
        });
        httpRequest.image(url,resp);
    }
}
