package com.huadin.login;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.huadin.base.BaseActivity;
import com.huadin.base.BaseFragment;
import com.huadin.bean.Person;
import com.huadin.dialog.PromptFragment;
import com.huadin.eventbus.EventCenter;
import com.huadin.fault.ReportFragment;
import com.huadin.fault.ReportPresenter;
import com.huadin.interf.OnFragmentOpenDrawerListener;
import com.huadin.setting.SettingFragment;
import com.huadin.urgent.UrgentFragment;
import com.huadin.urgent.UrgentPresenter;
import com.huadin.userinfo.UserInfoActivity;
import com.huadin.util.ActivityCollector;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnFragmentOpenDrawerListener, PromptFragment.PromptListener

{

  private int[][] status = new int[][]{
          new int[]{-android.R.attr.state_checked},
          new int[]{android.R.attr.state_checked}};

  @BindView(R.id.drawer_layout)
  DrawerLayout mDrawer;
  @BindView(R.id.nav_view)
  NavigationView mNavigationView;
  private Person mUser;
  //用户名字后一位
  private TextView nameAfter;
  //用户名
  private TextView userName;
  private ColorStateList cls;
  // 再点一次退出程序时间设置
  private static final long WAIT_TIME = 2000L;
  private long TOUCH_TIME = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    mUser = BmobUser.getCurrentUser(Person.class);

    //初始化item菜单样式
    initColorStatsList();

    //初始化View
    initView();

    initFragment(savedInstanceState);
  }

  private void initColorStatsList()
  {
    int colors[] = new int[]{getResources().getColor(R.color.item_menu_color),
            getResources().getColor(R.color.colorPrimaryDark)};
    cls = new ColorStateList(status, colors);
  }

  private void initFragment(Bundle savedInstanceState)
  {
    if (savedInstanceState == null)
    {
      //加载根Fragment
      loadRootFragment(R.id.fragment_ground, MapFragment.newInstance());
    }
  }

  private void initView()
  {
    mNavigationView.setNavigationItemSelectedListener(this);
    mNavigationView.setCheckedItem(R.id.map_home);
    mNavigationView.setItemTextColor(cls);
    mNavigationView.setItemIconTintList(cls);

    nameAfter = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name_after);
    userName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name);
    nameAfter.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        // TODO: 2016/12/3 判断是否已经登录,mUser != null
        closeDrawer();
        mDrawer.postDelayed(new Runnable()
        {
          @Override
          public void run()
          {
            if (mUser != null)
            {
              //进入个人信息
              startUserInfoActivity();
            } else
            {
              //登录注册
              startLoginFragment();
            }
          }
        }, 250);
      }
    });

    setUserName();
  }

  /**
   * 个人信息
   */
  private void startUserInfoActivity()
  {
    startActivity(UserInfoActivity.class);
  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_main;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull final MenuItem item)
  {
    mDrawer.closeDrawer(GravityCompat.START);
    // TODO: 2016/12/15 停电报修和设置进出栈有bug,会先进栈后出栈，有动画残留
    mDrawer.postDelayed(new Runnable()
    {
      @Override
      public void run()
      {
        switch (item.getItemId())
        {
          case R.id.blackout_repair:
            startReportFragment();//停电报修
            break;
          case R.id.map_home://地图
            MapFragment mapFragment = findFragment(MapFragment.class);
            start(mapFragment, SupportFragment.SINGLETASK);
            break;
          case R.id.setting://设置
            startSettingFragment();
            break;

          case R.id.urgent_info://个人信息
            startUrgentFragment();
            break;
        }
      }
    }, 250);

    return true;
  }

  /**
   * 登录
   */
  private void startLoginFragment()
  {
    LoginFragment loginFragment = findFragment(LoginFragment.class);
    if (loginFragment == null)
    {
      loginFragment = LoginFragment.newInstance();
      new LoginPresenter(mContext, loginFragment);
      popTo(loginFragment);
    } else
    {
      start(loginFragment, SupportFragment.SINGLETASK);
    }
  }

  /**
   * 紧急信息
   */
  private void startUrgentFragment()
  {
    UrgentFragment urgentFragment = findFragment(UrgentFragment.class);

    if (urgentFragment == null)
    {
      urgentFragment = UrgentFragment.newInstance();
      new UrgentPresenter(urgentFragment);
      popTo(urgentFragment);
    } else
    {
      start(urgentFragment, SupportFragment.SINGLETASK);
    }
  }

  /**
   * 启动设置Fragment
   */
  private void startSettingFragment()
  {
    SettingFragment settingFragment = findFragment(SettingFragment.class);
    if (settingFragment == null)
    {
      settingFragment = SettingFragment.newInstance();
      // TODO: 2016/12/15 没有添加 Presenter

      popTo(settingFragment);
    } else
    {
      start(settingFragment, SupportFragment.SINGLETASK);
    }
  }

  /**
   * 启动停电报修
   */
  private void startReportFragment()
  {
    ReportFragment reportFragment = findFragment(ReportFragment.class);
    if (reportFragment == null)
    {
      reportFragment = ReportFragment.newInstance();
      new ReportPresenter(reportFragment);
      popTo(reportFragment);
    } else
    {
      start(reportFragment, SupportFragment.SINGLETASK);
    }
  }

  /**
   * 启动Fragment
   */
  private void popTo(final SupportFragment fragment)
  {
    popTo(MapFragment.class, false, new Runnable()
    {
      @Override
      public void run()
      {
        start(fragment);
      }
    });
  }


  @Override
  protected void onStop()
  {
    super.onStop();
    closeDrawer();
  }

  /**
   * 登录或退出回调事件
   *
   * @param eventCenter EventCenter
   */
  @Override
  protected void onEventComming(EventCenter eventCenter)
  {
    int code = eventCenter.getEventCode();
    mUser = BmobUser.getCurrentUser(Person.class);
    switch (code)
    {
      //登录成功
      case EventCenter.EVENT_CODE_LOGIN_SUCCESS:
        setUserName();
        break;

      //退出成功
      case EventCenter.EVENT_CODE_OUT_SUCCESS:
        clearUserName();
        break;

      //强制退出
      case EventCenter.OTHER_DEVICE_LOGIN:
        PromptFragment fragment = PromptFragment.newInstance(getString(R.string.push_login_content));
        fragment.setOnPromptListener(this);
        fragment.show(getSupportFragmentManager(), getClass().getSimpleName());
        break;
    }
  }

  /*地图回调方法,打开抽屉*/
  @Override
  public void onOpenDrawer()
  {
    if (!mDrawer.isDrawerOpen(GravityCompat.START))
    {
      setUserName();
      mDrawer.openDrawer(GravityCompat.START);
    }
  }

  /*显示用户姓名*/
  private void setUserName()
  {
    if (mUser != null)
    {
      String personName = mUser.getUsername();
      userName.setText(personName);
      int startLength = personName.length() - 1;
      String nameEnd = personName.substring(startLength);
      nameAfter.setText(nameEnd);
    }
  }

  /*清除用户名设置*/
  private void clearUserName()
  {
    nameAfter.setText(R.string.user_name);
    userName.setText(R.string.user_name_1);
  }

  /*关闭抽屉*/
  private void closeDrawer()
  {
    if (mDrawer.isDrawerOpen(GravityCompat.START))
    {
      mDrawer.closeDrawer(GravityCompat.START);
    }
  }

  /*回退或退出程序*/
  @Override
  public void onBackPressedSupport()
  {
    //关闭抽屉
    if (mDrawer.isDrawerOpen(GravityCompat.START))
    {
      mDrawer.closeDrawer(GravityCompat.START);
    } else
    {
      Fragment topFragment = getTopFragment();
      if (topFragment instanceof BaseFragment)
      {
        mNavigationView.setCheckedItem(R.id.map_home);
      }
      if (getSupportFragmentManager().getBackStackEntryCount() > 1)
      {
        pop();
      } else
      {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME)
        {
          finish();
        } else
        {
          TOUCH_TIME = System.currentTimeMillis();
          showMessage(R.string.press_again_exit);
        }
      }
    }
  }

  //弹出强制下线的 dialog 确认回调
  @Override
  public void promptOk()
  {

    Person.logOut();
    mUser = null;
    clearUserName();
    for (Activity activity : ActivityCollector.sActivities)
    {
      if (activity instanceof MainActivity)
      {
        LogUtil.i(LOG_TAG, "弹出登录界面");
        startLoginFragment();
      } else
      {
        activity.finish();
      }
    }
  }
}
