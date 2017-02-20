package com.huadin.database;

import org.litepal.crud.DataSupport;

/**
 * Created by Snow on 2017/2/20.
 * 正编码解析的数据
 */

public class ScopeLatLng extends DataSupport
{
  private String scope;
  private double latitude;
  private double longitude;

  public String getScope()
  {
    return scope;
  }

  public void setScope(String scope)
  {
    this.scope = scope;
  }

  public double getLatitude()
  {
    return latitude;
  }

  public void setLatitude(double latitude)
  {
    this.latitude = latitude;
  }

  public double getLongitude()
  {
    return longitude;
  }

  public void setLongitude(double longitude)
  {
    this.longitude = longitude;
  }
}
