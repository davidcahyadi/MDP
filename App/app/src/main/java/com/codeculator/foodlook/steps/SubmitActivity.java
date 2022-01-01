package com.codeculator.foodlook.steps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.services.RetrofitApi;
import com.codeculator.foodlook.services.admin.AdminDeleteResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitActivity extends AppCompatActivity {
    ImageButton[] rateStars;
    Button ratingSubmitBtn, cancelBtn;
    EditText reviewDescription;
    int rateScore = 0;
    private int recipeID;
    PrefHelper prefHelper;

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
        reviewDescription = findViewById(R.id.review_description);
        recipeID = getIntent().getIntExtra("recipe",0);
        prefHelper = new PrefHelper(this);

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
                finish();
            }
        });
    }

    public void doSubmit(){
        Call<String> call = RetrofitApi.getInstance().getReviewInterface().addReviews(rateScore,
                reviewDescription.getText().toString(), recipeID, prefHelper.getAccess());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getBaseContext(), "Your review has been successfully submitted!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Error submitting: " + t.getMessage());
            }
        });

    }

    public void toggleStars(int itemID){
        for (int i = 0; i < 5; i++) {
            if(rateStars[i].getId() == itemID){
                rateScore = i;
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