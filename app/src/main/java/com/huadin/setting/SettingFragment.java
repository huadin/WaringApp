package com.huadin.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.userinfo.address.AddressFragment;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 设置
 */

public class SettingFragment extends BaseFragment
{


  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;

  public static SettingFragment newInstance()
  {

    Bundle args = new Bundle();
    SettingFragment fragment = new SettingFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.setting_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbarHome(mToolbar, R.string.setting, mContext);
    return view;
  }


  @OnClick({R.id.address_linear_layout, R.id.msg_linear_layout,
          R.id.contact_us_linear_layout, R.id.feedback_linear_layout})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.address_linear_layout:
        startAddressFragment();//预警
        break;
      case R.id.msg_linear_layout:
        break;
      case R.id.contact_us_linear_layout:
        break;
      case R.id.feedback_linear_layout:
        break;

    }
  }

  /**
   * 启动预警地址
   */
  private void startAddressFragment()
  {
    AddressFragment addressFragment = findFragment(AddressFragment.class);
    if (addressFragment == null)
    {
      addressFragment = AddressFragment.newInstance(getString(R.string.setting_info_flag_key));
      start(addressFragment, SupportFragment.SINGLETASK);
    }
  }
}