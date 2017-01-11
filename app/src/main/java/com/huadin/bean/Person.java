package com.huadin.bean;

import cn.bmob.v3.BmobUser;

public class Person extends BmobUser
{
  private boolean userPermission;
  private boolean isUserNameChange;
  private String areaName;
  private String areaId;
  private String address;

  public boolean isUserNameChange()
  {
    return isUserNameChange;
  }

  public void setUserNameChange(boolean userNameChange)
  {
    isUserNameChange = userNameChange;
  }

  public boolean isUserPermission()
  {
    return userPermission;
  }

  public void setUserPermission(boolean userPermission)
  {
    this.userPermission = userPermission;
  }

  public String getAreaId()
  {
    return areaId;
  }

  public void setAreaId(String areaId)
  {
    this.areaId = areaId;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getAreaName()
  {
    return areaName;
  }

  public void setAreaName(String areaName)
  {
    this.areaName = areaName;
  }
}
