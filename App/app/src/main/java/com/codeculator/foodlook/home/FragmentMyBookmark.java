package com.codeculator.foodlook.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.BookmarkAdapter;
import com.codeculator.foodlook.databinding.FragmentMyBookmarkBinding;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.model.LoggedIn;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.services.RetrofitApi;
import com.codeculator.foodlook.services.response.BasicResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMyBookmark#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMyBookmark extends Fragment {

    FragmentMyBookmarkBinding binding;
    ArrayList<Recipe> bookmarks;
    BookmarkAdapter adapter;
    PrefHelper prefHelper;

    public FragmentMyBookmark() {
        // Required empty public constructor
    }

    public static FragmentMyBookmark newInstance(String param1, String param2) {
        FragmentMyBookmark fragment = new FragmentMyBookmark();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentMyBookmarkBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Bookmarks");
        prefHelper = new PrefHelper((AppCompatActivity) getActivity());
        binding.rvBookmark.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        loadBookmark();
    }

    public void loadBookmark(){
        binding.loadingSpinner.setVisibility(View.VISIBLE);
        binding.textView10.setVisibility(View.VISIBLE);

        Call<ArrayList<Recipe>> call = RetrofitApi.getInstance().getRecipeService().getBookmarks(prefHelper.getAccess());
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if(response.isSuccessful()){
                    bookmarks = new ArrayList<>();
                    bookmarks.addAll(response.body());
                    adapter = new BookmarkAdapter(getActivity(), getParentFragmentManager());
                    adapter.setRecipes(bookmarks);
                    adapter.setClickListener(new BookmarkAdapter.ClickListener() {
                        @Override
                        public void delButtonClick(int recipeID) {
                            removeBookmark(recipeID);
                        }
                    });
                    binding.rvBookmark.setAdapter(adapter);
                    binding.loadingSpinner.setVisibility(View.INVISIBLE);
                    binding.textView10.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                binding.loadingSpinner.setVisibility(View.INVISIBLE);
                binding.textView10.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void removeBookmark(int recipeID){
        Call<BasicResponse> call = RetrofitApi.getInstance().getRecipeService().removeBookmark(recipeID,
                prefHelper.getAccess());
        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Item successfully removed from bookmark", Toast.LENGTH_SHORT).show();
                    for(Recipe r: bookmarks){
                        if(r.id == recipeID) {
                            bookmarks.remove(r);
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {

            }
        });
    }
}