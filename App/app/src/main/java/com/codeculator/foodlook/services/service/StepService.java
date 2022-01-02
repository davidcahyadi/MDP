package com.codeculator.foodlook.services.service;

import com.codeculator.foodlook.model.Step;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface StepService {

    @FormUrlEncoded
    @POST("steps/add")
    Call<Step> addStep();
}
