package com.codeculator.foodlook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codeculator.foodlook.model.User;
import com.codeculator.foodlook.services.RetrofitApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminUserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminUserDetailFragment extends Fragment {

    TextView detailUIDTV, detailNameTV, detailEmailTV, detailJoinedSinceTV, totalRecipesTV, totalReviewsTV;
    int UID;
    User selectedUser;

    public AdminUserDetailFragment() {
        // Required empty public constructor
    }

    public static AdminUserDetailFragment newInstance(String param1, String param2) {
        AdminUserDetailFragment fragment = new AdminUserDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if(b != null){
            UID = b.getInt("userID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_user_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailUIDTV = view.findViewById(R.id.detailUIDTV);
        detailNameTV = view.findViewById(R.id.detailNameTV);
        detailEmailTV = view.findViewById(R.id.detailEmailTV);
        detailJoinedSinceTV = view.findViewById(R.id.detailJoinedSinceTV);
        totalRecipesTV = view.findViewById(R.id.totalRecipesTV);
        totalReviewsTV = view.findViewById(R.id.totalReviewsTV);

        loadUserInfo();
    }

    public void loadUserInfo(){
        Call<User> call = RetrofitApi.getInstance().getAdminService().getUserByID(UID);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("Detail response: " + response.body());
                if(response.isSuccessful()){
                    selectedUser = response.body();
                    detailUIDTV.setText("#" + selectedUser.getId());
                    detailNameTV.setText(selectedUser.getName());
                    detailEmailTV.setText(selectedUser.getEmail());
                    detailJoinedSinceTV.setText(selectedUser.getCreated_at());
                    loadRecipeCount();
                    loadReviewCount();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong while loading information. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadRecipeCount(){

    }

    public void loadReviewCount(){

    }
}