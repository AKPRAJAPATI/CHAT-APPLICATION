package com.example.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapplication.Model.updateModel;
import com.example.chatapplication.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private Uri IMAGE_URI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                    String userImage = snapshot.child("image_url").getValue(String.class);
                    String userName = snapshot.child("name").getValue(String.class);
                    String userStatus = snapshot.child("status").getValue(String.class);

                    Picasso.get().load(userImage).into(binding.profileImage);
                    binding.chatName.setText(userName);
                    binding.chatStatus.setText(userStatus);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        binding.profileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.chatName.getText().toString().equals("")) {
                    Toast.makeText(ProfileActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
                } else if (binding.chatStatus.getText().toString().equals("")) {
                    Toast.makeText(ProfileActivity.this, "Enter your status", Toast.LENGTH_SHORT).show();
                } else {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name",binding.chatName.getText().toString());
                    hashMap.put("status",binding.chatStatus.getText().toString());

                    firebaseDatabase.child("Users").child(auth.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this, "Update Success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 || resultCode == RESULT_OK) {
            IMAGE_URI = data.getData();
            binding.profileImage.setImageURI(IMAGE_URI);
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Update Image");
            progressDialog.show();

            storageReference.child("Profile Image").child(auth.getUid()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image_uri = uri.toString();

                            firebaseDatabase.child("Users").child(auth.getUid()).child("image_url").setValue(image_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                }
            });


        }
    }
}