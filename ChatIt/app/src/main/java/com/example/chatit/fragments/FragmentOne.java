package com.example.chatit.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatit.BottomSheetMenu;
import com.example.chatit.CreateProfile;
import com.example.chatit.ImageActivity;
import com.example.chatit.IndividualPost;
import com.example.chatit.R;
import com.example.chatit.UpdateProfile;
import com.example.chatit.chat_functionality.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class FragmentOne extends Fragment implements View.OnClickListener {

        ImageView imageView ;
        TextView nameEt, profEt, bioET, emailET, webEt, postTv;
        ImageButton imageButtonEdit, imageButtonMenu;
        Button btnSendMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_one, container, false);

        return  view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageView = requireActivity().findViewById(R.id.iv_f1);
        nameEt = requireActivity().findViewById(R.id.tv_name_f1);
        profEt = requireActivity().findViewById(R.id.tv_prof_f1);
        bioET = requireActivity().findViewById(R.id.tv_bio_f1);
        emailET = requireActivity().findViewById(R.id.tv_email_f1);
        webEt = requireActivity().findViewById(R.id.tv_web_f1);
        imageButtonEdit = requireActivity().findViewById(R.id.ib_edit_f1);
        imageButtonMenu = requireActivity().findViewById(R.id.ib_menu_f1);
        postTv = requireActivity().findViewById(R.id.tv_posts_f1);
        btnSendMessage = requireActivity().findViewById(R.id.btn_sendmessage_f1);

        postTv.setOnClickListener(this);
        imageButtonEdit.setOnClickListener(this);
        imageButtonMenu.setOnClickListener(this);
        imageView.setOnClickListener(this);
        webEt.setOnClickListener(this);

        btnSendMessage.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.ib_edit_f1){
            Intent intent = new Intent(getActivity(), UpdateProfile.class);
            startActivity(intent);
        } else if (view.getId() == R.id.ib_menu_f1) {

            // opening Bottom Sheet
            BottomSheetMenu bottomSheetMenu = new BottomSheetMenu();
            bottomSheetMenu.show(getFragmentManager(), "bottomsheet");
        } else if (view.getId() == R.id.iv_f1) {
            Intent intent = new Intent(getActivity(), ImageActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.btn_sendmessage_f1) {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.tv_web_f1) {

            // Sometimes the given url is correct or may not correct
            try {
                String url = webEt.getText().toString();
                Intent intent2  =  new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(url));
                startActivity(intent2);
            }catch (Exception e){
                Toast.makeText(getActivity(),"Invalid Url", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.tv_posts_f1) {
            Intent intent3 = new Intent(getActivity(), IndividualPost.class);
            startActivity(intent3);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            String currentId = user.getUid();
            DocumentReference reference;
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            reference = firestore.collection("user").document(currentId);

            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        String nameResult = task.getResult().getString("name");
                        String bioResult = task.getResult().getString("bio");
                        String emailResult = task.getResult().getString("email");
                        String webResult = task.getResult().getString("web");
                        String url = task.getResult().getString("url");
                        String profResult = task.getResult().getString("prof");

                        Picasso.get().load(url).into(imageView);
                        nameEt.setText(nameResult);
                        bioET.setText(bioResult);
                        emailET.setText(emailResult);
                        webEt.setText(webResult);
                        profEt.setText(profResult);

                    } else {
                        Intent intent = new Intent(getActivity(), CreateProfile.class);
                        startActivity(intent);
                    }
                }
            });

        }else{

            // User is not authenticated, handle this case accordingly
            Log.d("FragmentOne", "User is not authenticated");
            Intent intent = new Intent(getActivity(), CreateProfile.class);
            startActivity(intent);
        }

    }


}