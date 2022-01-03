package com.codeculator.foodlook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codeculator.foodlook.databinding.FragmentAdminRecipeDetailBinding;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.services.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAdminRecipeDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdminRecipeDetail extends Fragment {

    FragmentAdminRecipeDetailBinding binding;
    Recipe selectedRecipe;
    int recipeID;

    public FragmentAdminRecipeDetail() {
        // Required empty public constructor
    }

    public static FragmentAdminRecipeDetail newInstance(String param1, String param2) {
        FragmentAdminRecipeDetail fragment = new FragmentAdminRecipeDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if(b != null)
            recipeID = b.getInt("recipeID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminRecipeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestRecipe(recipeID);
    }

    public void requestRecipe(int id){
        Call<Recipe> call = RetrofitApi.getInstance().getAdminService().getRecipeByID(id);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if(response.isSuccessful()){
                    selectedRecipe = response.body();
                    loadRecipe();
                    requestSteps();
                    requestReviews();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong. Please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadRecipe(){
        binding.detailRecipeIDTV.setText("#" + selectedRecipe.id);
        binding.detailRecipeCreatedTV.setText(selectedRecipe.created_at);
        binding.detailRecipeUpdatedTV.setText(selectedRecipe.updated_at);
        binding.detailRecipeDescriptionTV.setText(selectedRecipe.description);
        binding.detailRecipeLikeTV.setText(selectedRecipe.like + "");
        binding.detailRecipePrepTV.setText(selectedRecipe.prep_duration + "");
        binding.detailRecipeRatingTV.setText(selectedRecipe.rate + "");
        binding.detailRecipeServeTV.setText(selectedRecipe.serve_portion + "");
        binding.detailRecipeViewTV.setText(selectedRecipe.view + "");
    }

    public void requestSteps(){
        Call<ArrayList<Step>> call = RetrofitApi.getInstance().getRecipeService().getRecipeSummary(recipeID);
        call.enqueue(new Callback<ArrayList<Step>>() {
            @Override
            public void onResponse(Call<ArrayList<Step>> call, Response<ArrayList<Step>> response) {
                if(response.isSuccessful()){
                    binding.totalStepsTV.setText(response.body().size() + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Step>> call, Throwable t) {

            }
        });
    }

    public void requestReviews(){
        Call<ArrayList<Review>> call = RetrofitApi.getInstance().getRecipeService().getRecipeReviews(recipeID);
        call.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                if(response.isSuccessful()){
                    binding.totalRecipesReviewsTV.setText(response.body().size() + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Review>> call, Throwable t) {

            }
        });
    }
}