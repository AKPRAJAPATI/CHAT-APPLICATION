package com.example.chatapplication.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.chatapplication.MainActivity;
import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.loginEmail.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Email is emtpy", Toast.LENGTH_SHORT).show();
                }else if (!binding.loginEmail.getText().toString().contains("@gmail.com")){
                    Toast.makeText(LoginActivity.this, "enter right email", Toast.LENGTH_SHORT).show();
                }else if (binding.loginpassword.getText().toString().length() <8){
                    Toast.makeText(LoginActivity.this, "Password more than 8 character", Toast.LENGTH_SHORT).show();
                }else{
                   auth.signInWithEmailAndPassword(binding.loginEmail.getText().toString(),binding.loginpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               startActivity(new Intent(getApplicationContext(), MainActivity.class));
                           }
                       }
                   });
                }
            }
        });

    }

    public void registerAccount(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        finish();
    }
}