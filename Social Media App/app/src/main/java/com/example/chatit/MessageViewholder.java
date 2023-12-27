package com.example.chatit;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessageViewholder  extends RecyclerView.ViewHolder {

    TextView senderTv, recieverTv;
    public MessageViewholder(@NonNull View itemView) {
        super(itemView);
        senderTv = itemView.findViewById(R.id.senders_tv);
        recieverTv = itemView.findViewById(R.id.receivers_tv);
    }

    public void setMessage(Application application, String message, String time, String date, String type, String senderuid, String recieveruid){


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = user.getUid();

        if (type.equals("text")) {
            if (currentUid.equals(senderuid)) {
                // Message was sent by the current user, display it as sent
                senderTv.setVisibility(View.VISIBLE);
                recieverTv.setVisibility(View.GONE);
                senderTv.setText(message);
            } else if (currentUid.equals(recieveruid)) {
                // Message was received by the current user, display it as received
                senderTv.setVisibility(View.GONE);
                recieverTv.setVisibility(View.VISIBLE);
                recieverTv.setText(message);
            }
        }
    }

}
