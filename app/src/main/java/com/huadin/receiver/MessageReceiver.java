package com.huadin.receiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huadin.bean.Message;
import com.huadin.bean.Person;
import com.huadin.database.WaringAddress;
import com.huadin.eventbus.EventCenter;
import com.huadin.login.MainActivity;
import com.huadin.userinfo.UpdateUserInfoActivity;
import com.huadin.util.AMUtils;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import cn.bmob.push.PushConstants;

public class MessageReceiver extends BroadcastReceiver
{
  private static final String TAG = "MessageReceiver";
  private static final String TITLE_KEY = "TITLE_KEY";

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
    Person person = Person.getCurrentUser(Person.class);
    String pushType = message.getType();
    switch (Integer.valueOf(pushType))
    {
      case 1:
        //检测是否在其他设备上登录
        String deviceId = AMUtils.getDeviceId(context);

        if (deviceId.equals(message.getResult()) || person == null) return;
        //退出当前账号
        EventBus.getDefault().post(new EventCenter(EventCenter.OTHER_DEVICE_LOGIN));
        break;

      case 2:
        //停电报修
        repairNotify(context, message, person);
        break;

      case 3:
        //接收管理员发布的信息
        // TODO: 2017/2/8 管理员自己也可以收到消息
        if (message.getArea().equals(person.getAreaId()))
        {
          sendNotification(context, MainActivity.class, new int[]{Intent.FLAG_ACTIVITY_SINGLE_TOP},
                  R.string.fault_info, R.string.push_release_title, R.string.push_release_content);
        }

        break;
    }
  }

  /**
   * 停电报修
   *
   * @param context Context
   * @param message 消息实体
   * @param person  用户
   */
  private void repairNotify(@NonNull Context context, Message message, Person person)
  {
    if (person == null) return;

//        WaringAddress address = DataSupport.where("isLocal = ?", "0").findFirst(WaringAddress.class);
    WaringAddress address = DataSupport.findFirst(WaringAddress.class);
    //找到相同地区
    if (address != null && address.getWaringArea().equals(message.getArea()))
    {
      //找到管理员
      if (person.isUserPermission())
      {
        sendNotification(context, UpdateUserInfoActivity.class,
                new int[]{Intent.FLAG_ACTIVITY_SINGLE_TOP, Intent.FLAG_ACTIVITY_NEW_TASK},
                R.string.fault_info, R.string.push_report_title, R.string.push_report_content);
      }
    }
  }

  /**
   * 发送通知，并可点击
   *
   * @param context            Context
   * @param cls                点击后跳转的 Activity
   * @param flags              Activity启动模式
   * @param toolbarTitleResId  toolbar 标题资源Id
   * @param notifyTitleResId   通知标题的id
   * @param notifyContentResId 通知内容的id
   */
  private void sendNotification(Context context, Class<?> cls, int[] flags, int toolbarTitleResId,
                                int notifyTitleResId, int notifyContentResId)
  {
    Intent intent = new Intent(context, cls);
    intent.putExtra(TITLE_KEY, toolbarTitleResId);
    for (int flag : flags)
    {
      intent.addFlags(flag);
    }
    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notification = new NotificationCompat.Builder(context)
            .setContentTitle(context.getString(notifyTitleResId))
            .setContentText(context.getString(notifyContentResId))
            .setSmallIcon(R.drawable.icon_logo_small)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_logo_large))
            .setDefaults(NotificationCompat.DEFAULT_ALL)//响铃及震动等
            .setAutoCancel(true)//点击消失
            .setContentIntent(pi)
            .build();

    manager.notify(0, notification);

  }


}

