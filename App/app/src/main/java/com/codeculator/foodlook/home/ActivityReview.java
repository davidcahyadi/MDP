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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityReview extends AppCompatActivity {

    RecyclerView selected_review, comment_rview;
    EditText et_comments;
    Button button_send;
    ArrayList<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        comments = new ArrayList<>();

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
            int id = ((Review) getIntent().getParcelableExtra("review")).recipe_id;
            getComments(id);
            CommentAdapter ca = new CommentAdapter(this, comments);
            comment_rview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            comment_rview.setAdapter(ca);
        }

        button_send.setOnClickListener(view -> {
            if(et_comments.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Comments cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else{
                int user_id = 1;
                int id = ((Review) getIntent().getParcelableExtra("review")).id;
                Comment c = new Comment(et_comments.getText().toString(), user_id);
                PrefHelper helper = new PrefHelper(this);
                Call<String> call = RetrofitApi.getInstance().getCommentService().addComment(id, c,helper.getAccess());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Comment Posted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Comment Failed to be posted :(", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void getComments(int recipe_id){
        Call<ArrayList<Comment>> call = RetrofitApi.getInstance().getCommentService().getAllComment(recipe_id);
        call.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                if(response.isSuccessful()){
                    comments = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

            }
        });
    }
}