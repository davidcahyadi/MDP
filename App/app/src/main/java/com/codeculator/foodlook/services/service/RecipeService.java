package com.codeculator.foodlook.services.service;

import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.RecipeIngredient;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.services.response.BasicResponse;
import com.codeculator.foodlook.services.response.BookmarkCheckResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeService {
    @GET("recipe/{id}/details")
    Call<Recipe> getRecipeDetail(@Path("id") int id);

    @GET("recipe/{id}/summary")
    Call<ArrayList<Step>> getRecipeSummary(@Path("id") int id);

    @GET("recipe/{id}/ingredients")
    Call<ArrayList<RecipeIngredient>> getRecipeIngredients(@Path("id") int id);

    @GET("recipe/{id}/reviews")
    Call<ArrayList<Review>> getRecipeReviews(@Path("id") int id);

    @POST("recipe/add")
    Call<Recipe> addRecipe(Recipe recipe);

    @POST("recipe/add/step")
    Call<Step> addStep(Step step);

    @POST("recipe/add/ingredient")
    Call<RecipeIngredient> addIngredient(RecipeIngredient ingredient);

    @GET("recipe/{id}/view")
    Call<BasicResponse> addRecipeView(@Path("id") int id);

    @GET("my/bookmark/check")
    Call<BookmarkCheckResponse> checkBookmark(@Query("recipe_id") int recipe_id, @Header("x-api-key") String token);

    @POST("my/bookmark/add")
    Call<BasicResponse> addBookmark(@Query("recipe_id") int recipe_id, @Header("x-api-key") String token);

    @POST("my/bookmark/remove")
    Call<BasicResponse> removeBookmark(@Query("recipe_id") int recipe_id, @Header("x-api-key") String token);
}
