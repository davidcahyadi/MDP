package com.codeculator.foodlook.services.service;

import com.codeculator.foodlook.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CatalogService {
    @GET("catalog/{type}/{page}")
    Call<ArrayList<Recipe>> getCatalogs(@Path("type") String type, @Path("page") int page);

    @FormUrlEncoded
    @POST("catalog/recommendation")
    Call<ArrayList<Recipe>> getRecommendations(@Field("id[]") List<Integer> ids);
}
