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

    void registerError(String errorMsg);

    String userName();

    String userPassword();

    void inputError(int errorRes);

  }

  interface Presenter extends BasePresenter
  {

  }
}
