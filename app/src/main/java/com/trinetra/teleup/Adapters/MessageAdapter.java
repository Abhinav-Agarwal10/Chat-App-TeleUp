package com.trinetra.teleup.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trinetra.teleup.Models.MessageModel;
import com.trinetra.teleup.Models.UsersModel;
import com.trinetra.teleup.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public MessageAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public MessageAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
        {
//            Log.d("AbhinavAgarwal", "uidse : " + messageModels.get(position).getUid());
            return SENDER_VIEW_TYPE;
        }
        else {
//            Log.d("AbhinavAgarwal", "uidre : " + messageModels.get(position).getUid());
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel =messageModels.get(position);

        if(holder.getClass() == SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).senmessage.setText(messageModel.getMessage());
            ((SenderViewHolder)holder).senname.setText(messageModel.getUsername());

//            Log.d("AbhinavAgarwal", "name: " + username);
//            Log.d("AbhinavAgarwal", "onBindViewHolder: sender");
        }
        else {
            ((ReceiverViewHolder)holder).recmessage.setText(messageModel.getMessage());
            ((ReceiverViewHolder)holder).recname.setText(messageModel.getUsername());
//            Log.d("AbhinavAgarwal", "onBindViewHolder: receiver");
        }

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senname, senmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senname = itemView.findViewById(R.id.sendername);
            senmessage = itemView.findViewById(R.id.sendermessage);
        }
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView recname, recmessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            recname = itemView.findViewById(R.id.receivername);
            recmessage = itemView.findViewById(R.id.receivermessage);
        }
    }
}
