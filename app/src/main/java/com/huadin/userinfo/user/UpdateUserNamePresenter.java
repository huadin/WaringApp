package com.huadin.userinfo.user;

import android.content.Context;

import com.huadin.base.InstallationListener;
import com.huadin.bean.Person;
import com.huadin.util.InstallationUtil;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.google.common.base.Preconditions.checkNotNull;

class UpdateUserNamePresenter implements UserContract.Presenter, InstallationListener
{

  private static final String TAG = "UpdateUserNamePresenter";
  private UserContract.View mUserView;
  private Context mContext;

  UpdateUserNamePresenter(UserContract.View userView, Context context)
  {
    mUserView = userView;
    mUserView = checkNotNull(userView, "userView cannot be null");
    mUserView.setPresenter(this);

    mContext = context;
    mContext = checkNotNull(context, "context cannot be null");
  }

  @Override
  public void start()
  {
    final String userName = mUserView.updateUserName();
    // TODO: 2017/1/2 没有进行正则

    Person person = new Person();
    person.setUsername(userName);
    person.setUserNameChange(true);
    Person currentPerson = Person.getCurrentUser(Person.class);

    mUserView.showLoading();
    person.update(currentPerson.getObjectId(), new UpdateListener()
    {
      @Override
      public void done(BmobException e)
      {

        if (e == null)
        {
          pusUserName(userName);
        } else
        {
          //异常
          errorCode(e);
        }
      }
    });
  }

  private void errorCode(BmobException e)
  {
    int code = e.getErrorCode();
    LogUtil.i(TAG, "code = " + code + " / message = " + e.getMessage());
    showCode(code);
  }

  //将用户名作为推送的 唯一设别tag
  private void pusUserName(String userName)
  {
    InstallationUtil.newInstance()
            .with(mContext)
            .addInstallationListener(this)
            .bindingPush(userName);
  }


  @Override
  public void installationSuccess()
  {
    mUserView.hindLoading();
    mUserView.updateSuccess();
  }

  @Override
  public void installationError(int code)
  {
    mUserView.hindLoading();
    showCode(code);
  }

  /**
   * 显示异常原因
   *
   * @param code 错误码
   */
  private void showCode(int code)
  {
    mUserView.hindLoading();
    switch (code)
    {
      case 9010:
        mUserView.updateError(R.string.error_code_9010);
        break;
    }
  }

}
