package com.dansoonie.experiments.videosamples;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dansoonie on 16/10/25/.
 */

public class FileInfo {
  private class DownloadTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private FileInfo fileInfo;
    private PowerManager.WakeLock mWakeLock;

    public DownloadTask(Context context, FileInfo fileInfo) {
      this.context = context;
      this.fileInfo = fileInfo;
    }

    @Override
    protected String doInBackground(String... params) {
      InputStream input = null;
      OutputStream output = null;
      HttpURLConnection connection = null;
      try {
        URL url = new URL(fileInfo.getUrl());
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        // expect HTTP 200 OK, so we don't mistakenly save error report
        // instead of the file
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
          return "Server returned HTTP " + connection.getResponseCode()
              + " " + connection.getResponseMessage();
        }

        // this will be useful to display download percentage
        // might be -1: server did not report the length
        int fileLength = connection.getContentLength();

        // download the file
        input = connection.getInputStream();
        output = new FileOutputStream(new File(context.getFilesDir(), fileInfo.getName()));

        byte data[] = new byte[4096];
        long total = 0;
        int count;
        while ((count = input.read(data)) != -1) {
          // allow canceling with back button
          if (isCancelled()) {
            publishProgress(0);
            input.close();
            return null;
          }
          total += count;
          // publishing the progress....
          if (fileLength > 0) {
            // only if total length is known
            float downloadProgress = (1000 * ((float)total / fileLength));
            publishProgress((int) downloadProgress);
          }
          output.write(data, 0, count);
        }
      } catch (Exception e) {
        return e.toString();
      } finally {
        try {
          if (output != null)
            output.close();
          if (input != null)
            input.close();
        } catch (IOException ignored) {
          ignored.printStackTrace();
        }

        if (connection != null) {
          connection.disconnect();
        }
        downloadTask = null;
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      Log.d("download", "on progress update: " + values[0]);
      fileInfo.setProgress(values[0]);
      if (fileInfo.progress == 1000) {
        fileInfo.downloaded = true;
      }
    }
  }

  protected String url;
  protected String name;
  protected int length;
  protected boolean downloaded;
  protected int progress;
  protected DownloadTask downloadTask;
  protected DownloadActivity.Holder.OnProgressUpdateListener progressUpdateListener;

  public FileInfo(String url, int length) {
    this.url = url;
    this.length = length;
    this.name = url.substring(url.lastIndexOf("/") + 1);
    downloaded = false;
  }

  public String getUrl() {
    return url;
  }

  public String getName() {
    return name;
  }

  public boolean isDownloaded() {
    return downloaded;
  }

  public boolean isDownloading() {
    if (downloadTask != null) {
      return true;
    }
    return false;
  }

  public void setProgress(int progress) {
    this.progress = progress;
    if (progressUpdateListener != null) {
      progressUpdateListener.onProgressUpdate(progress);
    }
  }

  public void setOnProgressUpdateListener(DownloadActivity.Holder.OnProgressUpdateListener listener) {
    progressUpdateListener = listener;
  }

  public int getProgress() {
    return progress;
  }

  public void startDownload(Context context) {
    downloadTask = new DownloadTask(context, this);
    downloadTask.execute();
  }

  public void cancelDownload() {
    if (downloadTask != null) {
      downloadTask.cancel(true);
    }
  }
}
