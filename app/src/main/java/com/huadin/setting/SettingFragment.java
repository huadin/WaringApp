package com.huadin.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.setting.contact.ContactFragment;
import com.huadin.setting.feedback.FeedbackFragment;
import com.huadin.setting.feedback.FeedbackPresenter;
import com.huadin.userinfo.address.AddressFragment;
import com.huadin.userinfo.address.AddressPresenter;
import com.huadin.util.LogUtil;
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
          R.id.contact_us_linear_layout, R.id.feedback_linear_layout, R.id.app_update})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.address_linear_layout:
        startAddressFragment();//预警
        break;
      case R.id.msg_linear_layout://消息设置

        break;
      case R.id.contact_us_linear_layout://联系我们
        startContactFragment();
        break;
      case R.id.feedback_linear_layout://意见反馈
        startFeedbackFragment();
        break;

      case R.id.app_update:
        LogUtil.i(LOG_TAG, "******************");
        break;
    }
    //锁定抽屉
    lockDrawer();
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
    new AddressPresenter(mContext, addressFragment);
  }

  /**
   * 联系我们
   */
  private void startContactFragment()
  {
    ContactFragment contactFragment = findFragment(ContactFragment.class);
    if (contactFragment == null)
    {
      contactFragment = ContactFragment.newInstance();
      start(contactFragment, SupportFragment.SINGLETASK);
    }
  }

  /**
   * 意见反馈
   */
  private void startFeedbackFragment()
  {
    FeedbackFragment feedbackFragment = findFragment(FeedbackFragment.class);
    if (feedbackFragment == null)
    {
      feedbackFragment = FeedbackFragment.newInstance();
      start(feedbackFragment, SupportFragment.SINGLETASK);
    }
    new FeedbackPresenter(feedbackFragment);
  }


}