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
import com.trinetra.teleup.Adapters.UserAdapter;
import com.trinetra.teleup.Models.UsersModel;
import com.trinetra.teleup.R;
import com.trinetra.teleup.databinding.FragmentUsersBinding;

import java.util.ArrayList;

public class Users extends Fragment {

    public Users() {
        // Required empty public constructor
    }

    FragmentUsersBinding binding;
    ArrayList<UsersModel> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);

        UserAdapter usersAdapter = new UserAdapter(list, getContext());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.chatRecyclerView.setAdapter(usersAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(manager);


        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    UsersModel usersModel =dataSnapshot.getValue(UsersModel.class);
                    usersModel.setUserID(dataSnapshot.getKey());
                    if(usersModel.getUserID().equals(auth.getUid())) continue;
                    list.add(usersModel);
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return binding.getRoot();
    }
}