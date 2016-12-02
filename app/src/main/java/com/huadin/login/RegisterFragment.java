package com.huadin.login;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.base.BaseFragment;
import com.huadin.util.AMUtils;
import com.huadin.waringapp.R;
import com.huadin.widget.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterFragment extends BaseFragment implements RegisterContract.View, TextWatcher
{
  @BindView(R.id.register_login_name)
  ClearEditText mRegisterName;
  @BindView(R.id.register_login_password)
  ClearEditText mRegisterPassword;
  @BindView(R.id.register_code)
  ClearEditText mRegister;
  @BindView(R.id.request_register_code)
  TextView codeText;

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
    mRegisterName.addTextChangedListener(this);
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
    getActivity().finish();
  }

  @Override
  public void registerError(int errorCode)
  {
    mToast.showMessage(errorCode, 500);
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
  public String registerCode()
  {
    return mRegister.getText().toString();
  }

  @Override
  public boolean networkIsAvailable()
  {
    return isNetwork;
  }

  @Override
  public void codeOnFinish()
  {
    codeText.setEnabled(true);
    codeText.setText(R.string.send_phone_code);
  }

  @Override
  public void codeOnTick(long m)
  {
    codeText.setText(String.valueOf(m));
  }

  @Override
  public void inputError(int errorRes)
  {
    codeText.setEnabled(true);
    mToast.showMessage(errorRes,500);
  }


  @Override
  public void setPresenter(RegisterContract.Presenter presenter)
  {
    mPresenter = checkNotNull(presenter);
  }

  @OnClick({R.id.register_app,R.id.request_register_code})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.request_register_code:
        codeText.setEnabled(false);
        mPresenter.getRegisterCode();
        break;
      case R.id.register_app:
        mPresenter.start();
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
      AMUtils.onInactive(getApplicationContext(), mRegisterName);
    }
  }

  @Override
  public void afterTextChanged(Editable s)
  {

  }
}
