package com.codeculator.foodlook.services;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public abstract class FirebaseUpload<T> {
    public StorageReference storageReference;
    public DatabaseReference databaseReference;
    private Uri imageUri;
    private String savePath = "";
    private Context context;
    private StorageTask uploadTask;
    private ActivityResultLauncher<Intent> launcher;
    private AppCompatActivity app;
    private T obj;

    public FirebaseUpload(AppCompatActivity app, Context context, String savePath) {
        this.app = app;
        this.context = context;
        this.savePath = savePath;

        storageReference = FirebaseStorage.getInstance().getReference(this.savePath);
        databaseReference = FirebaseDatabase
                .getInstance("https://foodlook-af12b-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(this.savePath);

        launcher = app.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        onSuccessChooseImage(result);
                    }
                }
        );
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public void save() {
        if(uploadTask != null && uploadTask.isInProgress()){
            Toast.makeText(context, "Upload in progress", Toast.LENGTH_SHORT).show();
        }
        else{
            if(imageUri != null) {
                StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageUri));
                uploadTask = fileRef.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                bar.setProgress(0);
//                            }
//                        }, 500);

                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        onSuccessUpload(uri);
                                        String uploadId = databaseReference.push().getKey();
                                        databaseReference.child(uploadId).setValue(obj);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                        bar.setProgress((int) progress);
                            }
                        });
            }
        }

    }

    public void load() {

    }

    public String getFileExt(Uri uri){
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void openFileChooserDialog(View view){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(i);
    }

    public abstract void onSuccessUpload(Uri uri);

    public abstract void onSuccessChooseImage(ActivityResult result);
}
