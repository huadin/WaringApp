package com.huadin.login;

import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.huadin.base.BaseView;
import com.huadin.database.ScopeLatLng;

import java.util.ArrayList;
import java.util.List;

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
     * @param latLng 经纬度对象
     */
    void latLng(LatLng latLng);

    /**
     * 定位异常信息
     *
     * @param errorInfo 错误信息
     */
    void locationError(String errorInfo);

    /**
     * 准备添加覆盖物
     *
     * @param options MarkerOptions
     */
    void addMarker(ArrayList<MarkerOptions> options);
  }

  interface MapListener extends AMapLocationListener
  {
    /**
     * 开启定位
     */
    void startLocation(LocationSource.OnLocationChangedListener onLocationChangedListener);

    /**
     * 恢复定位
     */
    void resumeLocation();

    /**
     * 停止定位
     */
    void stopLocation();

    /**
     * 销毁定位
     */
    void destroyLocation();

    /**
     * 添加覆盖物到地图上
     *
     * @param latLng          当前位置的经纬度
     * @param scopeLatLngList 解析出来的经纬度的集合
     */
    void addMarkerToMap(List<ScopeLatLng> scopeLatLngList, LatLng latLng);

    /**
     * marker 点击事件
     *
     * @param marker Marker
     */
    void markerClick(Marker marker);

    /**
     * 地图点击事件
     */
    void mapClick();

    /**
     * 窗口点击事件
     */
    void windowClick(Marker marker);
  }

}
