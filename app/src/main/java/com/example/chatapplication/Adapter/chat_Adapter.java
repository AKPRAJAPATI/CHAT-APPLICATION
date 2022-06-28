package com.example.chatapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.Model.Model;
import com.example.chatapplication.R;
import com.example.chatapplication.chatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_Adapter extends RecyclerView.Adapter<chat_Adapter.chatViewHolder> {
    Context context;
    ArrayList<Model> arrayList;
    String myImage;

    public chat_Adapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new chatViewHolder(LayoutInflater.from(context).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull chatViewHolder holder, int position) {
        Model model = arrayList.get(position);
        holder.userName.setText(model.getName());
        holder.userStatus.setText(model.getStatus());
        Picasso.get().load(model.getImage_url()).into(holder.userProfile);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                  myImage = snapshot.child("image_url").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, chatActivity.class);
                intent.putExtra("u_name", model.getName());
                intent.putExtra("u_img", model.getImage_url());
                intent.putExtra("u_userid", model.getUserId());
                intent.putExtra("myImage",myImage);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class chatViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView userProfile;
        private TextView userName;
        private TextView userStatus;

        public chatViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.userListImageView);
            userName = itemView.findViewById(R.id.userListname);
            userStatus = itemView.findViewById(R.id.userListstatus);

        }
    }
}
