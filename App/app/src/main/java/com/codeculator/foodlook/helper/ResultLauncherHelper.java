package com.codeculator.foodlook.helper;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Objects;

public class ResultLauncherHelper {

    AppCompatActivity app;
    ActivityResultLauncher<Intent> launcher;
    HashMap<Integer,LauncherListener> listeners;

    public ResultLauncherHelper(AppCompatActivity app){
        this.app = app;
        this.listeners = new HashMap<>();
        launcher = app.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                r->{
                    if(listeners.containsKey(r.getResultCode()) && r.getData() != null){
                        Objects.requireNonNull(listeners.get(r.getResultCode())).listen(r.getData());
                    }
                });
    }

    public void addListener(int code,LauncherListener l){
        this.listeners.put(code,l);
    }

    public void launch(Intent i){
        launcher.launch(i);
    }

    public interface LauncherListener{
        void listen(Intent data);
    }
}
