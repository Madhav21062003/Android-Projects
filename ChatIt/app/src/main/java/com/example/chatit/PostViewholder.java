package com.example.chatit;

import android.media.Image;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;


import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;


public class PostViewholder extends RecyclerView.ViewHolder {

  public   ImageView imageViewProfile, iv_post;
    public  TextView tv_name, tv_desc, tv_likes, tv_comment, tv_time,  tv_nameprofile;
    public  ImageButton likeButton, menuOptions, commnetBtn;
      DatabaseReference likesref;
      FirebaseDatabase database = FirebaseDatabase.getInstance();
    int  likesCount;
    public PostViewholder(@NonNull View itemView) {
        super(itemView);
    }

    // here we use FragmentActivity because we have to show the data in the fragment
    public void SetPost(FragmentActivity activity, String name, String url, String postUri, String time, String uid, String type, String desc  ){

        imageViewProfile = itemView.findViewById(R.id.ivprofile_item);
        iv_post = itemView.findViewById(R.id.iv_post_item);
      //  tv_comment = itemView.findViewById(R.id.commentbutton_posts);
        tv_desc = itemView.findViewById(R.id.tv_desc_post);
//        commnetBtn = itemView.findViewById(R.id.commentbutton_posts);
        likeButton = itemView.findViewById(R.id.likebutton_posts);
        tv_likes = itemView.findViewById(R.id.tv_likes_post);
        menuOptions = itemView.findViewById(R.id.morebutton_posts);
        tv_time = itemView.findViewById(R.id.tv_time_post);
        tv_nameprofile = itemView.findViewById(R.id.tv_name_post);

        SimpleExoPlayer exoPlayer;
        PlayerView playerView = itemView.findViewById(R.id.exoplayer_item_post);

        // Checking type of post image or video
        if (type!=null) {
            if (type.equals("iv")) {
                // Showing person profile photo of user
                Picasso.get().load(url).into(imageViewProfile);

                // Showing image of post
                Picasso.get().load(postUri).into(iv_post);
                tv_desc.setText(desc);
                tv_time.setText(time);
                tv_nameprofile.setText(name);
                playerView.setVisibility(View.INVISIBLE);
            } else if (type.equals("vv")) {
                iv_post.setVisibility(View.INVISIBLE);
                tv_desc.setText(desc);
                tv_time.setText(time);
                tv_nameprofile.setText(name);
                // Showing person profile photo of user
                Picasso.get().load(url).into(imageViewProfile);
                // Showing image of post
                Picasso.get().load(postUri).into(iv_post);

                try {
                    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(activity).build();
                    TrackSelector trackSelector = new DefaultTrackSelector(activity);
                    exoPlayer = new SimpleExoPlayer.Builder(activity).setTrackSelector(trackSelector).build();
                    Uri video = Uri.parse(postUri);
                    DefaultHttpDataSourceFactory df = new DefaultHttpDataSourceFactory("video");
                    ExtractorsFactory ef = new DefaultExtractorsFactory();
                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(df).createMediaSource(video);

                    playerView.setPlayer(exoPlayer);
                    exoPlayer.prepare(mediaSource);
                    exoPlayer.setPlayWhenReady(false);
                } catch (Exception e) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public void likesChecker(String postKey) {

      likeButton = itemView.findViewById(R.id.likebutton_posts);

        likesref = database.getReference("post likes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();;

        likesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postKey).hasChild(uid))
                {
                    likeButton.setImageResource(R.drawable.ic_like);
                     likesCount = (int) snapshot.child(postKey).getChildrenCount();
                     tv_likes.setText(Integer.toString(likesCount)+" likes");
                }else {
                    likeButton.setImageResource(R.drawable.ic_dislike);
                    likesCount = (int) snapshot.child(postKey).getChildrenCount();
                    tv_likes.setText(Integer.toString(likesCount)+" likes");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
