package com.codeculator.foodlook.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReviewDAO {
    @Query("SELECT * FROM review")
    List<Review> getAllReview();

    @Query("SELECT * FROM review WHERE recipe_id = :recipeId")
    List<Review> getAllReviewByID(int recipeId);

    @Insert
    void insert(Review review);

    @Update
    void update(Review review);

    @Delete
    void delete(Review review);

    @Query("DELETE FROM review")
    void deleteAll();
}
