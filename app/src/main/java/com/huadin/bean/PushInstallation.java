package com.huadin.bean;

import cn.bmob.v3.BmobInstallation;

/**
 * 推送使用
 */

public class PushInstallation extends BmobInstallation
{
  //推送tag
  private String pushUserName;
  //推送条件,地区Id
  private String areaId;
  //设备识别码，预留
  private String pushDevicesId;


  public String getPushUserName()
  {
    return pushUserName;
  }

  public void setPushUserName(String pushUserName)
  {
    this.pushUserName = pushUserName;
  }

  public String getAreaId()
  {
    return areaId;
  }

  public void setAreaId(String areaId)
  {
    this.areaId = areaId;
  }

  public String getPushDevicesId()
  {
    return pushDevicesId;
  }

  public void setPushDevicesId(String pushDevicesId)
  {
    this.pushDevicesId = pushDevicesId;
  }
}
