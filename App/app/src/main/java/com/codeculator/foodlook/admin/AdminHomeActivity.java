package com.codeculator.foodlook.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityAdminHomeBinding;
import com.codeculator.foodlook.databinding.ActivityHomeBinding;
import com.codeculator.foodlook.helper.ResultLauncherHelper;
import com.codeculator.foodlook.home.FragmentCatalog;
import com.codeculator.foodlook.home.FragmentMyRecipe;
import com.codeculator.foodlook.home.FragmentMyReview;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class AdminHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActivityAdminHomeBinding binding;
    ResultLauncherHelper launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        launcher = new ResultLauncherHelper(this);

        binding.adminNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return bottomNavSelected(item);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    private boolean bottomNavSelected(@NonNull MenuItem item){
        Fragment f;
        switch (item.getItemId()){
            case R.id.admin_menu_home:
//                f = new FragmentCatalog();
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
                //TODO: implement Fragments
                return true;
            case R.id.admin_menu_users:
                //TODO: implement Fragments
                return true;
            case R.id.admin_menu_recipes:
                //TODO: implement Fragments
                return true;
            case R.id.admin_menu_reviews:
                //TODO: implement Fragments
                return true;
        }
        return false;
    }
}