package com.trinetra.teleup.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trinetra.teleup.Adapters.UserChatAdapter;
import com.trinetra.teleup.Models.ChatModel;
import com.trinetra.teleup.databinding.FragmentChatBinding;

import java.util.ArrayList;
import java.util.Date;

public class Chat extends Fragment {

    public Chat() {
        // Required empty public constructor
    }

    FragmentChatBinding binding;

    ArrayList<ChatModel> list = new ArrayList<>();

    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);

        UserChatAdapter adapter = new UserChatAdapter(list, getContext());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        binding.chatRecyclerView.setLayoutManager(manager);

        database.getReference().child("UsersChat").child(auth.getUid()).orderByChild("timestamp")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list.clear();
                                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                                {
                                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                                    chatModel.setUid(dataSnapshot.getKey());
                                    list.add(chatModel);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

//        database.getReference().child("UsersChat").child(auth.getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        list.clear();
//                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
//                        {
//                            ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
//                            chatModel.setUid(dataSnapshot.getKey());
//                            list.add(chatModel);
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

        return binding.getRoot();
    }
}