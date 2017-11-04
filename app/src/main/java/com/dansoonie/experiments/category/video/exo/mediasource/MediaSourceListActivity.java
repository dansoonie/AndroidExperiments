package com.dansoonie.experiments.category.video.exo.mediasource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MediaSourceListActivity extends Activity {

  public class Sample {
    public final String NAME;
    public final String URI;

    public Sample(String name, String uri) {
      NAME = name;
      URI = uri;
    }
  }

  public class SampleViewHolder extends RecyclerView.ViewHolder {

    private TextView mName;
    private TextView mUri;

    public SampleViewHolder(View itemView) {
      super(itemView);
      mName = (TextView) itemView.findViewById(android.R.id.text1);
      mUri = (TextView) itemView.findViewById(android.R.id.text2);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(MediaSourceListActivity.this, ExoPlayerTestMediaSourceActivity.class);
          intent.putExtra("uri", mUri.getText().toString());
          startActivity(intent);
        }
      });
    }

    protected void bind(Sample sample) {
      mName.setText(sample.NAME);
      mUri.setText(sample.URI);
    }
  }

  public class SamplesAdapter extends RecyclerView.Adapter<SampleViewHolder> {

    private Sample[] mData;

    public SamplesAdapter(Sample[] samples) {
      mData = samples;
    }

    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.two_line_list_item, parent, false);
      return new SampleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
      holder.bind(mData[position]);
    }

    @Override
    public int getItemCount() {
      return mData.length;
    }
  }

  private Sample[] mSamples = {
      new Sample("Dizzy", "https://html5demos.com/assets/dizzy.mp4"),
      new Sample("a", "b"),
      new Sample("a", "b")
  };
  private RecyclerView mRecyclerView;
  private SamplesAdapter mAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mRecyclerView = new RecyclerView(this);
    setContentView(mRecyclerView);
    mAdapter = new SamplesAdapter(mSamples);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
  }
}
