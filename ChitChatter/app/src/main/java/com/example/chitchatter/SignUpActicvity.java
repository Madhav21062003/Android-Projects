package com.example.chitchatter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chitchatter.Models.Users;
import com.example.chitchatter.databinding.ActivitySignUpActicvityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActicvity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseDatabase database ;
    ActivitySignUpActicvityBinding binding ;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpActicvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //  Removing  Action Bar from Signup Activity
        getSupportActionBar().hide();

        // auth is used of taking email saving
        auth = FirebaseAuth.getInstance();

        // databsew use to save editext values
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActicvity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(), binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Users user = new Users(binding.etUserName.getText().toString(), binding.etEmail.getText().toString(), binding.etPassword.getText().toString());

                             String id = task.getResult().getUser().getUid();
                             database.getReference().child("Users").child(id).setValue(user);

                            Toast.makeText(SignUpActicvity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignUpActicvity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        binding.tvAlredyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActicvity.this, signInActivity.class);
                startActivity(intent);
            }
        });




     }
}