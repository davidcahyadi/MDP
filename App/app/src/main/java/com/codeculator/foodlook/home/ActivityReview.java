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
import com.codeculator.foodlook.helper.PrefHelper;
import com.codeculator.foodlook.model.Comment;
import com.codeculator.foodlook.model.Review;
import com.codeculator.foodlook.services.RetrofitApi;
import com.codeculator.foodlook.services.response.BasicResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityReview extends AppCompatActivity {

    RecyclerView selected_review, comment_rview;
    EditText et_comments;
    Button button_send;
    ArrayList<Review> comments;
    CommentAdapter ca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        comments = new ArrayList<>();

        selected_review = findViewById(R.id.selected_review);
        comment_rview = findViewById(R.id.comment_rview);
        et_comments = findViewById(R.id.et_comments);
        button_send = findViewById(R.id.button_send);

        if(getIntent().getParcelableExtra("review") != null){
            // review yang terpilih
            ArrayList<Review> reviews = new ArrayList<>();
            Review review = getIntent().getParcelableExtra("review");
            reviews.add(review);
            ReviewAdapter ra = new ReviewAdapter(reviews);
            selected_review.setLayoutManager(new LinearLayoutManager(getApplication()));
            selected_review.setAdapter(ra);

            //ambil comment
            int id = review.id;
            ca = new CommentAdapter(this, comments);
            getComments(id);
            comment_rview.setLayoutManager(new LinearLayoutManager(getApplication()));
            comment_rview.setAdapter(ca);
        }

        button_send.setOnClickListener(view -> {
            if(et_comments.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Comments cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else{
                int id = ((Review) getIntent().getParcelableExtra("review")).id;
                PrefHelper helper = new PrefHelper(this);
                Call<BasicResponse> call = RetrofitApi.getInstance().getCommentService().addComment(id, et_comments.getText().toString(),helper.getAccess());
                call.enqueue(new Callback<BasicResponse>() {
                    @Override
                    public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Comment Posted", Toast.LENGTH_SHORT).show();
                            getComments(id);
                        }
                    }

                    @Override
                    public void onFailure(Call<BasicResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Comment Failed to be posted :(", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void getComments(int review_id){
        Call<ArrayList<Review>> call = RetrofitApi.getInstance().getCommentService().getAllComment(review_id);
        call.enqueue(new Callback<ArrayList<Review>>() {
            @Override
            public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                if(response.isSuccessful()){
                    comments.clear();
                    comments.addAll(response.body());
                    System.out.println(comments);
                    for (Review comment :
                            comments) {
                        System.out.println(comment.description);
                    }
                    ca.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Review>> call, Throwable t) {

            }
        });
    }
}