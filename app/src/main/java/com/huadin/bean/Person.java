package com.huadin.bean;

import cn.bmob.v3.BmobUser;

public class Person extends BmobUser
{
  private boolean userPermission;
  private boolean isUserNameChange;

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

}
