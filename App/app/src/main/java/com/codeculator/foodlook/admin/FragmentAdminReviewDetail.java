package com.codeculator.foodlook.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeculator.foodlook.databinding.FragmentAdminReviewDetailBinding;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.User;
import com.codeculator.foodlook.services.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAdminReviewDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdminReviewDetail extends Fragment {

    FragmentAdminReviewDetailBinding binding;
    int reviewID;
    Review selectedReview;

    public FragmentAdminReviewDetail() {
        // Required empty public constructor
    }

    public static FragmentAdminReviewDetail newInstance(String param1, String param2) {
        FragmentAdminReviewDetail fragment = new FragmentAdminReviewDetail();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if(b != null)
            reviewID = b.getInt("reviewID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminReviewDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestReview(reviewID);
    }

    public void requestReview(int id){
        Call<Review> call = RetrofitApi.getInstance().getAdminService().getReviewByID(id);
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if(response.isSuccessful()){
                    selectedReview = response.body();
                    loadReview();
                    loadPoster();
                    loadReplies();
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {

            }
        });
    }

    public void loadReview(){
        binding.detailReviewIDTV.setText("#" + selectedReview.id);
        binding.detailReviewPostedTV.setText(selectedReview.created_at);
        binding.detailReviewContentTV.setText(selectedReview.description);
        if(selectedReview.review_id == 0)
            binding.detailReviewRatingTV.setText(selectedReview.rate + "");
        else
            binding.detailReviewRatingTV.setText("-");
    }

    public void loadPoster(){
        Call<User> call = RetrofitApi.getInstance().getAdminService().getUserByID(selectedReview.user_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    binding.detailReviewPosterTV.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void loadReplies(){
        Call<ArrayList<Review>> call = RetrofitApi.getInstance().getAdminService().getReviews(-1);
        call.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                if(response.isSuccessful()){
                    int replies = 0;
                    for(Review r : response.body()){
                        if(r.review_id == selectedReview.id) replies++;
                    }
                    binding.detailReviewRepliesTV.setText(replies + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Review>> call, Throwable t) {

            }
        });
    }
}