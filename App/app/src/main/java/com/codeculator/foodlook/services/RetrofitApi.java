package com.codeculator.foodlook.services;

import com.codeculator.foodlook.services.service.AdminService;
import com.codeculator.foodlook.services.service.CatalogService;
import com.codeculator.foodlook.services.service.CommentService;
import com.codeculator.foodlook.services.service.RecipeService;
import com.codeculator.foodlook.services.service.ReviewService;
import com.codeculator.foodlook.services.service.StepService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {
    private static volatile RetrofitApi instance = null;

    public static String BASE_URL;

    // interfaces
    private RecipeService recipeService;
    private AdminService adminService;
    private CatalogService catalogService;
    private ReviewService reviewService;
    private CommentService commentService;
    private StepService stepService;

    public static RetrofitApi getInstance() {
        if (instance == null) {
            instance = new RetrofitApi();
            synchronized (RetrofitApi.class) {
                if (instance == null) {
                    instance = new RetrofitApi();
                }
            }
        }
        return instance;
    }

    // Build retrofit once when creating a single instance
    private RetrofitApi() {
        // Implement a method to build your retrofit
        buildRetrofit();
    }

    private void buildRetrofit() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        // Build your services once
        this.recipeService = retrofit.create(RecipeService.class);
        this.adminService = retrofit.create(AdminService.class);
        this.catalogService = retrofit.create(CatalogService.class);
        this.reviewService = retrofit.create(ReviewService.class);
        this.commentService = retrofit.create(CommentService.class);
    }

    public RecipeService getRecipeService() {
        return recipeService;
    }

    public AdminService getAdminService(){
        return adminService;
    }

    public CatalogService getCatalogService() {
        return catalogService;
    }

    public ReviewService getReviewInterface(){
        return reviewService;
    }

    public CommentService getCommentService(){
        return commentService;
    }
}
