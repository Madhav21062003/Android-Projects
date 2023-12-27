package com.example.chatit;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AnswerViewHolders extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameTv, timeTv, ansTv, upvoteTv, voteNoTv;
    int votesCount;
    DatabaseReference reference;
    FirebaseDatabase database ;
    public AnswerViewHolders(@NonNull View itemView) {
        super(itemView);
    }


    public void setAnswer(Application application, String name, String answer, String uid, String time, String url){

        imageView = itemView.findViewById(R.id.imageView_ans);
        nameTv = itemView.findViewById(R.id.tv_name_ans);
        timeTv = itemView.findViewById(R.id.tv_time_ans);
        ansTv = itemView.findViewById(R.id.tv_ans);

        nameTv.setText(name);
        timeTv.setText(time);
        ansTv.setText(answer);
        Picasso.get().load(url).into(imageView);

    }

    public void upVoteChecker(String postKey){

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("votes");

        upvoteTv = itemView.findViewById(R.id.tv_vote_ans);
        voteNoTv = itemView.findViewById(R.id.tv_vote_no);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postKey).hasChild(currentUserId)){
                        upvoteTv.setText("VOTED");
                        votesCount = (int)snapshot.child(postKey).getChildrenCount();

                        voteNoTv.setText(Integer.toString(votesCount)+"-VOTES");
                }else{
                    upvoteTv.setText("UPVOTE");
                    votesCount = (int)snapshot.child(postKey).getChildrenCount();

                    voteNoTv.setText(Integer.toString(votesCount)+"-VOTES");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
