package com.krys.exoplayer.player;

import android.content.ContentUris;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.krys.exoplayer.R;
import com.krys.exoplayer.base.BaseActivity;
import com.krys.exoplayer.utils.ConstantStrings;
import com.krys.exoplayer.utils.PrefUtils;

public class OfflinePlayer extends BaseActivity {

    private PlayerView playerView;
    private SimpleExoPlayer player;

    private long videoId;
    private long duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_player);
        findViewById();
        getIntentParams();
        init();
    }

    private void getIntentParams() {
        if (getIntent().getExtras() != null){
            if (getIntent().getExtras().containsKey(ConstantStrings.VIDEO_ID)){
                String id = getIntent().getStringExtra(ConstantStrings.VIDEO_ID);
                videoId = Long.parseLong(id);
            }

            if (getIntent().getExtras().containsKey(ConstantStrings.VIDEO_DURATION)){
                String id = getIntent().getStringExtra(ConstantStrings.VIDEO_DURATION);
                duration = Long.parseLong(id);
            }
        }
    }

    private void findViewById(){
        playerView = findViewById(R.id.playerView);
    }

    private void init() {


    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        Uri videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId);
        MediaSource mediaSource = buildMediaSource(videoUri);
        setVideoEventListeners();
        player.prepare(mediaSource);
    }

    private void setVideoEventListeners() {
        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                setRequestedOrientation(width > height ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if(duration != 0){
                    player.seekTo(duration);
                }
                player.setPlayWhenReady(true);
            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, getString(R.string.app_name));
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void saveLastPlayedVideo() {
        PrefUtils.setPref(getActivity(), ConstantStrings.VIDEO_DURATION, player.getCurrentPosition());
        PrefUtils.setPref(getActivity(), ConstantStrings.VIDEO_ID, videoId);
    }

    private void releasePlayer(){
        saveLastPlayedVideo();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        if(Util.SDK_INT<24){
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(Util.SDK_INT>=24){
            releasePlayer();
        }
        super.onStop();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return playerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

}