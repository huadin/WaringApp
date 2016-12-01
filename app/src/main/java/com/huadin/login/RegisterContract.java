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

    boolean networkIsAvailable();

    /**
     * 重新获取验证码
     */
    void codeOnFinish();

    /**
     * 显示验证码剩余时间
     * @param m 6000
     */
    void codeOnTick(long m);

  }

  interface Presenter extends BasePresenter
  {
    void getRegisterCode();
  }
}
