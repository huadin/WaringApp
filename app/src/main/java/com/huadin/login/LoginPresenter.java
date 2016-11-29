package com.huadin.login;


import com.huadin.util.AMUtils;
import com.huadin.waringapp.R;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 登录
 */

public class LoginPresenter implements LoginContract.Presenter
{

  private LoginContract.View mLoginView;

  public LoginPresenter(LoginContract.View loginView)
  {
    this.mLoginView = loginView;
    mLoginView = checkNotNull(loginView, "loginView cannot be null");
    mLoginView.setPresenter(this);
  }

  @Override
  public void start()
  {
    login();
  }

  private void login()
  {
    int errorRes = 0;
    String loginName = mLoginView.getLoginName();
    String loginPassword = mLoginView.getLoginPassword();

    if (AMUtils.isEmpty(loginName))
    {
      errorRes = R.string.login_name_not_null;
    } else if (!AMUtils.isMobile(loginName))
    {
      errorRes = R.string.login_name_error;
    } else if (AMUtils.isEmpty(loginPassword))
    {
      errorRes = R.string.login_password_not_null;
    } else if (AMUtils.validatePassword(loginPassword))
    {
      errorRes = R.string.login_password_length_error;
    }

    if (errorRes != 0)
    {
      mLoginView.loginError(errorRes);
      return;
    }
    //显示 dialog
    mLoginView.showLoading();

    // TODO: 2016/11/29 登录
  }


}
