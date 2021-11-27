package com.codeculator.foodlook.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.codeculator.foodlook.model.Step;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateStep{
    private final WeakReference<Context> weakContext;
    private final WeakReference<UpdateStepCallback> weakUpdateStepCallback;
    private Step step;

    UpdateStep(Context context, UpdateStepCallback updateStepCallback, Step step) {
        this.weakContext = new WeakReference<>(context);
        this.weakUpdateStepCallback = new WeakReference<>(updateStepCallback);
        this.step = step;
    }

    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakUpdateStepCallback.get().preUpdateStep();
        executorService.execute(() -> {
            Context context = weakContext.get();
            AppDatabase db = AppDatabase.getAppDatabase(context);
            db.stepDAO().update(step);
            List<Step> listSteps = db.stepDAO().getAllSteps();
            handler.post(() -> weakUpdateStepCallback.get().postUpdateStep(listSteps));
        });
    }

    interface UpdateStepCallback {
        void preUpdateStep();
        void postUpdateStep(List<Step> listStep);
    }
}

