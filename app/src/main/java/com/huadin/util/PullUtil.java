package com.huadin.util;

import android.util.Xml;

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

  public static void pullParser(InputStream inputStream)
  {
    XmlPullParser parser = Xml.newPullParser();
    try
    {
      parser.setInput(inputStream, "UTF-8");
      int type = parser.getEventType();
      List<String> sList = null;
      while (type != XmlPullParser.END_DOCUMENT)
      {
        switch (type)
        {
          case XmlPullParser.START_DOCUMENT:
            sList = new ArrayList<>();
            break;
          case XmlPullParser.START_TAG:
            if (parser.getName().equals("id"))
            {
              type = parser.next();
              sList.add(parser.getText());
            }
            break;

          case XmlPullParser.END_TAG:

            break;
        }
        type = parser.next();
      }
      if (sList != null) LogUtil.i(TAG, "sList = " + sList.toString());
    } catch (Exception e)
    {
      e.printStackTrace();
    }

  }
}
