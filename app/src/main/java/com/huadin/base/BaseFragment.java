package com.huadin.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.eventbus.EventCenter;
import com.huadin.util.ToastUtil;
import com.huadin.widget.LoadDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public abstract class BaseFragment extends Fragment
{
  protected ToastUtil mToast;
  protected BaseActivity mContext;
  protected boolean isNetwork;
  protected String LOG_TAG;

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);
    mContext = (BaseActivity)context;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
    mToast = new ToastUtil(mContext);
    isNetwork = mContext.isNetwork;
    LOG_TAG = getClass().getSimpleName();
  }

  /**
   *  获取 Fragment 的 View
   * @param inflater LayoutInflater
   * @param container ViewGroup
   * @param resId int
   * @return view
   */
  protected  View getViewResId (LayoutInflater inflater,@Nullable ViewGroup container,int resId)
  {
    return inflater.inflate(resId,container,false);
  }

  /**
   * 显示 dialog
   * @param resId int
   */
  protected void showLoading(int resId)
  {
    LoadDialog.show(mContext,getString(resId));
  }

  protected void dismissLoading()
  {
    LoadDialog.dismiss(mContext);
  }


  @Override
  public void onDestroy()
  {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe()
  public void defaultOnEvent(EventCenter eventCenter)
  {
    if (eventCenter != null)
    {
      fragmentOnEvent(eventCenter);
    }
  }

  private void fragmentOnEvent(EventCenter eventCenter)
  {

  }

}
