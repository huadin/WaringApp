package com.huadin.bean;

import cn.bmob.v3.BmobUser;

public class Person extends BmobUser
{
  private boolean userPermission;
  private String userCreateDate;

  public boolean isUserPermisson()
  {
    return userPermission;
  }

  public void setUserPermisson(boolean userPermission)
  {
    this.userPermission = userPermission;
  }

  public String getUserCreateDate()
  {
    return userCreateDate;
  }

  public void setUserCreateDate(String userCreateDate)
  {
    this.userCreateDate = userCreateDate;
  }

  public boolean isUserPermission()
  {
    return userPermission;
  }

  @Override
  public String toString()
  {
    return "Person{" +
            "userPermission=" + userPermission +
            ", userCreateDate='" + userCreateDate + '\'' +
            '}';
  }
}
