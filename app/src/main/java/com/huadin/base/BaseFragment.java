package com.huadin.base;

import android.content.Context;
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

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

import static com.google.common.base.Preconditions.checkNotNull;


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
   */
  protected void initToolbar(@NonNull Toolbar toolbar, int toolbarTitleResId)
  {
    checkNotNull(toolbar, "toolbar cannot null");
    toolbar.setTitle(toolbarTitleResId);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        pop();
      }
    });
  }

  /**
   * 打开抽屉
   */
  protected void initToolbarHome(@NonNull Toolbar toolbar, int toolbarTitleResId,
                                 final FragmentActivity activity)
  {
    checkNotNull(toolbar, "toolbar cannot null");
    checkNotNull(activity, "activity cannot null");
    toolbar.setTitle(toolbarTitleResId);
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

  /**
   * eventBus 回调
   *
   * @param eventCenter EventCenter
   */
  private void fragmentOnEvent(EventCenter eventCenter)
  {

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

}
