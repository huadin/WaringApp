package com.huadin.login;

import android.os.Bundle;

import com.huadin.base_ui.BaseActivity;
import com.huadin.waringapp.R;

import butterknife.ButterKnife;


public class LoginActivity extends BaseActivity
{


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_login;
  }


}

