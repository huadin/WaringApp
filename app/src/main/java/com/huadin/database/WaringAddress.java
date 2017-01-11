package com.huadin.database;

import org.litepal.crud.DataSupport;

/**
 * Created by 潇湘 on 2017/1/11.
 * 预警地址
 */

public class WaringAddress extends DataSupport
{
  private String waringArea;
  private String waringAddress;

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
}
