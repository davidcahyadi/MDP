package com.codeculator.foodlook.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.IngredientAdapter;
import com.codeculator.foodlook.databinding.FragmentFindRecipeBinding;
import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.services.HTTPRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentFindRecipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFindRecipe extends Fragment {

    FragmentFindRecipeBinding binding;
    public static ArrayList<ArrayList<Ingredient>> listIngredients = new ArrayList<>(); // for recommendation access
    ArrayList<String> types = new ArrayList<>();
    IngredientAdapter adapter;
    HTTPRequest httpRequest;
    int idx = 0;

    public FragmentFindRecipe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentFindRecipe.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFindRecipe newInstance(String param1, String param2) {
        FragmentFindRecipe fragment = new FragmentFindRecipe();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFindRecipeBinding.inflate(inflater,container, false);
        Log.i("CREATE","VIEW");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Find Recipes");

        httpRequest = new HTTPRequest((AppCompatActivity) getActivity());
        // pull ingredient_type and ingredient data from api to dictIngredient
        callAPI();
    }

    public void callAPI(){
        // api ingredient type
         callAPIType();
    }

    public void setSpinner(){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, types);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        binding.spinner.setAdapter(spinnerArrayAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // update recycler view
                idx = i;
                setAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void callAPIType(){
        HTTPRequest.Response<String> response = new HTTPRequest.Response<>();
        response.onError(e->{
            Log.e("ERROR",e.toString());
            Toast.makeText(getActivity(), "Load Type Error", Toast.LENGTH_SHORT).show();
        });

        response.onSuccess(res-> {
            try{
                JSONArray arr = new JSONArray(res);
                int i = 0;
                listIngredients = new ArrayList<>();
                while(!arr.isNull(i)){
                    JSONObject obj = arr.getJSONObject(i);

                    types.add(obj.getString("name"));
                    listIngredients.add(new ArrayList<>());
                    i++;
                }

                callAPIIngredient();
            }
            catch (Exception e){
                Log.e("ERROR",e.getMessage());
            }
        });

        httpRequest.get(getString(R.string.APP_URL)+"/ingredient/types",new HashMap<>(),
                response);
    }

    private void callAPIIngredient(){
        HTTPRequest.Response<String> response = new HTTPRequest.Response<>();
        response.onError(e->{
            Log.e("ERROR",e.toString());
            Toast.makeText(getActivity(), "Load Ingredient Error", Toast.LENGTH_SHORT).show();
        });

        response.onSuccess(res-> {
            try{
                JSONArray arr = new JSONArray(res);
                int i = 0;
                while(!arr.isNull(i)){
                    JSONObject obj = arr.getJSONObject(i);

                    int j = obj.getInt("type_id");
                    System.out.println(j);
                    Ingredient ingredient = new Ingredient(obj);
                    listIngredients.get(j-1).add(ingredient);
                    i++;
                }
                setSpinner();
            }
            catch (Exception e){
                Log.e("ERROR",e.getMessage());
            }
        });

        httpRequest.get(getString(R.string.APP_URL)+"/ingredient/all",new HashMap<>(),
                response);
    }

    public void setAdapter(){
        adapter = new IngredientAdapter(getActivity());
        adapter.setIngredients(listIngredients.get(idx));
        adapter.setOnClick(ingredient -> {
            chooseIngredient(ingredient);
        });
        binding.rvIngredient.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rvIngredient.setAdapter(adapter);
    }

    public void chooseIngredient(Ingredient ingredient){
        System.out.println(ingredient.name);
        for (int i = 0; i < listIngredients.get(idx).size(); i++) {
            if(listIngredients.get(idx).get(i).name.equals(ingredient.name)){
                System.out.println(listIngredients.get(idx).get(i).name);
                listIngredients.get(idx).get(i).changeStatus();
//                adapter.notifyItemChanged(i);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_find_recipe, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_send){
            // go to fragment recommendation
            int ctr = 0;
            for (ArrayList<Ingredient> ingredients : listIngredients) {
                for (Ingredient ingredient : ingredients) {
                    if(ingredient.check_status)
                        ctr++;
                }
            }
            if(ctr > 0){
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.container, new FragmentCatalogRecommendation()).commit();
            }
            else{
                Toast.makeText(getActivity(), "Please choose the ingredient", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}