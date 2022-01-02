package com.codeculator.foodlook.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.codeculator.foodlook.model.Crawler;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddCrawler {
    private final WeakReference<Context> weakContext;
    private final WeakReference<StoreCallback> weakUpdateStepCallback;

    public AddCrawler(Context context, StoreCallback updateStepCallback) {
        this.weakContext = new WeakReference<>(context);
        this.weakUpdateStepCallback = new WeakReference<>(updateStepCallback);
    }

    public void execute(Crawler crawler){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase db = AppDatabase.getAppDatabase(context);
            db.crawlerDAO().insert(crawler);
            handler.post(() -> weakUpdateStepCallback.get().postProcess());
        });
    }
}
