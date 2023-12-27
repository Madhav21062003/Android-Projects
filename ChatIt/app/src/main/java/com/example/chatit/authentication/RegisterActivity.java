package com.example.chatit.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.example.chatit.CreateProfile;
import com.example.chatit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEt, passEt, confirm_pass ;
    Button register_btn, login_btn ;
    CheckBox checkBox ;
    ProgressBar progressBar ;
    FirebaseAuth mAuth ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Providing Reference of Edit text
        emailEt = findViewById(R.id.register_email_et);
        passEt = findViewById(R.id.register_password_et);
        confirm_pass = findViewById(R.id.register_confirm_password_et);

        // Providing Reference of Buttons
        register_btn = findViewById(R.id.btn_register);
        login_btn = findViewById(R.id.btn_signup_to_login);

        // Providing Reference of Checkbox
        checkBox = findViewById(R.id.register_checkbox);

        // Providing Reference of progressbar
        progressBar = findViewById(R.id.progress_register);

        // Providing Reference of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    passEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    passEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEt.getText().toString();
                String pass = passEt.getText().toString();
                String confirm_password = confirm_pass.getText().toString();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass) || !TextUtils.isEmpty(confirm_password)){

                    if(pass.equals(confirm_password)){
                        // Make the progress bar visible
                        progressBar.setVisibility(View.VISIBLE);


                        // Register user into the firebase database
                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    sendToMain();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }else{
                                    String error = task.getException().toString();
                                    Toast.makeText(RegisterActivity.this, error , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "Password and confirm password is not matching", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Please fill all the entries", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Go to Login Screen When clock on logon Button
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // This function sends you to Register  Screen to HomeScreen of the app
    private void sendToMain() {
        Intent intent = new Intent(RegisterActivity.this, CreateProfile.class);
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