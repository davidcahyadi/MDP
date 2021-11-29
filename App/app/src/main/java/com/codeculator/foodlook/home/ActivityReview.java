package com.codeculator.foodlook.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.ReviewAdapter;
import com.codeculator.foodlook.model.Review;

import java.util.ArrayList;

public class ActivityReview extends AppCompatActivity {

    RecyclerView selected_review, comment_rview;
    EditText et_comments;
    Button button_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        selected_review = findViewById(R.id.selected_review);
        comment_rview = findViewById(R.id.comment_rview);
        et_comments = findViewById(R.id.et_comments);
        button_send = findViewById(R.id.button_send);
        //todo lanjutin ambil comments dari recipe yang dikirim
    }
}