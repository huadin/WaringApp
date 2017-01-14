package com.huadin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.huadin.bean.Message;
import com.huadin.eventbus.EventCenter;
import com.huadin.util.AMUtils;
import com.huadin.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.push.PushConstants;

import static android.R.id.message;

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
      String msg = intent.getStringExtra("msg");
      LogUtil.i(TAG, "msg = " + msg);
      Message message = new Gson().fromJson(msg, Message.class);
      paresMessage(context,message);
    }
  }

  private void paresMessage (@NonNull Context context, Message message)
  {
    String pushType = message.getType();
    switch (Integer.valueOf(pushType))
    {
      case 1:
        //检测是否在其他设备上登录
        String deviceId = AMUtils.getDeviceId(context);
        if (deviceId.equals(message.getResult())) return;
        //退出当前账号
        EventBus.getDefault().post(new EventCenter(EventCenter.OTHER_DEVICE_LOGIN));
        break;
    }
  }

}
