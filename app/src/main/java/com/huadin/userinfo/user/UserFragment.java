package com.huadin.userinfo.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huadin.base.BaseFragment;
import com.huadin.bean.Person;
import com.huadin.waringapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;

/**
 * Created by NCL on 2017/1/2.
 * 用户信息
 */

public class UserFragment extends BaseFragment
{
  @BindView(R.id.user_login_account_content)
  TextView mLoginAccount;
  @BindView(R.id.user_binding_phone_content)
  TextView mUserPhone;
  @BindView(R.id.user_login_account)
  LinearLayout mUserNameLayout;

  private Person mPerson;

  public static UserFragment newInstance()
  {
    Bundle args = new Bundle();

    UserFragment fragment = new UserFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context)
  {
    super.onAttach(context);
    //设置fragment横向动画
    _mActivity.setFragmentAnimator(new DefaultHorizontalAnimator());
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    mPerson = Person.getCurrentUser(Person.class);
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.user_fragment);
    ButterKnife.bind(this, view);

    return view;
  }

  @Override
  public void onResume()
  {
    super.onResume();
    initViews();
  }

  private void initViews()
  {
    if (mPerson == null) return;
    mLoginAccount.setText(mPerson.getUsername());
    mUserPhone.setText(mPerson.getMobilePhoneNumber());

    if (mPerson.isUserNameChange())
    {
      // 不可点击
      mUserNameLayout.setClickable(false);
    }
  }


  @OnClick({R.id.user_login_account})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.user_login_account:
        //设置用户名
        UpdateUserNameFragment fragment = UpdateUserNameFragment.newInstance();
        new UpdateUserNamePresenter(fragment);
        start(fragment);
        break;
    }
  }


}
