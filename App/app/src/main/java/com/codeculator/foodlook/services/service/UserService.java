package com.codeculator.foodlook.services.service;

import com.codeculator.foodlook.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {

    @GET("my/profile")
    Call<User> getUserBiodata(@Header("x-api-key") String key);

}
