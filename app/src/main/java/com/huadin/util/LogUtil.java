package com.huadin.util;


import android.util.Log;

public class LogUtil
{
  private static boolean isTrue;

  public static void i(String tag, String msg)
  {
    if (!isTrue)//判断标记，发布后改变此标记
    {
      Log.i(tag, msg);
    }
  }

  public static void e(String tag, String msg)
  {
    if (!isTrue)//判断标记，发布后改变此标记
    {
      Log.e(tag, msg);
    }
  }

  public static void d(String tag, String msg)
  {
    if (!isTrue)//判断标记，发布后改变此标记
    {
      Log.d(tag, msg);
    }
  }

  public static void w(String tag, String msg)
  {
    if (!isTrue)//判断标记，发布后改变此标记
    {
      Log.w(tag, msg);
    }
  }
}
