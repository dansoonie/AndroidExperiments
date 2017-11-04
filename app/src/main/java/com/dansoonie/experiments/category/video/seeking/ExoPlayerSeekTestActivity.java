package com.dansoonie.experiments.category.video.seeking;

import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;

import com.dansoonie.experiments.videosamples.Videos;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerSeekTestActivity extends SeekTestActivity {

  protected SimpleExoPlayer exoPlayer;
  protected long playbackPosition;
  protected boolean playWhenReady;
  protected int currentWindow;
  private boolean seeked;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void loadVideo() {
    exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
    exoPlayer.setVideoSurface(new Surface(surfaceTexture));
    exoPlayer.prepare(buildMediaSource(Uri.parse(Videos.getPath(this, 2))), true, false);

    btnLoad.setEnabled(false);
    btnPlayPause.setEnabled(true);
    btnSeek.setEnabled(true);
    exoPlayer.addListener(new ExoPlayer.EventListener() {
      @Override
      public void onTimelineChanged(Timeline timeline, Object manifest) {

      }

      @Override
      public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

      }

      @Override
      public void onLoadingChanged(boolean isLoading) {

      }

      @Override
      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY) {
          if (seeked) {
            long currentPosition = exoPlayer.getCurrentPosition();
            long error = currentPosition - seekPosition;
            String msg = String.format("Requested: %d, SeekedTo: %d, Error: %d", seekPosition, currentPosition, error);
            tvInfo.setText(msg);
            seeked = false;
          }
        }
      }

      @Override
      public void onRepeatModeChanged(int repeatMode) {

      }

      @Override
      public void onPlayerError(ExoPlaybackException error) {

      }

      @Override
      public void onPositionDiscontinuity() {

      }

      @Override
      public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

      }
    });
  }

  private MediaSource buildMediaSource(Uri uri) {
    return new ExtractorMediaSource(uri,
        new DefaultDataSourceFactory(this, Util.getUserAgent(this, "test")),
        new DefaultExtractorsFactory(), null, null);
  }

  @Override
  protected void playPause() {
    exoPlayer.setPlayWhenReady(!exoPlayer.getPlayWhenReady());
  }

  @Override
  protected void randomSeek() {
    long duration = exoPlayer.getDuration();
    seekPosition = (int) (duration * Math.random());
    exoPlayer.seekTo(seekPosition);
    seeked = true;
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

  private void releasePlayer() {
    if (exoPlayer != null) {
      playbackPosition = exoPlayer.getCurrentPosition();
      currentWindow = exoPlayer.getCurrentWindowIndex();
      playWhenReady = exoPlayer.getPlayWhenReady();
      exoPlayer.release();
      exoPlayer = null;
    }
  }


}
