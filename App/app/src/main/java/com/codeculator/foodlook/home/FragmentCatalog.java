package com.codeculator.foodlook.home;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.RecommendationAdapter;
import com.codeculator.foodlook.databinding.FragmentCatalogBinding;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.services.HTTPRequest;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCatalog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCatalog extends Fragment {
    ArrayList<Recipe> recipes = new ArrayList<>();
    ArrayList<Recipe> searchRecipes = new ArrayList<>();
    FragmentCatalogBinding binding;
    HTTPRequest httpRequest;
    RecommendationAdapter adapter;
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

        httpRequest = new HTTPRequest((AppCompatActivity) getActivity());

        binding.tvMostPopular.setOnClickListener(v -> {
            if(filter != 0){
                setAdapter();
                filter = 0;
                type = "popular";
                CatalogRecipeRequest();
                setFilter();
            }
        });

        binding.tvNewest.setOnClickListener(v -> {
            if(filter != 1){
                setAdapter();
                filter = 1;
                type = "newest";
                CatalogRecipeRequest();
                setFilter();
            }
        });

        binding.tvMostLike.setOnClickListener(v -> {
            if(filter != 2){
                setAdapter();
                filter = 2;
                type = "like";
                CatalogRecipeRequest();
                setFilter();
            }
        });

        setToMostPopularPage();
        binding.loading.setVisibility(View.VISIBLE);

        binding.nestedScrollView.setOnScrollChangeListener(
                (NestedScrollView.OnScrollChangeListener)
                        (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                            if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                                page++;
                                binding.progressBar2.setVisibility(View.VISIBLE);
                                CatalogRecipeRequest();
                            }
        });
    }

    public void setAdapter(){
        recipes = new ArrayList<>();
        adapter = new RecommendationAdapter(getActivity(), getParentFragmentManager());
        adapter.setRecipes(recipes);
        binding.rvRecipeCatalog.setAdapter(adapter);
        binding.rvRecipeCatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.loading.setVisibility(View.VISIBLE);
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
        binding.tvMostPopular.setBackgroundColor(Color.WHITE);
        binding.tvNewest.setTextColor(getResources().getColor(R.color.yellow_300, null));
        binding.tvNewest.setBackgroundColor(Color.WHITE);
        binding.tvMostLike.setTextColor(getResources().getColor(R.color.yellow_300, null));
        binding.tvMostLike.setBackgroundColor(Color.WHITE);
        binding.rvRecipeCatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new RecommendationAdapter(getActivity(), getParentFragmentManager());
        binding.rvRecipeCatalog.setAdapter(adapter);
        view.setTextColor(Color.WHITE);
        view.setBackgroundColor(getResources().getColor(R.color.yellow_300, null));
    }

    public void setToMostPopularPage()
    {
        binding.tvMostPopular.performClick();
    }

    public void CatalogRecipeRequest()
    {
        HTTPRequest.Response<String> catalogResponse = new HTTPRequest.Response<>();
        catalogResponse.onError(e->{
            Log.e("ERROR",e.toString());
            Toast.makeText(getActivity(), "Load Recipe Error", Toast.LENGTH_SHORT).show();
        });

        catalogResponse.onSuccess(res-> {
            try{
                JSONArray arr = new JSONArray(res);
                int i = 0;
                while(!arr.isNull(i)){
                    JSONObject obj = arr.getJSONObject(i);

                    Recipe recipe = new Recipe(obj);
                    recipes.add(recipe);
                    i++;
                }
                Log.i("size", recipes.size()+"");
                adapter.notifyItemRangeChanged(page*10-10, 10);
                binding.loading.setVisibility(View.GONE);
                binding.progressBar2.setVisibility(View.GONE);
            }
            catch (Exception e){
                Log.e("ERROR",e.getMessage());
            }
        });

        RequestQueue rq = Volley.newRequestQueue(getActivity());

//        HTTPRequest.Response<String> catalogResponse = new HTTPRequest.Response<>();
//        catalogResponse.onError(e->{
//            Log.e("ERROR",e.toString());
//            Toast.makeText(getActivity(), "Load Recipe Error", Toast.LENGTH_SHORT).show();
//        });
//
//        catalogResponse.onSuccess(res-> {
//            try{
//                ArrayList<Recipe> recipes = new ArrayList<>();
//                JSONArray arr = new JSONArray(res);
//                int i = 0;
//                while(!arr.isNull(i)){
//                    Recipe recipe = new Recipe(arr.getJSONObject(i));
//                    recipes.add(recipe);
//                    i++;
//                }
//                Log.i("size", recipes.size()+"");
//                RecommendationAdapter adapter = new RecommendationAdapter(getActivity(), recipes, getParentFragmentManager());
//                binding.rvRecipeCatalog.setAdapter(adapter);
//                binding.rvRecipeCatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//                binding.loading.setVisibility(View.GONE);
//            }
//            catch (Exception e){
//                Log.e("ERROR",e.getMessage());
//            }
//        });
//        binding.loading.setVisibility(View.VISIBLE);
//
//        httpRequest.get(getString(R.string.APP_URL)+"/catalog/"+ type +"/" + page,new HashMap<>(),
//                catalogResponse);
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