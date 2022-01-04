package com.codeculator.foodlook.recipes;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.IngredientAdapter;
import com.codeculator.foodlook.adapter.IngredientBarAdapter;
import com.codeculator.foodlook.adapter.SummaryStepAdapter;
import com.codeculator.foodlook.firebase.Upload;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.helper.ResultLauncherHelper;
import com.codeculator.foodlook.home.ActivityTemp;
import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.RecipeIngredient;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.model.User;
import com.codeculator.foodlook.services.FirebaseUpload;
import com.codeculator.foodlook.services.RetrofitApi;
import com.codeculator.foodlook.services.response.BasicResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddRecipe extends AppCompatActivity {


    EditText add_recipe_title, cook_duration, prep_duration, serve_portion, recipe_description;
    RecyclerView rc_ingredients, rc_steps;
    Button b_add_ingredients, b_add_steps, btn_upload_image, btn_choose_image;
    Button btnSave;
    ImageView iv;
    IngredientBarAdapter ia;
    SummaryStepAdapter ssa;

    FirebaseUpload<Upload> firebaseUpload;

    String photos = "";

    ResultLauncherHelper launcher;

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        add_recipe_title = findViewById(R.id.add_recipe_title);
        rc_ingredients = findViewById(R.id.rc_ingredients);
        rc_steps = findViewById(R.id.rc_steps);
        b_add_ingredients = findViewById(R.id.b_add_ingredients);
        b_add_steps = findViewById(R.id.b_add_steps);
//        btn_upload_image = findViewById(R.id.btn_upload_image);

        cook_duration = findViewById(R.id.cook_duration);
        prep_duration = findViewById(R.id.prep_duration);
        serve_portion = findViewById(R.id.serve_portion);
        recipe_description = findViewById(R.id.recipe_description);
        btn_choose_image = findViewById(R.id.btn_choose_image);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        iv = findViewById(R.id.preview);
        iv.setVisibility(View.GONE);

        launcher = new ResultLauncherHelper(this);
        launcher.addListener(ActivityAddStep.CODE,l ->{
            Step step =  l.getParcelableExtra("STEP");
            recipe.steps.add(step);
            Toast.makeText(ActivityAddRecipe.this, "Success add step !", Toast.LENGTH_SHORT).show();
            ssa.notifyDataSetChanged();
        });
        launcher.addListener(ActivityAddIngredient.CODE,l ->{
            RecipeIngredient ing =  l.getParcelableExtra("INGREDIENT");
            recipe.ingredients.add(ing);
            Toast.makeText(ActivityAddRecipe.this, "Success add ingredient !", Toast.LENGTH_SHORT).show();
            ia.notifyDataSetChanged();
        });



        //onclick ingredients
        b_add_ingredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityAddRecipe.this, ActivityAddIngredient.class);
                launcher.launch(i);
            }
        });

        //onclick steps
        b_add_steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityAddRecipe.this, ActivityAddStep.class);
                launcher.launch(i);
            }
        });

        // create new recipe
        recipe = new Recipe();

        //setup recycler view ingredients
        rc_ingredients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ia = new IngredientBarAdapter(getApplicationContext(), recipe.ingredients);
        rc_ingredients.setAdapter(ia);

        //setup recycler view steps
        rc_steps.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ssa = new SummaryStepAdapter(getApplicationContext(), recipe.steps);
        rc_steps.setAdapter(ssa);

        firebaseUpload = new FirebaseUpload<Upload>(this, this,"uploads/recipes") {
            @Override
            public void onSuccessUpload(Uri uri) {
                Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();

                // class untuk firebase database
                Upload upload = new Upload("photo", uri.toString());
                photos = uri.toString();
                recipe.photo = photos;
                firebaseUpload.setObj(upload);
                addRecipe(recipe);

            }

            @Override
            public void onSuccessChooseImage(ActivityResult result) {
                //nothing
                Intent data = result.getData();
                if(data != null){
                    Uri imageUri = data.getData();
                    firebaseUpload.setImageUri(imageUri);


                    try {
                        iv.setImageBitmap(MediaStore.Images.Media.getBitmap(ActivityAddRecipe.this.getContentResolver(), firebaseUpload.getImageUri()));
                        iv.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    };
                }
            }
        };

        //btn upload image click
//        btn_upload_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseUpload.save();
//            }
//        });

        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUpload.openFileChooserDialog(view);

            }
        });

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipe.title = add_recipe_title.getText().toString();
                recipe.cook_duration= Integer.parseInt(cook_duration.getText().toString());
                recipe.prep_duration = Integer.parseInt(prep_duration.getText().toString());
                recipe.serve_portion = Integer.parseInt(serve_portion.getText().toString());
                recipe.description = recipe_description.getText().toString();
                if(iv.getVisibility() == View.VISIBLE){
                    firebaseUpload.save();
                }
                else{
                    addRecipe(recipe);
                }


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
            recipe.title = add_recipe_title.getText().toString();
            recipe.cook_duration= Integer.parseInt(cook_duration.getText().toString());
            recipe.prep_duration = Integer.parseInt(prep_duration.getText().toString());
            recipe.serve_portion = Integer.parseInt(serve_portion.getText().toString());
            recipe.description = recipe_description.getText().toString();

            addRecipe(recipe);

        }
        return super.onOptionsItemSelected(item);
    }

    public void addRecipe(Recipe recipe){
        PrefHelper prefHelper = new PrefHelper(this);
        Call<BasicResponse> call = RetrofitApi.getInstance().getRecipeService().saveRecipe(recipe, prefHelper.getAccess());
        System.out.println("CALL API");
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Recipe Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                System.out.println(response.raw());;
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Recipe Addition Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}