package com.huadin.userinfo;

import android.os.Bundle;

import com.huadin.base.BaseFragment;

/**
 * 用户信息
 */

public class UserInfoFragment extends BaseFragment
{

  public static UserInfoFragment newInstance()
  {
    Bundle args = new Bundle();
    UserInfoFragment fragment = new UserInfoFragment();
    fragment.setArguments(args);
    return fragment;
  }

}
