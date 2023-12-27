package com.example.chatit;

import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

public class VideosFragment extends RecyclerView.ViewHolder {


    public VideosFragment(@NonNull View itemView) {
        super(itemView);
    }



    public void SetVideo(FragmentActivity activity, String name, String url, String postUri, String time, String uid, String type, String desc  ){

        SimpleExoPlayer exoPlayer;
        PlayerView   playerView = itemView.findViewById(R.id.exoplayer_item_post);

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
