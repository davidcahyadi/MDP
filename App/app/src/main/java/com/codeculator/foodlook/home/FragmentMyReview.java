package com.codeculator.foodlook.home;

import android.content.Intent;
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
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.MyRecipeAdapter;
import com.codeculator.foodlook.adapter.ReviewAdapter;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.services.HTTPRequest;
import com.codeculator.foodlook.services.RetrofitApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMyReview extends Fragment {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HTTPRequest httpRequest;

    public FragmentMyReview() {
        // Required empty public constructor
    }

    public static FragmentMyReview newInstance(String param1, String param2) {
        FragmentMyReview fragment = new FragmentMyReview();
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
        return inflater.inflate(R.layout.fragment_my_review, container, false);
    }

    ArrayList<Review> reviews;
    RecyclerView review_recylerview;
    ReviewAdapter reviewAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        review_recylerview = view.findViewById(R.id.review_recylerview);
        review_recylerview.setLayoutManager(new LinearLayoutManager(getContext()));
        reviews = new ArrayList<>();
        getReviews();
        reviewAdapter = new ReviewAdapter(reviews);
        reviewAdapter.setReviewListener(v -> {
            Intent i = new Intent(getContext(), ActivityReview.class);
            i.putExtra("review", v);
            startActivity(i);
        });
        review_recylerview.setAdapter(reviewAdapter);
    }

    public void getReviews(){
        PrefHelper prefHelper = new PrefHelper((AppCompatActivity) getActivity());
        Call<ArrayList<Review>> call = RetrofitApi.getInstance().getReviewInterface().getMyReviews(prefHelper.getAccess());
        call.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                if(response.isSuccessful()){
                    reviews = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Review>> call, Throwable t) {

            }
        });
    }
}