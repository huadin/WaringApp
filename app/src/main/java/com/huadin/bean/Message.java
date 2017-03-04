package com.huadin.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NCL on 2017/1/14.
 * 消息实体类
 */

public class Message implements Parcelable
{

  private String result;
  private String type;
  private String title;
  private String content;
  private String area;


  public String getResult()
  {
    return result;
  }

  public void setResult(String result)
  {
    this.result = result;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent(String content)
  {
    this.content = content;
  }

  public String getArea()
  {
    return area;
  }

  public void setArea(String area)
  {
    this.area = area;
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.result);
    dest.writeString(this.type);
    dest.writeString(this.title);
    dest.writeString(this.content);
    dest.writeString(this.area);
  }

  public Message()
  {
  }

  protected Message(Parcel in)
  {
    this.result = in.readString();
    this.type = in.readString();
    this.title = in.readString();
    this.content = in.readString();
    this.area = in.readString();
  }

  public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>()
  {
    @Override
    public Message createFromParcel(Parcel source)
    {
      return new Message(source);
    }

    @Override
    public Message[] newArray(int size)
    {
      return new Message[size];
    }
  };
}
