package com.codeculator.foodlook.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient {

    public int id;
    public String name;
    public String icon_url;
    public int type_id;
    public boolean check_status;

    public Ingredient(JSONObject obj) {
        try {
            this.id = obj.getInt("id");
            this.name = obj.getString("name");
            this.icon_url = obj.getString("icon_url");
//        this.type_id = obj.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.check_status = false;
    }

    public void changeStatus() {
        this.check_status = !this.check_status;
    }
}
