package com.huadin.userinfo.user;

import com.huadin.userinfo.UpdateContract;

/**
 * Created by NCL on 2017/1/2.
 * 更新用户信息
 */

interface UserContract
{
  interface Presenter extends UpdateContract.Presenter
  {

  }

  interface View extends UpdateContract.View<Presenter>
  {
    /**
     * 更新用户名
     *
     * @return String
     */
    String updateUserName();

  }

}
