package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.model.Recipe;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MyRecipeAdapter extends RecyclerView.Adapter<MyRecipeAdapter.ListViewHolder> {
    ArrayList<Recipe> recipes;
    Context context;

    public MyRecipeAdapter(ArrayList<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_my_recipe,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipeAdapter.ListViewHolder holder, int position) {
        Recipe r = recipes.get(position);
        holder.bind(r);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder{
        TextView tvRecipeName3, tvRecipeDescription, btnRecipeDetail;
        ImageView ivRecipeImage;

        public ListViewHolder(@NonNull View iv) {
            super(iv);
            tvRecipeName3 = iv.findViewById(R.id.tvRecipeName3);
            tvRecipeDescription = iv.findViewById(R.id.tvRecipeDescription);
            btnRecipeDetail = iv.findViewById(R.id.btnRecipeDetail);
            ivRecipeImage = iv.findViewById(R.id.ivRecipeImage);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        public void bind(Recipe r){
            tvRecipeName3.setText(r.title);
            String imageUrl = r.photo;
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
                ivRecipeImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                System.out.println(e.toString());
            }

            tvRecipeDescription.setText(r.description);
            btnRecipeDetail.setOnClickListener(view -> {
                //todo get to detail
                Toast.makeText(itemView.getContext(), r.id + "", Toast.LENGTH_SHORT).show();
            });
        }
    }
}


