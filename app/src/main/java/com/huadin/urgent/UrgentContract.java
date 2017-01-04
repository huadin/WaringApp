package com.huadin.urgent;

import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;

/**
 * 紧急信息
 */

interface UrgentContract
{
  interface View extends BaseView<Presenter>
  {
    void showLoading();

    void hindLoading();

    void success();

    /**
     * 请求数据失败
     *
     * @param errorId 错误信息
     */
    void error(int errorId);
  }

  interface Presenter extends BasePresenter
  {

  }
}
