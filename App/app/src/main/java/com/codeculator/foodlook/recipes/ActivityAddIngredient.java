package com.codeculator.foodlook.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.RecipeIngredient;
import com.codeculator.foodlook.services.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddIngredient extends AppCompatActivity {

    EditText ingredient_name, ingredient_amount;
    Button b_add_ingredient;
    Spinner ingredient_spinner, measurement_spinner;
    ArrayList<String> types;
    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    ArrayAdapter<String> ingredientAdapter;
    public static final int CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        b_add_ingredient = findViewById(R.id.b_add_ingredient);
        ingredient_name = findViewById(R.id.ingredient_name);
        ingredient_amount = findViewById(R.id.ingredient_amount);

        ingredient_spinner = findViewById(R.id.ingredient_spinner);
        measurement_spinner = findViewById(R.id.measurement_spinner);


        getTypes();
        ingredientAdapter = new ArrayAdapter<>
                (getApplicationContext(), R.layout.spinner_colored, types);
        ingredientAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        ingredient_spinner.setAdapter(ingredientAdapter);

        ArrayList<String> measurementType = new ArrayList<>();
        measurementType.add("Cups");
        measurementType.add("Kg");
        measurementType.add("g");
        measurementType.add("Ml");
        measurementType.add("L");
        measurementType.add("Teaspoon");
        measurementType.add("Spoon");

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>
                (getApplicationContext(),  R.layout.spinner_colored, measurementType);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        measurement_spinner.setAdapter(spinnerArrayAdapter2);



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
                        int id = 0;
                        double amount = Double.parseDouble(ingredient_amount.getText().toString());
                        String title = ingredient_name.getText().toString();
                        int recipe_id = 0;
                        int ingredient_id = ingredient_spinner.getSelectedItemPosition()+1;
                        int measurement_id = measurement_spinner.getSelectedItemPosition()+1;
                        RecipeIngredient ri = new RecipeIngredient(id, amount, title, recipe_id, ingredient_id, measurement_id);
                        insertIngredient(ri);

                    }
                }
            }
        });
    }

    private void getTypes(){
        types = new ArrayList<>();
        Call<ArrayList<Ingredient>> call = RetrofitApi.getInstance().getRecipeService().getIngredients();
        call.enqueue(new Callback<ArrayList<Ingredient>>() {
            @Override
            public void onResponse(Call<ArrayList<Ingredient>> call, Response<ArrayList<Ingredient>> response) {
                if(response.isSuccessful()){
                    ingredientArrayList = response.body();
                    if(ingredientArrayList != null){
                        for (Ingredient i :
                                ingredientArrayList) {
                            types.add(i.name);
                        }
                        ingredientAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Ingredient>> call, Throwable t) {

            }
        });
    }

    private void insertIngredient(RecipeIngredient recipeIngredient){
        Intent i = new Intent();
        i.putExtra("INGREDIENT",recipeIngredient);
        setResult(CODE,i);
        finish();
    }
}