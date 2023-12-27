package com.example.chatit.chat_functionality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatit.MessageViewholder;
import com.example.chatit.ProfileViewHolder;
import com.example.chatit.R;
import com.example.chatit.models.All_UserMmber;
import com.example.chatit.models.MessageMember;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView;
    ImageButton sendBtn, cameraBtn, micBtn;
    TextView userName;
    EditText messageEt;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootref1, rootref2;
    MessageMember messageMember;
    String reciever_name, reciever_uid, sender_uid, url;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            url = bundle.getString("u");
            reciever_name = bundle.getString("n");
            reciever_uid = bundle.getString("uid");
        }
        else {
            Toast.makeText(this, "User Missing", Toast.LENGTH_SHORT).show();
        }

        messageMember = new MessageMember();
        recyclerView = findViewById(R.id.rv_message);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        imageView = findViewById(R.id.iv_message);
        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.iBSend);

        Picasso.get().load(url).into(imageView);
        userName = findViewById(R.id.username_messageTv);
        userName.setText(reciever_name);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        sender_uid = user.getUid();

        rootref1 = database.getReference("Message").child(sender_uid).child(reciever_uid);
        rootref2 = database.getReference("Message").child(reciever_uid).child(sender_uid);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<MessageMember> options1 =
                new FirebaseRecyclerOptions.Builder<MessageMember>()
                        .setQuery(rootref1, MessageMember.class)
                        .build();

        FirebaseRecyclerAdapter<MessageMember, MessageViewholder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<MessageMember, MessageViewholder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull MessageViewholder holder, int position, @NonNull MessageMember model) {

                        holder.setMessage(getApplication(), model.getMessage(), model.getTime(),
                                model.getDate(), model.getType(), model.getSendersuid(), model.getReceieveruid());




                    }

                    @NonNull
                    @Override
                    public MessageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout,parent, false);
                        return new MessageViewholder(view);
                    }
                };

        firebaseRecyclerAdapter1.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);

    }

    private void sendMessage() {

        String message = messageEt.getText().toString();
        Calendar cDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");

        final  String savedate = currentDate.format(cDate.getTime());

        Calendar cTime =  Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        final  String saveTime = currentTime.format(cTime.getTime());

        String time = savedate+":"+ saveTime;

        if (message.isEmpty()){
            Toast.makeText(this, "Cannot send Empty message", Toast.LENGTH_SHORT).show();
        }
        else{
            messageMember.setDate(savedate);
            messageMember.setTime(saveTime);
            messageMember.setMessage(message);
            messageMember.setReceieveruid(reciever_uid);
            messageMember.setSendersuid(sender_uid);

            messageMember.setType("text");

            String id = rootref1.push().getKey();
            rootref1.child(id).setValue(messageMember);

            String id1 = rootref2.push().getKey();
            rootref2.child(id1).setValue(messageMember);

            messageEt.setText("");
        }
    }


}