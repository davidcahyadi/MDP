package com.codeculator.foodlook.recipes;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.IngredientAdapter;
import com.codeculator.foodlook.adapter.IngredientBarAdapter;
import com.codeculator.foodlook.adapter.SummaryStepAdapter;
import com.codeculator.foodlook.firebase.Upload;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.home.ActivityTemp;
import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.RecipeIngredient;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.model.User;
import com.codeculator.foodlook.services.FirebaseUpload;
import com.codeculator.foodlook.services.RetrofitApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddRecipe extends AppCompatActivity {


    EditText add_recipe_title, cook_duration, prep_duration, serve_portion, recipe_description;
    RecyclerView rc_ingredients, rc_steps;
    Button b_add_ingredients, b_add_steps, btn_upload_image;

    ArrayList<RecipeIngredient> recipeIngredients;
    ArrayList<Step> steps;

    final int RECIPE_ID = 1;

    FirebaseUpload<Upload> firebaseUpload;

    String photos = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        add_recipe_title = findViewById(R.id.add_recipe_title);
        rc_ingredients = findViewById(R.id.rc_ingredients);
        rc_steps = findViewById(R.id.rc_steps);
        b_add_ingredients = findViewById(R.id.b_add_ingredients);
        b_add_steps = findViewById(R.id.b_add_steps);
        btn_upload_image = findViewById(R.id.btn_upload_image);

        cook_duration = findViewById(R.id.cook_duration);
        prep_duration = findViewById(R.id.prep_duration);
        serve_portion = findViewById(R.id.serve_portion);
        recipe_description = findViewById(R.id.recipe_description);


        //onclick ingredients
        b_add_ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityAddRecipe.this, ActivityAddIngredient.class);
                startActivity(i);
            }
        });

        //onclick steps
        b_add_steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityAddRecipe.this, ActivityAddStep.class);
                startActivity(i);
            }
        });

        //setup recycler view ingredients
        rc_ingredients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recipeIngredients = new ArrayList<>();
        getIngredients();
        IngredientBarAdapter ia = new IngredientBarAdapter(getApplicationContext(), recipeIngredients);
        rc_ingredients.setAdapter(ia);

        //setup recycler view steps
        rc_steps.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        steps = new ArrayList<>();
        getSteps();

        SummaryStepAdapter ssa = new SummaryStepAdapter(getApplicationContext(), steps);
        rc_steps.setAdapter(ssa);

        firebaseUpload = new FirebaseUpload<Upload>(this, this,"uploads/recipes") {
            @Override
            public void onSuccessUpload(Uri uri) {
                Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();

                // class untuk firebase database
                Upload upload = new Upload("photo", uri.toString());
                photos = uri.toString();
                firebaseUpload.setObj(upload);
            }

            @Override
            public void onSuccessChooseImage(ActivityResult result) {
                //nothing
                Intent data = result.getData();
                if(data != null){
                    Uri imageUri = data.getData();
                    firebaseUpload.setImageUri(imageUri);
                }
            }
        };

        //btn upload image click
        btn_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUpload.openFileChooserDialog(view);
            }
        });
    }

    public void getIngredients(){
        Call<ArrayList<RecipeIngredient>> call = RetrofitApi.getInstance().getRecipeService().getRecipeIngredients(RECIPE_ID);
        call.enqueue(new Callback<ArrayList<RecipeIngredient>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeIngredient>> call, Response<ArrayList<RecipeIngredient>> response) {
                if(response.isSuccessful()){
                    recipeIngredients = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeIngredient>> call, Throwable t) {

            }
        });

    }

    public void getSteps(){
        Call<ArrayList<Step>> call = RetrofitApi.getInstance().getRecipeService().getRecipeStep(RECIPE_ID);
        call.enqueue(new Callback<ArrayList<Step>>() {
            @Override
            public void onResponse(Call<ArrayList<Step>> call, Response<ArrayList<Step>> response) {
                if(response.isSuccessful()){
                    steps = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Step>> call, Throwable t) {

            }
        });
    }

    //setup button save ontop
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_save){
            String title = add_recipe_title.getText().toString();
            int cook_dur = Integer.parseInt(cook_duration.getText().toString());
            int prep_dur = Integer.parseInt(prep_duration.getText().toString());
            int serve_port = Integer.parseInt(serve_portion.getText().toString());
            String descr = recipe_description.getText().toString();
            Recipe r = new Recipe(title,-1,0,0,0,cook_dur,prep_dur,serve_port,descr, new Date().toString(),new Date().toString(),photos,"-");
            addRecipe(r);

        }
        return super.onOptionsItemSelected(item);
    }

    public void addRecipe(Recipe recipe){
        PrefHelper prefHelper = new PrefHelper(this);
        Call<Recipe> call = RetrofitApi.getInstance().getRecipeService().addRecipe(recipe, prefHelper.getAccess());
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Recipe Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Recipe Addition Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}