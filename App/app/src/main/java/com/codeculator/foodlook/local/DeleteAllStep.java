package com.codeculator.foodlook.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeleteAllStep {
    private final WeakReference<Context> weakContext;
    private final WeakReference<StoreCallback> weakCallback;

    public DeleteAllStep(Context context, StoreCallback weakCallback) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(weakCallback);
    }

    public void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preProcess();
        executorService.execute(() -> {
            Context c = weakContext.get();
            AppDatabase.getAppDatabase(c).stepDAO().deleteAll();
            handler.post(() -> weakCallback.get().postProcess());
        });
    }
}
