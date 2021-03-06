package com.huadin.userinfo;

import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;

/**
 * Created by 潇湘 on 2016/12/29.
 * 所有更新个人信息的 contract 都继承次接口
 */

public interface UpdateContract
{
  interface Presenter extends BasePresenter
  {

  }

  interface View<T> extends BaseView<T>
  {

    void showLoading();

    void hindLoading();

    /**
     * 成功
     */
    void updateSuccess();

    /**
     * 失败
     */
    void updateError(int errorId);

    /**
     * 网络连接状态
     *
     * @return 有网络 - true;
     */
    boolean networkState();
  }
}
