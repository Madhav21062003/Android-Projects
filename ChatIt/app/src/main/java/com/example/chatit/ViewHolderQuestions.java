package com.example.chatit;

import android.app.Application;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewHolderQuestions extends RecyclerView.ViewHolder {

   public ImageView imageView ;
   public TextView time_result, name_result, question_result, deleteBtn, replyBtn ,  replyBtn1;
    public ImageButton fvrt_btn ;
    DatabaseReference favoriteRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public ViewHolderQuestions(@NonNull View itemView) {
        super(itemView);
    }


    public void setItem(FragmentActivity activity, String name, String url, String userId, String key, String question, String privacy,
                    String time){

        imageView = itemView.findViewById(R.id.iv_que_item);
        time_result =itemView.findViewById(R.id.time_que_item_tv);
        name_result = itemView.findViewById(R.id.name_que_item_tv);
        question_result = itemView.findViewById(R.id.que_item_tv);
        replyBtn = itemView.findViewById(R.id.reply_item_que);


        Picasso.get().load(url).into(imageView);
        time_result.setText(time);
        name_result.setText(name);
        question_result.setText(question);

    }

    public void favouriteChecker(String postKey) {

        fvrt_btn = itemView.findViewById(R.id.fvrt_f2_item);


        favoriteRef = database.getReference("favourites");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();;

        favoriteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postKey).hasChild(uid))
                {
                    fvrt_btn.setImageResource(R.drawable.baseline_turned_in_24);
                }else {
                    fvrt_btn.setImageResource(R.drawable.baseline_turned_in_not_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setItemRelated(Application activity, String name, String url, String userId, String key, String question, String privacy,
                               String time){

        TextView timetv = itemView.findViewById(R.id.related_time_que_item_tv);
        ImageView imageView = itemView.findViewById(R.id.related_iv_que_item);
        TextView nameTv = itemView.findViewById(R.id.related_name_que_item_tv);
        TextView queTv = itemView.findViewById(R.id.related_que_item_tv);
        replyBtn1 = itemView.findViewById(R.id.related_reply_item_que);

       Picasso.get().load(url).into(imageView);
        nameTv.setText(name);
        timetv.setText(time);
        queTv.setText(question);


    }

    public void setItemDelete(Application activity, String name, String url, String userId, String key, String question, String privacy,
                               String time){

        TextView timetv = itemView.findViewById(R.id.yourQuestion_time_que_item_tv);
        ImageView imageView = itemView.findViewById(R.id.yourQuestion_iv_que_item);
        TextView nameTv = itemView.findViewById(R.id.yourQuestion_name_que_item_tv);
        TextView queTv = itemView.findViewById(R.id.yourQuestion_que_item_tv);
        deleteBtn = itemView.findViewById(R.id.yourQuestion_delete_item_que);

        Picasso.get().load(url).into(imageView);
        nameTv.setText(name);
        timetv.setText(time);
        queTv.setText(question);


    }
}
