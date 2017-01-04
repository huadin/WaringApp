package com.huadin.login;


import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;

interface LoginContract
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

    boolean networkIsAvailable();
  }
}
