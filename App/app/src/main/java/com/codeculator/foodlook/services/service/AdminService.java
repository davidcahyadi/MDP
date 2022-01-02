package com.codeculator.foodlook.services.service;

import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.User;
import com.codeculator.foodlook.services.response.AdminDeleteResponse;
import com.codeculator.foodlook.services.response.BasicResponse;
import com.codeculator.foodlook.services.response.CrawlResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminService {
    @GET("admin/recipes")
    Call<ArrayList<Recipe>> getRecipes(@Query("page")int page);

    @GET("admin/users")
    Call<ArrayList<User>> getUsers(@Query("page")int page);

    @GET("admin/reviews")
    Call<ArrayList<Review>> getReviews(@Query("page")int page);

    @GET("admin/users/{id}")
    Call<User> getUserByID(@Path("id") int id);

    @GET("admin/recipes/{id}")
    Call<Recipe> getRecipeByID(@Path("id") int id);

    @GET("admin/reviews/{id}")
    Call<Review> getReviewByID(@Path("id") int id);

    @POST("admin/delete/user/{id}")
    Call<AdminDeleteResponse> deleteUserById(@Path("id") int id);

    @POST("admin/delete/recipe/{id}")
    Call<AdminDeleteResponse> deleteRecipeById(@Path("id") int id);

    @POST("admin/delete/review/{id}")
    Call<AdminDeleteResponse> deleteReviewById(@Path("id") int id);

    @POST("admin/crawl/{id}")
    Call<CrawlResponse> crawl(@Path("id") int id);
}
