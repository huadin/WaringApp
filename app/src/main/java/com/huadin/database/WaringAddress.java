package com.huadin.database;

import org.litepal.crud.DataSupport;

/**
 * Created by 潇湘 on 2017/1/11.
 * 预警地址
 */

public class WaringAddress extends DataSupport
{
  private String waringArea;//部门
  private String waringAddress;//详细地址
  private String isLocal;

  public String getWaringArea()
  {
    return waringArea;
  }

  public void setWaringArea(String waringArea)
  {
    this.waringArea = waringArea;
  }

  public String getWaringAddress()
  {
    return waringAddress;
  }

  public void setWaringAddress(String waringAddress)
  {
    this.waringAddress = waringAddress;
  }

  public String getIsLocal()
  {
    return isLocal;
  }

  public void setIsLocal(String isLocal)
  {
    this.isLocal = isLocal;
  }
}
