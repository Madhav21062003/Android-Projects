package com.example.chatit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatit.models.PostMember;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ImageTab extends Fragment {

    FirebaseDatabase database;
    DatabaseReference reference;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.images_tab, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        database = FirebaseDatabase.getInstance();
        recyclerView = getActivity().findViewById(R.id.rv_imagesTab);
        reference = database.getReference("All images").child(uid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<PostMember> options = new FirebaseRecyclerOptions.Builder<PostMember>()
                .setQuery(reference, PostMember.class).build();

        FirebaseRecyclerAdapter<PostMember, ImagesFragment> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<PostMember, ImagesFragment>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ImagesFragment holder, int position, @NonNull PostMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserid = user.getUid();

                        final  String postKey = getRef(position).getKey();
                        holder.SetImage(getActivity(), model.getPostUri());

                        final String postUri = model.getPostUri();
                        holder.SetImage(getActivity(),postUri);

                    }

                    @NonNull
                    @Override
                    public ImagesFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_tab,parent,false);

                        return new ImagesFragment(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();

        GridLayoutManager glm = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}
