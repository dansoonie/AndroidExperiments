package com.dansoonie.experiments.category.video.exo.mediasource;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by dansoonie on 17/11/4/.
 */

public class MediaSourceFactory {

  private final Context CONTEXT;
  private final String USER_AGENT;
  private final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

  private DataSource.Factory mMediaDataSourceFactory;
  private Handler mMainHandler;
  private EventLogger mEventLogger;

  private MediaSourceFactory(Context context, String userAgent) {
    CONTEXT = context;
    USER_AGENT = userAgent;
    mMediaDataSourceFactory = buildDataSourceFactory(true);
  }

  public static final MediaSourceFactory create(Context context, String userAgent) {
    return new MediaSourceFactory(context, userAgent);
  }

  public MediaSource buildMediaSource(Uri uri, String overrideExtension) {
    int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
        : Util.inferContentType("." + overrideExtension);
    switch (type) {
      case C.TYPE_SS:
        return new SsMediaSource(
            uri,
            buildDataSourceFactory(false),
            new DefaultSsChunkSource.Factory(mMediaDataSourceFactory),
            mMainHandler,
            mEventLogger);
      case C.TYPE_DASH:
        return new DashMediaSource(
            uri,
            buildDataSourceFactory(false),
            new DefaultDashChunkSource.Factory(mMediaDataSourceFactory),
            mMainHandler,
            mEventLogger);
      case C.TYPE_HLS:
        return new HlsMediaSource(
            uri,
            mMediaDataSourceFactory,
            mMainHandler,
            mEventLogger);
      case C.TYPE_OTHER:
        return new ExtractorMediaSource(
            uri,
            mMediaDataSourceFactory,
            new DefaultExtractorsFactory(),
            mMainHandler,
            mEventLogger);
      default: {
        throw new IllegalStateException("Unsupported type: " + type);
      }
    }
  }

  /**
   * Returns a new DataSource factory.
   *
   * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
   *     DataSource factory.
   * @return A new DataSource factory.
   */
  private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
    return buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
  }

  public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultDataSourceFactory(CONTEXT, bandwidthMeter,
        buildHttpDataSourceFactory(bandwidthMeter));
  }


  public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultHttpDataSourceFactory(USER_AGENT, bandwidthMeter);
  }
}
