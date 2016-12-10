package com.huadin.login;

import com.amap.api.location.AMapLocationListener;
import com.huadin.base.BaseView;

/**
 * 地图
 */

interface MapContract
{
  interface View extends BaseView<MapListener>
  {
    /**
     * 返回经纬度信息
     *
     * @param latitude  维度
     * @param longitude 经度
     */
    void latLng(double latitude, double longitude);

    /**
     * 定位异常信息
     *
     * @param errorCode 错误码
     * @param errorInfo 错误信息
     */
    void locationError(int errorCode, String errorInfo);
  }

  interface MapListener extends AMapLocationListener
  {
    /**
     * 开启定位
     */
    void startLocation();

    /**
     * 停止定位
     */
    void stopLocation();

    /**
     * 销毁定位
     */
    void destroyLocation();
  }

}
