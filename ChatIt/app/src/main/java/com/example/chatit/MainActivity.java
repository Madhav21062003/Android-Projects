package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.chatit.authentication.LoginActivity;
import com.example.chatit.fragments.FragmentFour;
import com.example.chatit.fragments.FragmentOne;
import com.example.chatit.fragments.FragmentThree;
import com.example.chatit.fragments.FragmentTwo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);

        // when we open the app it first shows the profile activity by default
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentOne()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment  selected = null ;

            if (item.getItemId() == R.id.profile_bottom){
                selected = new FragmentOne();
            } else if (item.getItemId() == R.id.ask_bottom) {
                selected = new FragmentTwo();
            }else if (item.getItemId() == R.id.queue_bottom) {
                selected = new FragmentThree();
            } else if (item.getItemId() == R.id.home_bottom) {
                selected = new FragmentFour();
            }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selected).commit();
            return true;
        }
    };

    public void logout(View view) {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}