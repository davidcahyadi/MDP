package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {
    ArrayList<Ingredient> ingredients;
    Context context;
    private OnClickListener<Ingredient> onClick;

    public IngredientAdapter(Context context) {
        this.context = context;
    }

    public void setOnClick(OnClickListener<Ingredient> onClick) {
        this.onClick = onClick;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_find_ingredient,parent,false);
        return new IngredientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientHolder extends RecyclerView.ViewHolder {
        ImageView ivDisplay, ivShadow, ivIcon;
        TextView tvName;


        public IngredientHolder(@NonNull View itemView) {
            super(itemView);

            ivDisplay = itemView.findViewById(R.id.imageViewDisplayRecipe2);
            ivShadow = itemView.findViewById(R.id.imageViewShadow);
            ivIcon = itemView.findViewById(R.id.imageViewIcon);
            tvName = itemView.findViewById(R.id.tvRecipeName);
        }

        public void bind(Ingredient i){
            tvName.setText(i.name);
            Picasso.get().load(i.icon_url).into(ivDisplay);

            tvName.setTextColor(Color.WHITE);
            ivShadow.setVisibility(View.INVISIBLE);
            ivIcon.setVisibility(View.INVISIBLE);
            if(i.check_status) {
                ivShadow.setVisibility(View.VISIBLE);
                ivIcon.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(view -> {
                onClick.onClick(i);
            });
        }
    }


}
