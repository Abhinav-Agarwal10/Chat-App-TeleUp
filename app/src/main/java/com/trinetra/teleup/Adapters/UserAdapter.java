package com.trinetra.teleup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.trinetra.teleup.ChatDetailActivity;
import com.trinetra.teleup.Models.UsersModel;
import com.trinetra.teleup.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ArrayList<UsersModel> list;
    Context context;

    public UserAdapter(ArrayList<UsersModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_user_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsersModel users =list.get(position);
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.profile).into(holder.img);
        holder.name.setText(users.getUsername());
        holder.lastmessage.setText("");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("UserID", users.getUserID());
                intent.putExtra("Profilepic", users.getProfilepic());
                intent.putExtra("UserName", users.getUsername());
                context.startActivity(intent);
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

            img =itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.userName);
            lastmessage = itemView.findViewById(R.id.lastmessage);
        }
    }
}
