package com.codeculator.foodlook.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;


public class PrefHelper {

    AppCompatActivity activity;

    public PrefHelper(AppCompatActivity activity){
        this.activity = activity;
    }

    public void setAccess(String access){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("access",access);
        editor.apply();
    }

    public String getAccess(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPref.getString("access","null");
    }

    public void setUser(String user){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("user",user);
        editor.apply();
    }

    public String getUser(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPref.getString("user","null");
    }

    public void setRefresh(String refresh){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("refresh",refresh);
        editor.apply();
    }

    public String getRefresh(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        return sharedPref.getString("refresh","null");
    }

    public void deleteTokens(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.remove("access");
        editor.remove("refresh");
        editor.apply();
    }
}
