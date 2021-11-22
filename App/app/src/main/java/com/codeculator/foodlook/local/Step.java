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
    public int id;

    @ColumnInfo(name = "recipe_id")
    public int recipeId;

    @ColumnInfo(name = "order")
    public int order;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "duration")
    public int duration;

    @ColumnInfo(name = "type_id")
    public int typeId;

    public Step(int id, int order, String title, String url, String description){
        this.id = id;
        this.order = order;
        this.title = title;
        this.url = url;
        this.description = description;
    }
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

    @Query("DELETE FROM steps")
    void deleteAll();
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

