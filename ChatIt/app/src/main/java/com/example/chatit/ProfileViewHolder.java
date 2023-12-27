package com.example.chatit;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileViewHolder extends RecyclerView.ViewHolder {
  public   TextView textViewName, textViewProfession, viewUserProfile, sendMessageBtn ;
   public ImageView imageView;
   public CardView cardView;
    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setProfile(FragmentActivity fragmentActivity, String name, String uid, String prof, String url){
        cardView = itemView.findViewById(R.id.cardview_profile);
        textViewName = itemView.findViewById(R.id.tv_name_profile);
        textViewProfession = itemView.findViewById(R.id.tv_profession_profile);
        viewUserProfile = itemView.findViewById(R.id.viewUser_profile);
        imageView = itemView.findViewById(R.id.profile_imageView);

        Picasso.get().load(url).into(imageView);
        textViewName.setText(name);
        textViewProfession.setText(prof);
    }

    public void setProfileInChat(Application fragmentActivity, String name, String uid, String prof, String url)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        ImageView imageView1 = itemView.findViewById(R.id.iv_ch_item);
        TextView nameTv = itemView.findViewById(R.id.name_ch_item_tv);
        TextView profTv = itemView.findViewById(R.id.ch_itemprof_tv);
        sendMessageBtn = itemView.findViewById(R.id.send_messagech_item_btn);

        // Logic for you cannot send message to yourself
        if (userid.equals(uid)){
            Picasso.get().load(url).into(imageView1);
            nameTv.setText(name);
            profTv.setText(prof);
            sendMessageBtn.setVisibility(View.INVISIBLE);
        }else{
            Picasso.get().load(url).into(imageView1);
            nameTv.setText(name);
            profTv.setText(prof);
            sendMessageBtn.setVisibility(View.VISIBLE);

        }
    }
}
