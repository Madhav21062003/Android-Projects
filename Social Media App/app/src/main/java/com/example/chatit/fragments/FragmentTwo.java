package com.example.chatit.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatit.AskActivity;
import com.example.chatit.BottomSheetF2;
import com.example.chatit.models.QuestionMember;
import com.example.chatit.R;
import com.example.chatit.ReplyActivity;
import com.example.chatit.ViewHolderQuestions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class FragmentTwo extends Fragment implements View.OnClickListener {


    FloatingActionButton fb;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference reference ;
    ImageView imageView ;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference, fvrtRef, fvrt_listRef;

    RecyclerView recyclerView;

    Boolean fvrtChecker = false ;
    QuestionMember member ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);


        return  view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();

        recyclerView = getActivity().findViewById(R.id.rv_f2);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference  = database.getReference("All Questions");
        member = new QuestionMember();
        fvrtRef = database.getReference("favourites");
        fvrt_listRef = database.getReference("favouriteList").child(currentUserid);

        imageView  = requireActivity().findViewById(R.id.iv_f2);
        fb = requireActivity().findViewById(R.id.floatingActionButton2);
        reference = db.collection("user").document(currentUserid);

        fb.setOnClickListener(this);
        imageView.setOnClickListener(this);

        FirebaseRecyclerOptions<QuestionMember> options = new FirebaseRecyclerOptions.Builder<QuestionMember>()
                .setQuery(databaseReference, QuestionMember.class).build();

        FirebaseRecyclerAdapter<QuestionMember, ViewHolderQuestions> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<QuestionMember, ViewHolderQuestions>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderQuestions holder, int position, @NonNull QuestionMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserid = user.getUid();

                        final  String postKey = getRef(position).getKey();

                        // name, url, userId, key, question, privacy, time
                        holder.setItem(getActivity(), model.getName(), model.getUrl(), model.getUserId(), model.getKey(), model.getQuestion(), model.getPrivacy(), model.getTime());

                        String que = getItem(position).getQuestion();
                        String name = getItem(position).getName();
                        String url = getItem(position).getUrl();
                        final String time = getItem(position).getTime();
                        String privacy = getItem(position).getPrivacy();
                        String userid = getItem(position).getUserId();

                        holder.replyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ReplyActivity.class);
                                intent.putExtra("uid", userid);
                                intent.putExtra("q", que);
                                intent.putExtra("postkey", postKey);
//                                intent.putExtra("key", privacy);
                                startActivity(intent);
                            }
                        });

                        holder.favouriteChecker(postKey);
                        holder.fvrt_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                fvrtChecker = true;
                                fvrtRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (fvrtChecker.equals(true)){
                                                if (snapshot.child(postKey).hasChild(currentUserid)){
                                                    fvrtRef.child(postKey).child(currentUserid).removeValue();

                                                    delete(time);

                                                    fvrtChecker = false ;

                                                }else{
                                                    fvrtRef.child(postKey).child(currentUserid).setValue(true);
                                                    member.setName(name);
                                                    member.setTime(time);
                                                    member.setPrivacy(privacy);
                                                    member.setUserId(userid);
                                                    member.setUrl(url);
                                                    member.setQuestion(que);

//                                                    String id = fvrt_listRef.push().getKey();
                                                    fvrt_listRef.child(postKey).setValue(member);
                                                    fvrtChecker = false;
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
                    public ViewHolderQuestions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_items,parent,false);

                        return new ViewHolderQuestions(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    void  delete(String time){

        Query query = fvrt_listRef.orderByChild("time").equalTo(time);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();
                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.iv_f2){
            BottomSheetF2 bottomSheetF2 = new BottomSheetF2();
            bottomSheetF2.show(getChildFragmentManager(),"bottom");
        } else if (view.getId() == R.id.floatingActionButton2) {
            Intent intent = new Intent(getActivity(), AskActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()){
                    String url = task.getResult().getString("url");
                    Picasso.get().load(url).into(imageView);

                }else{
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}