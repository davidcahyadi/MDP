package com.codeculator.foodlook.services.service;

import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Step;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeService {
    @GET("recipe/{id}/details")
    Call<Recipe> getRecipeDetail(@Path("id") int id);

    @GET("recipe/{id}/summary")
    Call<ArrayList<Step>> getRecipeSummary(@Path("id") int id);

    @GET("recipe/{id}/ingredients")
    Call<ArrayList<Ingredient>> getRecipeIngredients(@Path("id") int id);
}
