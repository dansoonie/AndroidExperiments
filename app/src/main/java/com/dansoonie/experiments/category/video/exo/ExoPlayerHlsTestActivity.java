package com.dansoonie.experiments.category.video.exo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dansoonie.experiments.Log;
import com.dansoonie.experiments.R;
import com.dansoonie.experiments.category.video.customplayer.ExoHlsPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;

public class ExoPlayerHlsTestActivity extends Activity {
  private static final String TAG = "Exo";

  private ExoHlsPlayer mPlayer;
  private Button mButton;
  private boolean mFill;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.exo_hls_player);

    Log.ENABLE_DEBUG = true;
    mPlayer = (ExoHlsPlayer) findViewById(R.id.exo_view);
    //mPlayer.setResumeOnResume(true);
    mPlayer.setOnPlayerInitializedListener(new ExoHlsPlayer.OnPlayerInitializedListener() {
      @Override
      public void onInitialized(ExoHlsPlayer player) {
        //player.load("https://s3.ap-northeast-2.amazonaws.com/gb-tv/58/broadcasts/187/vod.m3u8");
        player.load("https://s3.ap-northeast-2.amazonaws.com/gb-tv-dev/35/broadcasts/1011/index.m3u8");
      }
    });
    mButton = (Button) findViewById(R.id.button);
    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mFill) {
          mPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
        } else {
          mPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        }
        mFill = !mFill;
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
