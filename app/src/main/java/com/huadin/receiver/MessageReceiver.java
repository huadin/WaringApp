package com.huadin.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huadin.bean.Message;
import com.huadin.bean.Person;
import com.huadin.eventbus.EventCenter;
import com.huadin.service.PushIntentService;
import com.huadin.util.AMUtils;
import com.huadin.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

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
    try
    {
      if (intent.getAction().equals(PushConstants.ACTION_MESSAGE))
      {
        String msg = intent.getStringExtra("msg");
        LogUtil.i(TAG, "msg = " + msg);
        Message message = new Gson().fromJson(msg, Message.class);
        paresMessage(context, message);
      }
    } catch (JsonSyntaxException e)
    {
      e.printStackTrace();
    }
  }

  private void paresMessage(@NonNull Context context, Message message)
  {
    String pushType = message.getType();
    switch (Integer.valueOf(pushType))
    {
      case 1:
        //检测是否在其他设备上登录
        Person person = Person.getCurrentUser(Person.class);
        String deviceId = AMUtils.getDeviceId(context);

        if (deviceId.equals(message.getResult()) || person == null) return;
        //退出当前账号
        EventBus.getDefault().post(new EventCenter(EventCenter.EVENT_CODE_OTHER_DEVICE_LOGIN));
        break;

      default:
        Intent intent = new Intent(context, PushIntentService.class);
        intent.putExtra("push", message);
        context.startService(intent);
        break;
    }
  }

}

