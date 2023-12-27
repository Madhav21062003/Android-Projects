package com.example.chatit.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chatit.ProfileViewHolder;
import com.example.chatit.R;
import com.example.chatit.ShowUser;
import com.example.chatit.chat_functionality.ChatActivity;
import com.example.chatit.models.All_UserMmber;
import com.example.chatit.models.PostMember;
import com.example.chatit.models.RequestMember;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentThree extends Fragment {

    FirebaseDatabase database;
    DatabaseReference databaseReference, databaseReference1, profileRef;
    RecyclerView recyclerView, recyclerView_profile ;
   RequestMember requestMember;
    TextView requestTv ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Requests").child(currentUserId);
        profileRef = database.getReference("All Users");

        requestMember = new RequestMember();

        recyclerView_profile = requireActivity().findViewById(R.id.recyclerview_profile);

        recyclerView_profile.setHasFixedSize(true);
        recyclerView_profile.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView = requireActivity().findViewById(R.id.recyclerview_requestsf3);
        requestTv = getActivity().findViewById(R.id.requestsTv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        //  MediaController mediaController;


    }

    @Override
    public void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    requestTv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    requestTv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<All_UserMmber> options1 =
                new FirebaseRecyclerOptions.Builder<All_UserMmber>()
                        .setQuery(profileRef, All_UserMmber.class)
                        .build();

        FirebaseRecyclerAdapter<All_UserMmber, ProfileViewHolder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<All_UserMmber, ProfileViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProfileViewHolder holder, int position, @NonNull All_UserMmber model) {

                        final  String postKey = getRef(position).getKey();
                        holder.setProfile(getActivity(), model.getName(), model.getUid(), model.getProf(), model.getUrl());

                        String name = getItem(position).getName();
                        String url = getItem(position).getUrl();
                        String uid = getItem(position).getUid();

                        holder.viewUserProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ShowUser.class);
                                intent.putExtra("n", name);
                                intent.putExtra("u", url);
                                intent.putExtra("uid", uid);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile,parent, false);
                        return new ProfileViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter1.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);


    }
}