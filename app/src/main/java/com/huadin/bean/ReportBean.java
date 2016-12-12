package com.huadin.bean;

import cn.bmob.v3.BmobObject;

/**
 * 故障信息实体类
 */
public class ReportBean extends BmobObject
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
}
