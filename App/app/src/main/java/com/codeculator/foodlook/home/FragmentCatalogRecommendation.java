package com.codeculator.foodlook.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.RecommendationAdapter;
import com.codeculator.foodlook.adapter.SummaryStepAdapter;
import com.codeculator.foodlook.databinding.FragmentCatalogBinding;
import com.codeculator.foodlook.databinding.FragmentCatalogRecommendationBinding;
import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.services.HTTPRequest;
import com.codeculator.foodlook.services.RetrofitApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCatalogRecommendation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCatalogRecommendation extends Fragment {
    RecommendationAdapter adapter;
    FragmentCatalogRecommendationBinding binding;
    ArrayList<Recipe> recipes = new ArrayList<>();
    ArrayList<Recipe> searchRecipes = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    HTTPRequest httpRequest;

    public FragmentCatalogRecommendation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCatalogRecommendation.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCatalogRecommendation newInstance(String param1, String param2) {
        FragmentCatalogRecommendation fragment = new FragmentCatalogRecommendation();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCatalogRecommendationBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Recommendation");
        httpRequest = new HTTPRequest((AppCompatActivity) getActivity());

        callAPI();

        setAdapter();
    }

    public void callAPI(){
        // create arraylist of id
        for (ArrayList<Ingredient> list: FragmentFindRecipe.listIngredients) {
            for (Ingredient i: list) {
                if(i.check_status)
                    ids.add(i.id);
            }
        }

        // call api
        Call<ArrayList<Recipe>> call = RetrofitApi.getInstance().getCatalogService().getRecommendations(ids);
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
                        binding.progressBar3.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.e("error recommendation : ", "error call");
            }
        });
    }

    public void setAdapter(){
        adapter = new RecommendationAdapter(getContext(), getParentFragmentManager());
        adapter.setRecipes(recipes);
        binding.rvRecommendation.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rvRecommendation.setAdapter(adapter);
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
                binding.rvRecommendation.setAdapter(adapter);
                binding.rvRecommendation.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}