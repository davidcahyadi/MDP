package com.codeculator.foodlook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.helper.FetchImage;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.services.HTTPRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkItemHolder> {
    Context context;
    FragmentManager fm;
    ArrayList<Recipe> recipes;
    HTTPRequest httpRequest;
    FetchImage fetchImage;
    ClickListener clickListener;

    public BookmarkAdapter(Context context,FragmentManager fm) {
        this.context = context;
        this.recipes = new ArrayList<>();
        httpRequest = new HTTPRequest((AppCompatActivity) context);
        fetchImage = new FetchImage(httpRequest);
        this.fm = fm;
    }

    public void setRecipes(ArrayList<Recipe> recipes){
        this.recipes = recipes;
    }

    public void setClickListener(ClickListener clickListener){ this.clickListener = clickListener; }

    @NonNull
    @Override
    public BookmarkItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_bookmarked_recipe,parent,false);
        return new BookmarkItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkItemHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class BookmarkItemHolder extends RecyclerView.ViewHolder {
        ImageView bookmarkImageIV,removeBookmarkBtn;
        TextView bookmarkNameTV;

        public BookmarkItemHolder(@NonNull View itemView) {
            super(itemView);
            bookmarkImageIV = itemView.findViewById(R.id.bookmarkImageIV);
            removeBookmarkBtn = itemView.findViewById(R.id.removeBookmarkBtn);
            bookmarkNameTV = itemView.findViewById(R.id.bookmarkNameTV);
        }

        public void bind(Recipe recipe){
            bookmarkNameTV.setText(recipe.title);
            Picasso.get().load(recipe.photo).into(bookmarkImageIV);
            removeBookmarkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.delButtonClick(recipe.id);
                }
            });
        }
    }

    public interface ClickListener{
        void delButtonClick(int recipeID);
    }
}
