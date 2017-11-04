package com.dansoonie.experiments.category.video.customplayer;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by dansoonie on 17/8/7/.
 */

public class ExoHlsPlayer extends FrameLayout implements Player.EventListener{
  private static final String TAG = "ExoHlsPlayer";

  private Context mContext;
  private String mUserAgent;
  private CustomSimpleExoPlayerView mPlayerView;
  private SimpleExoPlayer mPlayer;
  private DataSource.Factory mMediaDataSourceFactory;
  private MediaSource mMediaSource;
  private TrackSelector mTrackSelector;
  private boolean mShouldAutoPlay;
  private int mResumeWindow;
  private long mResumePosition;
  private boolean mResumeOnResume = false;
  private OnPlayerInitializedListener mOnPlayerInitializedListener;
  private OnCompleteListener mOnCompleteListener;
  private OnBufferingListener mOnBufferingListener;
  private int mState;

  public interface OnPlayerInitializedListener {
    void onInitialized(ExoHlsPlayer player);
  }

  public interface OnCompleteListener {
    void onComplete(ExoHlsPlayer player);
  }

  public interface OnBufferingListener {
    void onBufferingStart(ExoHlsPlayer player);
    void onBufferingEnd(ExoHlsPlayer player);
  }

  public void setOnPlayerInitializedListener(OnPlayerInitializedListener listener) {
    mOnPlayerInitializedListener = listener;
  }

  public void setOnCompleteListener(OnCompleteListener listener) {
    mOnCompleteListener = listener;
  }

  public void setOnBufferingListener(OnBufferingListener listener) {
    mOnBufferingListener = listener;
  }

  public ExoHlsPlayer(@NonNull Context context) {
    super(context);
    onCreate(context);
  }

  public ExoHlsPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    onCreate(context);
  }

  public ExoHlsPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    onCreate(context);
  }

  public void onCreate(Context context) {
    mContext = context;
    mShouldAutoPlay = true;
    mUserAgent = Util.getUserAgent(mContext, "ExoPlayerDemo");
    mMediaDataSourceFactory = buildDataSourceFactory(null);
    mPlayerView = new CustomSimpleExoPlayerView(context);
    addView(mPlayerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
  }

  public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultDataSourceFactory(mContext, bandwidthMeter,
        buildHttpDataSourceFactory(bandwidthMeter));
  }

  public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultHttpDataSourceFactory(mUserAgent, bandwidthMeter);
  }

  private void initializePlayer() {
    boolean needNewPlayer = mPlayer == null;
    if (needNewPlayer) {
      mTrackSelector = new DefaultTrackSelector();

      mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, new DefaultTrackSelector());
      mPlayer.addListener(this);

      mPlayerView.setPlayer(mPlayer);
      mPlayer.setPlayWhenReady(mShouldAutoPlay);
      mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
    }
    if (mOnPlayerInitializedListener != null) {
      mOnPlayerInitializedListener.onInitialized(this);
    }
  }

  public void setResizeMode(int resizeMode) {
    mPlayerView.setResizeMode(resizeMode);
  }

  public void load(String path) {
    Uri uri = Uri.parse(path);
    mMediaSource = new HlsMediaSource(uri, mMediaDataSourceFactory, null, null);
    mPlayer.prepare(mMediaSource);
  }

  public void onStart() {
    Log.d(TAG, "onStart");
    if (Util.SDK_INT > 23) {
      initializePlayer();
    }
  }

  public void onResume() {
    Log.d(TAG, "onResume");
    if ((Util.SDK_INT <= 23 || mPlayer == null)) {
      initializePlayer();
    }
  }

  public void onPause() {
    Log.d(TAG, "onPause");
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  public void onStop() {
    Log.d(TAG, "onStop");
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  private void releasePlayer() {
    if (mPlayer != null) {
      mShouldAutoPlay = mPlayer.getPlayWhenReady();
      if (mResumeOnResume) {
        updateResumePosition();
      }
      mPlayer.release();
      mPlayer = null;
      mTrackSelector = null;
    }
  }

  private void updateResumePosition() {
    mResumeWindow = mPlayer.getCurrentWindowIndex();
    mResumePosition = mPlayer.isCurrentWindowSeekable() ? Math.max(0, mPlayer.getCurrentPosition()) : C.TIME_UNSET;
  }

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
    if (mState == Player.STATE_BUFFERING && playbackState != Player.STATE_BUFFERING) {
      Log.d(TAG, "Buffering end state");
      if (mOnBufferingListener != null) {
        mOnBufferingListener.onBufferingEnd(this);
      }
    }
    switch (playbackState) {
      case Player.STATE_ENDED:
        Log.d(TAG, "Ended state");
        if (mOnCompleteListener != null) {
          mOnCompleteListener.onComplete(this);
        }
        break;
      case Player.STATE_BUFFERING:
        Log.d(TAG, "Buffering start state");
        if (mOnBufferingListener != null) {
          mOnBufferingListener.onBufferingStart(this);
        }
        break;
      case Player.STATE_IDLE:
        Log.d(TAG, "Idle state");
        break;
      case Player.STATE_READY:
        Log.d(TAG, "Ready state");
        break;
    }
    mState = playbackState;
  }

  @Override
  public void onRepeatModeChanged(int repeatMode) {

  }

  @Override
  public void onPlayerError(ExoPlaybackException error) {
    Log.d(TAG, "On Error");
    Log.d(TAG, error.toString());
    mPlayer.prepare(mMediaSource);
  }

  @Override
  public void onPositionDiscontinuity() {

  }

  @Override
  public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

  }
}
