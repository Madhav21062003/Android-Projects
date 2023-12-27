package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatit.models.AnswerMember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AnswerActivity extends AppCompatActivity {

    String uid, que, postKey;
    EditText editText;
    Button button;
    AnswerMember member;
    FirebaseDatabase database  = FirebaseDatabase.getInstance();
    DatabaseReference AllQuestions ;

    String name, url, time ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);


        member = new AnswerMember();
        editText = findViewById(R.id.answer_et);
        button = findViewById(R.id.btn_answer_submit);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null){

            uid = bundle.getString("uid");
            postKey = bundle.getString("postkey");
        }else{
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        AllQuestions = database.getReference("All Questions").child(postKey).child("Answer");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnswer();
            }
        });


    }

     void saveAnswer() {
         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         String userId = user.getUid();

        String answer = editText.getText().toString();
        if (answer != null){

            Calendar cDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");

            final  String savedate = currentDate.format(cDate.getTime());

            Calendar cTime =  Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            final  String saveTime = currentTime.format(cTime.getTime());

            String time = savedate+":"+ saveTime;

            member.setAnswer(answer);
            member.setTime(time);
            member.setName(name);
            member.setUid(userId);
            member.setUrl(url);

            String id = AllQuestions.push().getKey();
            AllQuestions.child(id).setValue(member);

            editText.setText(" ");

            Toast.makeText(this, "Thanks for Submission...", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Please answer answer", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        FirebaseFirestore d =FirebaseFirestore.getInstance();
        DocumentReference reference ;
        reference = d.collection("user").document(userId);

        // question user reference
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    url = task.getResult().getString("url");
                    name = task.getResult().getString("name");

                }else{
                    Toast.makeText(AnswerActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}