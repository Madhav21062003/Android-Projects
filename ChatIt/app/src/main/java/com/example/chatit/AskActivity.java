package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatit.models.QuestionMember;
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

public class AskActivity extends AppCompatActivity {

    EditText editText ;
    Button button ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allQuestions, userQuestions ;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference ;
    QuestionMember member ;
    String name, url, privacy, uid ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        editText = findViewById(R.id.ask_et_question);
        button = findViewById(R.id.btn_submit);

        documentReference = db.collection("user").document(currentUserId);


        allQuestions = database.getReference("All Questions");
        userQuestions = database.getReference("User Questions").child(currentUserId);

        member = new QuestionMember();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = editText.getText().toString();

                Calendar cDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");

                final  String savedate = currentDate.format(cDate.getTime());

                Calendar cTime =  Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                final  String saveTime = currentTime.format(cTime.getTime());

                String time = savedate+":"+ saveTime;

                if (question != null){
                    member.setQuestion(question);
                    member.setName(name);
                    member.setPrivacy(privacy);
                    member.setUrl(url);
                    member.setUserId(uid);
                    member.setTime(time);

                    String id = userQuestions.push().getKey();
                    userQuestions.child(id).setValue(member);

                    String child = allQuestions.push().getKey();
                    member.setKey(id);
                    allQuestions.child(child).setValue(member);
                    Toast.makeText(AskActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                    editText.setText(" ");
                }else{
                    Toast.makeText(AskActivity.this, "Please Ask The question", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();



        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()){

                    name = task.getResult().getString("name");
                     url = task.getResult().getString("url");
                     privacy = task.getResult().getString("privacy");
                     uid = task.getResult().getString("uid");

                }else{
                    Toast.makeText(AskActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}