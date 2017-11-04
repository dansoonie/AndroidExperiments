package com.dansoonie.experiments.category.video.exo.mediasource;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.dansoonie.experiments.R;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerTestMediaSourceActivity extends Activity {

  private SimpleExoPlayerView mSimpleExoPlayerView;
  private SimpleExoPlayer mPlayer;

  private boolean mNeedsNewPlayer;
  private MediaSourceFactory mMediaSourceFactory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.player_activity);
    mSimpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
    mMediaSourceFactory = MediaSourceFactory.create(this, "test");
  }

  private void initializePlayer() {
    mNeedsNewPlayer = mPlayer == null;
    if (mNeedsNewPlayer) {
      DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(
          this,
          null,
          DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF);
      mPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, new DefaultTrackSelector());
      mSimpleExoPlayerView.setPlayer(mPlayer);
    }
    Intent intent = getIntent();
    String uri = intent.getStringExtra("uri");
    String extension = intent.getStringExtra("ext");
    MediaSource mediaSource = mMediaSourceFactory.buildMediaSource(Uri.parse(uri), extension);
    mPlayer.prepare(mediaSource, true, false);
  }

  private void releasePlayer() {
    if (mPlayer != null) {
      mPlayer.release();
      mPlayer = null;
      //trackSelector = null;
      //trackSelectionHelper = null;
      //eventLogger = null;
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initializePlayer();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if ((Util.SDK_INT <= 23 || mPlayer == null)) {
      initializePlayer();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    //releaseAdsLoader();
  }
}
