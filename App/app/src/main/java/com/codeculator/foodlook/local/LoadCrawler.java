package com.codeculator.foodlook.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.codeculator.foodlook.model.Crawler;
import com.codeculator.foodlook.model.Step;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadCrawler {
    private final WeakReference<Context> weakContext;
    private final FetchCallback<ArrayList<Crawler>> weakCallback;

    public LoadCrawler(Context context, FetchCallback<ArrayList<Crawler>> weakCallback) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = weakCallback;
    }

    public void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.preProcess();
        executorService.execute(() -> {
            Context c = weakContext.get();
            List<Crawler> crawlers = AppDatabase.getAppDatabase(c).crawlerDAO().get_all();
            ArrayList<Crawler> result = new ArrayList<>();
            result.addAll(crawlers);
            handler.post(() -> weakCallback.postProcess(result));
        });
    }
}
