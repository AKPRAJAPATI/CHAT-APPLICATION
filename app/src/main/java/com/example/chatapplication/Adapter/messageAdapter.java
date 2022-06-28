package com.example.chatapplication.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.Model.message;
import com.example.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<message> arrayList;

    private int ITEM_SEND = 1;
    private int ITEM_RECEIVE = 2;

    public messageAdapter(Context context, ArrayList<message> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (viewType == ITEM_SEND){
           return new sendMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.send_message,parent,false));
       }else{
           return new recieveMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.recieve_message,parent,false));
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        message msg = arrayList.get(position);
        if (holder.getClass() == sendMessageViewHolder.class){
            sendMessageViewHolder sendMessageViewHolder = (messageAdapter.sendMessageViewHolder) holder;
            sendMessageViewHolder.userchat.setText(msg.getMessage());
            sendMessageViewHolder.usertime.setText(msg.getTime());

        }else{
            recieveMessageViewHolder hold = (recieveMessageViewHolder) holder;
            hold.userchat.setText(msg.getMessage());
            hold.usertime.setText(msg.getTime());

        }
    }

    @Override
    public int getItemViewType(int position) {
        message message = arrayList.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getUserid())){
            return ITEM_SEND;
        }else{
            return ITEM_RECEIVE;
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class sendMessageViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userProfilePic;
        TextView userchat;
        TextView usertime;
        public sendMessageViewHolder(@NonNull View itemView) {
            super(itemView);


            userchat = itemView.findViewById(R.id.myMessageSend_Message);
            usertime = itemView.findViewById(R.id.myMessageSendTime);


        }
    }
     public class recieveMessageViewHolder extends RecyclerView.ViewHolder{
        CircleImageView userProfilePic;
        TextView userchat;
        TextView usertime;

        public recieveMessageViewHolder(@NonNull View itemView) {
             super(itemView);

            userchat = itemView.findViewById(R.id.myMessageReceive_Message);
            usertime = itemView.findViewById(R.id.myMessageReceiveTime);
         }
     }

}
