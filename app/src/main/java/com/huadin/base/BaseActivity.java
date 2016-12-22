package com.huadin.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huadin.eventbus.EventCenter;
import com.huadin.util.NetworkUtil;
import com.huadin.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity extends SupportActivity
{

  protected static String LOG_TAG = null;
  protected Context mContext;
  protected ToastUtil mToast;
  protected boolean isNetwork;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mContext = getApplicationContext();
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

  @Override
  protected void onResume()
  {
    super.onResume();
    mToast.onResume();
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    mToast.onPause();
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

  //eventBus 调用
  protected void onEventComming(EventCenter eventCenter)
  {

  }

  //启动 Activity
  protected void startActivity(Class<?> cls)
  {
    Intent intent = new Intent(this, cls);
    startActivity(intent);
  }

}
