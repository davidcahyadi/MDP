package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Comment;
import com.codeculator.foodlook.model.Review;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ListViewHolder>{
    Context context;
    ArrayList<Review> comments;

    public CommentAdapter(Context context, ArrayList<Review> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Review c = comments.get(position);
        holder.bind(c);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView tvReviewName,tvReviewDescription;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReviewName = itemView.findViewById(R.id.tvReviewName);
            tvReviewDescription = itemView.findViewById(R.id.tvReviewDescription);
        }
        public void bind(Review c){
            tvReviewName.setText(c.name);
            tvReviewDescription.setText(c.description);
        }
    }
}



