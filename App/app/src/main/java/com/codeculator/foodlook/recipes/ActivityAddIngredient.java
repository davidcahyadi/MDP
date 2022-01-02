package com.codeculator.foodlook.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codeculator.foodlook.R;

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
                    }
                }
            }
        });
    }
}