package com.huadin.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Snow on 2017/2/20.
 * 停电信息实体类
 */

public class StopPowerBean extends DataSupport implements Parcelable
{
  /**
   * date : 2016-06-23
   * scope : 怀北镇西庄村
   * time : 06:00:00 - 17:00:00
   * lineName : 范大路
   * typeCode : 计划停电(01)
   * orgCode : 地区编码 11401
   */

  private String date;
  private String time;
  private String scope;
  private String lineName;
  private String typeCode;
  private String orgCode;

  public String getDate()
  {
    return date;
  }

  public void setDate(String date)
  {
    this.date = date;
  }

  public String getTime()
  {
    return time;
  }

  public void setTime(String time)
  {
    this.time = time;
  }

  public String getScope()
  {
    return scope;
  }

  public void setScope(String scope)
  {
    this.scope = scope;
  }

  public String getLineName()
  {
    return lineName;
  }

  public void setLineName(String lineName)
  {
    this.lineName = lineName;
  }

  public String getTypeCode()
  {
    return typeCode;
  }

  public void setTypeCode(String typeCode)
  {
    this.typeCode = typeCode;
  }

  public String getOrgCode()
  {
    return orgCode;
  }

  public void setOrgCode(String orgCode)
  {
    this.orgCode = orgCode;
  }

  public static Creator<StopPowerBean> getCREATOR()
  {
    return CREATOR;
  }

  public StopPowerBean()
  {
  }

  @Override
  public String toString()
  {
    return "StopPowerBean{" +
            "date='" + date + '\'' +
            ", time='" + time + '\'' +
            ", scope='" + scope + '\'' +
            ", lineName='" + lineName + '\'' +
            ", typeCode='" + typeCode + '\'' +
            ", orgCode='" + orgCode + '\'' +
            '}';
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.date);
    dest.writeString(this.time);
    dest.writeString(this.scope);
    dest.writeString(this.lineName);
    dest.writeString(this.typeCode);
    dest.writeString(this.orgCode);
  }

  protected StopPowerBean(Parcel in)
  {
    this.date = in.readString();
    this.time = in.readString();
    this.scope = in.readString();
    this.lineName = in.readString();
    this.typeCode = in.readString();
    this.orgCode = in.readString();
  }

  public static final Creator<StopPowerBean> CREATOR = new Creator<StopPowerBean>()
  {
    @Override
    public StopPowerBean createFromParcel(Parcel source)
    {
      return new StopPowerBean(source);
    }

    @Override
    public StopPowerBean[] newArray(int size)
    {
      return new StopPowerBean[size];
    }
  };
}
