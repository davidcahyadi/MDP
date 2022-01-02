package com.codeculator.foodlook.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
    public int id;
    public String comment;
    public int user_id;

    public Comment(JSONObject obj){
        try {
            this.id = obj.getInt("id");
            this.comment = obj.getString("comment");
            this.user_id = obj.getInt("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Comment(String comment, int user_id){
        this.comment = comment;
        this.user_id = user_id;
    }
}
