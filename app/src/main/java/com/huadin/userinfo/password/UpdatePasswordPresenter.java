package com.huadin.userinfo.password;

import com.huadin.bean.Person;
import com.huadin.util.AMUtils;
import com.huadin.util.LogUtil;
import com.huadin.util.MD5util;
import com.huadin.waringapp.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2016/12/29.
 * 修改密码
 */

public class UpdatePasswordPresenter implements UpdatePasswordContract.Presenter
{
  private static final String TAG = "UpdatePasswordPresenter";
  private UpdatePasswordContract.View mPasswordView;

  public UpdatePasswordPresenter(UpdatePasswordContract.View passwordView)
  {
    mPasswordView = passwordView;
    mPasswordView = checkNotNull(passwordView, "passwordView cannot be null");
    mPasswordView.setPresenter(this);
  }

  @Override
  public void start()
  {
    // TODO: 2016/12/29 检验数据平获取密码
    int errorId = 0;

    String oldPassword = mPasswordView.oldPassword();
    String newPassword = mPasswordView.newPassword();

    if (AMUtils.isEmpty(oldPassword))
    {
      errorId = R.string.user_info_old_password;
    } else if (AMUtils.isEmpty(newPassword))
    {
      errorId = R.string.user_info_new_password;
    } else if (AMUtils.validatePassword(newPassword))
    {
      errorId = R.string.login_password_length_error;
    }

    if (errorId != 0)
    {
      mPasswordView.updateError(errorId);
      return;
    }

    mPasswordView.showLoading();
    //更新
    Person.updateCurrentUserPassword(MD5util.getMD5(oldPassword), MD5util.getMD5(newPassword), new UpdateListener()
    {
      @Override
      public void done(BmobException e)
      {
        mPasswordView.hindLoading();

        if (e == null)
        {
          //更新成功
          mPasswordView.updateSuccess();
        } else
        {
          //失败
          String message = e.getMessage();
          int code = e.getErrorCode();
          LogUtil.i(TAG, "code = " + code + " / message = " + message);
          showErrorCode(code);
        }
      }
    });
  }

  private void showErrorCode(int code)
  {
    switch (code)
    {
      case 210:
        //原密码错误
        mPasswordView.updateError(R.string.error_code_210);
        break;
      case 206:
        // TODO: 2016/12/30 用户没登录
        break;
      case 9010:
        mPasswordView.updateError(R.string.error_code_9010);
        break;
      case 9016:
        mPasswordView.updateError(R.string.error_code_9016);
        break;
    }
  }

}
