package com.huadin.login;


import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;

public interface RegisterContract
{
  interface View extends BaseView<Presenter>
  {
    void showLoading();

    void hindLoading();

    void registerSuccess();

    void registerError(int errorCode);

    String userName();

    String userPassword();

    void inputError(int errorRes);

    String registerCode();

  }

  interface Presenter extends BasePresenter
  {
    void getRegisterCode();
  }
}
