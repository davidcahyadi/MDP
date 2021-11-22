package com.codeculator.foodlook.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadStep {
    private final WeakReference<Context> weakContext;
    private final WeakReference<FetchCallback<ArrayList<Step>>> weakCallback;

    public LoadStep(Context context, FetchCallback<ArrayList<Step>> weakCallback) {
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(weakCallback);
    }

    public void execute() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preProcess();
        executorService.execute(() -> {
            Context c = weakContext.get();
            List<Step> steps = AppDatabase.getAppDatabase(c).stepDAO().getAllSteps();
            ArrayList<Step> result = new ArrayList<>();
            result.addAll(steps);
            handler.post(() -> weakCallback.get().postProcess(result));
        });
    }
}
