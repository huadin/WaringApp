package com.huadin.search;

import com.huadin.base.BasePresenter;
import com.huadin.base.BaseView;
import com.huadin.database.City;
import com.huadin.database.StopPowerBean;

import java.util.List;

/**
 * Created by NCL on 2017/2/24.
 * 搜索
 */

public interface SearchContract
{
  interface Presenter extends BasePresenter
  {
    void hindLoading();
  }

  interface View extends BaseView<Presenter>
  {
    void showLoading();

    void hindLoading();

    /**
     * 查询成功
     *
     * @param powerBeanList 停电信息集合
     */
//    void searchSuccess(List<StopPowerBean> powerBeanList);

    /**
     * 查询异常
     *
     * @param errorResId 异常资源Id
     */
    void searchError(int errorResId);

    boolean networkState();

    /**
     * 获取日期
     *
     * @return String
     */
    String getStartDate();

    /**
     * 获取地区
     *
     * @return City
     */
    City getArea();

    /**
     * 获取范围
     *
     * @return String
     */
    String getScope();

    /**
     * 获取停电类型
     *
     * @return 停电类型
     */
    String getType();

    /**
     * 启动搜索服务
     *
     * @param areaId    地区
     * @param type      类型
     * @param startTime 开始时间
     * @param scope     范围
     */
    void startSearchService(String areaId, String type, String startTime, String scope);

  }
}
