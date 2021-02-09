package com.krys.exoplayer.player;

import android.content.ContentUris;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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

public class OfflinePlayer extends BaseActivity {

    private PlayerView playerView;
    private SimpleExoPlayer player;

    private boolean orientationChanged = false;
    private int lastOrientation = 0;

    private long videoId;
    private int height;
    private int width;

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

            if (getIntent().getExtras().containsKey(ConstantStrings.VIDEO_HEIGHT)){
                height = getIntent().getIntExtra(ConstantStrings.VIDEO_HEIGHT, 0);
            }

            if (getIntent().getExtras().containsKey(ConstantStrings.VIDEO_WIDTH)){
                width = getIntent().getIntExtra(ConstantStrings.VIDEO_WIDTH, 0);
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
        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                setRequestedOrientation(width > height ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                player.setPlayWhenReady(true);
            }
        });
        player.prepare(mediaSource);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, getString(R.string.app_name));
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
    }

    private void releasePlayer(){
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if(orientation != lastOrientation){
            orientationChanged  = true;
            lastOrientation = orientation ;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

}