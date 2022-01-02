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
    ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_my_recipe, parent, false);
        return new CommentAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Comment c = comments.get(position);
        holder.bind(c);
    }

    @Override
    public int getItemCount() {
        return comments.size();
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
        public void bind(Comment c){
            reviewContentTv.setText(c.comment);
            countCommentsTv.setVisibility(View.GONE);
            star1Image.setVisibility(View.GONE);
            star2Image.setVisibility(View.GONE);
            star3Image.setVisibility(View.GONE);
            star4Image.setVisibility(View.GONE);
            star5Image.setVisibility(View.GONE);
            reviewScoreTv.setVisibility(View.GONE);
        }
    }
}



