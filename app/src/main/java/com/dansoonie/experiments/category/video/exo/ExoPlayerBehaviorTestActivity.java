package com.dansoonie.experiments.category.video.exo;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.dansoonie.experiments.Log;
import com.dansoonie.experiments.R;
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

public class ExoPlayerBehaviorTestActivity extends Activity implements TextureView.SurfaceTextureListener {
  private static final String TAG = "Exo";

  private TextureView textureView;
  private Button buttonLoadFile;
  private Button buttonLoadUrl;
  private Button buttonPlayPause;
  private Button buttonSeek;

  private SimpleExoPlayer exoPlayer;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.exo_behavior);

    Log.ENABLE_DEBUG = true;

    textureView = (TextureView) findViewById(R.id.texture_view);
    textureView.setSurfaceTextureListener(this);
    buttonLoadFile = (Button) findViewById(R.id.button_load_file);
    buttonLoadUrl = (Button) findViewById(R.id.button_load_url);
    buttonPlayPause = (Button) findViewById(R.id.button_play_pause);
    buttonSeek = (Button) findViewById(R.id.button_seek);

    buttonLoadFile.setEnabled(false);
    buttonLoadUrl.setEnabled(false);
    buttonPlayPause.setEnabled(false);
    buttonSeek.setEnabled(false);

    View.OnClickListener loadListener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (v.equals(buttonLoadFile)) {
          exoPlayer.prepare(buildMediaSource(Videos.getPath(getApplicationContext(), 1)));
        } else {
          exoPlayer.prepare(buildMediaSource(Videos.getURL(1)));
        }
        buttonLoadFile.setEnabled(false);
        buttonLoadUrl.setEnabled(false);
        buttonPlayPause.setEnabled(true);
        buttonSeek.setEnabled(true);
      }
    };
    buttonLoadFile.setOnClickListener(loadListener);
    buttonLoadUrl.setOnClickListener(loadListener);

    buttonPlayPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (exoPlayer.getPlayWhenReady()) {
          Log.d(TAG, "Pausing");
          exoPlayer.setPlayWhenReady(false);
        } else {
          Log.d(TAG, "Starting");
          exoPlayer.setPlayWhenReady(true);
        }
      }
    });

    buttonSeek.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        long duration = exoPlayer.getDuration();
        long seekPosition = (long) (duration * Math.random());
        exoPlayer.seekTo(seekPosition);
      }
    });

    exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
    exoPlayer.addListener(new ExoPlayer.EventListener() {
      @Override
      public void onTimelineChanged(Timeline timeline, Object manifest) {
        Log.d(TAG, "TimelineChanged");
      }

      @Override
      public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d(TAG, "TracksChanged");
      }

      @Override
      public void onLoadingChanged(boolean isLoading) {
        Log.d(TAG, "LoadingChanged");
      }

      @Override
      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String state;
        switch (playbackState) {
          case ExoPlayer.STATE_IDLE:
            state = "STATE_IDLE";
            break;
          case ExoPlayer.STATE_BUFFERING:
            state = "STATE_BUFFERING";
            break;
          case ExoPlayer.STATE_READY:
            state = "STATE_READY";
            break;
          case ExoPlayer.STATE_ENDED:
            state = "STATE_ENDED";
            break;
          default:
            state = "STATE_????";
        }
        Log.d(TAG, String.format("PlayerStateChanged - playWhenReady: %b, playbackState: %s", playWhenReady, state));
      }

      @Override
      public void onRepeatModeChanged(int repeatMode) {

      }

      @Override
      public void onPlayerError(ExoPlaybackException error) {
        Log.d(TAG, "PlayerError");
      }

      @Override
      public void onPositionDiscontinuity() {
        Log.d(TAG, "PositionDiscontinuity");
      }

      @Override
      public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.d(TAG, "PlaybackRarametersChanged");
      }
    });
  }

  private MediaSource buildMediaSource(String path) {
    Uri uri = Uri.parse(path);
    return new ExtractorMediaSource(uri,
        new DefaultDataSourceFactory(this, Util.getUserAgent(this, "test")),
        new DefaultExtractorsFactory(), null, null);
  }

  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
    buttonLoadFile.setEnabled(true);
    buttonLoadUrl.setEnabled(true);
    exoPlayer.setVideoSurface(new Surface(surface));
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
}
