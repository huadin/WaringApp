package com.huadin.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;

/**
 * 故障信息实体类
 */
public class ReportBean extends BmobObject implements Parcelable
{
  /**
   * 标题
   */
  private String reportTitle;

  /**
   * 内容
   */
  private String reportContent;

  /**
   * 提价联系人
   */
  private String reportUser;
  /**
   * 联系电话
   */
  private String reportPhone;

  /**
   * 故障地址
   */
  private String reportAddress;

  /**
   * 提交时间
   */
  private String reportCreateTime;

  /**
   * 所在地区编码
   */
  private String areaId;

  /**
   * 是否已处理
   */
  private boolean isHandle;

  /**
   * 是否已读
   */
  private boolean isRead;


  public String getReportCreateTime()
  {
    return reportCreateTime;
  }

  public void setReportCreateTime(String reportCreateTime)
  {
    this.reportCreateTime = reportCreateTime;
  }

  public String getReportTitle()
  {
    return reportTitle;
  }

  public void setReportTitle(String reportTitle)
  {
    this.reportTitle = reportTitle;
  }

  public String getReportContent()
  {
    return reportContent;
  }

  public void setReportContent(String reportContent)
  {
    this.reportContent = reportContent;
  }

  public String getReportUser()
  {
    return reportUser;
  }

  public void setReportUser(String reportUser)
  {
    this.reportUser = reportUser;
  }

  public String getReportPhone()
  {
    return reportPhone;
  }

  public void setReportPhone(String reportPhone)
  {
    this.reportPhone = reportPhone;
  }

  public String getReportAddress()
  {
    return reportAddress;
  }

  public void setReportAddress(String reportAddress)
  {
    this.reportAddress = reportAddress;
  }

  public String getAreaId()
  {
    return areaId;
  }

  public void setAreaId(String areaId)
  {
    this.areaId = areaId;
  }

  public boolean isHandle()
  {
    return isHandle;
  }

  public void setHandle(boolean handle)
  {
    isHandle = handle;
  }

  public boolean isRead()
  {
    return isRead;
  }

  public void setRead(boolean read)
  {
    isRead = read;
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.reportTitle);
    dest.writeString(this.reportContent);
    dest.writeString(this.reportUser);
    dest.writeString(this.reportPhone);
    dest.writeString(this.reportAddress);
    dest.writeString(this.reportCreateTime);
    dest.writeString(this.areaId);
    dest.writeByte(this.isHandle ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
  }

  public ReportBean()
  {
  }

  protected ReportBean(Parcel in)
  {
    this.reportTitle = in.readString();
    this.reportContent = in.readString();
    this.reportUser = in.readString();
    this.reportPhone = in.readString();
    this.reportAddress = in.readString();
    this.reportCreateTime = in.readString();
    this.areaId = in.readString();
    this.isHandle = in.readByte() != 0;
    this.isRead = in.readByte() != 0;
  }

  public static final Parcelable.Creator<ReportBean> CREATOR = new Parcelable.Creator<ReportBean>()
  {
    @Override
    public ReportBean createFromParcel(Parcel source)
    {
      return new ReportBean(source);
    }

    @Override
    public ReportBean[] newArray(int size)
    {
      return new ReportBean[size];
    }
  };
}
