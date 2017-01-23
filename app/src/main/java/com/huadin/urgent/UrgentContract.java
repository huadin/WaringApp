package com.huadin.urgent;

import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;
import com.huadin.bean.ReleaseBean;

import java.util.List;

/**
 * 紧急信息
 */

interface UrgentContract
{
  interface View extends BaseView<Presenter>
  {
    void showLoading();

    void hindLoading();

    void success(List<ReleaseBean> beanList);

    /**
     * 请求数据失败
     *
     * @param errorId 错误信息
     */
    void error(int errorId);

    boolean networkStatus();
  }

  interface Presenter extends BasePresenter
  {
    /**
     * 刷新
     */
    void refresh();

    /**
     * 加载更多
     */
    void loadMore();
  }
}
