package com.huadin.userinfo.password;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2016/12/29.
 * 修改密码
 */

public class UpdatePasswordPresenter implements UpdatePasswordContract.Presenter
{

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
  }
}
