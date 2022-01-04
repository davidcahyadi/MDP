package com.codeculator.foodlook.services.service;

import com.codeculator.foodlook.model.Comment;
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

public interface CommentService {

    @FormUrlEncoded
    @POST("review/replies/add")
    Call<BasicResponse> addComment(@Field("review_id") int review_id, @Field("description") String c, @Header("x-api-key") String key);

    @GET("review/{id_review}/replies")
    Call<ArrayList<Review>> getAllComment(@Path("id_review") int id_review);
}
