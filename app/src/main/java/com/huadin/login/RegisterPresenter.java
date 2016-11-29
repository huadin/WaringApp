package com.huadin.login;

import android.util.Log;

import com.huadin.bean.Person;
import com.huadin.util.AMUtils;
import com.huadin.util.MD5util;
import com.huadin.waringapp.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.greenrobot.eventbus.EventBus.TAG;

public class RegisterPresenter implements RegisterContract.Presenter
{

  private RegisterContract.View mRegisterView;

  public RegisterPresenter(RegisterContract.View registerView)
  {
    this.mRegisterView = registerView;
    mRegisterView = checkNotNull(registerView, "registerView cannot be null");
    mRegisterView.setPresenter(this);
  }

  @Override
  public void start()
  {
    try
    {
      register();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void register() throws Exception
  {
    int errorRes = 0;
    String registerPhone = mRegisterView.userName();
    String registerPassword = mRegisterView.userPassword();

    if (AMUtils.isEmpty(registerPhone))
    {
      errorRes = R.string.login_name_not_null;
    } else if (!AMUtils.isMobile(registerPhone))
    {
      errorRes = R.string.login_name_error;
    } else if (AMUtils.isEmpty(registerPassword))
    {
      errorRes = R.string.login_password_not_null;
    } else if (AMUtils.validatePassword(registerPassword))
    {
      errorRes = R.string.login_password_length_error;
    }

    if (errorRes != 0)
    {
      mRegisterView.inputError(errorRes);
      return;
    }

    Person person = new Person();
    person.setUsername(registerPhone);
    person.setPassword(MD5util.getMD5(registerPassword));//密码加密
    person.setUserPermisson(false);
    mRegisterView.showLoading();
    person.signUp(new SaveListener<Person>()
    {
      @Override
      public void done(Person person, BmobException e)
      {
        mRegisterView.hindLoading();
        if (e == null)
        {
          mRegisterView.registerSuccess();
        }else
        {
          int code = e.getErrorCode();
          Log.i(TAG, "done: error = " + e.getMessage() + "/ code = " + e.getErrorCode());
          switch (code)
          {
            case 202:
              mRegisterView.registerError("用户名已存在");
              break;
          }


        }
      }
    });
  }
}
