package com.codeculator.foodlook.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.RecipeIngredient;
import com.codeculator.foodlook.services.RetrofitApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddIngredient extends AppCompatActivity {

    EditText ingredient_name, ingredient_amount;
    Button b_add_ingredient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        b_add_ingredient = findViewById(R.id.b_add_ingredient);
        ingredient_name = findViewById(R.id.ingredient_name);
        ingredient_amount = findViewById(R.id.ingredient_amount);

        //setup button
        b_add_ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ingredient_name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "ingredient name is missing", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(ingredient_amount.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "ingredient amount is missing", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //todo masukin ingredient pake retrofit
                        int id = 0;
                        double amount = 1;
                        String title = "title";
                        int recipe_id = 1;
                        int ingredient_id = 1;
                        int measurement_id = 1;
                        RecipeIngredient ri = new RecipeIngredient(id, amount, title, recipe_id, ingredient_id, measurement_id);
                        insertIngredient(ri);
                        finish();
                    }
                }
            }
        });
    }
    private void insertIngredient(RecipeIngredient recipeIngredient){
        Call<RecipeIngredient> call = RetrofitApi.getInstance().getRecipeService().addIngredient(recipeIngredient);
        call.enqueue(new Callback<RecipeIngredient>() {
            @Override
            public void onResponse(Call<RecipeIngredient> call, Response<RecipeIngredient> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Ingredients Successfully Inserted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeIngredient> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Inserting Ingredients Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}