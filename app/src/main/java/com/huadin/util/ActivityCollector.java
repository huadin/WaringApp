package com.huadin.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NCL on 2017/1/14.
 * Activity 管理器
 */

public class ActivityCollector
{
  public static List<Activity> sActivities = new ArrayList<>();

  /**
   * 添加到 Activity集合
   *
   * @param activity Activity
   */
  public static void addActivity(Activity activity)
  {
    sActivities.add(activity);
  }

  /**
   * 移除 Activity
   *
   * @param activity Activity
   */
  public static void removeActivity(Activity activity)
  {
    sActivities.remove(activity);
  }

  /**
   * finish 全部 Activity
   */
  public static void finishAll()
  {
    for (Activity activity : sActivities)
    {
      if (!activity.isFinishing())
      {
        activity.finish();
      }
    }
  }

}
