package com.codeculator.foodlook.firebase;

public class UploadUserImage {
    public int userId;
    public String imageUrl;
    public String key;

    public UploadUserImage(){}

    public UploadUserImage(int userId, String imageUrl) {
        this.userId = userId;
        this.imageUrl = imageUrl;
    }
}
