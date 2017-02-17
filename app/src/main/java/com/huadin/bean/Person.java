package com.huadin.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobUser;

public class Person extends BmobUser implements Parcelable
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

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeByte(this.userPermission ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isUserNameChange ? (byte) 1 : (byte) 0);
    dest.writeString(this.areaName);
    dest.writeString(this.areaId);
    dest.writeString(this.address);
  }

  public Person()
  {
  }

  protected Person(Parcel in)
  {
    this.userPermission = in.readByte() != 0;
    this.isUserNameChange = in.readByte() != 0;
    this.areaName = in.readString();
    this.areaId = in.readString();
    this.address = in.readString();
  }

  public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>()
  {
    @Override
    public Person createFromParcel(Parcel source)
    {
      return new Person(source);
    }

    @Override
    public Person[] newArray(int size)
    {
      return new Person[size];
    }
  };
}
