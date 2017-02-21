package com.huadin.database;

import org.litepal.crud.DataSupport;

/**
 * Created by Snow on 2017/2/21.
 * 范围集合
 */

public class Range extends DataSupport
{
  private double range;

  public double getRange()
  {
    return range;
  }

  public void setRange(double range)
  {
    this.range = range;
  }
}
