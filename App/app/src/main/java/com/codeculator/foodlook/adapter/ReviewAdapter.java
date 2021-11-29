package com.codeculator.foodlook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ListViewHolder>{

    ArrayList<Review> reviews;

    ReviewListener reviewListener;

    public ReviewAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public void setReviewListener(ReviewListener reviewListener) {
        this.reviewListener = reviewListener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_my_recipe, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Review r = reviews.get(position);
        holder.countCommentsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewListener.onCommentClick(r);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView displayNameTv,reviewScoreTv, reviewContentTv, countCommentsTv;
        ImageView displayPictureImage;
        ImageView star1Image, star2Image, star3Image, star4Image, star5Image;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            displayNameTv = itemView.findViewById(R.id.displayNameTv);
            reviewScoreTv = itemView.findViewById(R.id.reviewScoreTv);
            reviewContentTv = itemView.findViewById(R.id.reviewContentTv);
            countCommentsTv = itemView.findViewById(R.id.countCommentsTv);
            displayPictureImage = itemView.findViewById(R.id.displayPictureImage);
            star1Image = itemView.findViewById(R.id.star1Image);
            star2Image = itemView.findViewById(R.id.star2Image);
            star3Image = itemView.findViewById(R.id.star3Image);
            star4Image = itemView.findViewById(R.id.star4Image);
            star5Image = itemView.findViewById(R.id.star5Image);
        }
    }

    public interface ReviewListener{
        void onCommentClick(Review v);
    }
}
