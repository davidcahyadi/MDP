package com.codeculator.foodlook.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.MyRecipeAdapter;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.services.HTTPRequest;
import com.codeculator.foodlook.services.RetrofitApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMyRecipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMyRecipe extends Fragment {

    ArrayList<Recipe> recipes;

    public FragmentMyRecipe() {
        // Required empty public constructor
    }

    public static FragmentMyRecipe newInstance(ArrayList<Recipe> recipes) {
        FragmentMyRecipe fragment = new FragmentMyRecipe();
        Bundle args = new Bundle();
        args.putParcelableArrayList("recipes", recipes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipes = getArguments().getParcelableArrayList("recipes");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_recipe, container, false);
    }


    FragmentMyRecipeListener fragmentMyRecipeListener;
    MyRecipeAdapter myRecipeAdapter;
    Button buttonAddRecipe;
    RecyclerView recyclerViewMyRecipe;

    public void setFmrl(FragmentMyRecipeListener fragmentMyRecipeListener) {
        this.fragmentMyRecipeListener = fragmentMyRecipeListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipes = new ArrayList<>();
        PrefHelper prefHelper = new PrefHelper((AppCompatActivity) getActivity());
        Call<ArrayList<Recipe>> call = RetrofitApi.getInstance().getRecipeService().myAllRecipes(prefHelper.getAccess());

        recyclerViewMyRecipe = view.findViewById(R.id.recyclerViewMyRecipe);
        // get recipes catalog
        HTTPRequest.Response<String> recipeResponse = new HTTPRequest.Response<String>();
        recipeResponse.onError(e->{
            System.out.println(e.toString());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        });

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if(response.isSuccessful()){
                    recipes.addAll(response.body());

                    MyRecipeAdapter adapter = new MyRecipeAdapter(recipes, getContext());
                    recyclerViewMyRecipe.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerViewMyRecipe.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

            }
        });
    }



    public interface FragmentMyRecipeListener{
        void gotoDetail();
        void addRecipe();
    }
}