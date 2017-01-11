package com.huadin.database;

import org.litepal.crud.DataSupport;

/**
 * Created by 潇湘 on 2017/1/11.
 * 地区实体类
 */

public class City extends DataSupport
{
  private String areaName;
  private String areaId;

  public String getAreaId()
  {
    return areaId;
  }

  public void setAreaId(String areaId)
  {
    this.areaId = areaId;
  }

  public String getAreaName()
  {
    return areaName;
  }

  public void setAreaName(String areaName)
  {
    this.areaName = areaName;
  }

  @Override
  public String toString()
  {
    return "City{" +
            "areaName='" + areaName + '\'' +
            ", areaId='" + areaId + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    City city = (City) o;

    if (areaName != null ? !areaName.equals(city.areaName) : city.areaName != null) return false;
    return areaId != null ? areaId.equals(city.areaId) : city.areaId == null;

  }

  @Override
  public int hashCode()
  {
    int result = areaName != null ? areaName.hashCode() : 0;
    result = 31 * result + (areaId != null ? areaId.hashCode() : 0);
    return result;
  }
}
