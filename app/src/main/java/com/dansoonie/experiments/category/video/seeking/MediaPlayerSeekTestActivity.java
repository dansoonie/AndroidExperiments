package com.dansoonie.experiments.category.video.seeking;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;

import com.dansoonie.experiments.videosamples.Videos;

import java.io.IOException;

public class MediaPlayerSeekTestActivity extends SeekTestActivity {
  private MediaPlayer mediaPlayer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  protected void loadVideo() {
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

  @Override
  protected void randomSeek() {
    int duration = mediaPlayer.getDuration();
    seekPosition = (int) (duration * Math.random());
    mediaPlayer.seekTo(seekPosition);
  }

  @Override
  protected void playPause() {
    if (mediaPlayer.isPlaying()) {
      mediaPlayer.pause();
    } else {
      mediaPlayer.start();
    }
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
