package com.huadin.util;

import com.huadin.database.StopPowerBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Snow on 2017/2/20.
 * 解析
 */

class ParseUtil
{

  private static List<StopPowerBean> sList = new ArrayList<>();
  private static String sOrgCode;
  private static TreeMap<String, String> sKeyMap = new TreeMap<>();

  /**
   * 解析网络数据
   *
   * @param jsonList 数据集合
   * @param orgCode  地区编码
   * @return List<StopPowerBean>
   */
  static List<StopPowerBean> pareJson(List<JSONArray> jsonList, String orgCode)
  {
    sOrgCode = orgCode;
    try
    {
      sList.clear();
      for (JSONArray array : jsonList)
      {
        for (int i = 0; i < array.length(); i++)
        {

          JSONObject obj = array.getJSONObject(i);
          String lineName = obj.getString("lineName");
          String startTime = obj.getString("startTime");
          String stopDate = obj.getString("stopDate");
          String typeCode = obj.getString("typeCode");
          String scope = obj.getString("scope");

          //日期
          String[] tempStartTime = startTime.trim().split(" ");
          String date = tempStartTime[0];//日期
          String tStartTime = tempStartTime[1];//开始时间

          //时间
          String[] tempEndTime = stopDate.trim().split(" ");
          String endTime = tempEndTime[1];//结束时间
          String time = tStartTime + " - " + endTime;


          //判断scope中个数
          String[] scopeArr = scope.trim().split(",");

          if (scopeArr.length > 1)
          {
            splitScope(scopeArr, lineName, time, date, typeCode);
          } else
          {
            setNewStopType(scope, lineName, time, date, typeCode);
          }
        }
      }
    } catch (JSONException e)
    {
      e.printStackTrace();
    }
    sKeyMap.clear();
    return sList;
  }

  /**
   * 拆分地址 scope : xxx村，xxx村
   */
  private static void splitScope(String[] scopeArr, String lineName, String time, String date, String typeCode)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(date);
    sb.append(" ");
    sb.append(time);

    for (String scope : scopeArr)
    {
      boolean isKeyContains = sKeyMap.containsKey(scope);
      boolean isValueContains = sKeyMap.containsValue(sb.toString());
      sKeyMap.put(scope, sb.toString());
      //key 和 value 同时为 true，则说明 存在同一地点同一时间停电,既重复
      if (isKeyContains && isValueContains) continue;
      setNewStopType(scope, lineName, time, date, typeCode);
    }
    sb.delete(0, sb.length());
  }

  private static void setNewStopType(String scope, String lineName, String time, String date, String typeCode)
  {
    String type = null;
    if (typeCode.equals("01"))
    {
      type = "计划停电";
    } else if (typeCode.equals("02"))
    {
      type = "故障停电";
    }
    if (typeCode.equals("07"))
    {
      type = "临时停电";
    }

    StopPowerBean st = new StopPowerBean();
    st.setScope(scope);
    st.setLineName(lineName);
    st.setTime(time);
    st.setDate(date);
    st.setTypeCode(type);
    st.setOrgCode(sOrgCode);
    sList.add(st);
  }
}
