package com.huadin.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.huadin.bean.Message;
import com.huadin.bean.Person;
import com.huadin.database.WaringAddress;
import com.huadin.login.MainActivity;
import com.huadin.userinfo.UpdateUserInfoActivity;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

import static android.provider.MediaStore.Audio.AudioColumns.TITLE_KEY;

public class PushIntentService extends IntentService
{
  private static final String TAG = "PushIntentService";
  private static final String KEY_PART_ON = "PART_ON";
  private static final String KEY_ALL_ON = "KEY_ALL_ON";
  private static final String KEY_SWITCH_STATE = "SWITCH_STATE";
  private Context mContext;

  public PushIntentService()
  {
    super("");
    mContext = this;
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent)
  {

    if (intent == null) return;
    Person person = BmobUser.getCurrentUser(Person.class);
    Message message = intent.getParcelableExtra("push");
    String pushType = message.getType();
    switch (Integer.valueOf(pushType))
    {
      case 2:
        //停电报修
        if (startOrStopPush(mContext)) break;
        repairNotify(mContext, message, person);
        break;

      case 3:
        //接收管理员发布的信息
        // TODO: 2017/2/8 管理员自己也可以收到消息
        if (startOrStopPush(mContext)) break;
        if (message.getArea().equals(person.getAreaId()))
        {
          sendNotification(mContext, MainActivity.class, new int[]{Intent.FLAG_ACTIVITY_SINGLE_TOP},
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
  private void sendNotification(@NonNull Context context, Class<?> cls, int[] flags, int toolbarTitleResId,
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


  /**
   * 启动或关闭推送及开启免打扰
   *
   * @param context Context
   * @return true - 关闭推送或开启免打扰
   */
  private boolean startOrStopPush(@NonNull Context context)
  {
    SharedPreferences preferences = context.getSharedPreferences(KEY_SWITCH_STATE, Context.MODE_APPEND);

    boolean isAllOn = preferences.getBoolean(KEY_ALL_ON, true);
    boolean isPartOn = preferences.getBoolean(KEY_PART_ON, false);

    if (!isAllOn) return true;
    //免打扰开启
    return (isPartOn && compareTime());
  }

  /**
   * 比较时间是否在 22:00 - 07:00时间段内
   *
   * @return true - 是，false - 否
   */
  private boolean compareTime()
  {
    long timeLong = getServerTime();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
    String time = sdf.format(new Date(timeLong));
    LogUtil.i(TAG, "time = " + time);
    int compare_1 = time.compareTo("22;00"); // >=0
    int compare_2 = time.compareTo("07:00"); // <=0
    LogUtil.i(TAG, "compare = " + (compare_1 >= 0 || compare_2 <= 0));
    return (compare_1 >= 0 || compare_2 <= 0);
  }

  /**
   * 获取服务器时间
   *
   * @return 服务器当前毫秒值
   */
  private long getServerTime()
  {
    final long[] timeLong = {0};
    Bmob.getServerTime(new QueryListener<Long>()
    {
      @Override
      public void done(Long aLong, BmobException e)
      {
        if (e == null)
        {
          synchronized (KEY_ALL_ON)
          {
            timeLong[0] = aLong * 1000L;
            KEY_ALL_ON.notify();
          }
        } else
        {
          timeLong[0] = System.currentTimeMillis();
        }
      }
    });
    synchronized (KEY_ALL_ON)
    {
      try
      {
        if (timeLong[0] == 0) KEY_ALL_ON.wait();
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }

    return timeLong[0];
  }


  @Override
  public void onDestroy()
  {
    super.onDestroy();
    LogUtil.i(TAG, "PushIntentService -- onDestroy --");
  }
}
