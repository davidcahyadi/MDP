package com.codeculator.foodlook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "crawlers")
public class Crawler {

    @PrimaryKey
    @ColumnInfo(name = "id")
    int id;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "updated_at")
    String updated_at;

    public Crawler(int id, String name){
        this.id = id;
        this.name = name;
        this.updated_at = "";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at){
        this.updated_at = updated_at;
    }
}
