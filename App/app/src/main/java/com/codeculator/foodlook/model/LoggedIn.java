package com.codeculator.foodlook.model;

public class LoggedIn {
    public static User user;

    public static User convertToUser(String str){
        String[] user = str.split("\\|");
        return new User(Integer.parseInt(user[0]), user[1], user[2], user[3], user[4], user[5]);
    }
}
