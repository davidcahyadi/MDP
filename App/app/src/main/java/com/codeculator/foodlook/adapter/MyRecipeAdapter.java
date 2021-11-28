package com.codeculator.foodlook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Recipe;

import java.util.ArrayList;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.ListViewHolder> {
    ArrayList<Recipe> recipes;

    public MyRecipeAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_my_recipe, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Recipe r = recipes.get(position);
        holder.tvRecipeName3.setText(r.title);
        holder.tvRecipeDescription.setText(r.description);
        holder.btnRecipeDetail.setOnClickListener(view -> {
            //todo gotodetail
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView tvRecipeName3, tvRecipeDescription, btnRecipeDetail;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName3 = itemView.findViewById(R.id.tvRecipeName3);
            tvRecipeDescription = itemView.findViewById(R.id.tvRecipeDescription);
            btnRecipeDetail = itemView.findViewById(R.id.btnRecipeDetail);
        }
    }
}
