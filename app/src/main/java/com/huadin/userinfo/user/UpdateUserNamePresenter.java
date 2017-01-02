package com.huadin.userinfo.user;

import com.huadin.bean.Person;
import com.huadin.util.LogUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateUserNamePresenter implements UserContract.Presenter
{

  private static final String TAG = "UpdateUserNamePresenter";
  private UserContract.View mUserView;

  public UpdateUserNamePresenter(UserContract.View userView)
  {
    mUserView = userView;
    mUserView = checkNotNull(userView,"userView cannot be null");
    mUserView.setPresenter(this);
  }

  @Override
  public void start()
  {
    String userName = mUserView.updateUserName();
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
        mUserView.hindLoading();
        if (e == null)
        {
          mUserView.updateSuccess();
        }else
        {
          int code = e.getErrorCode();
          LogUtil.i(TAG,"code = " + code + " / message = " + e.getMessage());
        }
      }
    });
  }


}
