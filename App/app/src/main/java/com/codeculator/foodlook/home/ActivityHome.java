package com.codeculator.foodlook.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.helper.PrefHelper;
import com.google.android.material.navigation.NavigationView;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView leftNav;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        leftNav = findViewById(R.id.leftNav);
        leftNav.setNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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