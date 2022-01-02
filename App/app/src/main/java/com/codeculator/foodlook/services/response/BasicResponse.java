package com.codeculator.foodlook.services.response;

public class BasicResponse {
    String message;

    public BasicResponse(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
