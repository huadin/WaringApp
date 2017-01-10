package com.huadin.userinfo.password;

import com.huadin.userinfo.UpdateContract;

/**
 * Created by 潇湘 on 2016/12/29.
 * 个人修改密码
 */

interface UpdatePasswordContract
{
  interface Presenter extends UpdateContract.Presenter
  {

  }

  interface View extends UpdateContract.View<Presenter>
  {
    /**
     * 获取旧密码
     */
    String oldPassword();

    /**
     * 获取新密码
     */
    String newPassword();

  }

}
