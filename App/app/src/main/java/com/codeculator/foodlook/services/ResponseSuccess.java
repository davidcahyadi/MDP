package com.codeculator.foodlook.services;

import org.json.JSONObject;

public interface ResponseSuccess<T> {
    public void onSuccess(T res);
}
