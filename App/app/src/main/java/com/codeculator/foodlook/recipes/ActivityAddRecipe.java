package com.codeculator.foodlook.recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.RecipeIngredient;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.services.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddRecipe extends AppCompatActivity {


    EditText add_recipe_title;
    RecyclerView rc_ingredients, rc_steps;
    Button b_add_ingredients, b_add_steps;

    ArrayList<RecipeIngredient> recipeIngredients;
    ArrayList<Step> steps;

    final int RECIPE_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        add_recipe_title = findViewById(R.id.add_recipe_title);
        rc_ingredients = findViewById(R.id.rc_ingredients);
        rc_steps = findViewById(R.id.rc_steps);
        b_add_ingredients = findViewById(R.id.b_add_ingredients);
        b_add_steps = findViewById(R.id.b_add_steps);


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
            Recipe r = new Recipe("title",1,0,0,0,0,0,0,"description","created at","updated at","photo","from");
            addRecipe(r);

        }
        return super.onOptionsItemSelected(item);
    }

    public void addRecipe(Recipe recipe){
        Call<Recipe> call = RetrofitApi.getInstance().getRecipeService().addRecipe(recipe);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Recipe Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {

            }
        });
    }
}