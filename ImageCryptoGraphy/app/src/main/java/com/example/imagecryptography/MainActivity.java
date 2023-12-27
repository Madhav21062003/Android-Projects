package com.example.imagecryptography;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

        ImageView imageEncrypt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        imageEncrypt = findViewById(R.id.img_encrypt);

        // setting onClickListener to move PhotoCrypto.java
        imageEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m2 = new Intent(MainActivity.this,PhotoCrypto.class);
                startActivity(m2);
            }
        });
    }
}