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

import com.example.chatit.models.QuestionMember;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Related extends AppCompatActivity {


    RecyclerView recyclerView ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related);

        recyclerView = findViewById(R.id.rv_related);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();
        reference = database.getReference("favouriteList").child(currentUserid);


        FirebaseRecyclerOptions<QuestionMember> options = new FirebaseRecyclerOptions.Builder<QuestionMember>()
                .setQuery(reference, QuestionMember.class).build();

        FirebaseRecyclerAdapter<QuestionMember, ViewHolderQuestions> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<QuestionMember, ViewHolderQuestions>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderQuestions holder, @SuppressLint("RecyclerView") int position, @NonNull QuestionMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserid = user.getUid();

                        final  String postKey = getRef(position).getKey();

                        // name, url, userId, key, question, privacy, time
                        holder.setItemRelated(getApplication(), model.getName(), model.getUrl(), model.getUserId(), model.getKey(), model.getQuestion(), model.getPrivacy(), model.getTime());

                        holder.replyBtn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                             final    String userid = getItem(position).getUserId();
                             final    String que = getItem(position).getQuestion();

                                Intent intent = new Intent(Related.this, ReplyActivity.class);
                                intent.putExtra("uid", userid);
                                intent.putExtra("q", que);
                                intent.putExtra("postkey", postKey);

                                startActivity(intent);
                            }
                        });





                    }

                    @NonNull
                    @Override
                    public ViewHolderQuestions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_item,parent,false);

                        return new ViewHolderQuestions(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}