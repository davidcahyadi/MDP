package com.codeculator.foodlook.home;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.databinding.ActivityChangeProfileBinding;
import com.codeculator.foodlook.firebase.Upload;
import com.codeculator.foodlook.firebase.UploadUserImage;
import com.codeculator.foodlook.model.LoggedIn;
import com.codeculator.foodlook.services.FirebaseUpload;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

public class ActivityChangeProfile extends AppCompatActivity {
    ActivityChangeProfileBinding binding;
    FirebaseUpload<UploadUserImage> firebaseUpload;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.setTitle("Change Profile");

        firebaseUpload = new FirebaseUpload<UploadUserImage>(this, this, "uploads/users") {
            @Override
            public void onSuccessUpload(Uri uri) {
                Toast.makeText(ActivityChangeProfile.this, "Change profile successful", Toast.LENGTH_LONG).show();

                // class untuk firebase database
                UploadUserImage upload = new UploadUserImage(LoggedIn.user.getId(), uri.toString());
                firebaseUpload.setObj(upload);
            }

            @Override
            public void onSuccessChooseImage(ActivityResult result) {
                Intent data = result.getData();

                if(data != null){
                    imageUri = data.getData();
                    firebaseUpload.setImageUri(imageUri);
                    Picasso.get().load(imageUri).into(binding.imageViewChangeProfilePic);
                    firebaseUpload.save();
                }
            }
        };

        firebaseUpload.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.imageViewChangeProfilePic.setImageResource(R.drawable.john_xina);
                int ctr = 0;
                UploadUserImage deletedUpload = new UploadUserImage();
                for (DataSnapshot postSnapShot : snapshot.getChildren()){
                    UploadUserImage upload = postSnapShot.getValue(UploadUserImage.class);
                    upload.key = postSnapShot.getKey();
                    if(upload.userId == LoggedIn.user.getId()){
                        ctr++;
                        if(ctr == 1){
                            deletedUpload = upload;
                        }
                        Picasso.get().load(upload.imageUrl).into(binding.imageViewChangeProfilePic);
                    }
                }
                if(deletedUpload.userId == LoggedIn.user.getId() && ctr == 2){
                    StorageReference imageRef = firebaseUpload.storageReference.getStorage().getReferenceFromUrl(deletedUpload.imageUrl);
                    UploadUserImage finalDeletedUpload = deletedUpload;
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            firebaseUpload.databaseReference.child(finalDeletedUpload.key).removeValue();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnChooseImage.setOnClickListener(v -> {
            firebaseUpload.openFileChooserDialog(v);
        });
    }
}