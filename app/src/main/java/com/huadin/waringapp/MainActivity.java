package com.huadin.waringapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.huadin.base.BaseActivity;
import com.huadin.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
{

  private static final String TAG = "MainActivity";
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
    MenuItem menuItem = menu.findItem(R.id.action_search_view);
    SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
    {
      @Override
      public boolean onQueryTextSubmit(String query)
      {
        Log.i(TAG, "onQueryTextSubmit: ");
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText)
      {
        Log.i(TAG, "onQueryTextChange: ");
        return false;
      }
    });
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    int id = item.getItemId();
    if (id == R.id.action_settings)
    {
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
}
