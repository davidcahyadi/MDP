package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Ingredient;
import com.codeculator.foodlook.model.Step;

import java.util.ArrayList;

public class IngredientBarAdapter extends RecyclerView.Adapter<IngredientBarAdapter.IngredientBarHolder> {
    Context context;
    ArrayList<Ingredient> ingredients;
    OnClickListener<Ingredient> onClick;

    public IngredientBarAdapter(Context context,ArrayList<Ingredient> steps){
        this.context = context;
        this.ingredients = steps;
    }

    public void setOnClickListener(OnClickListener<Ingredient> onClick){
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public IngredientBarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ingredient,parent,false);
        return new IngredientBarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientBarHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class IngredientBarHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvOption;

        public IngredientBarHolder(@NonNull View iv) {
            super(iv);
            tvName = iv.findViewById(R.id.name);
            tvOption = iv.findViewById(R.id.option);
        }

        public void bind(Ingredient ingredient){
            tvName.setText(ingredient.name);
            tvOption.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context,tvOption);
                popupMenu.inflate(R.menu.ingredient_menu);
                popupMenu.setOnMenuItemClickListener(item->{
                    if(item.getItemId() == R.id.ingredient_change){
                        Toast.makeText(context, "Change", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                });
                popupMenu.show();
            });
        }
    }
}
