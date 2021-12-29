package com.codeculator.foodlook.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityHomeBinding;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.helper.ResultLauncherHelper;
import com.codeculator.foodlook.model.User;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ActionBarDrawerToggle actionBarDrawerToggle;
    ActivityHomeBinding binding;
    ResultLauncherHelper launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        launcher = new ResultLauncherHelper(this);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        // drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.myDrawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        binding.myDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.leftNav.setNavigationItemSelectedListener(this);

        binding.navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return bottomNavSelected(item);
            }
        });
        binding.navigation.setBackground(null);
        binding.navigation.getMenu().getItem(2).setEnabled(false);

        binding.navigation.setSelectedItemId(R.id.menu_catalog);
        getSupportFragmentManager().addFragmentOnAttachListener(new FragmentOnAttachListener() {
            @Override
            public void onAttachFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
                if(fragment instanceof FragmentMyRecipe){
                    FragmentMyRecipe f = (FragmentMyRecipe) fragment;
                    f.setFmrl(new FragmentMyRecipe.FragmentMyRecipeListener() {
                        @Override
                        public void gotoDetail() {
                            //todo gotodetail
                        }
                    });
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private boolean bottomNavSelected(@NonNull MenuItem item){
        Fragment f;
        switch (item.getItemId()){
            case R.id.menu_catalog:
                f = new FragmentCatalog();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
                return true;
            case R.id.menu_my_review:
                f = new FragmentMyReview();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
                return true;
            case R.id.menu_bookmarks:
                //TODO: implement Fragments
                return true;
            case R.id.menu_my_recipe:
                f = new FragmentMyRecipe();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, f).commit();
                return true;
        }
        return false;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
//        MenuItem menuItem = menu.findItem(R.id.item_search);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Type here to search");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                Toast.makeText(ActivityHome.this, s, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item1){
            Toast.makeText(getBaseContext(), "Item 1", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.sidebar_menu_logout){
            logout();
        }
        return false;
    }

    private void logout(){
        setResult(User.LOGOUT,new Intent());
        finish();
    }
}