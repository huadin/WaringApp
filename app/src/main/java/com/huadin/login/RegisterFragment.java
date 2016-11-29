package com.huadin.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huadin.base.BaseFragment;
import com.huadin.waringapp.R;
import com.huadin.widget.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterFragment extends BaseFragment implements RegisterContract.View
{
  @BindView(R.id.register_login_name)
  ClearEditText mRegisterName;
  @BindView(R.id.register_login_password)
  ClearEditText mRegisterPassword;

  private RegisterContract.Presenter mPresenter;

  public static RegisterFragment newInstance()
  {
    return new RegisterFragment();
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
    View view = getViewResId(inflater, container, R.layout.register_fragmnet_layout);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onResume()
  {
    super.onResume();
    mToast.onResume();
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
    showLoading(R.string.register_loading);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void registerSuccess()
  {
    //注册成功
    mToast.showMessage(R.string.register_success,500);
  }

  @Override
  public void registerError(String errorMsg)
  {
    mToast.showMessage(errorMsg, 500);
  }

  @Override
  public String userName()
  {
    return mRegisterName.getText().toString();
  }

  @Override
  public String userPassword()
  {
    return mRegisterPassword.getText().toString();
  }

  @Override
  public void inputError(int errorRes)
  {
    mToast.showMessage(errorRes,500);
  }

  @Override
  public void setPresenter(RegisterContract.Presenter presenter)
  {
    mPresenter = checkNotNull(presenter);
  }

  @OnClick(R.id.register_app)
  public void onClick(View view)
  {
    mPresenter.start();
  }

}
