package com.codeculator.foodlook.services.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewInterface {
    @FormUrlEncoded
    @POST("review/add")
    Call<String> addReviews(@Field("rate") int rate,
                            @Field("description") String description,
                            @Query("recipe") int recipe_id);

}
