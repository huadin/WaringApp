package com.huadin.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    return super.onCreateView(inflater, container, savedInstanceState);
  }


}
