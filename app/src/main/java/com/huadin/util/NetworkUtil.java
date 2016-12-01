package com.huadin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 获取网络状态
 */
public class NetworkUtil
{
  public static boolean getNetworkState(Context context)
  {
    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = manager.getActiveNetworkInfo();
    return (null != info && info.isAvailable());
  }
}
