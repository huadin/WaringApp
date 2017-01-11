package com.huadin.util;

import android.util.Xml;

import com.huadin.database.City;

import org.litepal.crud.DataSupport;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NCL on 2017/1/10.
 * 解析
 */

public class PullUtil
{
  private static final String TAG = "PullUtil";

  /**
   * 解析xml文件
   *
   * @param inputStream InputStream
   */
  public static List<City> pullParser(InputStream inputStream)
  {
    XmlPullParser parser = Xml.newPullParser();
    DataSupport.deleteAll(City.class);
    List<City> cityList = new ArrayList<>();
    try
    {
      parser.setInput(inputStream, "UTF-8");
      int eventType = parser.getEventType();
      String areaId = "";
      String areaName = "";
      while (eventType != XmlPullParser.END_DOCUMENT)
      {
        String nodeName = parser.getName();
        switch (eventType)
        {
          case XmlPullParser.START_DOCUMENT:

            break;
          case XmlPullParser.START_TAG:
            if ("name".equals(nodeName))
            {
              areaName = parser.nextText();
            } else if ("id".equals(nodeName))
            {
              areaId = parser.nextText();
            }
            break;

          case XmlPullParser.END_TAG:
            if ("area".equals(nodeName))
            {
              City city = new City();
              city.setAreaName(areaName);
              city.setAreaId(areaId);
              cityList.add(city);
              DataSupport.saveAll(cityList);
            }
            break;
        }
        eventType = parser.next();
      }

      return cityList;
    } catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
