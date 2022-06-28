package com.example.chatapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.Adapter.messageAdapter;
import com.example.chatapplication.Model.message;
import com.example.chatapplication.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    private messageAdapter adapter;
    private ArrayList<message> arrayList;


    private String name;
    private String userImage,myImage;
    private String userid;

    String SENDER_ROOM;
    String RECEIVER_ROOM;

    private message msg,msg2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //////////////////////////////toolbar work end///////////////////////////////////////////////

        name = getIntent().getStringExtra("u_name");
        userImage = getIntent().getStringExtra("u_img");
        myImage = getIntent().getStringExtra("myImage");
        userid = getIntent().getStringExtra("u_userid");

        SENDER_ROOM = auth.getUid() + userid;
        RECEIVER_ROOM = userid + auth.getUid();

        binding.userName.setText(name);
        Picasso.get().load(userImage).into(binding.userprofileImage);

        //////////////////////////////toolbar work end///////////////////////////////////////////////

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.userMessage.getText().toString().equals("")) {
                    Toast.makeText(chatActivity.this, "Enter any message", Toast.LENGTH_SHORT).show();
                } else {
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String myDate = dateFormat.format(date);

                      msg = new message();
                    msg.setMessage(binding.userMessage.getText().toString());
                    msg.setUserid(auth.getUid());
                    msg.setTime(myDate);


                    databaseReference.child("chats").child(SENDER_ROOM).child("message").push().setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                databaseReference.child("chats").child(RECEIVER_ROOM).child("message").push().setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        binding.userMessage.setText("");
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        binding.chatRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecyclerview.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        adapter = new messageAdapter(getApplicationContext(), arrayList);
        getOurChats();
        binding.chatRecyclerview.setAdapter(adapter);

    }

    private void getOurChats() {
        databaseReference.child("chats").child(SENDER_ROOM).child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    message msg = dataSnapshot.getValue(message.class);
                    arrayList.add(msg);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}