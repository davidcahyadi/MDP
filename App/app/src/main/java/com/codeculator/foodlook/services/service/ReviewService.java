package com.codeculator.foodlook.services.service;

import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.services.response.BasicResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewService {

    @FormUrlEncoded
    @POST("review/add")
    Call<BasicResponse> addReviews(@Field("rate") int rate,
                                   @Field("description") String description,
                                   @Query("recipe") int recipe_id,
                                   @Header("x-api-key") String key);
    @GET("my/reviews")
    Call <ArrayList<Review>> getMyReviews(@Header("x-api-key") String key);
}
