package com.codeculator.foodlook.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityAdminHomeBinding;
import com.codeculator.foodlook.databinding.FragmentAdminCrawlerBinding;
import com.codeculator.foodlook.helper.ResultLauncherHelper;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class AdminHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActivityAdminHomeBinding binding;
    ResultLauncherHelper launcher;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        launcher = new ResultLauncherHelper(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        binding.adminNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return bottomNavSelected(item);
            }
        });
        binding.adminNavigation.setSelectedItemId(R.id.admin_menu_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    private boolean bottomNavSelected(@NonNull MenuItem item){
        Fragment f;
        Bundle b = new Bundle();
        switch (item.getItemId()){
            case R.id.admin_menu_home:
                f = new AdminCrawlerFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_container, f).commit();
                return true;
            case R.id.admin_menu_users:
                f = new AdminListFragment();
                type = "users";
                b.putString("type", type);
                f.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_container, f).commit();
                return true;
            case R.id.admin_menu_recipes:
                f = new AdminListFragment();
                type = "recipes";
                b.putString("type", type);
                f.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_container, f).commit();
                return true;
            case R.id.admin_menu_reviews:
                f = new AdminListFragment();
                type = "reviews";
                b.putString("type", type);
                f.setArguments(b);
                getSupportFragmentManager().beginTransaction().replace(R.id.admin_container, f).commit();
                return true;
        }
        return false;
    }
}