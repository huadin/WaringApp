package com.huadin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.huadin.eventbus.EventCenter;

import org.greenrobot.eventbus.EventBus;


/**
 * 网络监听
 */

public class NetworkReceiver extends BroadcastReceiver
{
  private static final String NETWORK_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

  @Override
  public void onReceive(Context context, Intent intent)
  {
    if (intent.getAction().equals(NETWORK_ACTION))
    {
      ConnectivityManager manager = (ConnectivityManager) context
              .getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo info = manager.getActiveNetworkInfo();

      if (null != info && info.isConnected())
      {
        //有网络
        EventBus.getDefault().post(new EventCenter<Boolean>(EventCenter.EVENT_CODE_NETWORK,true));
      } else
      {
        //无网络
        EventBus.getDefault().post(new EventCenter<Boolean>(EventCenter.EVENT_CODE_NETWORK,false));
      }
    }
  }



}
