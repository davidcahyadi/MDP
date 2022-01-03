package com.codeculator.foodlook.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.IngredientBarAdapter;
import com.codeculator.foodlook.adapter.ReviewAdapter;
import com.codeculator.foodlook.adapter.SummaryStepAdapter;
import com.codeculator.foodlook.databinding.FragmentRecipeDetailBinding;
import com.codeculator.foodlook.helper.FetchImage;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.model.RecipeIngredient;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.services.HTTPRequest;
import com.codeculator.foodlook.services.RetrofitApi;
import com.codeculator.foodlook.services.response.BasicResponse;
import com.codeculator.foodlook.services.response.BookmarkCheckResponse;
import com.codeculator.foodlook.steps.ActivityStep;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRecipeDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecipeDetail extends Fragment {
    FragmentRecipeDetailBinding binding;
    int recipeID;
    PrefHelper prefHelper;

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

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Fragment fragment = new FragmentCatalog();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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

        binding.recipeDetailRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        changeToSummary();
        Log.i("CREATED","VIEW");

        prefHelper = new PrefHelper((AppCompatActivity) getActivity());

        Call<BasicResponse> call = RetrofitApi.getInstance().getRecipeService().addRecipeView(recipeID);
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                loadRecipe();
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });

        Call<BookmarkCheckResponse> bookmark = RetrofitApi.getInstance()
                .getRecipeService().checkBookmark(recipeID,prefHelper.getAccess());

        bookmark.enqueue(new Callback<BookmarkCheckResponse>() {
            @Override
            public void onResponse(Call<BookmarkCheckResponse> call, Response<BookmarkCheckResponse> response) {
                System.out.println(response.raw());
                if(response.isSuccessful()){
                    assert response.body() != null;
                    System.out.println(response.body().getStatus());
                    if(response.body().getStatus()){
                        binding.btnBookmark.setText("Bookmarked");
                    }
                }
            }

            @Override
            public void onFailure(Call<BookmarkCheckResponse> call, Throwable t) {

            }
        });

        binding.btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmark();
            }
        });
    }

    private void bookmark(){
        if(binding.btnBookmark.getText().toString().toLowerCase().equals("bookmark")){
            Call<BasicResponse> call = RetrofitApi.getInstance().getRecipeService().addBookmark(recipeID,prefHelper.getAccess());
            call.enqueue(new Callback<BasicResponse>() {
                @Override
                public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                    if(response.isSuccessful()){
                        binding.btnBookmark.setText("Bookmarked");
                    }
                }

                @Override
                public void onFailure(Call<BasicResponse> call, Throwable t) {

                }
            });
        }
        else{
            Call<BasicResponse> call = RetrofitApi.getInstance().getRecipeService().removeBookmark(recipeID,prefHelper.getAccess());
            call.enqueue(new Callback<BasicResponse>() {
                @Override
                public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                    if(response.isSuccessful()){
                        binding.btnBookmark.setText("Bookmark");
                    }
                }

                @Override
                public void onFailure(Call<BasicResponse> call, Throwable t) {

                }
            });
        }
    }

    private void loadRecipe(){
        Call<Recipe> call = RetrofitApi.getInstance().getRecipeService().getRecipeDetail(recipeID);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if(response.isSuccessful()){
                    Recipe recipe = response.body();
                    assert recipe != null;
                    binding.title.setText(recipe.title);
                    binding.description.setText(recipe.description);
                    Picasso.get().load(recipe.photo).into(binding.detailFoodImage);
                    binding.cookDuration.setText(recipe.cook_duration+"m");
                    binding.prepDuration.setText(recipe.prep_duration+"m");
                    binding.servePortion.setText(recipe.serve_portion+"");
                    binding.rating.setText(recipe.rate+"");
                    binding.like.setText(recipe.like+"");
                    binding.view.setText(recipe.view+"");
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {

            }
        });
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

        Call<ArrayList<Step>> call = RetrofitApi.getInstance().getRecipeService().getRecipeSummary(recipeID);
        call.enqueue(new Callback<ArrayList<Step>>() {
            @Override
            public void onResponse(Call<ArrayList<Step>> call, Response<ArrayList<Step>> response) {
                if(response.isSuccessful()){
                    SummaryStepAdapter adapter = new SummaryStepAdapter(getActivity(),response.body());
                    binding.recipeDetailRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Step>> call, Throwable t) {
            }
        });
    }

    private void changeToIngredient(){
        binding.detailLayout.setVisibility(View.GONE);
        binding.detailTitleTv.setText("Ingredients");

        Call<ArrayList<RecipeIngredient>> call = RetrofitApi.getInstance().getRecipeService().getRecipeIngredients(recipeID);
        call.enqueue(new Callback<ArrayList<RecipeIngredient>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeIngredient>> call, Response<ArrayList<RecipeIngredient>> response) {
                if(response.isSuccessful()){
                    IngredientBarAdapter adapter = new IngredientBarAdapter(getActivity(),response.body());
                    binding.recipeDetailRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeIngredient>> call, Throwable t) {

            }
        });
    }

    private void changeToReview(){
        binding.detailLayout.setVisibility(View.GONE);
        binding.detailTitleTv.setText("Reviews");

        Call<ArrayList<Review>> call = RetrofitApi.getInstance().getRecipeService().getRecipeReviews(recipeID);
        call.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                if(response.isSuccessful()){
                    ReviewAdapter adapter = new ReviewAdapter(response.body());
                    adapter.setReviewListener(new ReviewAdapter.ReviewListener() {
                        @Override
                        public void onCommentClick(Review v) {
                            Intent i = new Intent(getActivity(),ActivityReview.class);
                            i.putExtra("review",v);
                            startActivity(i);
                        }
                    });
                    binding.recipeDetailRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Review>> call, Throwable t) {

            }
        });
    }

    private void changeToLetsCook(){
        binding.detailTitleTv.setText("Lets Cook");
        Intent i = new Intent(getActivity(), ActivityStep.class);
        i.putExtra(ActivityStep.RECIPE_ID,recipeID);
        ((ActivityHome)getActivity()).launcher.launch(i);
    }
}