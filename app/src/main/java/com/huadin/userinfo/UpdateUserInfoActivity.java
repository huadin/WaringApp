package com.huadin.userinfo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.huadin.base.BaseActivity;
import com.huadin.userinfo.password.UpdatePasswordFragment;
import com.huadin.userinfo.password.UpdatePasswordPresenter;
import com.huadin.userinfo.phone.UpdatePhoneFragment;
import com.huadin.userinfo.phone.UpdatePhonePresenter;
import com.huadin.userinfo.user.UserFragment;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateUserInfoActivity extends BaseActivity
{

  private int mTitleResId;

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ButterKnife.bind(this);
    initFragment();
  }

  private void initFragment()
  {
    mTitleResId = getIntent().getIntExtra(TITLE_KEY, 0);
    switch (mTitleResId)
    {
      case R.string.password_modify:
        //修改密码
        UpdatePasswordFragment updateFragment = UpdatePasswordFragment.newInstance();
        loadRootFragment(R.id.update_user_info_fragment_ground, updateFragment);
        new UpdatePasswordPresenter(updateFragment);
        break;

      case R.string.user_info_phone:
        UpdatePhoneFragment phoneFragment = UpdatePhoneFragment.newInstance();
        loadRootFragment(R.id.update_user_info_fragment_ground,phoneFragment);
        new UpdatePhonePresenter(phoneFragment);
        break;

      case R.string.user_info_user:
        UserFragment userFragment = UserFragment.newInstance();
        loadRootFragment(R.id.update_user_info_fragment_ground,userFragment);
        break;

    }
    initToolbar(mToolbar, mTitleResId);
  }

  @Override
  protected int getContentViewLayoutID()
  {
    return R.layout.activity_update_user_info;
  }


}
