package com.example.chatit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.chatit.models.PostMember;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    ImageView imageView ;
    ProgressBar progressBar;
    Button btnChooseFile, btnUploadFile;
    private Uri selectedUri;
    private  static  final  int PICK_FILE = 1 ;
    UploadTask uploadTask;
    EditText etdesc;
    VideoView videoView;
    String url, name;

    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db1, db2, db3;

    MediaController mediaController;
    String type;
    PostMember postMember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mediaController = new MediaController(this);

        progressBar = findViewById(R.id.progressdbar_post);
        imageView = findViewById(R.id.iv_post);
        videoView = findViewById(R.id.vv_post);
        btnChooseFile = findViewById(R.id.btn_choosefile_post);
        btnUploadFile = findViewById(R.id.btn_uploadfile_post);
        etdesc = findViewById(R.id.et_desc_post);
        postMember = new PostMember();
        storageReference = FirebaseStorage.getInstance().getReference("User posts");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUId = user.getUid();

        db1 = database.getReference("All images").child(currentUId);
        db2 = database.getReference("All videos").child(currentUId);
        db3 = database.getReference("All posts");

        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoPost();

            }
        });

        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


    }

    @SuppressLint("IntentReset")
    private void chooseImage() {
        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FILE);
    }



    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Ensure uri is not null before accessing it  // mark it for later if it gives the null pointer exception
//        if (uri != null) {
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//        } else {
//            return null;
//        }
    }

    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUId = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("user").document(currentUId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()){

                    name = task.getResult().getString("name");
                    url = task.getResult().getString("url");

                }else{
                    Toast.makeText(PostActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedUri = data.getData();

            // Check the MIME type of the selected file
            String mimeType = getContentResolver().getType(selectedUri);

            if (mimeType != null && mimeType.startsWith("video/")) {
                // It's a video
                videoView.setMediaController(mediaController);
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVideoURI(selectedUri);
                videoView.start();
                type = "vv";
            } else if (mimeType != null && mimeType.startsWith("image/")) {
                // It's an image
                Picasso.get().load(selectedUri).into(imageView);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                type = "iv";
            } else {
                // Unsupported file format
                Toast.makeText(this, "Unsupported file format", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void DoPost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUId = user.getUid();

        String desc = etdesc.getText().toString();

        Calendar cDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String savedate = currentDate.format(cDate.getTime());

        Calendar cTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        final String saveTime = currentTime.format(cTime.getTime());

        String time = savedate + ":" + saveTime;

        // Checking file is empty or not
        if (!TextUtils.isEmpty(desc) && selectedUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(selectedUri));
            uploadTask = reference.putFile(selectedUri);

            // Get the URL of the uploaded file
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        postMember.setDesc(desc);
                        postMember.setName(name);
                        postMember.setPostUri(downloadUri.toString());
                        postMember.setTime(time);
                        postMember.setUid(currentUId);
                        postMember.setUrl(url);

                        if (type.equals("iv")) {
                            postMember.setType("iv");

                            // Save image post to the appropriate database reference
                            String id = db1.push().getKey();
                            db1.child(id).setValue(postMember);

                            // Save image post to the common database reference
                            String id1 = db3.push().getKey();
                            db3.child(id1).setValue(postMember);
                        } else if (type.equals("vv")) {
                            postMember.setType("vv");

                            // Save video post to the appropriate database reference
                            String id3 = db2.push().getKey();
                            db2.child(id3).setValue(postMember);

                            // Save video post to the common database reference
                            String id4 = db3.push().getKey();
                            db3.child(id4).setValue(postMember);
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PostActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PostActivity.this, "Error uploading file", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(PostActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }


}



//    private void DoPost(){
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String currentUId = user.getUid();
//
//        String desc = etdesc.getText().toString();
//
//        Calendar cDate = Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
//
//        final  String savedate = currentDate.format(cDate.getTime());
//
//        Calendar cTime =  Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
//        final  String saveTime = currentTime.format(cTime.getTime());
//
//        String time = savedate+":"+ saveTime;
//
//        // Checking file is empty or not
//        if (TextUtils.isEmpty(desc) || selectedUri != null){
//
//            progressBar.setVisibility(View.VISIBLE);
//            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(selectedUri));
//            uploadTask = reference.putFile(selectedUri);
//
//            // way to get uri of uploaded image
//            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//
//                    if (!task.isSuccessful()) {
//                        throw Objects.requireNonNull(task.getException());
//                    }
//                    return reference.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//
//                        if (type.equals("iv")){
//                            postMember.setDesc(desc);
//                            postMember.setName(name);
//                            postMember.setPostUri(downloadUri.toString());
//                            postMember.setTime(time);
//                            postMember.setUid(currentUId);
//                            postMember.setUrl(url);
//                            postMember.setType("iv");
//
//                            // for image
//                            String id = db1.push().getKey();
//                            db1.child(id).setValue(postMember);
//
//                            // for both
//                            String id1  = db3.push().getKey();
//                            db3.child(id1).setValue(postMember);
//                            progressBar.setVisibility(View.INVISIBLE);
//                            Toast.makeText(PostActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
//
//                        }else if (type.equals("vv")){
//                            if (type.equals("iv")){
//                                postMember.setDesc(desc);
//                                postMember.setName(name);
//                                postMember.setPostUri(downloadUri.toString());
//                                postMember.setTime(time);
//                                postMember.setUid(currentUId);
//                                postMember.setUrl(url);
//                                postMember.setType("vv");
//
//                                // for video
//                                String id3 = db2.push().getKey();
//                                db1.child(id3).setValue(postMember);
//
//                                // for both
//                                String id4  =  db3.push().getKey();
//                                db3.child(id4).setValue(postMember);
//                                progressBar.setVisibility(View.INVISIBLE);
//                                Toast.makeText(PostActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(PostActivity.this, "error", Toast.LENGTH_SHORT).show();}
//                        }
//
//
//                    } else {
//                        Toast.makeText(PostActivity.this, "Please Fill all fields", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//        }
//
//    }


//========================================================================================================

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_FILE || resultCode == RESULT_OK || data != null || data.getData() != null){
//
//            selectedUri = Objects.requireNonNull(data).getData();
//
//            if (requestCode == PICK_FILE && resultCode == RESULT_OK && data.getData() != null){
//                Picasso.get().load(selectedUri).into(imageView);
//                imageView.setVisibility(View.VISIBLE);
//                videoView.setVisibility(View.INVISIBLE);
//                type = "iv";
//            } else {
//                assert selectedUri != null;
//                if (selectedUri.toString().contains("video")) {
//                    videoView.setMediaController(mediaController);
//                    videoView.setVisibility(View.VISIBLE);
//                    imageView.setVisibility(View.INVISIBLE);
//                    videoView.setVideoURI(selectedUri);
//                    videoView.start();
//                    type = "vv";
//                }else{
//                    Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }