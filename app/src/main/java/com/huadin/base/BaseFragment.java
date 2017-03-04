package com.huadin.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.eventbus.EventCenter;
import com.huadin.interf.OnFragmentOpenDrawerListener;
import com.huadin.util.NetworkUtil;
import com.huadin.util.ToastUtil;
import com.huadin.waringapp.R;
import com.huadin.widget.LoadDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportFragment;


public abstract class BaseFragment extends SupportFragment
{
  protected ToastUtil mToast;
  protected BaseActivity mContext;
  protected String LOG_TAG;

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);
    mContext = (BaseActivity) context;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
    mToast = new ToastUtil(mContext);
    LOG_TAG = getClass().getSimpleName();
  }

  /**
   * 初始化toolbar
   *
   * @param toolbar           Toolbar
   * @param toolbarTitleResId 标题
   * @param isCloseActivity   是否关闭当前Activity
   */
  protected void initToolbar(@NonNull Toolbar toolbar, int toolbarTitleResId, final boolean isCloseActivity)
  {
    mContext.initToolbar(toolbar, toolbarTitleResId, isCloseActivity);
  }

  /**
   * 打开抽屉
   */
  protected void initToolbarHome(@NonNull Toolbar toolbar, int toolbarTitleResId,
                                 @NonNull final FragmentActivity activity)
  {
    toolbar.setTitle(toolbarTitleResId);
    toolbar.setTitleTextColor(getResources().getColor(R.color.app_text_color));
    toolbar.setNavigationIcon(R.drawable.icon_home_72px);
    toolbar.setNavigationOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        if (activity instanceof OnFragmentOpenDrawerListener)
        {
          ((OnFragmentOpenDrawerListener) activity).onOpenDrawer();
        }
      }
    });
  }


  /**
   * 获取 Fragment 的 View
   *
   * @param inflater  LayoutInflater
   * @param container ViewGroup
   * @param resId     int
   * @return view
   */
  protected View getViewResId(LayoutInflater inflater, @Nullable ViewGroup container, int resId)
  {
    return inflater.inflate(resId, container, false);
  }

  /**
   * 显示 dialog
   *
   * @param resId int
   */
  protected void showLoading(int resId)
  {
    LoadDialog.show(mContext, getString(resId));
  }

  /**
   * 关闭 dialog
   */
  protected void dismissLoading()
  {
    LoadDialog.dismiss(mContext);
  }

  @Override
  public void onResume()
  {
    super.onResume();
    mToast.onResume();
  }

  @Override
  public void onPause()
  {
    super.onPause();
    mToast.onPause();
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
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


  @Subscribe()
  public void defaultOnEvent(EventCenter eventCenter)
  {
    if (eventCenter != null)
    {
      fragmentOnEvent(eventCenter);
    }
  }

  /**
   * eventBus 回调
   *
   * @param eventCenter EventCenter
   */
  protected void fragmentOnEvent(EventCenter eventCenter)
  {

  }

  /**
   * 启动服务获取网络数据
   */
  protected void startService(String areaId, String type, String startTime, String scope)
  {
    mContext.startService(areaId, type, startTime, scope);
  }

  /**
   * 查看网络状态
   *
   * @return boolean
   */
  protected boolean isNetwork()
  {
    return NetworkUtil.getNetworkState(mContext);
  }

  /**
   * 设置权限界面
   */
  protected void settingPermission()
  {
    Intent localIntent = new Intent();
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    if (Build.VERSION.SDK_INT > 9)
    {
      localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
      localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
    }
    startActivity(localIntent);
  }

  /**
   * 弹出顶部 fragment
   */
  protected void popTopFragment()
  {
    mContext.popTopFragment();
  }

  /**
   * 锁定抽屉
   */
  protected void lockDrawer()
  {
    mContext.lockDrawer();
  }

  protected void unLockDrawer()
  {
    mContext.unLockDrawer();
  }

}
