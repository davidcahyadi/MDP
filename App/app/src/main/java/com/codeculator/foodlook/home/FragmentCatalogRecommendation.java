package com.codeculator.foodlook.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.RecommendationAdapter;
import com.codeculator.foodlook.adapter.SummaryStepAdapter;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Step;
import com.codeculator.foodlook.services.HTTPRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCatalogRecommendation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCatalogRecommendation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rvRecommendation;
    RecommendationAdapter adapter;
    ArrayList<Recipe> recipes = new ArrayList<>();

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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvRecommendation = view.findViewById(R.id.rvRecommendation);

        // get recipes
//        HTTPRequest.Response<String> stepResponse = new HTTPRequest.Response<>();
//        stepResponse.onSuccess(res->{
//            try{
//                ArrayList<Step> steps = new ArrayList<>();
//
//                JSONArray arr = new JSONArray(res);
//                int i = 0;
//                while(!arr.isNull(i)){
//                    JSONObject obj = arr.getJSONObject(i);
//
//                    Step step = new Step(
//                            obj.getInt("id"),
//                            obj.getInt("order"),
//                            obj.getString("title"),
//                            obj.getString("url"),
//                            obj.getString("description").substring(0,120)+"..."
//                    );
//                    steps.add(step);
//                    i++;
//                }
//                SummaryStepAdapter adapter = new SummaryStepAdapter(getActivity(),steps);
//                binding.recipeDetailRecycler.setAdapter(adapter);
//            }
//            catch (Exception e){
//                Log.e("ERROR",e.getMessage());
//            }
//        });
//
//        httpRequest.get(getString(R.string.APP_URL)+"/recipe/"+recipeID+"/summary",new HashMap<>(),
//                stepResponse);

        adapter = new RecommendationAdapter(getContext(), recipes);
        rvRecommendation.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvRecommendation.setAdapter(adapter);
    }
}