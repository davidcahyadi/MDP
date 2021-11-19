package com.codeculator.foodlook.local;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Entity(tableName = "steps")
public class Step {
    @PrimaryKey
    @ColumnInfo(name = "id")
    int id;

    @ColumnInfo(name = "recipe_id")
    int recipeId;

    @ColumnInfo(name = "order")
    int order;

    @ColumnInfo(name = "title")
    String title;

    @ColumnInfo(name = "description")
    String description;

    @ColumnInfo(name = "url")
    String url;

    @ColumnInfo(name = "duration")
    int duration;

    @ColumnInfo(name = "type_id")
    int typeId;
}

@Dao
interface StepDAO {
    @Query("SELECT * FROM steps")
    List<Step> getAllSteps();

    @Query("SELECT * FROM steps WHERE recipe_id = :recipeId ORDER BY `order` ASC")
    List<Step> getStepsByRecipe(int recipeId);

    @Insert
    void insert(Step step);

    @Update
    void update(Step step);

    @Delete
    void delete(Step step);
}

class AddStep{
    private final WeakReference<Context> weakContext;
    private final WeakReference<AddStepCallback> weakAddStepCallback;
    private Step step;

    public AddStep(Context weakContext, AddStepCallback weakAddStepCallback,
                          Step step){
        this.weakContext = new WeakReference<>(weakContext);
        this.weakAddStepCallback = new WeakReference<>(weakAddStepCallback);
        this.step = step;
    }


    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakAddStepCallback.get().preAddStep();
        executorService.execute(()->{
            Context c = weakContext.get();
            AppDatabase.getAppDatabase(c).stepDAO().insert(step);
            handler.post(()->weakAddStepCallback.get().postAddStep());
        });
    }

    interface AddStepCallback{
        void preAddStep();
        void postAddStep();
    }
}

class LoadStep{
    private final WeakReference<Context> weakContext;
    private final WeakReference<LoadStepCallback> weakCallback;

    public LoadStep(Context context, LoadStepCallback weakCallback){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(weakCallback);
    }
    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preLoadStep();
        executorService.execute(()->{
            Context c = weakContext.get();
            List<Step> result = AppDatabase.getAppDatabase(c).stepDAO().getAllSteps();
            handler.post(()->weakCallback.get().postLoadStep(result));
        });
    }
    interface LoadStepCallback{
        void preLoadStep();
        void postLoadStep(List<Step> listStep);
    }
}

class UpdateStep{
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

class DeleteStep{
    private final WeakReference<Context> weakContext;
    private final WeakReference<DeleteStepCallback> weakCallback;
    private Step step;

    public DeleteStep(Context context, DeleteStepCallback weakCallback, Step step){
        this.weakContext = new WeakReference<>(context);
        this.weakCallback = new WeakReference<>(weakCallback);
        this.step = step;
    }
    void execute(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        weakCallback.get().preDeleteStep();
        executorService.execute(()->{
            Context c = weakContext.get();
            AppDatabase.getAppDatabase(c).stepDAO().delete(step);
            handler.post(()->weakCallback.get().posteDeleteStep());
        });
    }
    interface DeleteStepCallback{
        void preDeleteStep();
        void posteDeleteStep();
    }
}
