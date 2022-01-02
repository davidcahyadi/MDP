package com.codeculator.foodlook.home;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.RecommendationAdapter;
import com.codeculator.foodlook.adapter.SummaryStepAdapter;
import com.codeculator.foodlook.databinding.FragmentCatalogBinding;
import com.codeculator.foodlook.helper.EndlessRecyclerViewScrollListener;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.services.HTTPRequest;
import com.codeculator.foodlook.services.RetrofitApi;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCatalog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCatalog extends Fragment {
    ArrayList<Recipe> recipes = new ArrayList<>();
    ArrayList<Recipe> searchRecipes = new ArrayList<>();
    FragmentCatalogBinding binding;
    RecommendationAdapter adapter;
    EndlessRecyclerViewScrollListener scrollListener;
    int filter = -1;
    int page = 1;
    String type = "popular";

    public FragmentCatalog() {
        // Required empty public constructor
    }

    public static FragmentCatalog newInstance() {
        FragmentCatalog fragment = new FragmentCatalog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCatalogBinding.inflate(inflater,container, false);
        Log.i("CREATE","VIEW");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Recipes");

        setAdapter();
        binding.tvMostPopular.setOnClickListener(v -> {
            if(filter != 0){
                resetAdapter();
                filter = 0;
                type = "popular";
                page = 1;
                CatalogRecipeRequest();
                setFilter();
            }
        });

        binding.tvNewest.setOnClickListener(v -> {
            if(filter != 1){
                resetAdapter();
                filter = 1;
                type = "newest";
                page = 1;
                CatalogRecipeRequest();
                setFilter();
            }
        });

        binding.tvMostLike.setOnClickListener(v -> {
            if(filter != 2){
                resetAdapter();
                filter = 2;
                type = "like";
                page = 1;
                CatalogRecipeRequest();
                setFilter();
            }
        });

        setToMostPopularPage();
        binding.loading.setVisibility(View.VISIBLE);

    }

    public void setAdapter(){
        recipes = new ArrayList<>();
        adapter = new RecommendationAdapter(getActivity(), getParentFragmentManager());
        adapter.setRecipes(recipes);
        binding.rvRecipeCatalog.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rvRecipeCatalog.setLayoutManager(gridLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                FragmentCatalog.this.page++;
                binding.progressBar2.setVisibility(View.VISIBLE);
                CatalogRecipeRequest();
            }
        };
        binding.rvRecipeCatalog.addOnScrollListener(scrollListener);
    }

    private void resetAdapter(){
        binding.loading.setVisibility(View.VISIBLE);
        recipes.clear();
        adapter.notifyDataSetChanged();
        scrollListener.resetState();
    }

    public void setFilter()
    {
        TextView view = binding.tvMostLike;
        if(filter == 0){
            view = binding.tvMostPopular;
        }else if(filter == 1){
            view = binding.tvNewest;
        }

        binding.tvMostPopular.setTextColor(getResources().getColor(R.color.yellow_300, null));
        binding.tvMostPopular.setBackgroundTintList(getResources().getColorStateList(R.color.transparent, null));
        binding.tvNewest.setTextColor(getResources().getColor(R.color.yellow_300, null));
        binding.tvNewest.setBackgroundTintList(getResources().getColorStateList(R.color.transparent, null));
        binding.tvMostLike.setTextColor(getResources().getColor(R.color.yellow_300, null));
        binding.tvMostLike.setBackgroundTintList(getResources().getColorStateList(R.color.transparent, null));
        view.setTextColor(Color.WHITE);
        view.setBackgroundTintList(getResources().getColorStateList(R.color.yellow_300, null));
    }


    public void setToMostPopularPage()
    {
        binding.tvMostPopular.performClick();
    }

    public void CatalogRecipeRequest()
    {
        Call<ArrayList<Recipe>> call = RetrofitApi.getInstance().getCatalogService().getCatalogs(type, page);
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if(response.isSuccessful()){
                    if(response.body().size() > 0){
                        int newIndex = recipes.size();
                        recipes.addAll(response.body());
                        Log.i("size", recipes.size()+"");
                        adapter.notifyItemRangeInserted(newIndex, response.body().size());
                        binding.loading.setVisibility(View.GONE);
                        binding.progressBar2.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem menuItem = menu.findItem(R.id.item_search);
        SearchView searchView = new SearchView(((ActivityHome) getActivity()).getSupportActionBar().getThemedContext());
        menuItem.setActionView(searchView);
        searchView.setQueryHint("Search recipes...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    adapter = new RecommendationAdapter(getActivity(), getParentFragmentManager());
                    adapter.setRecipes(recipes);
                }
                else{
                    searchRecipes = new ArrayList<>();
                    for (Recipe r: recipes) {
                        if(r.title.toLowerCase().contains(s.toLowerCase())){
                            searchRecipes.add(r);
                        }
                    }
                    adapter = new RecommendationAdapter(getActivity(), getParentFragmentManager());
                    adapter.setRecipes(searchRecipes);
                }
                binding.rvRecipeCatalog.setAdapter(adapter);
                binding.rvRecipeCatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}