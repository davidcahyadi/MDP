package com.codeculator.foodlook.services;

import org.json.JSONObject;

public interface ResponseInterface{
    public void onSuccess(JSONObject res);
    public void onError();
}
