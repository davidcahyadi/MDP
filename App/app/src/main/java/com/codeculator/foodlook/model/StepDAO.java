package com.codeculator.foodlook.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StepDAO {
    @Query("SELECT * FROM steps")
    List<Step> getAllSteps();

    @Query("SELECT * FROM steps WHERE recipe_id = :recipeId ORDER BY `order` ASC")
    List<Step> getStepsByRecipe(int recipeId);

    @Insert
    void insert(Step step);

    @Update
    void update(Step step);

    @Delete
    void delete(Step step);

    @Query("DELETE FROM steps")
    void deleteAll();
}
