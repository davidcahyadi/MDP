package com.codeculator.foodlook.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.OnClickListener;
import com.codeculator.foodlook.adapter.WelcomeCardAdapter.*;
import com.codeculator.foodlook.adapter.WelcomeCardAdapter;
import com.codeculator.foodlook.databinding.ActivityWelcomeBinding;
import com.codeculator.foodlook.helper.ResultLauncherHelper;
import com.codeculator.foodlook.model.WelcomeCard;

import java.util.LinkedList;
import java.util.List;

public class ActivityWelcome extends AppCompatActivity {

    ActivityWelcomeBinding binding;
    ResultLauncherHelper launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        launcher = new ResultLauncherHelper(this);

        WelcomeCardAdapter adapter = new WelcomeCardAdapter(this,createWelcomeCards());
        adapter.setOnClickListener(new OnClickListener<WelcomeCard>() {
            @Override
            public void onClick(WelcomeCard data) {
                if(data.getCode() == 1){
                    Toast.makeText(ActivityWelcome.this, "Food", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ActivityWelcome.this, "Search", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.viewPager.setAdapter(adapter);

    }

    private List<WelcomeCard> createWelcomeCards(){
        List<WelcomeCard> cards = new LinkedList<>();
        cards.add(new WelcomeCard("Food Catalog",R.raw.food,1));
        cards.add(new WelcomeCard("Search Food",R.raw.search,2));
        return cards;
    }
}