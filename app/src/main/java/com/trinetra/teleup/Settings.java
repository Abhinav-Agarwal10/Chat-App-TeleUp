package com.trinetra.teleup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.trinetra.teleup.Models.UsersModel;
import com.trinetra.teleup.databinding.ActivitySettingsBinding;

import java.util.HashMap;

public class Settings extends AppCompatActivity {

    ActivitySettingsBinding binding;

    FirebaseStorage storage;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.statusbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etUsername.getText().toString().isEmpty())
                {
                    binding.etUsername.setError("Required");
                }
                else {
                    String username = binding.etUsername.getText().toString();
                    String about = binding.etAbout.getText().toString();

                    HashMap<String, Object> obj = new HashMap<>();
                    obj.put("username", username);
                    obj.put("About", about);

                    database.getReference().child("Users").child(auth.getUid()).updateChildren(obj);
                }
            }
        });

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersModel users =snapshot.getValue(UsersModel.class);
                Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.profile).into(binding.profile);
                Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.statusprofilepic).into(binding.statuspic);
                binding.etUsername.setText(users.getUsername());
                binding.etAbout.setText(users.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 56);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null)
        {
            Uri sfile = data.getData();
            binding.profile.setImageURI(sfile);
            binding.statuspic.setImageURI(sfile);

            final StorageReference reference = storage.getReference().child("Profilepictures").child(auth.getUid());

            reference.putFile(sfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Settings.this, "Image updated", Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(auth.getUid())
                                    .child("profilepic").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }
}