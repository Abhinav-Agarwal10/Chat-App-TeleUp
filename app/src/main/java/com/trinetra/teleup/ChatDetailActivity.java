package com.trinetra.teleup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.trinetra.teleup.Adapters.MessageAdapter;
import com.trinetra.teleup.Models.ChatModel;
import com.trinetra.teleup.Models.MessageModel;
import com.trinetra.teleup.Models.UsersModel;
import com.trinetra.teleup.databinding.ActivityChatDetailBinding;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;

    FirebaseDatabase database;
    FirebaseAuth auth;

    String receiverid, username, profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String senderid = auth.getUid();

        receiverid = getIntent().getStringExtra("UserID");
        username = getIntent().getStringExtra("UserName");
        profilepic = getIntent().getStringExtra("Profilepic");

        binding.userName.setText(username);
        Picasso.get().load(profilepic).placeholder(R.drawable.profile).into(binding.profileImage);


        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openstatus();
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openstatus();
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final MessageAdapter adapter = new MessageAdapter(messageModels, this, receiverid);
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        binding.chatRecyclerView.setLayoutManager(manager);

        final String senderRoom = senderid + receiverid;
        final String receiverRoom = receiverid + senderid;

        database.getReference().child("Chats")
                        .child(senderRoom).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            MessageModel model = dataSnapshot.getValue(MessageModel.class);
                            model.setMessageid(dataSnapshot.getKey());

                            messageModels.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        binding.chatRecyclerView.scrollToPosition(messageModels.size()-1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersModel usersModel = snapshot.getValue(UsersModel.class);
                binding.senusername.setText(usersModel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.editmessage.getText().toString().isEmpty())
                {
                    String message = binding.editmessage.getText().toString();
                    String name = binding.senusername.getText().toString();
                    final MessageModel model = new MessageModel(senderid, message, name);
                    model.setTimestamp(new Date().getTime());
                    binding.editmessage.setText("");
                    ChatModel chatModel = new ChatModel();
                    chatModel.setTimestamp(new Date().getTime());

                    database.getReference().child("UsersChat").child(auth.getUid()).child(receiverid).setValue(chatModel);
                    database.getReference().child("UsersChat").child(receiverid).child(auth.getUid()).setValue(chatModel);

                    database.getReference().child("Chats")
                            .child(senderRoom)
                            .push()
                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.getReference().child("Chats")
                                            .child(receiverRoom)
                                            .push()
                                            .setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });
                }
            }
        });


    }

    void openstatus()
    {
        Intent intent = new Intent(ChatDetailActivity.this, Status.class);
        intent.putExtra("Username", username);
        intent.putExtra("Profilepic", profilepic);
        intent.putExtra("receiverid", receiverid);
        startActivity(intent);
    }
}