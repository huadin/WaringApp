package com.huadin.userinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户信息
 */

public class UserInfoFragment extends BaseFragment
{
  @BindView(R.id.user_info_collapsing)
  CollapsingToolbarLayout mInfoCollapsing;
  //  @BindView(R.id.user_info_image)
//  ImageView mInfoImage;
  @BindView(R.id.user_info_toolbar)
  Toolbar mInfoToolbar;
//  @BindView(R.id.user_info_recycler)
//  RecyclerView mInfoRecyclerView;

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
    View view = getViewResId(inflater, container, R.layout.user_info_fragment);
    ButterKnife.bind(this, view);
    initViews();
    return view;
  }

  private void initViews()
  {
    initToolbar(mInfoToolbar, R.string.user_info_title);
    mInfoCollapsing.setTitle(getString(R.string.user_info_title));
    mInfoCollapsing.setCollapsedTitleTextColor(getResources().getColor(R.color.app_text_color));


  }


}
