package com.huadin.userinfo.fault;

import com.huadin.bean.ReportBean;
import com.huadin.userinfo.UpdateContract;

import java.util.List;


public interface FaultContract
{
  interface Presenter extends UpdateContract.Presenter
  {
    /**
     * 上拉刷新
     */
    void refresh();

    /**
     * 下拉加载
     */
    void loadMore();
  }

  interface View extends UpdateContract.View<Presenter>
  {
    /**
     * 数据查询成功
     *
     * @param beanList List<ReportBean>
     */
    void querySuccess(List<ReportBean> beanList);

    /**
     * 加載更多失敗
     */
    void loadMoreFailed();
  }
}
