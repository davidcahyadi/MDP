package com.codeculator.foodlook.services.admin;

import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AdminService {
    @GET("admin/recipes")
    Call<ArrayList<Recipe>> getRecipes();

    @GET("admin/users")
    Call<ArrayList<User>> getUsers();

    @GET("admin/reviews")
    Call<ArrayList<Review>> getReviews();
}
