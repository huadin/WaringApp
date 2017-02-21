package com.huadin.database;

import org.litepal.crud.DataSupport;

/**
 * Created by Snow on 2017/2/21.
 * 经纬度点
 */

public class LatLngPoint extends DataSupport
{
  private int number;
  private double range;


  public int getNumber()
  {
    return number;
  }

  public void setNumber(int number)
  {
    this.number = number;
  }

  public double getRange()
  {
    return range;
  }

  public void setRange(double range)
  {
    this.range = range;
  }
}
