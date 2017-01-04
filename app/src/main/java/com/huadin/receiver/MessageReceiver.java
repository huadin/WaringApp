package com.huadin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.huadin.util.LogUtil;

import cn.bmob.push.PushConstants;

public class MessageReceiver extends BroadcastReceiver
{
  private static final String TAG = "MessageReceiver";

  public MessageReceiver()
  {
  }

  @Override
  public void onReceive(Context context, Intent intent)
  {
    if (intent.getAction().equals(PushConstants.ACTION_MESSAGE))
    {
      LogUtil.i(TAG,"msg = " + intent.getStringExtra("msg"));
    }
  }
}
