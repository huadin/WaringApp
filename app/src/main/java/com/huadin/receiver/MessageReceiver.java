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
import com.huadin.bean.Message;
import com.huadin.bean.Person;
import com.huadin.database.WaringAddress;
import com.huadin.eventbus.EventCenter;
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
    if (intent.getAction().equals(PushConstants.ACTION_MESSAGE))
    {
      String msg = intent.getStringExtra("msg");
      LogUtil.i(TAG, "msg = " + msg);
      Message message = new Gson().fromJson(msg, Message.class);
      paresMessage(context, message);
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

        Intent intent = new Intent(context, UpdateUserInfoActivity.class);
        intent.putExtra(TITLE_KEY, R.string.fault_info);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.push_report_title))
                .setContentText(context.getString(R.string.push_report_content))
                .setSmallIcon(R.drawable.icon_logo_small)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_logo_large))
                .setDefaults(NotificationCompat.DEFAULT_ALL)//设置震动等,默认
                .setContentIntent(pIntent)
                .setAutoCancel(true)//点击后消失
                .build();

        manager.notify(1, notification);
      }
    }
  }

}

