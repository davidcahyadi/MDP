package com.codeculator.foodlook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.model.User;
import com.codeculator.foodlook.services.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_review, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Review r = reviews.get(position);
        holder.bind(r);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView displayNameTv,reviewScoreTv, reviewContentTv, countCommentsTv;
        ImageView displayPictureImage;
        ImageView star1Image, star2Image, star3Image, star4Image, star5Image;
        ImageView[] stars;

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
            stars = new ImageView[5];
            stars[0] = star1Image;
            stars[1] = star2Image;
            stars[2] = star3Image;
            stars[3] = star4Image;
            stars[4] = star5Image;
        }

        public void bind(Review review){
            reviewContentTv.setText(review.description);
            displayNameTv.setText(review.name);
            countCommentsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reviewListener.onCommentClick(review);
                }
            });
            reviewScoreTv.setText(review.rate + "");
            for(int i = 0; i < 5; i++){
                if(review.rate >= i + 1)
                    stars[i].setImageResource(R.drawable.ic_baseline_star_24);
                else
                    stars[i].setImageResource(R.drawable.ic_baseline_star_white_border_24);
            }
            loadReplies(review);
        }

        public void loadReplies(Review selectedReview){
            Call<ArrayList<Review>> call = RetrofitApi.getInstance().getAdminService().getReviews(-1);
            call.enqueue(new Callback<ArrayList<Review>>() {
                @Override
                public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                    if(response.isSuccessful()){
                        int replies = 0;
                        for(Review r : response.body()){
                            if(r.review_id == selectedReview.id) replies++;
                        }
                        countCommentsTv.setText(replies + " REPLIES");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Review>> call, Throwable t) {

                }
            });
        }
    }

    public interface ReviewListener{
        void onCommentClick(Review v);
    }
}
