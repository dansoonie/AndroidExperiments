package com.dansoonie.experiments.category.video.exo;

import android.app.Activity;
import android.os.Bundle;

import com.dansoonie.experiments.Log;
import com.dansoonie.experiments.R;
import com.dansoonie.experiments.category.video.customplayer.ExoHlsPlayer;

public class ExoPlayerHlsTestActivity extends Activity {
  private static final String TAG = "Exo";

  private ExoHlsPlayer mPlayer;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.exo_hls_player);

    Log.ENABLE_DEBUG = true;
    mPlayer = (ExoHlsPlayer) findViewById(R.id.exo_view);
    mPlayer.setResumeOnResume(true);
    mPlayer.setOnPlayerInitializedListener(new ExoHlsPlayer.OnPlayerInitializedListener() {
      @Override
      public void onInitialized(ExoHlsPlayer player) {
        player.load("https://s3.ap-northeast-2.amazonaws.com/gb-tv/58/broadcasts/187/vod.m3u8");
      }
    });

  }

  @Override
  protected void onStart() {
    super.onStart();
    mPlayer.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mPlayer.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mPlayer.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mPlayer.onStop();
  }
}
