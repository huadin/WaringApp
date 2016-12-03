package com.huadin.login;


import com.huadin.bean.Person;
import com.huadin.util.AMUtils;
import com.huadin.util.LogUtil;
import com.huadin.util.MD5util;
import com.huadin.waringapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import rx.Subscriber;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 登录
 */

public class LoginPresenter implements LoginContract.Presenter
{

  private static final String TAG = "LoginPresenter";
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
    boolean isNetwork = mLoginView.networkIsAvailable();

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
    } else if (!isNetwork)
    {
      errorRes = R.string.no_network;
    }

    if (errorRes != 0)
    {
      mLoginView.loginError(errorRes);
      return;
    }
    //显示 dialog
    mLoginView.showLoading();

    BmobUser user = new BmobUser();
    user.setUsername(loginName);
    user.setPassword(MD5util.getMD5(loginPassword));
    user.loginObservable(Person.class).subscribe(new Subscriber<Person>()
    {
      @Override
      public void onCompleted()
      {

      }

      @Override
      public void onError(Throwable throwable)
      {

        BmobException e = new BmobException(throwable);
        String errorMsg = e.getMessage();
        if (e.getErrorCode() == 9015)
        {
          int start = errorMsg.indexOf(":") + 1;
          int end = errorMsg.indexOf(",");
          String errorCode = errorMsg.substring(start, end);

          switch (Integer.valueOf(errorCode))
          {
            case 101:
              mLoginView.hindLoading();
              mLoginView.loginError(R.string.error_code_101);
              break;
          }
        }
        LogUtil.e(TAG, "onError: errorMsg = " + errorMsg);
      }

      @Override
      public void onNext(Person person)
      {
        mLoginView.hindLoading();
        mLoginView.loginSuccess();
      }
    });
  }

}
