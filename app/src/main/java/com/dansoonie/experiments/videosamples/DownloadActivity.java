package com.dansoonie.experiments.videosamples;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dansoonie.experiments.Log;
import com.dansoonie.experiments.R;

public class DownloadActivity extends AppCompatActivity {

  public static class Holder {
    public interface OnProgressUpdateListener {
      void onProgressUpdate(int progress);
    };

    protected FileInfo fileInfo;
    protected TextView fileNameTextView;
    protected Button downloadButton;
    protected ProgressBar downloadProgressBar;
    protected OnProgressUpdateListener onProgressUpdateListener = new OnProgressUpdateListener() {
      @Override
      public void onProgressUpdate(int progress) {
        downloadProgressBar.setProgress(progress);
        Log.i("download", "%d", progress);
        if (progress == 1000) {
          downloadButton.setText("Downloaded");
          downloadButton.setEnabled(false);
        }
      }
    };
    protected View.OnClickListener onDownloadClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String btnText = downloadButton.getText().toString();
        if (btnText.equals("Download")) {
          downloadButton.setText("Cancel");
          Videos.startDownload(view.getContext(), fileInfo);
        } else if (btnText.equals("Cancel")) {
          Videos.cancelDownload(fileInfo);
          downloadButton.setText("Download");
        }

      }
    };

  }

  public class VideoListAdapter extends ArrayAdapter<FileInfo> {

    private FileInfo[] data;

    public VideoListAdapter(Context context) {
      super(context, R.layout.list_item_file, Videos.getFileInfos());
      data = Videos.getFileInfos();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View row = convertView;
      Holder holder = null;

      if(row == null) {
        LayoutInflater inflater = getLayoutInflater();
        row = inflater.inflate(R.layout.list_item_file, parent, false);

        holder = new Holder();
        holder.fileNameTextView = (TextView)row.findViewById(R.id.tv_file_name);
        holder.downloadButton = (Button)row.findViewById(R.id.btn_download_file);
        holder.downloadProgressBar = (ProgressBar)row.findViewById(R.id.pb_download_progress);
        holder.downloadProgressBar.setMax(1000);

        row.setTag(holder);
      } else {
        holder = (Holder)row.getTag();
      }

      FileInfo fileInfo = data[position];
      holder.fileNameTextView.setText(fileInfo.getName());
      if (holder.fileInfo != null) {
        holder.fileInfo.setOnProgressUpdateListener(null);
      }
      holder.fileInfo = fileInfo;
      if (fileInfo.isDownloaded()) {
        holder.downloadButton.setText("Downloaded");
        holder.downloadButton.setEnabled(false);
        holder.downloadButton.setOnClickListener(null);
        holder.downloadProgressBar.setProgress(1000);
        holder.fileInfo.setProgress(1000);
        holder.fileInfo.setOnProgressUpdateListener(null);
      } else {
        if (fileInfo.isDownloading()) {
          holder.downloadButton.setText("Cancel");
        } else {
          holder.downloadButton.setText("Download");
        }
        holder.downloadButton.setEnabled(true);
        holder.downloadButton.setOnClickListener(holder.onDownloadClickListener);
        holder.downloadProgressBar.setProgress(0);
        holder.fileInfo.setProgress(0);
        holder.fileInfo.setOnProgressUpdateListener(holder.onProgressUpdateListener);
      }

      return row;
    }
  }

  private ListView listView;
  private VideoListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_download);
    Videos.check(this);
    listView = (ListView) findViewById(R.id.file_list);
    adapter = new VideoListAdapter(this);
    listView.setAdapter(adapter);
  }
}
