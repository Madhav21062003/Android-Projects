package com.example.imagecryptography;

import static android.util.Base64.decode;
//import static com.example.imagecryptography.Manifest.*;
import static java.text.DateFormat.DEFAULT;
import static java.util.Base64.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Base64DataException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class PhotoCrypto extends AppCompatActivity {

    private static final int DEAFAULT = 0;
    Button encrypt, decrypt ;
    String image ; // we take string img  because when we take a image  we covert it and  generate a byte array in the form of text and that text is store in the string image
    android.content.ClipboardManager clipboardManager ;  //  clipboard manager for copying that text
    ImageView imgView ;
    EditText encImg ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_crypto);

        // here we set the Action Bar name as Crypto Image
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Crypto Image");
        actionBar.show();


        encrypt = findViewById(R.id.enc_btn);
        decrypt = findViewById(R.id.dec_btn);
        encImg = findViewById(R.id.enc_txt);
        encImg.setEnabled(false);
        imgView = findViewById(R.id.imgView);

     clipboardManager = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        encrypt.setOnClickListener(new View.OnClickListener() {
            class READ_EXTERNAL_STORAGE {
            }

            @Override
            public void onClick(View v) {
                // taking permission of photo gallery
//             //   String READ_EXTERNAL_STORAGE = null;
                if (ContextCompat.checkSelfPermission(PhotoCrypto.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PhotoCrypto.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                }
                else {
                    selectPhoto();
                }
            }
        });
            decrypt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Base64 base64 = new Base64 ;


                        byte[] bytes = decode(encImg.getText().toString(), DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgView.setImageBitmap(bitmap);

                }
            });
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            selectPhoto();
        }else {
            Toast.makeText(this, "Permission Denied!!!!!!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100 && resultCode == RESULT_OK && data!=null ){
            Uri uri = data.getData();
            Bitmap bitmap ;
            ImageDecoder.Source source = null ;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    source = ImageDecoder.createSource(this.getContentResolver(),uri);
                    try {
                        bitmap = ImageDecoder.decodeBitmap(source);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap. compress(Bitmap.CompressFormat.JPEG,100,stream);
                        byte[] bytes = stream.toByteArray();
                        image = android.util.Base64.encodeToString(bytes, DEAFAULT);
                        encImg.setText(image);
                        Toast.makeText(this, "Image Encrypted", Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
            }
        }
    }

    public void copyCode(View view) {
         String codes = encImg.getText().toString().trim();
         if (!codes.isEmpty()){
             ClipData temp = ClipData.newPlainText("text",codes);
             clipboardManager.setPrimaryClip(temp);
             Toast.makeText(this, "Copied to clipboard..........", Toast.LENGTH_SHORT).show();
         }
    }


}