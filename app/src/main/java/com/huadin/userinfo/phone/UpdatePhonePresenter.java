package com.huadin.userinfo.phone;

import com.huadin.bean.Person;
import com.huadin.util.AMUtils;
import com.huadin.util.CountDownTimer;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2016/12/30.
 * 更换手机逻辑处理
 */

public class UpdatePhonePresenter implements UpdatePhoneContract.Presenter
{
  private static final String TAG = "UpdatePhonePresenter";
  private UpdatePhoneContract.View mPhoneView;
  private String newPhone;
  private CountDownTimer mTimer = new CountDownTimer(60000, 1000)
  {
    @Override
    protected void onTick(long millisUntilFinished)
    {
      mPhoneView.codeOnTick(millisUntilFinished / 1000);
    }

    @Override
    protected void onFinish()
    {
      mPhoneView.codeOnFinish();
    }
  };

  public UpdatePhonePresenter(UpdatePhoneContract.View phoneView)
  {
    mPhoneView = phoneView;
    mPhoneView = checkNotNull(phoneView, "phoneView cannot be null");
    mPhoneView.setPresenter(this);

  }

  @Override
  public void getCode()
  {
    String phone = mPhoneView.getOldPhone();
    boolean isNetwork = mPhoneView.networkIsAvailable();

    if (!isNetwork)
    {
      mPhoneView.updateError(R.string.no_network);
      return;
    }

    String SMS_TEMPLATE_NAME = "更换手机号";
    mTimer.start();
    BmobSMS.requestSMSCode(phone, SMS_TEMPLATE_NAME, new QueryListener<Integer>()
    {
      @Override
      public void done(Integer smsId, BmobException e)
      {
        if (e == null)
        {
          //验证码获取成功
          LogUtil.i(TAG, "done: smsId = " + smsId);
        } else
        {
          LogUtil.i(TAG, "done: error = " + e.getMessage());
          int code = e.getErrorCode();
          showErrorCode(code);
        }
      }
    });
  }

  @Override
  public void start()
  {

    String code = mPhoneView.getCode();
    String oldPhone = mPhoneView.getOldPhone();
    newPhone = mPhoneView.getNewPhone();

    boolean isNetwork = mPhoneView.networkIsAvailable();
    int errorId = 0;

    if (!isNetwork)
    {
      errorId = R.string.no_network;
    } else if (AMUtils.isEmpty(newPhone))
    {
      errorId = R.string.login_name_not_null;
    } else if (AMUtils.validatePassword(newPhone))
    {
      errorId = R.string.login_name_error;
    }else if (!AMUtils.isMobile(newPhone))
    {
      errorId = R.string.login_name_error;
    } else if (AMUtils.isEmpty(code))
    {
      errorId = R.string.register_code_not_null;
    }

    if (errorId != 0)
    {
      mPhoneView.updateError(errorId);
      return;
    }

    mPhoneView.showLoading();
    BmobSMS.verifySmsCode(oldPhone, code, new UpdateListener()
    {
      @Override
      public void done(BmobException e)
      {
        if (e == null)
        {
          //验证码验证成功
          LogUtil.i(TAG, "验证码验证成功");
          updateMobilePhone();
        } else
        {
          mPhoneView.hindLoading();
          LogUtil.i(TAG, "done: error = " + e.getMessage());
          int code = e.getErrorCode();
          showErrorCode(code);
        }
      }
    });
  }

  private void updateMobilePhone()
  {
    Person person = new Person();
    person.setMobilePhoneNumber(newPhone);
    person.setMobilePhoneNumberVerified(true);
    Person personUser = BmobUser.getCurrentUser(Person.class);
    person.update(personUser.getObjectId(), new UpdateListener()
    {
      @Override
      public void done(BmobException e)
      {
        mPhoneView.hindLoading();
        if (e == null)
        {
          //更新成功
          mPhoneView.updateSuccess();
          mTimer.cancel();
        } else
        {
          int code = e.getErrorCode();
          LogUtil.i(TAG, "code = " + code + " / message = " + e.getMessage());
          showErrorCode(code);
        }
      }
    });

  }

  private void showErrorCode(int code)
  {
    // TODO: 2016/12/30 错误码没有全部验证,存在遗漏
    switch (code)
    {
      case 207:
        //验证码错误
        mPhoneView.updateError(R.string.error_code_207);
        break;
      case 9010:
        //网络连接超时
        mPhoneView.updateError(R.string.error_code_9010);
        break;
    }
  }

}
