package com.codeculator.foodlook.services.response;

public class CrawlResponse {
    String message;
    int newRecipes;

    public CrawlResponse(String message,int newRecipes){
        this.message = message;
        this.newRecipes = newRecipes;
    }

    public String getMessage(){
        return this.message;
    }

    public int getNewRecipes(){
        return this.newRecipes;
    }
}
