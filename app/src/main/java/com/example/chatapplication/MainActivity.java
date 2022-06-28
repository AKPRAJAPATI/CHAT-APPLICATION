package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapplication.Adapter.chat_Adapter;
import com.example.chatapplication.Model.Model;
import com.example.chatapplication.authentication.login_register_screen;
import com.example.chatapplication.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private chat_Adapter adapter;
    private ArrayList<Model> arrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        //check user register or not
        if (firebaseUser == null) {
            startActivity(new Intent(getApplicationContext(), login_register_screen.class));
        }

        ////////////////////////////////////// MAIN WORK START HERE /////////////////////////////////////////////
        binding.recyclerviewUserList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewUserList.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        adapter = new chat_Adapter(getApplicationContext(),arrayList);
        get_our_userList();
        binding.recyclerviewUserList.setAdapter(adapter);
        /////////////ALL METHOD IS HERE/////////////////////////////////////////
        getOurprofileData();
        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });

        /////////////ALL METHOD IS HERE/////////////////////////////////////////



    }

    private void get_our_userList() {

        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    Model mak = data.getValue(Model.class);

                    if (!auth.getUid().equals(mak.getUserId())){
                        arrayList.add(mak);
                    }


                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getOurprofileData() {
        databaseReference.child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String imageUrl = snapshot.child("image_url").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                  //  String imageUrl = snapshot.child("").getValue(String.class); ///////check online or not

                    binding.userName.setText(name);
                    Picasso.get().load(imageUrl).into(binding.userprofileImage);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}