package com.huadin.userinfo.fault;


import com.huadin.bean.ReportBean;
import com.huadin.userinfo.UpdateContract;

public class DetailedFaultContract
{
  interface Presenter extends UpdateContract.Presenter
  {

  }

  interface View extends UpdateContract.View<Presenter>
  {
    /**
     * 获取 bomb 数据表的 ObjectId
     *
     * @return String
     */
    String getObjectId();

    /**
     * 获取 ReportBean 实例
     *
     * @return ReportBean
     */
    ReportBean getReportBean();
  }
}
