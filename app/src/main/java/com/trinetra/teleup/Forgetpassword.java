package com.trinetra.teleup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.trinetra.teleup.databinding.ActivityForgetpasswordBinding;

public class Forgetpassword extends AppCompatActivity {

    ActivityForgetpasswordBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetpasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();

        binding.tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgetpassword.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuereset();
            }
        });
    }

    void continuereset()
    {
        if(binding.emailid.getText().toString().isEmpty())
        {
            binding.emailid.setError("Required");
            return;
        }
        auth.sendPasswordResetEmail(binding.emailid.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Forgetpassword.this, "Mail sent to your Mail ID", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Forgetpassword.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}