package com.huadin.waringapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.huadin.base_ui.BaseActivity;
import com.huadin.bean.Person;
import com.huadin.login.LoginActivity;
import com.huadin.util.MD5util;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
{

  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.drawer_layout)
  DrawerLayout mDrawer;
  @BindView(R.id.nav_view)
  NavigationView mNavigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    //初始化View
    initView();
  }

  private void initView()
  {
    setSupportActionBar(mToolbar);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    mDrawer.addDrawerListener(toggle);
    toggle.syncState();
    mNavigationView.setNavigationItemSelectedListener(this);
    View nameAfter = mNavigationView.getHeaderView(0).findViewById(R.id.user_name_after);
    nameAfter.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        toActivity(mContext, LoginActivity.class);
      }
    });


  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_main;
  }

  @Override
  public void onBackPressed()
  {
//    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (mDrawer.isDrawerOpen(GravityCompat.START))
    {
      mDrawer.closeDrawer(GravityCompat.START);
    } else
    {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == R.id.action_settings)
    {
      testBmob();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.nav_camera:
        break;
      case R.id.nav_gallery:
        break;
      case R.id.nav_slideshow:
        break;
      case R.id.nav_manage:
        break;
      case R.id.nav_share:
        break;
      case R.id.nav_send:
        break;
    }

//    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

  private void testBmob()
  {
    Person p = new Person();
    p.setLogin_name("HuaDin_1");
    p.setUser_name("HD");
    p.setUser_password(MD5util.getMD5("123456"));
    p.setUser_address("海淀");
    p.setUser_phone("18600000000");
    p.setCreate_date("2016-11-26");
    p.setUser_permission(true);

    p.save(new SaveListener<String>()
    {
      @Override
      public void done(String objectId, BmobException e)
      {
        if (e == null)
        {
          mToast.showMessage("添加数据成功，返回objectId为：", 1000);
        } else
        {
          int errorCode = e.getErrorCode();
          if (errorCode == 401)
          {
            mToast.showMessage(R.string.register_name_exist,1000);
          }else
          {
            mToast.showMessage("创建数据失败：" + e.getMessage(), 1000);
          }
        }
      }
    });
  }
}
