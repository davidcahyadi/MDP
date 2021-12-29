package com.codeculator.foodlook.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.MyRecipeAdapter;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.services.HTTPRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMyRecipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMyRecipe extends Fragment {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Recipe> recipes;
    HTTPRequest httpRequest;

    public FragmentMyRecipe() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
        httpRequest = new HTTPRequest((AppCompatActivity) getActivity());

        // get recipes catalog
        HTTPRequest.Response<String> catalogResponse = new HTTPRequest.Response<String>();
        catalogResponse.onError(e->{
            Toast.makeText(getActivity(), "Load Recipe Error", Toast.LENGTH_SHORT).show();
        });

        catalogResponse.onSuccess(res-> {
            try{
                ArrayList<Recipe> recipes = new ArrayList<>();

                JSONArray arr = new JSONArray(res);
                int i = 0;
                while(!arr.isNull(i)){
                    JSONObject obj = arr.getJSONObject(i);

                    Recipe recipe = new Recipe(
                            obj.getInt("id"),
                            obj.getString("title"),
                            obj.getInt("user_id"),
                            (float) obj.getDouble("rate"),
                            obj.getInt("view"),
                            obj.getInt("like"),
                            obj.getInt("cook_duration"),
                            obj.getInt("prep_duration"),
                            obj.getInt("serve_portion"),
                            obj.getString("description"),
                            (obj.getString("created_at")),
                            (obj.getString("updated_at")),
                            obj.getString("photo"),
                            obj.getString("crawling_from")
                    );
                    recipes.add(recipe);
                    i++;
                }
                MyRecipeAdapter adapter = new MyRecipeAdapter(recipes);
                recyclerViewMyRecipe.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerViewMyRecipe.setAdapter(myRecipeAdapter);
            }
            catch (Exception e){
                Log.e("ERROR",e.getMessage());
            }
        });

        httpRequest.get(getString(R.string.APP_URL)+"/catalog/popular/1",new HashMap<>(),
                catalogResponse);
        myRecipeAdapter = new MyRecipeAdapter(recipes);
        recyclerViewMyRecipe = view.findViewById(R.id.recyclerViewMyRecipe);
        buttonAddRecipe = view.findViewById(R.id.buttonAddRecipe);

        buttonAddRecipe.setOnClickListener(view1 -> {
            fragmentMyRecipeListener.gotoDetail();
        });
    }



    public interface FragmentMyRecipeListener{
        void gotoDetail();
    }
}