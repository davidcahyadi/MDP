package com.codeculator.foodlook.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.codeculator.foodlook.model.Step;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddStep {
    private final WeakReference<Context> weakContext;
    private final WeakReference<StoreCallback> weakStoreCallback;

    public AddStep(Context weakContext, StoreCallback weakStoreCallback) {
        this.weakContext = new WeakReference<>(weakContext);
        this.weakStoreCallback = new WeakReference<>(weakStoreCallback);
    }


    public void execute(List<Step> step) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakStoreCallback.get().preProcess();
        executorService.execute(() -> {
            Context c = weakContext.get();
            for(Step s : step){
                AppDatabase.getAppDatabase(c).stepDAO().insert(s);
            }
            handler.post(() -> weakStoreCallback.get().postProcess());
        });
    }
}
