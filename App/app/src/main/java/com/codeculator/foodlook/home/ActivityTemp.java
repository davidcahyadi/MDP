package com.codeculator.foodlook.home;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.firebase.Upload;
import com.codeculator.foodlook.services.FirebaseUpload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ActivityTemp extends AppCompatActivity {
    ImageView iv;
    private Uri imageUri;
    ProgressBar bar;
    EditText et;

    FirebaseUpload<Upload> firebaseUpload;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        iv = findViewById(R.id.imageView7);
        bar = findViewById(R.id.progressBar4);
        et = findViewById(R.id.editTextTextPersonName);

        firebaseUpload = new FirebaseUpload<Upload>(this, this,"uploads/recipes") {
            @Override
            public void onSuccessUpload(Uri uri) {
                Toast.makeText(ActivityTemp.this, "Upload successful", Toast.LENGTH_LONG).show();

                // class untuk firebase database
                Upload upload = new Upload(et.getText().toString(), uri.toString());
                firebaseUpload.setObj(upload);
            }

            @Override
            public void onSuccessChooseImage(ActivityResult result) {
                Intent data = result.getData();

                if(data != null){
                    imageUri = data.getData();
                    firebaseUpload.setImageUri(imageUri);
                    Picasso.get().load(imageUri).into(iv);
                }
            }
        };

        // for load image from firebase
        firebaseUpload.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()){
                    Upload upload = postSnapShot.getValue(Upload.class);
                    System.out.println(upload.getImageUrl());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void uploadFile(View view){
        if(imageUri != null) {
            firebaseUpload.save();
        }
    }

    public void openFileChooserDialog(View view){
        firebaseUpload.openFileChooserDialog(view);
    }
}