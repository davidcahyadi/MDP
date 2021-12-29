package com.codeculator.foodlook.model;

public class Ingredient {

    public int id;
    public String name;
    public String icon_url;
    public int type_id;

    public Ingredient(int id, String name, String icon_url, int type_id) {
        this.id = id;
        this.name = name;
        this.icon_url = icon_url;
        this.type_id = type_id;
    }
}
