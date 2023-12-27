package com.example.chitchatter;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chitchatter.Models.Users;
import com.example.chitchatter.databinding.ActivitySignInBinding;
import com.example.chitchatter.databinding.ActivitySignUpActicvityBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class signInActivity extends AppCompatActivity {


    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth ;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database =  FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(signInActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
          mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etEmailLogIn.getText().toString().isEmpty()){
                            binding.etEmailLogIn.setError("Email is Empty");
                            return;
                }

                if (binding.etPassword.getText().toString().isEmpty()){
                    binding.etPassword.setError("Password is Empty");
                    return;
                }


                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.etEmailLogIn.getText().toString(), binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            Intent intent = new Intent(signInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(signInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        binding.tvClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signInActivity.this, SignUpActicvity.class);
                startActivity(intent);
            }
        });

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        if (auth.getCurrentUser()!= null) {
        Intent intent = new Intent(signInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        }
    }
    int RC_SIGN_IN = 65 ;
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestode, int resultCode, Intent data){
        super.onActivityResult(resultCode,resultCode,data);

        if (requestode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG","firebaseAuthWithGoogle:"+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){
                Log.w("TAG","Google sign In failed",e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken){
    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
    auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success");
                FirebaseUser user = auth.getCurrentUser();
                Users users = new Users();

                assert user != null;
                users.setUserId(user.getUid());
                users.setUserName(user.getDisplayName());
                users.setProfilePic(Objects.requireNonNull(user.getPhotoUrl()).toString());
                database.getReference().child("Users").child(user.getUid()).setValue(users);


                Intent intent = new Intent(signInActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(signInActivity.this,"Signed in with Google",Toast.LENGTH_SHORT).show();

            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithCredential:failure", task.getException());
            }
        }
    });
    }
}
