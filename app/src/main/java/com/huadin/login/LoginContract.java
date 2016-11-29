package com.huadin.login;


import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;

public interface LoginContract
{
  interface Presenter extends BasePresenter
  {
  }

  interface View extends BaseView<Presenter>
  {
    void showLoading();

    void hindLoading();

    void loginSuccess();

    void loginError(int errorRes);

    String getLoginName();

    String getLoginPassword();
  }
}
