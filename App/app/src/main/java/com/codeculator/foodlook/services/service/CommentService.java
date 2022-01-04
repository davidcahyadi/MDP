package com.codeculator.foodlook.services.service;

import com.codeculator.foodlook.model.Comment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentService {

    @FormUrlEncoded
    @POST("recipe/{id_recipe}/replies/add")
    Call<String> addComment(@Path("id_recipe") int id_recipe,@Field("description") String c,@Header("x-api-key") String key);

    @GET("recipe/{id_recipe}/replies")
    Call<ArrayList<Comment>> getAllComment(@Path("id_recipe") int id_recipe);
}
