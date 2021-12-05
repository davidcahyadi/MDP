package com.codeculator.foodlook.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ActionBarDrawerToggle actionBarDrawerToggle;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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
        MenuItem menuItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.getQueryHint("Type here to search");
//        searchView.setOnQueryTextListener(new );
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item1){
            Toast.makeText(getBaseContext(), "Item 1", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.item2){
            Toast.makeText(getBaseContext(), "Item 2", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.item3){
            Toast.makeText(getBaseContext(), "Item 3", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.item4){
            Toast.makeText(getBaseContext(), "Item 4", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.item5){
            Toast.makeText(getBaseContext(), "Item 5", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}