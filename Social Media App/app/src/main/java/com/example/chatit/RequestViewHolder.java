package com.example.chatit;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class RequestViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;
    TextView button1, button2;
    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setRequest(FragmentActivity activity, String name, String url, String profession, String bio,
                           String Privacy, String email, String followers, String website, String userid){


        imageView = itemView.findViewById(R.id.imageview_request);
        textView = itemView.findViewById(R.id.tv_request_name_item);
        button1 = itemView.findViewById(R.id.accept_request);
        button2 = itemView.findViewById(R.id.decline_request);

        Picasso.get().load(url).into(imageView);
        textView.setText(name);

    }
}
