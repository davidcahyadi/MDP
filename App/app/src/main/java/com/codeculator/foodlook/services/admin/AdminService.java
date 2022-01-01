package com.codeculator.foodlook.services.admin;

import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AdminService {
    @GET("admin/recipes")
    Call<ArrayList<Recipe>> getRecipes();

    @GET("admin/users")
    Call<ArrayList<User>> getUsers();

    @GET("admin/reviews")
    Call<ArrayList<Review>> getReviews();

    @POST("admin/delete/user/{id}")
    Call<AdminDeleteResponse> deleteUserById(@Path("id") int id);

    @POST("admin/delete/recipe/{id}")
    Call<AdminDeleteResponse> deleteRecipeById(@Path("id") int id);

    @FormUrlEncoded
    @POST("admin/delete/review/{id}")
    Call<AdminDeleteResponse> deleteReviewById(@Body int id);
}
