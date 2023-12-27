package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatit.models.QuestionMember;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class YourQuestions extends AppCompatActivity {

        RecyclerView recyclerView ;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference allQuestions, userQuestions ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_questions);

        recyclerView = findViewById(R.id.yourQuestion_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        allQuestions  = database.getReference("All Questions");
        userQuestions = database.getReference("User Questions").child(currentUserId);

        FirebaseRecyclerOptions<QuestionMember> options = new FirebaseRecyclerOptions.Builder<QuestionMember>()
                .setQuery(userQuestions, QuestionMember.class).build();

        FirebaseRecyclerAdapter<QuestionMember, ViewHolderQuestions> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<QuestionMember, ViewHolderQuestions>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderQuestions holder, int position, @NonNull QuestionMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserid = user.getUid();

                        final  String postKey = getRef(position).getKey();

                        // name, url, userId, key, question, privacy, time
                        holder.setItemDelete(getApplication(), model.getName(), model.getUrl(), model.getUserId(), model.getKey(), model.getQuestion(), model.getPrivacy(), model.getTime());

                        final  String time = getItem(position).getTime();

                        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    delete(time);
                            }
                        });




                    }

                    @NonNull
                    @Override
                    public ViewHolderQuestions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_question_item,parent,false);

                        return new ViewHolderQuestions(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    void  delete(String time){

        Query query = userQuestions.orderByChild("time").equalTo(time);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();
                    Toast.makeText(YourQuestions.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query1 = allQuestions.orderByChild("time").equalTo(time);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();
                    Toast.makeText(YourQuestions.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}