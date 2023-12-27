package com.example.chatit.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatit.R;
import com.example.chatit.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText emailEt, passEt ;
    Button register_btn, login_btn ;
    CheckBox checkBox ;
    ProgressBar progressBar ;
    FirebaseAuth mAuth ;
    TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Providing Reference of Edit text
        emailEt = findViewById(R.id.login_email_et);
        passEt = findViewById(R.id.login_password_et);
        forgotPass = findViewById(R.id.tv_forgot_pass);

        // Providing Reference of Buttons
        register_btn = findViewById(R.id.btn_login_to_signup);
        login_btn = findViewById(R.id.btn_login);

        // Providing Reference of Checkbox
        checkBox = findViewById(R.id.login_checkbox);

        // Providing Reference of progressbar
        progressBar = findViewById(R.id.progress_login);

        // Providing Reference of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEt.getText().toString();
                String pass = passEt.getText().toString();

                if (!TextUtils.isEmpty(email) ||!TextUtils.isEmpty(pass) ){
                        progressBar.setVisibility(View.VISIBLE);

                        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        sendToMain();
                                    }else{
                                        String error = Objects.requireNonNull(task.getException()).toString();
                                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });

                }else {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String email = emailEt.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);

                builder.setTitle("Reset Password").setMessage("Are you sure to Reset Password")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(LoginActivity.this, "Reset link sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                builder.create();
                builder.show();

            }
        });

    }

    private void sendToMain() {
        Intent intent = new Intent(LoginActivity.this, SplashScreen.class);
        startActivity(intent);
        finish();
    }


    // When you open app again it check you already registered user or not
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        // When you already a registered user in that case you directly redirected to the main screen
//        if (user!=null)
//            sendToMain();
//    }
}