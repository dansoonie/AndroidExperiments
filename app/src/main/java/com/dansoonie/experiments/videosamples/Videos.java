package com.dansoonie.experiments.videosamples;

import android.content.Context;

import java.io.File;

/**
 * Created by dansoonie on 16/10/21/.
 */

public class Videos {


  public static final int ANECHOIC_CHAMBER = 0; // 1280x720, 4m21s, 51MB
  public static final int VR_WARSHIP = 1; // 1920x1080, 7m39s, 107.1MB
  public static final int BUG_BUCK_BUNNY = 2; // 640x360, 1m, 5.5MB
  public static final int DRONE = 3; // 1280x720, 6m55s, 155.8MB
  public static final int AD_HELLO_MARKET = 4; // 640x360, 30s, 2MB
  public static final int AD_LG_QUICK_MEMO = 5; // 640x360, 31s, 2.6MB
  public static final int AD_ZIG_BANG = 6; // 640x360, 32s, 2.2MB

  public static FileInfo[] fileInfos = {
      new FileInfo("http://in.jocoos.com:8080/AnechoicChamber_SalfordUniversity.mp4", 50958995),
      new FileInfo("http://in.jocoos.com:8080/WorldOfWarships.mp4", 107085209),
      new FileInfo("http://in.jocoos.com:8080/big_buck_bunny.mp4", 5510872),
      new FileInfo("http://in.jocoos.com:8080/drone.mp4", 155796078),
      new FileInfo("http://in.jocoos.com:8080/ad/hello_market_640x360.mp4", 2040366),
      new FileInfo("http://in.jocoos.com:8080/ad/lg_quick_memo_640x360.mp4", 2577625),
      new FileInfo("http://in.jocoos.com:8080/ad/zigbang_640x360.mp4", 2238706)
  };

  public static final String[] list = {
      "Anechoic Chamber",
      "World of Warship",
      "Big Buck Bunny",
      "Drone",
      "Hello Market(ad)",
      "LG Quick Memo(ad)",
      "ZigBang(ad)"
  };

  public static String getURL(int videoId) {
    return fileInfos[videoId].getUrl();
  }

  public static String fileName(int videoId) {
    return fileInfos[videoId].name;
  }

  public static String getPath(Context context, int videoId) {
    File file = new File(context.getFilesDir(), fileName(videoId));
    return file.getPath();
  }

  public static FileInfo[] getFileInfos() {
    return fileInfos;
  }

  public static void check(Context context) {
    File dir = context.getFilesDir();
    for (FileInfo info : fileInfos) {
      File file = new File(dir.getAbsolutePath(), info.name);
      if (file.exists() && file.length() == info.length) {
        info.downloaded = true;
      }
    }
  }

  public static void startDownload(Context context, FileInfo fileInfo) {
    fileInfo.startDownload(context);
  }

  public static void cancelDownload(FileInfo fileInfo) {
    fileInfo.cancelDownload();
  }
}
