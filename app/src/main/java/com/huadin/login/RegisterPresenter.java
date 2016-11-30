package com.huadin.login;

import android.util.Log;

import com.huadin.bean.Person;
import com.huadin.util.AMUtils;
import com.huadin.util.MD5util;
import com.huadin.waringapp.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import static com.google.common.base.Preconditions.checkNotNull;

public class RegisterPresenter implements RegisterContract.Presenter
{

  private RegisterContract.View mRegisterView;
  private static final String TAG = "RegisterPresenter";

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
    String registerCode = mRegisterView.registerCode();
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
    }else if (AMUtils.isEmpty(registerCode))
    {
      errorRes = R.string.register_code_not_null;
    }

    if (errorRes != 0)
    {
      mRegisterView.inputError(errorRes);
      return;
    }

    Person person = new Person();
    person.setUsername(registerPhone);
    person.setMobilePhoneNumber(registerPhone);
    person.setPassword(MD5util.getMD5(registerPassword));//密码加密
    person.setUserPermisson(false);
    mRegisterView.showLoading();
    person.signOrLogin(registerCode, new SaveListener<Person>()
    {
      @Override
      public void done(Person person, BmobException e)
      {
        mRegisterView.hindLoading();
        if (e == null)
        {
          mRegisterView.registerSuccess();
        } else
        {
          int code = e.getErrorCode();
          Log.i(TAG, "done: error = " + e.getMessage() + "/ code = " + e.getErrorCode());
          switch (code)
          {
            case 202:
              mRegisterView.registerError(R.string.error_code_202);
              break;
            case 9010:
              mRegisterView.registerError(R.string.error_code_9010);
              break;
            case 9016:
              mRegisterView.registerError(R.string.error_code_9016);
              break;
          }
        }
      }
    });

  }


  @Override
  public void getRegisterCode()
  {
    int errorRes = 0;
    String registerPhone = mRegisterView.userName();
    if (AMUtils.isEmpty(registerPhone))
    {
      errorRes = R.string.login_name_not_null;
    }

    if (errorRes != 0)
    {
      mRegisterView.inputError(errorRes);
      return;
    }
    String SMS_TEMPLATE_NAME = "短信验证";
    BmobSMS.requestSMSCode(registerPhone, SMS_TEMPLATE_NAME, new QueryListener<Integer>()
    {
      @Override
      public void done(Integer smsId, BmobException e)
      {
        if (e == null)
        {
          Log.i(TAG, "done: smsId = " + smsId);
        }else
        {
          Log.i(TAG, "done: error = " + e.getMessage());
        }
      }
    });
  }
}
