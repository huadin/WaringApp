package com.huadin.login;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.LocationSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 地图
 */

class MapPresenter implements MapContract.MapListener
{
  private MapContract.View mView;
  private AMapLocationClient mClient;
  private Context mContext;
  private LocationSource.OnLocationChangedListener mListener;

  MapPresenter(MapContract.View view, Context context)
  {
    this.mView = view;
    this.mContext = context;
    mContext = checkNotNull(context, "context cannot null");
    mView = checkNotNull(view, "view cannot null");
    mView.setPresenter(this);
  }

  /**
   * 定位监听
   *
   * @param aMapLocation AMapLocation
   */
  @Override
  public void onLocationChanged(AMapLocation aMapLocation)
  {
    if (mListener != null && aMapLocation != null)
    {
      if (aMapLocation.getErrorCode() == 0)
      {
        mView.latLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        //显示系统小蓝点
        mListener.onLocationChanged(aMapLocation);
      } else
      {
        mView.locationError(aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
      }
    }
  }

  /**
   * 定位配置信息
   *
   * @return AMapLocationClientOption
   */
  private AMapLocationClientOption getDefaultOption()
  {
    AMapLocationClientOption option = new AMapLocationClientOption();
    //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
    option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
    //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
    option.setGpsFirst(false);
    //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
    option.setHttpTimeOut(30000);
    //可选，设置定位间隔。默认为2秒
    option.setInterval(2000);
    //可选，设置是否返回逆地理地址信息。默认是true
    option.setNeedAddress(true);
    //可选，设置是否单次定位。默认是false
    option.setOnceLocation(false);
    //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
    option.setOnceLocationLatest(false);
    //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
    AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
    //可选，设置是否使用传感器。默认是false
    option.setSensorEnable(false);
    return option;
  }

  /**
   * 开启定位
   */
  @Override
  public void startLocation(LocationSource.OnLocationChangedListener listener)
  {
    this.mListener = listener;
    mListener = checkNotNull(listener, "OnLocationChangedListener cannot null");
    mClient = new AMapLocationClient(mContext.getApplicationContext());
    mClient.setLocationOption(getDefaultOption());
    mClient.setLocationListener(this);

    mClient.startLocation();
  }

  /**
   * 停止定位
   */
  @Override
  public void stopLocation()
  {
    mClient = checkNotNull(mClient, "AMapLocationClient null");
    mClient.stopLocation();
  }

  /**
   * 销毁定位
   */
  @Override
  public void destroyLocation()
  {
    mClient = checkNotNull(mClient, "AMapLocationClient null");
    mClient.onDestroy();
    mClient = null;
  }


}
