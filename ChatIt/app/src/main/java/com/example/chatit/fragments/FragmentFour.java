package com.example.chatit.fragments;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatit.PostActivity;
import com.example.chatit.models.PostMember;
import com.example.chatit.PostViewholder;
import com.example.chatit.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FragmentFour extends Fragment implements View.OnClickListener {

    Button button ;
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference, likeref;
    Boolean likechecker = false ;
    DatabaseReference db1, db2,db3 ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_four, container, false);

        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button  = getActivity().findViewById(R.id.createpost_f4);
        reference = database.getReference("All posts");
        likeref = database.getReference("post likes");
        recyclerView = getActivity().findViewById(R.id.rv_posts);
        recyclerView.setHasFixedSize(true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUId = user.getUid();
        db1 = database.getReference("All images").child(currentUId);
        db2 = database.getReference("All videos").child(currentUId);
        db3 = database.getReference("All posts");


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.createpost_f4){
            Intent intent = new Intent(getActivity(), PostActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<PostMember> options = new FirebaseRecyclerOptions.Builder<PostMember>()
                .setQuery(reference, PostMember.class).build();

        FirebaseRecyclerAdapter<PostMember, PostViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<PostMember, PostViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostViewholder holder, int position, @NonNull PostMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserid = user.getUid();

                        final  String postKey = getRef(position).getKey();
                        holder.SetPost(getActivity(), model.getName(), model.getUrl(), model.getPostUri(),
                                model.getTime(), model.getUid(), model.getType(), model.getDesc() );

                        // name, url, userId, key, question, privacy, time
//                        holder.se
                        String url = getItem(position).getPostUri();
                        String name = getItem(position).getName();
//                        String url = getItem(position).getUrl();
                        final String time = getItem(position).getTime();
                        String type = getItem(position).getType();
                        String userid = getItem(position).getUid();



                        holder.likesChecker(postKey);
                        holder.menuOptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialog(name, url,time, userid, type);
                            }
                        });
                        holder.likeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                likechecker = true;
                                likeref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (likechecker.equals(true)){
                                            if (snapshot.child(postKey).hasChild(currentUserid)){
                                                likeref.child(postKey).child(currentUserid).removeValue();

                                              //  delete(time);

                                                likechecker = false ;

                                            }else{
                                                likeref.child(postKey).child(currentUserid).setValue(true);

                                                likechecker = false;
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
                    public PostViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);

                        return new PostViewholder(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    void showDialog(String name, String url, String time, String userId, String type){

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.post_options,null);
        TextView download = view.findViewById(R.id.download_tv_post);
        TextView share = view.findViewById(R.id.share_tv_post);
        TextView delete = view.findViewById(R.id.delete_tv_post);
        TextView copyUrl = view.findViewById(R.id.copyurl_tv_post);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view).create();
        alertDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        // if the user that is logged in , the post which uploaded by that particular user on that
        // particular post we show the delete button
        if (userId.equals(currentUserId)){
            delete.setVisibility(View.VISIBLE);
        }else {
            delete.setVisibility(View.INVISIBLE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Query query1 = db1.orderByChild("time").equalTo(time);
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
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

                Query query2 = db2.orderByChild("time").equalTo(time);
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
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

                Query query3 = db3.orderByChild("time").equalTo(time);
                query3.addListenerForSingleValueEvent(new ValueEventListener() {
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

                StorageReference reference1 = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                reference1.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Deleted  ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                alertDialog.dismiss();

            }
        });


        // foe download

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (type.equals("iv")){
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.setTitle("Download");
                    request.setDescription("Downloading image...");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name+System.currentTimeMillis() + ".jpg");
                    DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);

                    Toast.makeText(getActivity(), "Downloading.....", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else{
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.setTitle("Download");
                    request.setDescription("Downloading video...");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name+System.currentTimeMillis() + ".mp4");
                    DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);

                    Toast.makeText(getActivity(), "Downloading.....", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });


        // Share

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareText = name +"\n" +"\n"+ url;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                intent.setType("text/plain");
                startActivity(intent.createChooser(intent, "Share via"));
                alertDialog.dismiss();
            }
        });

        // Copy url
        copyUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cp = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("String", url);
                cp.setPrimaryClip(clip);
                clip.getDescription();
                Toast.makeText(getActivity(), "Copied..", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });


    }
}