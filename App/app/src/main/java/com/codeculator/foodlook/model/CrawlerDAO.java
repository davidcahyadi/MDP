package com.codeculator.foodlook.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CrawlerDAO {
    @Insert
    void insert(Crawler crawler);

    @Update
    void update(Crawler crawler);

    @Query("Select * from crawlers where id == :id")
    Crawler get(int id);

    @Query("Select * from crawlers")
    List<Crawler> get_all();
}
