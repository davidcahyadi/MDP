package com.codeculator.foodlook.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    public int id;
    public String comment;

    public Comment(JSONObject obj){
        try {
            this.id = obj.getInt("id");
            this.comment = obj.getString("comment");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Comment(String comment){
        this.comment = comment;
    }
}
