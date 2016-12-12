package com.huadin.fault;


import com.huadin.bean.ReportBean;
import com.huadin.util.AMUtils;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReportPresenter implements ReportContract.Presenter
{

  private static final String TAG = "ReportPresenter";
  private ReportContract.View mReportView;

  public ReportPresenter(ReportContract.View reportView)
  {
    this.mReportView = reportView;
    mReportView = checkNotNull(reportView, "reportView cannot be null");
    mReportView.setPresenter(this);
  }

  @Override
  public void start()
  {
    boolean isNetwork = mReportView.networkIsAvailable();
    String reportTitle = mReportView.getReportTitle();
    String reportContent = mReportView.getReportContent();
    String reportUser = mReportView.getReportUser();
    String reportPhone = mReportView.getReportPhone();
    String reportAddress = mReportView.getReportAddress();

    int errorId = 0;

    if (!isNetwork)
    {
      errorId = R.string.no_network;
    } else if (AMUtils.isEmpty(reportTitle))
    {
      errorId = R.string.report_title_cannot_be_null;
    } else if (AMUtils.isEmpty(reportContent))
    {
      errorId = R.string.report_content_cannot_be_null;
    } else if (reportUser.isEmpty())
    {
      errorId = R.string.report_user_cannot_be_null;
    } else if (AMUtils.isEmpty(reportPhone))
    {
      errorId = R.string.report_format_error;
    } else if (AMUtils.isEmpty(reportAddress))
    {
      errorId = R.string.report_address_cannot_be_null;
    }

    if (errorId != 0)
    {
      mReportView.submitError(errorId);
      return;
    }

    ReportBean bean = new ReportBean();

    bean.setReportTitle(reportTitle);
    bean.setReportContent(reportContent);
    bean.setReportUser(reportUser);
    bean.setReportPhone(reportPhone);
    bean.setReportAddress(reportAddress);

    mReportView.showLoading();
    bean.save(new SaveListener<String>()
    {
      @Override
      public void done(String s, BmobException e)
      {
        mReportView.hindLoading();
        if (e == null)
        {
          mReportView.submitSuccess();
        } else
        {
//          int code = e.getErrorCode();
          LogUtil.i(TAG, "done: error = " + e.getMessage() + "/ code = " + e.getErrorCode());
        }
      }
    });

  }


}
