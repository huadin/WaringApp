package com.huadin.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 潇湘 on 2017/2/13.
 * 建议
 */

public class FeedbackBean extends BmobObject
{
  private String feedbackUser;
  private String feedbackContent;

  public String getFeedbackUser()
  {
    return feedbackUser;
  }

  public void setFeedbackUser(String feedbackUser)
  {
    this.feedbackUser = feedbackUser;
  }

  public String getFeedbackContent()
  {
    return feedbackContent;
  }

  public void setFeedbackContent(String feedbackContent)
  {
    this.feedbackContent = feedbackContent;
  }
}
