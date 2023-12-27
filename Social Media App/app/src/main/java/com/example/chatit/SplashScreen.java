package com.example.chatit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatit.authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {


    ImageView imageView;
    TextView nameTV, name2Tv ;
    long animTime = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // This method is used so that your splash activity can cover the entire screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView,"y", 400f);
//        ObjectAnimator animatorName = ObjectAnimator.ofFloat(nameTV, "x", 200f);
//        animatorY.setDuration(animTime);
//        animatorName.setDuration(animTime);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(animatorY, animatorName);
//        animatorSet.start();





    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Intent is used to switch from one activity to another.
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i); // invoke the SecondActivity.
                    finish(); // the current activity will get finished.
                }
            },4000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Intent is used to switch from one activity to another.
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i); // invoke the SecondActivity.
                    finish(); // the current activity will get finished.
                }
            },4000);
        }
    }
}