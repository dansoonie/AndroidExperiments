package com.dansoonie.experiments.video;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dansoonie.experiments.R;
import com.dansoonie.experiments.videosamples.Videos;

import java.io.IOException;

public class MediaPlayerSeekTestActivity extends Activity implements TextureView.SurfaceTextureListener {

  private TextureView textureView;
  private Button btnLoad;
  private Button btnPlayPause;
  private Button btnSeek;
  private TextView tvInfo;
  private MediaPlayer mediaPlayer;
  private SurfaceTexture surfaceTexture;

  private int seekPosition;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mediaplayer_seektest);

    textureView = (TextureView) findViewById(R.id.textureView);
    textureView.setSurfaceTextureListener(this);
    btnLoad = (Button) findViewById(R.id.btn_load);
    btnPlayPause = (Button) findViewById(R.id.btn_play_pause);
    btnSeek = (Button) findViewById(R.id.btn_seek);
    tvInfo = (TextView) findViewById(R.id.tv_info);

    disableControls();
    btnLoad.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mediaPlayer = new MediaPlayer();
        try {
          mediaPlayer.setDataSource(Videos.getPath(MediaPlayerSeekTestActivity.this, 2));
          mediaPlayer.prepare();
          mediaPlayer.setSurface(new Surface(surfaceTexture));
          btnLoad.setEnabled(false);
          btnPlayPause.setEnabled(true);
          btnSeek.setEnabled(true);

          mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
              int currentPosition = mp.getCurrentPosition();
              int error = currentPosition - seekPosition;
              String msg = String.format("Requested: %d, SeekedTo: %d, Error: %d", seekPosition, currentPosition, error);
              tvInfo.setText(msg);
            }
          });
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    btnPlayPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mediaPlayer.isPlaying()) {
          mediaPlayer.pause();
        } else {
          mediaPlayer.start();
        }
      }
    });

    btnSeek.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int duration = mediaPlayer.getDuration();
        seekPosition = (int) (duration * Math.random());
        mediaPlayer.seekTo(seekPosition);
      }
    });
  }

  private void disableControls() {
    btnLoad.setEnabled(false);
    btnPlayPause.setEnabled(false);
    btnSeek.setEnabled(false);
  }
  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
    btnLoad.setEnabled(true);
    surfaceTexture = surface;
  }

  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

  }

  @Override
  public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    return false;
  }

  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) {

  }

  @Override
  protected void onPause() {
    super.onPause();
    mediaPlayer.pause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mediaPlayer.release();
  }
}
