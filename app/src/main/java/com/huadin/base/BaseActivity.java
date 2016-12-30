package com.huadin.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.huadin.eventbus.EventCenter;
import com.huadin.util.NetworkUtil;
import com.huadin.util.ToastUtil;
import com.huadin.waringapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseActivity extends SupportActivity
{

  protected static final String TITLE_KEY = "TITLE_KEY";
  protected static String LOG_TAG = null;
  protected Context mContext;
  protected ToastUtil mToast;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mContext = getApplicationContext();
    LOG_TAG = this.getClass().getSimpleName();
    mToast = new ToastUtil(mContext);
    EventBus.getDefault().register(this);

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
    startActivity(cls, 0);
  }

  /**
   * @param cls        目标Activity
   * @param titleResId Toolbar title resId
   */
  protected void startActivity(Class<?> cls, int titleResId)
  {
    Intent intent = new Intent(this, cls);
    intent.putExtra(TITLE_KEY, titleResId);
    startActivity(intent);
  }

  //获取网络是否连接
  protected boolean isNetwork()
  {
    return NetworkUtil.getNetworkState(mContext);
  }

  protected void initToolbar(Toolbar toolbar, int titleResId)
  {
    checkNotNull(toolbar, "toolbar cannot be null");
    toolbar.setTitle(titleResId);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });

  }

  /**
   * 显示信息
   *
   * @param resId 资源ID
   */
  protected void showMessage(int resId)
  {
    mToast.showMessage(resId, 500);
  }

  /**
   * 显示信息
   *
   * @param msg 消息
   */
  protected void showMessage(String msg)
  {
    mToast.showMessage(msg, 500);
  }
}
