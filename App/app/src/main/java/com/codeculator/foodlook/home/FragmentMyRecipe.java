package com.codeculator.foodlook.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.MyRecipeAdapter;
import com.codeculator.foodlook.model.Recipe;

import java.util.ArrayList;

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
        //todo ngambil recipes
        myRecipeAdapter = new MyRecipeAdapter(recipes);
        recyclerViewMyRecipe = view.findViewById(R.id.recyclerViewMyRecipe);
        buttonAddRecipe = view.findViewById(R.id.buttonAddRecipe);

        recyclerViewMyRecipe.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMyRecipe.setAdapter(myRecipeAdapter);
        buttonAddRecipe.setOnClickListener(view1 -> {
            fragmentMyRecipeListener.gotoDetail();
        });
    }



    public interface FragmentMyRecipeListener{
        void gotoDetail();
    }
}