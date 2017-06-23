package com.dansoonie.experiments;


/**
 * Created by dansoonie on 17/5/25/.
 */

public final class Log {
  public static boolean ENABLE_DEBUG = false;
  public static boolean ENABLE_INFO = true;
  public static boolean ENABLE_WARNING = true;

  public static void d(String tag, String msg, Object... args) {
    if (ENABLE_DEBUG) {
      if (args.length > 0) {
        android.util.Log.d(tag, String.format(msg, args));
      } else {
        android.util.Log.d(tag, msg);
      }
    }
  }

  public static void i(String tag, String msg, Object... args) {
    if (ENABLE_INFO) {
      if (args.length > 0) {
        android.util.Log.i(tag, String.format(msg, args));
      } else {
        android.util.Log.i(tag, msg);
      }
    }
  }

  public static void w(String tag, String msg, Object... args) {
    if (ENABLE_WARNING) {
      if (args.length > 0) {
        android.util.Log.w(tag, String.format(msg, args));
      } else {
        android.util.Log.w(tag, msg);
      }
    }
  }

  public static void e(String tag, String msg, Object... args) {
    if (args.length > 0) {
      android.util.Log.e(tag, String.format(msg, args));
    } else {
      android.util.Log.e(tag, msg);
    }
  }
}
