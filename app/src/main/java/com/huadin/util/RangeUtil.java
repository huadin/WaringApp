package com.huadin.util;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.huadin.database.LatLngPoint;
import com.huadin.database.Range;
import com.huadin.database.ScopeLatLng;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Snow on 2017/2/21.
 * 根据经纬度计算距离
 */

public class RangeUtil
{
  private static final String TAG = "RangeUtil";

  /**
   * 计算两点间距离
   *
   * @param mList 编码集合
   * @param p1    经纬度
   * @return 符合范围的 scope 集合
   */
  public static List<String> scopeFromRange(List<ScopeLatLng> mList, LatLng p1)
  {

    LogUtil.i(TAG,"p1 / lat = " + p1.latitude + " / lng = " + p1.longitude);

    List<String> scopeList = null;
    try
    {
      Range cr = DataSupport.findFirst(Range.class);
      if (cr == null)
      {
        cr = new Range();
        cr.setRange(2000.0);
        cr.save();
      }

      List<LatLngPoint> llpList = new ArrayList<>();//将查询到的数据保存到数据库
      scopeList = new ArrayList<>();//存储符合条件的 info

      if (llpList.size() > 0) llpList.clear();
      if (scopeList.size() > 0) scopeList.clear();

      for (int i = 0; i < mList.size(); i++)
      {
        double lat = mList.get(i).getLatitude();
        double lng = mList.get(i).getLongitude();

        LogUtil.i(TAG,"p2 / lat = " + lat + " / lng = " + lng);
        LatLng p2 = new LatLng(lat, lng);

        //两点间距离
        double range = AMapUtils.calculateLineDistance(p1, p2);
        LogUtil.i(TAG, "range = " + range);
        //按照顺序保存距离
        LatLngPoint llp = new LatLngPoint();
        llp.setNumber(i);
        llp.setRange(range);
        llpList.add(llp);

        //在范围内的 scope 保存到集合中
//        if (range <= cr.getRange())
//        {
        scopeList.add(mList.get(i).getScope());
//        }
      }

      //删除旧数据
      DataSupport.deleteAll(LatLngPoint.class);
      //保存到数据库
      DataSupport.saveAll(llpList);
      return scopeList;

    } catch (Exception e)
    {
      e.printStackTrace();
    }
    return scopeList;
  }


  /**
   * 自定义范围
   *
   * @param mList ScopeLatLng 集合
   * @return 符合范围的 scope 集合
   */
  public static List<String> resetRange(List<ScopeLatLng> mList)
  {
    LogUtil.i(TAG, "mList.size() = " + mList.size());
    List<String> scopeList = null;
    List<Integer> tempList; //存储符合条件的下标号

    List<LatLngPoint> pList = DataSupport.findAll(LatLngPoint.class);
    Range cr = DataSupport.findFirst(Range.class);
    if (pList.size() > 0)
    {
      tempList = new ArrayList<>();

      for (int i = 0; i < pList.size(); i++)
      {
        if (pList.get(i).getRange() <= cr.getRange())
        {
          tempList.add(pList.get(i).getNumber());
        }
      }
      LogUtil.i(TAG, "LatLngPoint = " + tempList.toString());
      //自定义距离时，判断集合中是否有数据
      if (tempList.size() > 0)
      {
        LogUtil.i(TAG, "mList.size() = " + mList.size());
        //显示数据
        scopeList = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++)
        {
          //根据下标号取出原集合中的scope
          LogUtil.i(TAG, "number = " + tempList.get(i));

          scopeList.add(mList.get(tempList.get(i)).getScope());
        }
      }
      return scopeList;
    }
    return null;
  }
}
