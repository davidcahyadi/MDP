package com.codeculator.foodlook.home;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.IngredientBarAdapter;
import com.codeculator.foodlook.adapter.SummaryStepAdapter;
import com.codeculator.foodlook.databinding.FragmentRecipeDetailBinding;
import com.codeculator.foodlook.helper.FetchImage;
import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.services.HTTPRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRecipeDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecipeDetail extends Fragment {
    FragmentRecipeDetailBinding binding;
    HTTPRequest httpRequest;
    FetchImage fetchImage;
    int recipeID;

    public FragmentRecipeDetail() {
        // Required empty public constructor
    }

    public static FragmentRecipeDetail newInstance() {
        FragmentRecipeDetail fragment = new FragmentRecipeDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("CREATE","Fragment Recipe Detail");
        Bundle bundle = this.getArguments();
        if(bundle != null){
            recipeID = bundle.getInt("ID");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecipeDetailBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.summaryNavigation.setOnItemSelectedListener(this::onNavigationChange);

        httpRequest = new HTTPRequest((AppCompatActivity) getActivity());
        fetchImage = new FetchImage(httpRequest);

        binding.recipeDetailRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        changeToSummary();
        Log.i("CREATED","VIEW");
        loadRecipe();
    }


    private void loadRecipe(){
        HTTPRequest.Response<String> resp = new HTTPRequest.Response<>();
        resp.onSuccess(res -> {
            try{
                JSONObject obj = new JSONObject(res);
                String title = obj.getString("title");
                String description = obj.getString("description");
                binding.title.setText(title);
                binding.description.setText(description);
                fetchImage.fetch(obj.getString("photo"),binding.detailFoodImage);
                binding.cookDuration.setText(obj.getString("cook_duration")+"m");
                binding.prepDuration.setText(obj.getString("prep_duration")+"m");
                binding.servePortion.setText(obj.getString("serve_portion"));
                binding.rating.setText(obj.getString("rate"));
                binding.like.setText(obj.getString("like"));
                binding.view.setText(obj.getString("view"));
            }
            catch (Exception e){}
        });
        httpRequest.get(getString(R.string.APP_URL)+"/recipe/"+recipeID+"/details",new HashMap<>(),resp);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationChange(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.detail_summary:
                changeToSummary();
                return true;
            case R.id.detail_ingredient:
                changeToIngredient();
                return true;
            case R.id.detail_review:
                changeToReview();
                return true;
            case R.id.detail_lets_cook:
                changeToLetsCook();
                return true;
        }
        return false;
    }

    private void changeToSummary(){
        binding.detailLayout.setVisibility(View.VISIBLE);
        binding.detailTitleTv.setText("Summary");

        HTTPRequest.Response<String> stepResponse = new HTTPRequest.Response<>();
        stepResponse.onSuccess(res->{
            try{
                ArrayList<Step> steps = new ArrayList<>();
                JSONArray arr = new JSONArray(res);
                int i = 0;
                while(!arr.isNull(i)){
                    JSONObject obj = arr.getJSONObject(i);

                    Step step = new Step(
                            obj.getInt("id"),
                            obj.getInt("order"),
                            obj.getString("title"),
                            obj.getString("url"),
                            obj.getString("description").substring(0,120)+"..."
                    );
                    steps.add(step);
                    i++;
                }
                SummaryStepAdapter adapter = new SummaryStepAdapter(getActivity(),steps);
                binding.recipeDetailRecycler.setAdapter(adapter);
            }
            catch (Exception e){
                Log.e("ERROR",e.getMessage());
            }
        });

        httpRequest.get(getString(R.string.APP_URL)+"/recipe/"+recipeID+"/summary",new HashMap<>(),
                stepResponse);
    }

    private void changeToIngredient(){
        binding.detailLayout.setVisibility(View.GONE);
        binding.detailTitleTv.setText("Ingredients");

        HTTPRequest.Response<String> resp = new HTTPRequest.Response<>();
        resp.onSuccess(res->{
            try{
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                JSONArray arr = new JSONArray(res);
                int i = 0;
                while(!arr.isNull(i)){
                    JSONObject obj = arr.getJSONObject(i);

                    Ingredient ingredient = new Ingredient(
                            obj.getInt("id"),
                            obj.getDouble("amount"),
                            obj.getString("name"),
                            obj.getInt("recipe_id"),
                            obj.getInt("ingredient_id"),
                            obj.getInt("measurement_id")
                    );
                    ingredients.add(ingredient);
                    i++;
                }

                IngredientBarAdapter adapter = new IngredientBarAdapter(getActivity(),ingredients);
                binding.recipeDetailRecycler.setAdapter(adapter);
            }
            catch (Exception e){}

        });

        httpRequest.get(getString(R.string.APP_URL)+"/recipe/"+recipeID+"/ingredients",new HashMap<>(),
                resp);

    }

    private void changeToReview(){
        binding.detailLayout.setVisibility(View.GONE);
        binding.detailTitleTv.setText("Reviews");
    }

    private void changeToLetsCook(){
        binding.detailTitleTv.setText("Lets Cook");
    }



}