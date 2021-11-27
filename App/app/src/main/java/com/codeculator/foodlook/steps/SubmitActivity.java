package com.codeculator.foodlook.steps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.codeculator.foodlook.R;

public class SubmitActivity extends AppCompatActivity {
    ImageButton[] rateStars;
    Button ratingSubmitBtn, cancelBtn;
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
        ratingSubmitBtn = findViewById(R.id.ratingSubmitBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        for (int i = 0; i < 5; i++) {
            rateStars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleStars(view.getId());
                }
            });
        }

        ratingSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSubmit();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void doSubmit(){

    }

    public void toggleStars(int itemID){
        for (int i = 0; i < 5; i++) {
            if(rateStars[i].getId() == itemID){
                for (int j = i; j > -1; j--) {
                    rateStars[j].setImageResource(R.drawable.ic_baseline_star_72);
                }
                for (int k = i+1; k < 5; k++) {
                    rateStars[k].setImageResource(R.drawable.ic_baseline_star_white_72);
                }
                break;
            }
        }
    }
}