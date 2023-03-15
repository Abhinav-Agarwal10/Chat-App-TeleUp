package com.trinetra.teleup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.trinetra.teleup.Models.UsersModel;
import com.trinetra.teleup.databinding.ActivitySignUpBinding;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dialog = new ProgressDialog(this);

        dialog.setTitle("Creating Account...");
        dialog.setMessage("Kindly Wait..");

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuelogin();
            }
        });
    }

    void continuelogin()
    {
        if(binding.UserName.getText().toString().isEmpty()){
            binding.UserName.setError("Required");
            return;
        }
        if(binding.UserName.getText().toString().length()>20){
            binding.UserName.setError("exceeded more than 20 characters");
            return;
        }
        if(binding.email.getText().toString().isEmpty()){
            binding.email.setError("Required");
            return;
        }
        if(binding.password.getText().toString().isEmpty()){
            binding.password.setError("Required");
            return;
        }
        dialog.show();
        auth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if(task.isSuccessful())
                        {
                            UsersModel user = new UsersModel(binding.UserName.getText().toString(), binding.email.getText().toString(), binding.password.getText().toString());
                            String userid = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(userid).setValue(user);
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(SignUp.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}