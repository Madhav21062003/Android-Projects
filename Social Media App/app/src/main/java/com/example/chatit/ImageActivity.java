package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    ImageView imageView ;
    TextView textView;
    Button btnEdit, btnDel ;
DocumentReference reference ;
String url ;
FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        btnDel = findViewById(R.id.btn_del_iv);
        btnEdit = findViewById(R.id.btn_edit_iv);
        imageView = findViewById(R.id.iv_expand);
        textView = findViewById(R.id.tv_name_image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();

        reference = db.collection("user").document(currentid);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()){
                    String name = task.getResult().getString("name");
                    url = task.getResult().getString("url");

                    Picasso.get().load(url).into(imageView);
                    textView.setText(name);
                }else{
                    Toast.makeText(ImageActivity.this, "No Profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}