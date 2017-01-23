package com.huadin.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 潇湘 on 2017/1/23.
 * 发布消息的实体
 */

public class ReleaseBean extends BmobObject implements Parcelable
{
  private String releaseTitle;
  private String releaseContent;
  private String releaseAreaId;

  public String getReleaseAreaId()
  {
    return releaseAreaId;
  }

  public void setReleaseAreaId(String releaseAreaId)
  {
    this.releaseAreaId = releaseAreaId;
  }

  public String getReleaseTitle()
  {
    return releaseTitle;
  }

  public void setReleaseTitle(String releaseTitle)
  {
    this.releaseTitle = releaseTitle;
  }

  public String getReleaseContent()
  {
    return releaseContent;
  }

  public void setReleaseContent(String releaseContent)
  {
    this.releaseContent = releaseContent;
  }

  public ReleaseBean()
  {
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.releaseTitle);
    dest.writeString(this.releaseContent);
    dest.writeString(this.releaseAreaId);
  }

  protected ReleaseBean(Parcel in)
  {
    this.releaseTitle = in.readString();
    this.releaseContent = in.readString();
    this.releaseAreaId = in.readString();
  }

  public static final Creator<ReleaseBean> CREATOR = new Creator<ReleaseBean>()
  {
    @Override
    public ReleaseBean createFromParcel(Parcel source)
    {
      return new ReleaseBean(source);
    }

    @Override
    public ReleaseBean[] newArray(int size)
    {
      return new ReleaseBean[size];
    }
  };
}
