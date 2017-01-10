package com.huadin.login;


import android.content.Context;

import com.huadin.base.InstallationListener;
import com.huadin.bean.Person;
import com.huadin.util.AMUtils;
import com.huadin.util.InstallationUtil;
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

class LoginPresenter implements LoginContract.Presenter, InstallationListener
{

  private static final String TAG = "LoginPresenter";
  private LoginContract.View mLoginView;
  private Context mContext;

  LoginPresenter(Context context, LoginContract.View loginView)
  {
    this.mLoginView = loginView;
    this.mContext = context;
    mContext = checkNotNull(context, "context cannot be null");
    mLoginView = checkNotNull(loginView, "loginView cannot be null");
    mLoginView.setPresenter(this);
  }

  @Override
  public void start()
  {
    String loginName = mLoginView.getLoginName();
    int errorResId = 0;

    if (AMUtils.isEmpty(loginName))
    {
      errorResId = R.string.login_name_not_null;
    } else if (!AMUtils.isMobile(loginName) && !AMUtils.isUserName(loginName))
    {
      errorResId = R.string.login_name_error;
    }

    if (errorResId != 0)
    {
      mLoginView.loginError(errorResId);
      return;
    }

    login(loginName);
  }

  /* 执行登录 */
  private void login(final String loginName)
  {
    int errorRes = 0;

    String loginPassword = mLoginView.getLoginPassword();
    boolean isNetwork = mLoginView.networkIsAvailable();


    if (AMUtils.isEmpty(loginPassword))
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
        //登录异常
        BmobException e = new BmobException(throwable);
        errorCode(e);
      }

      @Override
      public void onNext(Person person)
      {
        //登录成功，绑定推送
        InstallationUtil.newInstance()
                .with(mContext)
                .addInstallationListener(LoginPresenter.this)
                .bindingPush(loginName);
      }
    });
  }

  /* 获取错误码 */
  private void errorCode(BmobException e)
  {
    LogUtil.e(TAG, "onError: errorMsg = " + e.getMessage());
    // TODO: 2017/1/10 code码需要提取
    int code = e.getErrorCode();
    showCode(code);
  }

  /* 根据错误码显示异常信息 */
  private void showCode(int code)
  {
    mLoginView.hindLoading();
    switch (code)
    {
      case 101:
        mLoginView.hindLoading();
        mLoginView.loginError(R.string.error_code_101);
        break;
    }
  }

  @Override
  public void installationSuccess()
  {
    mLoginView.hindLoading();
    mLoginView.loginSuccess();
  }

  @Override
  public void installationError(int code)
  {
    showCode(code);
  }
}
