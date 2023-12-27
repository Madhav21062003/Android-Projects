package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatit.models.AnswerMember;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ReplyActivity extends AppCompatActivity {


    String uid, question, post_key, key ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference reference, reference2;

    TextView nameTv, questionTV, tvReply ;
    RecyclerView recyclerView;
    ImageView imageViewQue, imageViewUser ;
    FirebaseDatabase database = FirebaseDatabase.getInstance() ;
    DatabaseReference votesRef, AllQuestions;
    Boolean voteChecker = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        nameTv  = findViewById(R.id.name_reply_tv);
        questionTV = findViewById(R.id.que_reply_tv);
        imageViewQue = findViewById(R.id.iv_que_user);
        imageViewUser = findViewById(R.id.iv_reply_user);
        tvReply  = findViewById(R.id.answer_tv);

        recyclerView = findViewById(R.id.rv_ans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        Bundle extra = getIntent().getExtras();
        if (extra!=null){
            uid = extra.getString("uid");
            post_key = extra.getString("postkey");
            question = extra.getString("q");
//            key = extra.getString("key");
        }else{
            Toast.makeText(this, "OOPS!!!", Toast.LENGTH_SHORT).show();
        }




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();;

        AllQuestions = database.getReference("All Questions").child(post_key).child("Answer");

        votesRef = database.getReference("votes");

        reference = db.collection("user").document(uid);
        reference2 = db.collection("user").document(currentuid);


        tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReplyActivity.this, AnswerActivity.class);
                intent.putExtra("uid", uid);
//                intent.putExtra("q", question);
                intent.putExtra("postkey", post_key);
//                                intent.putExtra("key", privacy);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // question user reference
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    String url = task.getResult().getString("url");
                    String name = task.getResult().getString("name");

                    Picasso.get().load(url).into(imageViewQue);
                    questionTV.setText(question);
                    nameTv.setText(name);
                }else{
                    Toast.makeText(ReplyActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // reference for replying user
        reference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    String url = task.getResult().getString("url");
//                    String name = task.getResult().getString("name");

                    Picasso.get().load(url).into(imageViewUser);
//                    nameTv.setText(name);
                }else{
                    Toast.makeText(ReplyActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirebaseRecyclerOptions<AnswerMember> options = new FirebaseRecyclerOptions.Builder<AnswerMember>()
                .setQuery(AllQuestions, AnswerMember.class).build();

        FirebaseRecyclerAdapter<AnswerMember, AnswerViewHolders> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AnswerMember, AnswerViewHolders>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AnswerViewHolders holder, int position, @NonNull AnswerMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserid = user.getUid();

                        holder.setAnswer(getApplication(), model.getName(), model.getAnswer(), model.getUid(), model.getTime(), model.getUrl() );

                        final String postKey = getRef(position).getKey();
                        holder.upVoteChecker(postKey);

                        holder.upvoteTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                voteChecker = true;

                                votesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (voteChecker.equals(true)){
                                            if (snapshot.child(postKey).hasChild(currentUserid)){
                                                votesRef.child(postKey).child(currentUserid).removeValue();

                                                voteChecker = false;
                                            }else{
                                                votesRef.child(postKey).child(currentUserid).setValue(true);

                                                voteChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });



                    }

                    @NonNull
                    @Override
                    public AnswerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ans_layout,parent,false);

                        return new AnswerViewHolders(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }
}