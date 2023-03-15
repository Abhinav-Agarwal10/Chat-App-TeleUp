package com.trinetra.teleup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.trinetra.teleup.Models.UsersModel;
import com.trinetra.teleup.databinding.ActivityStatusBinding;

public class Status extends AppCompatActivity {

    ActivityStatusBinding binding;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();

        String username = getIntent().getStringExtra("Username");
        String profilepic = getIntent().getStringExtra("Profilepic");
        String receiverid = getIntent().getStringExtra("receiverid");

//        binding.statusname.setText(username);
//        Picasso.get().load(profilepic).placeholder(R.drawable.statusprofilepic).into(binding.statuspic);

        database.getReference().child("Users").child(receiverid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersModel users = snapshot.getValue(UsersModel.class);
                Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.statusprofilepic).into(binding.statuspic);
                binding.statusname.setText(users.getUsername());
                binding.statusabout.setText(users.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.statusbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}