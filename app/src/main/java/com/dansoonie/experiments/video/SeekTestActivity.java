package com.dansoonie.experiments.video;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dansoonie.experiments.R;

/**
 * Created by dansoonie on 17/6/23/.
 */

public abstract class SeekTestActivity extends Activity implements TextureView.SurfaceTextureListener {
  protected TextureView textureView;
  protected Button btnLoad;
  protected Button btnPlayPause;
  protected Button btnSeek;
  protected TextView tvInfo;
  protected SurfaceTexture surfaceTexture;

  protected int seekPosition;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.seektest);

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
        loadVideo();
      }
    });

    btnPlayPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        playPause();
      }
    });

    btnSeek.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        randomSeek();
      }
    });
  }

  protected void disableControls() {
    btnLoad.setEnabled(false);
    btnPlayPause.setEnabled(false);
    btnSeek.setEnabled(false);
  }

  abstract protected void loadVideo();

  abstract protected void playPause();

  abstract protected void randomSeek();

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
}
