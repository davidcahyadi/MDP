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

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.IngredientAdapter;
import com.codeculator.foodlook.adapter.SummaryStepAdapter;
import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Step;

import java.util.ArrayList;

public class ActivityAddRecipe extends AppCompatActivity {


    EditText add_recipe_title;
    RecyclerView rc_ingredients, rc_steps;
    Button b_add_ingredients, b_add_steps;

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
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        IngredientAdapter ia = new IngredientAdapter(getApplicationContext());
        //todo ambil ingredients dari retrofit

        ia.setIngredients(ingredients);
        rc_ingredients.setAdapter(ia);

        //setup recycler view steps
        rc_steps.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ArrayList<Step> steps = new ArrayList<>();
        //todo ambil steps dari retrofit

        SummaryStepAdapter ssa = new SummaryStepAdapter(getApplicationContext(), steps);
        rc_steps.setAdapter(ssa);
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
            //todo masukkin item ke database

        }
        return super.onOptionsItemSelected(item);
    }
}