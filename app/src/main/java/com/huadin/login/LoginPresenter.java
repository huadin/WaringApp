package com.huadin.login;


import android.content.Context;

import com.huadin.base.InstallationListener;
import com.huadin.bean.Person;
import com.huadin.bean.PushInstallation;
import com.huadin.util.AMUtils;
import com.huadin.util.InstallationUtil;
import com.huadin.util.LogUtil;
import com.huadin.util.MD5util;
import com.huadin.waringapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;
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
    String errorMsg = e.getMessage();
    LogUtil.e(TAG, "onError: errorMsg = " + e.getMessage());
    if (e.getErrorCode() == 9015)
    {
      int start = errorMsg.indexOf(":") + 1;
      int end = errorMsg.indexOf(",");
      String errorCode = errorMsg.substring(start, end);
      showCode(Integer.valueOf(errorCode));
    }
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
  public void installationSuccess(String userName)
  {
    BmobPushManager<PushInstallation> pushManager = new BmobPushManager<>();
    BmobQuery<PushInstallation> query = PushInstallation.getQuery();
    query.addWhereEqualTo("pushUserName", userName);
    pushManager.setQuery(query);

    JSONObject jsonObject = new JSONObject();
    try
    {
      jsonObject.put(mContext.getString(R.string.push_result), AMUtils.getDeviceId(mContext))// DeviceId
              .put(mContext.getString(R.string.push_type), mContext.getString(R.string.push_type_login))//推送类型
              .put(mContext.getString(R.string.push_title), mContext.getString(R.string.push_login_key))//标题
              .put(mContext.getString(R.string.push_content), mContext.getString(R.string.push_login_content));//内容

    } catch (JSONException e)
    {
      e.printStackTrace();
    }

    pushManager.pushMessage(jsonObject, new PushListener()
    {
      @Override
      public void done(BmobException e)
      {
        if (e != null)
        {
          LogUtil.i(TAG, "登录推送 code = " + e.getErrorCode() + " / message = " + e.getMessage());
        }
      }
    });

    mLoginView.hindLoading();
    mLoginView.loginSuccess();
  }

  @Override
  public void installationError(int code)
  {
    showCode(code);
  }
}
