package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.helper.FetchImage;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.services.HTTPRequest;

import java.util.ArrayList;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationItemHolder> {
    Context context;
    ArrayList<Recipe> recipes = new ArrayList<>();
    HTTPRequest httpRequest;
    FetchImage fetchImage;

    public RecommendationAdapter(Context context, ArrayList<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
        httpRequest = new HTTPRequest((AppCompatActivity) context);
        fetchImage = new FetchImage(httpRequest);
    }

    @NonNull
    @Override
    public RecommendationItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_find_recipe,parent,false);
        return new RecommendationItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationItemHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecommendationItemHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName2, tvRating, tvTime, tvViewCount;
        ImageView imageViewDisplayRecipe3;

        public RecommendationItemHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName2 = itemView.findViewById(R.id.tvRecipeName2);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvViewCount = itemView.findViewById(R.id.tvViewCount);
            imageViewDisplayRecipe3 = itemView.findViewById(R.id.imageViewDisplayRecipe3);
        }

        public void bind(Recipe r) {
            tvRecipeName2.setText(r.title);
            tvRating.setText(r.rate+"");
            tvTime.setText((r.prep_duration + r.cook_duration)+"");
            tvViewCount.setText(r.view+"");
            fetchImage.fetch(r.photo, imageViewDisplayRecipe3);
        }
    }
}
