package com.codeculator.foodlook.model;

public class WelcomeCard {
    private String title;
    private int resource;


    private int code;

    public WelcomeCard(String title, int resource, int code){
        this.title = title;
        this.resource = resource;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
