package com.dansoonie.experiments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dansoonie.experiments.adapter.ItemAdapter;
import com.dansoonie.experiments.category.video.exo.ExoPlayerBehaviorTestActivity;
import com.dansoonie.experiments.category.video.exo.ExoPlayerHlsTestActivity;
import com.dansoonie.experiments.category.video.exo.mediasource.MediaSourceListActivity;
import com.dansoonie.experiments.model.Item;
import com.dansoonie.experiments.category.video.seeking.ExoPlayerSeekTestActivity;
import com.dansoonie.experiments.category.video.seeking.MediaPlayerSeekTestActivity;
import com.dansoonie.experiments.videosamples.DownloadActivity;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private ItemAdapter adapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    adapter = new ItemAdapter();
    adapter.setItems(createItems());

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
  }

  private static List<Item> createItems() {
    List<Item> items = new LinkedList<>();
    items.add(new Item("MediaPlayer Seek Test", MediaPlayerSeekTestActivity.class));
    items.add(new Item("ExoPlayer Seek Test", ExoPlayerSeekTestActivity.class));
    items.add(new Item("ExoPlayer Behavior Test", ExoPlayerBehaviorTestActivity.class));
    items.add(new Item("ExoPlayer HLS Test", ExoPlayerHlsTestActivity.class));
    items.add(new Item("Download Video Samples", DownloadActivity.class));
    items.add(new Item("ExoPlayer MediaSources Test", MediaSourceListActivity.class));

    return items;
  }
}
