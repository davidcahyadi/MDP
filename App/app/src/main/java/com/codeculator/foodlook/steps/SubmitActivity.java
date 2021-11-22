package com.codeculator.foodlook.steps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.codeculator.foodlook.R;

public class SubmitActivity extends AppCompatActivity {
    ImageButton[] rateStars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        rateStars = new ImageButton[5];
        rateStars[0] = findViewById(R.id.rateStar1);
        rateStars[1] = findViewById(R.id.rateStar2);
        rateStars[2] = findViewById(R.id.rateStar3);
        rateStars[3] = findViewById(R.id.rateStar4);
        rateStars[4] = findViewById(R.id.rateStar5);
    }
}