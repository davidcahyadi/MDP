package com.codeculator.foodlook.home;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityTempBinding;
import com.codeculator.foodlook.model.Recipe;
import com.codeculator.foodlook.model.Upload;
import com.codeculator.foodlook.services.FirebaseUpload;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
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