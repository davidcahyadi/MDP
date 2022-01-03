package com.codeculator.foodlook.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.User;
import com.codeculator.foodlook.services.RetrofitApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRecipeListAdapter extends RecyclerView.Adapter<AdminRecipeListAdapter.AdminListRecipeHolder>{
    ArrayList<Recipe> recipes;
    Context context;
    public ListClickListener listClickListener;

    public AdminRecipeListAdapter(ArrayList<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    public void setListClickListener(AdminRecipeListAdapter.ListClickListener listClickListener){
        this.listClickListener = listClickListener;
    }

    @NonNull
    @Override
    public AdminListRecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_component_recipe_list,parent,false);
        return new AdminListRecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListRecipeHolder holder, int position) {
        holder.bind(recipes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class AdminListRecipeHolder extends RecyclerView.ViewHolder {
        ImageView recipeImageIV;
        ImageButton recipeMoreButton;
        TextView recipeNameTV, recipeCreatorTV, recipeCreatedAtTV;
        public AdminListRecipeHolder(@NonNull View itemView) {
            super(itemView);
            recipeImageIV = itemView.findViewById(R.id.recipeImageIV);
            recipeNameTV = itemView.findViewById(R.id.recipeNameTV);
            recipeCreatorTV = itemView.findViewById(R.id.recipeCreatorTV);
            recipeCreatedAtTV = itemView.findViewById(R.id.recipeCreatedAtTV);
            recipeMoreButton = itemView.findViewById(R.id.recipeMoreBtn);
        }

        public void bind(Recipe recipe, int idx){
            recipeNameTV.setText(recipe.title);
            recipeCreatedAtTV.setText("Created At: " + recipe.created_at);
            recipeMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listClickListener.moreButtonClick(recipe.id, recipeMoreButton);
                }
            });
            Picasso.get().load(recipe.photo).into(recipeImageIV);
            if(recipe.user_id == 0)
                recipeCreatorTV.setText("Created By: SYSTEM");
            else
                requestCreator(recipe.user_id);
        }

        public void requestCreator(int userID){
            Call<User> call = RetrofitApi.getInstance().getAdminService().getUserByID(userID);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        recipeCreatorTV.setText("Created By: " + response.body().getName());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    public interface ListClickListener{
        void moreButtonClick(int recipeID, ImageButton btn);
    }
}
