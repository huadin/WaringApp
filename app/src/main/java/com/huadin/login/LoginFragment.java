package com.huadin.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.eventbus.EventCenter;
import com.huadin.util.AMUtils;
import com.huadin.waringapp.R;
import com.huadin.widget.ClearEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.greenrobot.eventbus.EventBus.TAG;

public class LoginFragment extends BaseFragment implements LoginContract.View, TextWatcher
{

  @BindView(R.id.login_name)
  ClearEditText mNameET;
  @BindView(R.id.login_password)
  ClearEditText mPasswordET;

  private LoginContract.Presenter mPresenter;

  public static LoginFragment newInstance()
  {
    return new LoginFragment();
  }

  @Override
  public void setPresenter(@NonNull LoginContract.Presenter presenter)
  {
    mPresenter = checkNotNull(presenter);
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
    View view = getViewResId(inflater, container, R.layout.login_fragment_layout);
    ButterKnife.bind(this, view);
    mNameET.addTextChangedListener(this);
    return view;
  }

  @Override
  public void onResume()
  {
    super.onResume();
    mToast.onResume();
    Log.i(LOG_TAG, "onResume: ");
  }

  @Override
  public void onPause()
  {
    super.onPause();
    mToast.onPause();
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.loading_login);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void loginSuccess()
  {
    //登录成功
    mToast.showMessage(R.string.login_success, 500);
  }

  @Override
  public void loginError(int errorMsg)
  {
    //登录异常
    mToast.showMessage(errorMsg, 500);
  }

  @Override
  public String getLoginName()
  {
    return mNameET.getText().toString();
  }

  @Override
  public String getLoginPassword()
  {
    return mPasswordET.getText().toString();
  }

  @Override
  public boolean networkIsAvailable()
  {
    return isNetwork;
  }

  @OnClick({R.id.login_app, R.id.to_register})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.login_app:
        mPresenter.start();
        break;

      case R.id.to_register:
        EventBus.getDefault().post(new EventCenter(EventCenter.EVENT_CODE_LOAD_REGISTER));
        break;
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after)
  {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count)
  {
    if (s.length() == 11)
    {
      AMUtils.onInactive(getApplicationContext(), mNameET);
    }
  }

  @Override
  public void afterTextChanged(Editable s)
  {

  }
}
