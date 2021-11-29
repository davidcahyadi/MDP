package com.codeculator.foodlook.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Query("SELECT * FROM recipe")
    List<Recipe> getAllRecipe();

    @Query("SELECT * FROM recipe WHERE id = :id")
    Recipe getRecipeById(int id);

    @Insert
    void insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("DELETE FROM recipe")
    void deleteAll();
}
