package com.huadin.login;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.huadin.base.BaseActivity;
import com.huadin.eventbus.EventCenter;
import com.huadin.util.ActivityUtils;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends BaseActivity
{

  @BindView(R.id.login_toolbar)
  Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    initToolBar();
    initFragment();
  }

  private void initToolBar()
  {
    mToolbar.setNavigationIcon(R.drawable.icon_bg_left);
//    mToolbar.setTitle(getString(R.string.back));
    mToolbar.setNavigationOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        finish();
      }
    });
  }

  private void initFragment()
  {
    LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
            .findFragmentById(R.id.fragment_ground);

    if (loginFragment == null)
    {
      loginFragment = LoginFragment.newInstance();
      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              loginFragment, R.id.fragment_ground);
    }

    new LoginPresenter(loginFragment);
  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_login;
  }

  //eventBus回调
  @Override
  protected void onEventComming(EventCenter eventCenter)
  {
    super.onEventComming(eventCenter);
    if (eventCenter.getEventCode() == EventCenter.EVENT_CODE_LOAD_REGISTER)
    {

      RegisterFragment registerFragment = RegisterFragment.newInstance();
      ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
              registerFragment, R.id.fragment_ground);
      new RegisterPresenter(registerFragment);
    }
  }
}

