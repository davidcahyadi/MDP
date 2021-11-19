//package com.codeculator.foodlook.local;
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

//@Database(entities={},version=1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "GameDB").build();
        }
        return INSTANCE;
    }
}
