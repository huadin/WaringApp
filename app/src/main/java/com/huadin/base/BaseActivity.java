package com.huadin.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.huadin.bean.Person;
import com.huadin.database.WaringAddress;
import com.huadin.eventbus.EventCenter;
import com.huadin.login.LoginFragment;
import com.huadin.service.HttpIntentService;
import com.huadin.setting.contact.ContactFragment;
import com.huadin.setting.feedback.FeedbackFragment;
import com.huadin.userinfo.address.AddressFragment;
import com.huadin.util.ActivityCollector;
import com.huadin.util.NetworkUtil;
import com.huadin.util.ToastUtil;
import com.huadin.waringapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import me.yokeyword.fragmentation.SupportActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BaseActivity extends SupportActivity
{

  protected static final String TITLE_KEY = "TITLE_KEY";
  protected static final String KEY_ADDRESS_AREA_SHARED = "Address_Shared";
  protected static final String KEY_ADDRESS_AREA_KEY = "SHARED_KEY";
  //解锁抽屉模式时使用
  protected DrawerLayout mBaseDrawerLayout;
  protected static String LOG_TAG = null;
  protected Context mContext;
  protected ToastUtil mToast;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ActivityCollector.addActivity(this);
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
    ActivityCollector.removeActivity(this);
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

  /**
   * 启动服务
   *
   * @param areaId    地区
   * @param type      类型
   * @param startTime 时间
   * @param scope     范围
   */
  protected void startService(@Nullable String areaId, @Nullable String type,
                              @Nullable String startTime, @Nullable String scope)
  {
    if (TextUtils.isEmpty(areaId))
    {
      //登录点查询网络上地址,否则查询本地
      Person person = Person.getCurrentUser(Person.class);
      if (person != null && !TextUtils.isEmpty(person.getAreaId()))
      {
        areaId = person.getAreaId();
      } else
      {
        WaringAddress waringAddress = DataSupport.findFirst(WaringAddress.class);
        areaId = waringAddress.getWaringAreaId();
      }
    }
    Intent intent = new Intent(this, HttpIntentService.class);
    intent.putExtra(getString(R.string.key_org_code), areaId);
    intent.putExtra(getString(R.string.key_type), type);
    intent.putExtra(getString(R.string.key_start_time), startTime);
    intent.putExtra(getString(R.string.key_scope), scope);
    startService(intent);
  }


  /**
   * 初始化toolbar
   *
   * @param toolbar         Toolbar
   * @param titleResId      标题
   * @param isCloseActivity 是否关闭当前Activity
   */
  protected void initToolbar(Toolbar toolbar, int titleResId, final boolean isCloseActivity)
  {
    checkNotNull(toolbar, "toolbar cannot be null");
    toolbar.setTitle(titleResId);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
    toolbar.setNavigationOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (isCloseActivity)
        {
          //关闭Activity
          finish();
        } else
        {
          popTopFragment();
        }
      }
    });

  }

  /**
   * 锁定抽屉
   */
  protected void lockDrawer()
  {
    mBaseDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
  }

  /**
   * 弹出顶部 fragment
   */
  protected void popTopFragment()
  {
    //解除抽屉锁定
    Fragment topFragment = getTopFragment();
    if (topFragment instanceof LoginFragment || topFragment instanceof AddressFragment ||
            topFragment instanceof ContactFragment || topFragment instanceof FeedbackFragment)
    {
      if (mBaseDrawerLayout != null)
      {
        mBaseDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
      }
    }
    //弹出Fragment
    pop();
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
