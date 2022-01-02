package com.codeculator.foodlook.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.User;
import com.codeculator.foodlook.services.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReviewListAdapter extends RecyclerView.Adapter<AdminReviewListAdapter.AdminListReviewHolder>{
    public ArrayList<Review> reviews;
    Context context;
    ListClickListener listClickListener;
    ArrayList<String> ops;

    public AdminReviewListAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
        this.ops = new ArrayList<>();
    }

    public void setListClickListener(ListClickListener listClickListener){
        this.listClickListener = listClickListener;
    }

    @NonNull
    @Override
    public AdminListReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_component_review_list,parent,false);
        return new AdminReviewListAdapter.AdminListReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListReviewHolder holder, int position) {
        holder.bind(reviews.get(position), position);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public String getOPAt(int i){
        if(ops.size() > i)
            return ops.get(i);
        else
            return "";
    }

    public class AdminListReviewHolder extends RecyclerView.ViewHolder {
        TextView idTV, opTV, postedAtTV, typeTV, contentTV;
        ImageButton moreButton;
        public AdminListReviewHolder(@NonNull View itemView) {
            super(itemView);
            idTV = itemView.findViewById(R.id.idTV);
            opTV = itemView.findViewById(R.id.opTV);
            postedAtTV = itemView.findViewById(R.id.postedAtTV);
            typeTV = itemView.findViewById(R.id.typeTV);
            contentTV = itemView.findViewById(R.id.contentTV);
            moreButton = itemView.findViewById(R.id.review_more_btn);
        }

        public void bind(Review review, int index){
            idTV.setText(review.id + "");

            postedAtTV.setText(review.created_at);
            if(review.review_id == 0)
                typeTV.setText("Type: REVIEW");
            else
                typeTV.setText("Type: REPLY");
            contentTV.setText(review.description);

            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listClickListener.moreButtonClick(index, moreButton);
                }
            });

            Call<User> call = RetrofitApi.getInstance().getAdminService().getUserByID(review.user_id);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        ops.add(response.body().getName());
                        opTV.setText("Posted By: " + response.body().getName());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    public interface ListClickListener{
        void moreButtonClick(int reviewID, ImageButton btn);
    }
}
