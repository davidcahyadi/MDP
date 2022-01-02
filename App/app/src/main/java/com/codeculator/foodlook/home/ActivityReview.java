package com.codeculator.foodlook.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.CommentAdapter;
import com.codeculator.foodlook.adapter.ReviewAdapter;
import com.codeculator.foodlook.model.Comment;
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

        if(getIntent().getParcelableExtra("review") != null){
            // review yang terpilih
            ArrayList<Review> reviews = new ArrayList<>();
            reviews.add(getIntent().getParcelableExtra("review"));
            ReviewAdapter ra = new ReviewAdapter(reviews);
            selected_review.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            selected_review.setAdapter(ra);

            //ambil comment
            ArrayList<Comment> comments = new ArrayList<>();
            int id = ((Review) getIntent().getParcelableExtra("review")).id;
            //todo ambil comments pake retrofit dengan id

            CommentAdapter ca = new CommentAdapter(this, comments);
            comment_rview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            comment_rview.setAdapter(ca);
        }

        button_send.setOnClickListener(view -> {
            if(et_comments.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Comments cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else{
                Comment c = new Comment(et_comments.getText().toString());
                //todo tinggal tambah ke database
            }
        });
    }
}