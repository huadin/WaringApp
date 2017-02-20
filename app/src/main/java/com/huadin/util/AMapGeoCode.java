package com.huadin.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.huadin.database.ScopeLatLng;
import com.huadin.database.StopPowerBean;
import com.huadin.eventbus.EventCenter;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Snow on 2017/2/20.
 * 地理编码
 */

public class AMapGeoCode implements GeocodeSearch.OnGeocodeSearchListener
{
  private static final String TAG = "AMapGeoCode";
  private ExecutorService mPool;
  private Context mContext;
  private List<ScopeLatLng> mLatLngs = new ArrayList<>();
  private TreeMap<String, String> map;


  public AMapGeoCode(@NonNull Context context)
  {
    mContext = context;
    mContext = checkNotNull(context, "Content cannot be null");
  }

  public void startGeoCode()
  {
    if (mPool == null)
    {
      mPool = Executors.newFixedThreadPool(3);
    }
    mPool.submit(new GeoCodeThread());
    mPool.shutdown();
  }


  @Override
  public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i)
  {

  }

  /**
   * 正编码
   */
  @Override
  public void onGeocodeSearched(GeocodeResult geocodeResult, int code)
  {
    if (code == 1000)
    {
      if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null &&
              geocodeResult.getGeocodeAddressList().size() > 0)
      {
        GeocodeAddress geoCodeAddress = geocodeResult.getGeocodeAddressList().get(0);
        String scope = geoCodeAddress.getFormatAddress().substring(6);
        double lat = geoCodeAddress.getLatLonPoint().getLatitude();//纬度
        double lng = geoCodeAddress.getLatLonPoint().getLongitude();//经度
        LogUtil.i(TAG, "地址：" + scope + " / " + "纬度：" + lat + " / 经度：" + lng);

        ScopeLatLng latLng = new ScopeLatLng();
        latLng.setScope(scope);
        latLng.setLatitude(lat);
        latLng.setLongitude(lng);

        mLatLngs.add(latLng);

        if (mLatLngs.size() == map.size())
        {
          DataSupport.deleteAll(ScopeLatLng.class);
          DataSupport.saveAll(mLatLngs);
          //发送通知,显示标记物
          EventBus.getDefault().post(new EventCenter(EventCenter.GEO_CODE_COMPLETE));
        }
      }
    }
  }

  private class GeoCodeThread implements Runnable
  {

    @Override
    public void run()
    {
      geoCode();
    }
  }

  private void geoCode()
  {
    GeocodeSearch search = new GeocodeSearch(mContext);
    search.setOnGeocodeSearchListener(this);
    List<StopPowerBean> beanList = DataSupport.findAll(StopPowerBean.class);

    map = new TreeMap<>();

    for (StopPowerBean bean : beanList)
    {
      map.put(bean.getScope(), null);
    }

    if (map.size() > 0)
    {
      Set<String> setKey = map.keySet();
      for (String scope : setKey)
      {
//        LogUtil.i(TAG,"scope = " + scope);
        GeocodeQuery query = new GeocodeQuery(scope, "北京");
        search.getFromLocationNameAsyn(query);
      }
    }
  }

}
