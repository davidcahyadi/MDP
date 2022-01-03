package com.codeculator.foodlook.services.response;

public class BookmarkCheckResponse {
    boolean status;

    public BookmarkCheckResponse(boolean status){
        this.status = status;
    }

    public boolean getStatus(){
        return this.status;
    }
}
