package com.huadin.login;

import com.huadin.util.AMUtils;
import com.huadin.util.CountDownTimer;
import com.huadin.util.LogUtil;
import com.huadin.util.MD5util;
import com.huadin.waringapp.R;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 重置密码
 */

class ResetPresenter implements RegisterContract.Presenter
{
  private static final String TAG = "ResetPresenter";
  private RegisterContract.View mResetView;

  private CountDownTimer mTimer = new CountDownTimer(60000, 1000)
  {
    @Override
    protected void onTick(long millisUntilFinished)
    {
      mResetView.codeOnTick(millisUntilFinished / 1000);
    }

    @Override
    protected void onFinish()
    {
      mResetView.codeOnFinish();
    }
  };


  ResetPresenter(RegisterContract.View resetView)
  {
    this.mResetView = resetView;
    mResetView = checkNotNull(resetView, "resetView cannot null");
    mResetView.setPresenter(this);
  }

  //获取验证码
  @Override
  public void getRegisterCode()
  {
    int errorRes = 0;
    String resetPhone = mResetView.userName();
    boolean isNetwork = mResetView.networkIsAvailable();

    if (AMUtils.isEmpty(resetPhone))
    {
      errorRes = R.string.login_name_not_null;
    } else if (!AMUtils.isMobile(resetPhone))
    {
      errorRes = R.string.login_name_error;
    } else if (!isNetwork)
    {
      errorRes = R.string.no_network;
    }

    if (errorRes != 0)
    {
      mResetView.inputError(errorRes);
      return;
    }
    String SMS_TEMPLATE_NAME = "重置密码";
    mTimer.start();
    BmobSMS.requestSMSCode(resetPhone, SMS_TEMPLATE_NAME, new QueryListener<Integer>()
    {
      @Override
      public void done(Integer smsId, BmobException e)
      {
        if (e == null)
        {
          //验证码获取成功
          LogUtil.i(TAG, "smsId = " + smsId);
        } else
        {
          LogUtil.i(TAG, "done: error = " + e.getMessage());
          int code = e.getErrorCode();
          showErrorCode(code);
        }
      }
    });
  }

  //提交新密码
  @Override
  public void start()
  {
    int errorRes = 0;
    String resetPhone = mResetView.userName();
    String resetPassword = mResetView.userPassword();
    String resetCode = mResetView.registerCode();
    boolean isNetwork = mResetView.networkIsAvailable();
    if (AMUtils.isEmpty(resetPhone))
    {
      errorRes = R.string.login_name_not_null;
    } else if (!AMUtils.isMobile(resetPhone))
    {
      errorRes = R.string.login_name_error;
    } else if (AMUtils.isEmpty(resetPassword))
    {
      errorRes = R.string.login_password_not_null;
    } else if (AMUtils.validatePassword(resetPassword))
    {
      errorRes = R.string.login_password_length_error;
    } else if (AMUtils.isEmpty(resetCode))
    {
      errorRes = R.string.register_code_not_null;
    } else if (!isNetwork)
    {
      errorRes = R.string.no_network;
    }

    if (errorRes != 0)
    {
      mResetView.inputError(errorRes);
      return;
    }

    mResetView.showLoading();

    BmobUser.resetPasswordBySMSCode(resetCode, MD5util.getMD5(resetPassword), new UpdateListener()
    {
      @Override
      public void done(BmobException e)
      {
        mResetView.hindLoading();

        if (e == null)
        {
          //重置成功
          mResetView.registerSuccess();
          mTimer.cancel();
          LogUtil.i(TAG, "密码重置成功");
        } else
        {
          int code = e.getErrorCode();
          String errorMsg = e.getLocalizedMessage();
          LogUtil.e(TAG, "code = " + code + " / " + "errorMsg = " + errorMsg);
          showErrorCode(code);
        }
      }
    });
  }

  private void showErrorCode(int code)
  {
    switch (code)
    {
      case 205:
        mResetView.registerError(R.string.error_code_205);

      case 207:
        mResetView.registerError(R.string.error_code_207);
        break;
      case 9010:
        mResetView.registerError(R.string.error_code_9010);
        break;
      case 9016:
        mResetView.registerError(R.string.error_code_9016);
        break;
    }
  }

}
