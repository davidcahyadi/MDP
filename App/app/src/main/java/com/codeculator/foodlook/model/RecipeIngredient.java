package com.codeculator.foodlook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_ingredients")
public class RecipeIngredient {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "recipe_id")
    public int recipeId;

    @ColumnInfo(name = "ingredient_id")
    public int ingredientId;

    @ColumnInfo(name = "measurement_id")
    public int measurementId;

    public RecipeIngredient(int id, double amount, String name, int recipeId, int ingredientId, int measurementId) {
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.measurementId = measurementId;
    }
}
