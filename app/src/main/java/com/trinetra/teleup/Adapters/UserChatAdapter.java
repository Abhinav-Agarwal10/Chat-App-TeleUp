package com.trinetra.teleup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.trinetra.teleup.ChatDetailActivity;
import com.trinetra.teleup.Models.ChatModel;
import com.trinetra.teleup.Models.UsersModel;
import com.trinetra.teleup.R;

import java.util.ArrayList;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder> {

    ArrayList<ChatModel> list;

    Context context;
    FirebaseDatabase database;

    public UserChatAdapter(ArrayList<ChatModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public UserChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_user_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserChatAdapter.ViewHolder holder, int position) {
        ChatModel chatModel = list.get(position);
        String uid = chatModel.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UsersModel usersModel = snapshot.getValue(UsersModel.class);
                                Picasso.get().load(usersModel.getProfilepic()).placeholder(R.drawable.profile).into(holder.img);
                                holder.name.setText(usersModel.getUsername());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getUid() + uid)
                        .orderByChild("timestamp")
                                .limitToLast(1)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.hasChildren())
                                                {
                                                    for(DataSnapshot snapshot1: snapshot.getChildren())
                                                    {
                                                        holder.lastmessage.setText(snapshot1.child("message").getValue().toString());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UsersModel users = snapshot.getValue(UsersModel.class);
                                Intent intent = new Intent(context, ChatDetailActivity.class);
                                intent.putExtra("UserID", snapshot.getKey());
                                intent.putExtra("Profilepic", users.getProfilepic());
                                intent.putExtra("UserName", users.getUsername());
                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name, lastmessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.userName);
            lastmessage = itemView.findViewById(R.id.lastmessage);
        }
    }
}
