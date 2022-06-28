package com.example.chatapplication.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapplication.MainActivity;
import com.example.chatapplication.Model.Model;
import com.example.chatapplication.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri IMAGE_URI;
    private String image_uri;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Register Account");


        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        binding.registerButtonsClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.registerName.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Brother your name is emtpy", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.registerEmail.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
                } else if (!binding.registerEmail.getText().toString().contains("@gmail.com")) {
                    Toast.makeText(RegisterActivity.this, "Please enter right email", Toast.LENGTH_SHORT).show();
                } else if (binding.registerPassword.getText().toString().length() < 8) {
                    Toast.makeText(RegisterActivity.this, "Password more than 8 character", Toast.LENGTH_SHORT).show();
                } else if (IMAGE_URI == null) {
                    Toast.makeText(RegisterActivity.this, "Please choose you email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 100);
                } else {
                    progressDialog.show();
                    saveDataInDatabase();
                }
            }
        });
    }

    private void saveDataInDatabase() {
        auth.createUserWithEmailAndPassword(binding.registerEmail.getText().toString(),binding.registerPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    storageReference.child("Profile Image").child(auth.getUid()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_uri = uri.toString();
                                    String status = "Hey there , I am using this application.";
                                    Model model = new Model(image_uri, binding.registerName.getText().toString(), binding.registerEmail.getText().toString(), binding.registerPassword.getText().toString(), auth.getUid(),status);

                                    databaseReference.child("Users").child(auth.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            }
                                        }
                                    });

                                }
                            });
                        }
                    });


                }
            }
        });
    }

    public void loginAccount(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 || resultCode == RESULT_OK) {
            IMAGE_URI = data.getData();
            binding.profileImage.setImageURI(IMAGE_URI);
        }
    }
}