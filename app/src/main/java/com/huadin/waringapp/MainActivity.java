package com.huadin.waringapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.huadin.base.BaseActivity;
import com.huadin.bean.Person;
import com.huadin.login.LoginFragment;
import com.huadin.login.LoginPresenter;
import com.huadin.login.MapFragment;
import com.huadin.userinfo.UserInfoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        MapFragment.OnFragmentOpenDrawerListener

{

//  int[][] status = new int[][]{
//          new int[]{-android.R.attr.state_checked},
//          new int[]{android.R.attr.state_checked}};
//
//  int[] colors = new int[]{getResources().getColor(R.color.colorPrimaryDark),
//          getResources().getColor(R.color.test_color)};


  @BindView(R.id.drawer_layout)
  DrawerLayout mDrawer;
  @BindView(R.id.nav_view)
  NavigationView mNavigationView;
  private Person mUser;
  //用户名字后一位
  private TextView nameAfter;
  //用户名
  private TextView userName;
//  private ColorStateList cls;


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    mUser = BmobUser.getCurrentUser(Person.class);
//    cls = getResources().getColorStateList(R.color.colorPrimaryDark);

    //初始化View
    initView();
    //申请定位权限
    checkLocationPermission();
    initFragment(savedInstanceState);
  }

  private void checkLocationPermission()
  {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) !=
            PackageManager.PERMISSION_GRANTED)
    {
      //申请授权
      int PERMISSION_REQUEST_CODE = 0x127;
      ActivityCompat.requestPermissions(this,
              new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }
  }

  private void initFragment(Bundle savedInstanceState)
  {
    if (savedInstanceState == null)
    {
      loadRootFragment(R.id.fragment_ground, MapFragment.newInstance());
    }
  }

  private void initView()
  {
    mNavigationView.setNavigationItemSelectedListener(this);

    nameAfter = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name_after);
    userName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.user_name);
    nameAfter.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        // TODO: 2016/12/3 判断是否已经登录,已登录则无需跳转
        closeDrawer();
        if (mUser != null)
        {
          //进入个人信息
          UserInfoFragment infoFragment = UserInfoFragment.newInstance();
          start(infoFragment);
        } else
        {
          //登录注册
          LoginFragment loginFragment = LoginFragment.newInstance();
          new LoginPresenter(loginFragment);
          start(loginFragment);
        }
      }
    });

  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_main;
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item)
  {

    switch (item.getItemId())
    {
      // TODO: 2016/12/11 设置点击Item 的样式
    }

    mDrawer.closeDrawer(GravityCompat.START);
    return true;
  }

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
  protected void onStop()
  {
    super.onStop();
    closeDrawer();
  }

  //地图回调方法,打开抽屉
  @Override
  public void onOpenDrawer()
  {
    if (!mDrawer.isDrawerOpen(GravityCompat.START))
    {
      if (mUser != null)
      {
        String personName = mUser.getUsername();
        userName.setText(personName);
        int startLength = personName.length() - 1;
        String nameEnd = personName.substring(startLength);
        nameAfter.setText(nameEnd);
      }
      mDrawer.openDrawer(GravityCompat.START);
    }
  }

  /*关闭抽屉*/
  private void closeDrawer()
  {
    if (mDrawer.isDrawerOpen(GravityCompat.START))
    {
      mDrawer.closeDrawer(GravityCompat.START);
    }
  }


}
