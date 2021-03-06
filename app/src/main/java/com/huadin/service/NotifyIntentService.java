package com.huadin.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.huadin.database.StopPowerBean;
import com.huadin.database.WaringAddress;
import com.huadin.eventbus.EventCenter;
import com.huadin.login.MainActivity;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.List;

public class NotifyIntentService extends IntentService
{
  private static final String TAG = "NotifyIntentService";
  protected static final String TITLE_KEY = "TITLE_KEY";

  public NotifyIntentService()
  {
    super("");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent)
  {
    WaringAddress waringAddress = DataSupport.findFirst(WaringAddress.class);
    if (waringAddress == null) return;
    String areaId = waringAddress.getWaringAreaId();
    String scope = waringAddress.getWaringAddress();

    List<StopPowerBean> beanList = DataSupport
            .where("scope like ? and orgCode = ?", "%" + scope + "%", areaId)
            .find(StopPowerBean.class);
    LogUtil.i(TAG, "beanList = " + beanList.toString());

    EventBus.getDefault().post(new EventCenter<>(
            EventCenter.EVENT_CODE_START_LONG_RUN_SERVICE, beanList.size()));
    //发送通知
    if (beanList.size() > 0) waringNotify();
  }

  private void waringNotify()
  {
    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    Intent intent = new Intent(this, MainActivity.class);
    intent.putExtra(TITLE_KEY,R.string.message_notify);
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    PendingIntent PI = PendingIntent.getActivity(this,0,intent,0);

    Notification notification = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.icon_logo_small)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo_large))
            .setContentText(this.getString(R.string.message_notify_content))//通知内容
            .setContentTitle(this.getString(R.string.message_notify_title))//标题
            .setDefaults(Notification.DEFAULT_ALL)//默认通知
            .setAutoCancel(true)//点击自动取消
            .setContentIntent(PI)
            .build();

    manager.notify(0, notification);
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    LogUtil.i(TAG, "NotifyIntentService -- onDestroy --");
  }
}
