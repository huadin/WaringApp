package com.huadin.userinfo.phone;

import com.huadin.userinfo.UpdateContract;

/**
 * Created by 潇湘 on 2016/12/30.
 * 更换手机号 控制器
 */

interface UpdatePhoneContract
{
  interface Presenter extends UpdateContract.Presenter
  {
    /**
     * 获取验证码
     */
    void getCode();
  }

  interface View extends UpdateContract.View<Presenter>
  {
    /**
     * 获取验证码
     *
     * @return String
     */
    String getCode();

    /**
     * 获取原手机号码
     *
     * @return String
     */
    String getOldPhone();

    /**
     * 获取新手机号
     *
     * @return String
     */
    String getNewPhone();

    /**
     * 重新获取验证码
     */
    void codeOnFinish();

    /**
     * 验证码剩余时间
     *
     * @param m 6000
     */
    void codeOnTick(long m);

  }

}
