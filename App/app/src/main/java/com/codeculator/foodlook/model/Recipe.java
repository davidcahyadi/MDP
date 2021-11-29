package com.codeculator.foodlook.model;

import java.util.Date;

public class Recipe {
    public int id;
    public String title;
    public int user_id;
    public float rate;
    public int view;
    public int like;
    public int cook_duration;
    public int prep_duration;
    public int serve_portion;
    public String description;
    public Date created_at;
    public Date updated_at;
    public String photo;

    public Recipe(int id, String title, int user_id, float rate, int view, int like,
                  int cook_duration, int prep_duration, int serve_portion, String description,
                  Date created_at, Date updated_at, String photo) {
        this.id = id;
        this.title = title;
        this.user_id = user_id;
        this.rate = rate;
        this.view = view;
        this.like = like;
        this.cook_duration = cook_duration;
        this.prep_duration = prep_duration;
        this.serve_portion = serve_portion;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.photo = photo;
    }
}