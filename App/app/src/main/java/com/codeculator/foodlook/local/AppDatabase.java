package com.codeculator.foodlook.local;//package com.codeculator.foodlook.local;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//@Database(entities={},version=1)
//public abstract class AppDatabase extends RoomDatabase {
//    private static AppDatabase INSTANCE;
//
//    public static AppDatabase getAppDatabase(Context context) {
//        if (INSTANCE==null) {
//            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "GameDB").build();
//        }
//        return INSTANCE;
//    }
//}

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.RecipeDAO;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.model.StepDAO;

@Database(entities={Step.class, Recipe.class},version=1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract StepDAO stepDAO();
    public abstract RecipeDAO recipeDAO();

    public static AppDatabase getAppDatabase(Context context){
        if (INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "FoodlookDB").build();
        }
        return INSTANCE;
    }

}
