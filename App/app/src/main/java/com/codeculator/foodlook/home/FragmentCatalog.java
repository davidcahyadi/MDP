package com.codeculator.foodlook.home;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCatalog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCatalog extends Fragment {
    FragmentCatalogBinding binding;
    HTTPRequest httpRequest;
    RecommendationAdapter adapter;

    int filter = -1;

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

        httpRequest = new HTTPRequest((AppCompatActivity) getActivity());

        binding.tvMostPopular.setOnClickListener(v -> {
            if(filter != 0){
                filter = 0;
                CatalogRecipeRequest("popular", 1);
                setFilter(filter);
            }
        });

        binding.tvNewest.setOnClickListener(v -> {
            if(filter != 1){
                filter = 1;
                CatalogRecipeRequest("newest", 1);
                setFilter(filter);
            }
        });

        binding.tvMostLike.setOnClickListener(v -> {
            if(filter != 2){
                filter = 2;
                CatalogRecipeRequest("like", 1);
                setFilter(filter);
            }
        });

        setToMostPopularPage();
    }

    public void setFilter(int num)
    {
        TextView view = binding.tvMostLike;
        if(num == 0){
            view = binding.tvMostPopular;
        }else if(num == 1){
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

    public Date getDate(String str)
    {
        DateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));

        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public void CatalogRecipeRequest(String type, int page)
    {
        binding.loading.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.GET,
                getString(R.string.APP_URL)+"/catalog/"+ type +"/" + page,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<Recipe> recipes = new ArrayList<>();
                        try{
                            JSONArray arr = new JSONArray(response);
                            int i = 0;
                            while(!arr.isNull(i)){
                                Recipe recipe = new Recipe(arr.getJSONObject(i));
                                recipes.add(recipe);
                                i++;
                            }
                            binding.loading.setVisibility(View.GONE);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setRecipes(recipes);
                                    adapter.notifyDataSetChanged();
                                }
                            }).start();

                        }catch (Exception e){
                            binding.loading.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(req);

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
}