package com.codeculator.foodlook.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "steps")
public class Step {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "recipe_id")
    public int recipeId;

    @ColumnInfo(name = "order")
    public int order;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "duration")
    public int duration;

    @ColumnInfo(name = "type_id")
    public int typeId;

    public Step(int id, int order, String title, String url, String description, int duration){
        this.id = id;
        this.order = order;
        this.title = title;
        if(url.equals("0")) this.url = "";
        else this.url = url;
        this.description = description;
        this.duration = duration;
    }
}

