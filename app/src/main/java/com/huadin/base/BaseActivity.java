package com.huadin.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huadin.eventbus.EventCenter;
import com.huadin.util.NetworkUtil;
import com.huadin.util.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseActivity extends AutoLayoutActivity
{
  /**
   * log tag
   */
  protected static String LOG_TAG = null;
  protected Context mContext;
  protected ToastUtil mToast;
  protected boolean isNetwork;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mContext = this;
    LOG_TAG = this.getClass().getSimpleName();
    mToast = new ToastUtil(mContext);
    EventBus.getDefault().register(this);
    isNetwork = NetworkUtil.getNetworkState(mContext);

    if (getContentViewLayoutID() != 0)
    {
      setContentView(getContentViewLayoutID());
    } else
    {
      throw new IllegalArgumentException(
              "You must return a right contentView layout resource Id");
    }
  }

  /**
   * Activity 布局Id
   *
   * @return int
   */
  protected abstract int getContentViewLayoutID();

  protected void toActivity(Context context, Class<?> cls)
  {
    Intent intent = new Intent(context, cls);
    startActivity(intent);
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe()
  public void defaultOnEvent(EventCenter eventCenter)
  {
    if (eventCenter != null)
    {
      onEventComming(eventCenter);
    }
  }

  protected void onEventComming(EventCenter eventCenter)
  {

  }
}
