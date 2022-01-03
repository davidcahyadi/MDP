package com.codeculator.foodlook.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe_ingredients")
public class RecipeIngredient implements Parcelable {
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

    protected RecipeIngredient(Parcel in) {
        id = in.readInt();
        amount = in.readDouble();
        name = in.readString();
        recipeId = in.readInt();
        ingredientId = in.readInt();
        measurementId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(amount);
        dest.writeString(name);
        dest.writeInt(recipeId);
        dest.writeInt(ingredientId);
        dest.writeInt(measurementId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
        @Override
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };
}
