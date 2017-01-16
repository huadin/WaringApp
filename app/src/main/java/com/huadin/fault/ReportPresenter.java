package com.huadin.fault;


import android.content.Context;

import com.huadin.bean.PushInstallation;
import com.huadin.bean.ReportBean;
import com.huadin.database.WaringAddress;
import com.huadin.util.AMUtils;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReportPresenter implements ReportContract.Presenter
{

  private static final String TAG = "ReportPresenter";
  private ReportContract.View mReportView;
  private Context mContext;

  public ReportPresenter(Context context, ReportContract.View reportView)
  {
    this.mContext = context;
    this.mReportView = reportView;
    mContext = checkNotNull(context, "context cannot be null");
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
    final WaringAddress address = DataSupport.findFirst(WaringAddress.class);
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
    }else if (address == null)
    {
      errorId = R.string.warning_address_not_null;
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
        if (e == null)
        {
          pushMessage(address);
        } else
        {
          mReportView.hindLoading();
          int code = e.getErrorCode();
          LogUtil.i(TAG, "done: error = " + e.getMessage() + "/ code = " + e.getErrorCode());
          showCode(code);
        }
      }
    });

  }

  /*推送*/
  private void pushMessage(WaringAddress address)
  {
    BmobPushManager<PushInstallation> pushManager = new BmobPushManager<>();
    JSONObject jsonObject = new JSONObject();
    try
    {

      jsonObject.put(mContext.getString(R.string.push_result), mContext.getString(R.string.push_result_key))
              .put(mContext.getString(R.string.push_type), mContext.getString(R.string.push_report_type))
              .put(mContext.getString(R.string.push_title), mContext.getString(R.string.push_report_title))
              .put(mContext.getString(R.string.push_content), mContext.getString(R.string.push_report_content))
              .put(mContext.getString(R.string.push_area_id),address.getWaringArea());

    } catch (JSONException e)
    {
      e.printStackTrace();
    }

    pushManager.pushMessage(jsonObject, new PushListener()
    {
      @Override
      public void done(BmobException e)
      {
        mReportView.hindLoading();
        mReportView.submitSuccess();
        if (e != null)
          LogUtil.i(TAG, "code = " + e.getErrorCode() + " / message = " + e.getMessage());
      }
    });
  }

  private void showCode(int code)
  {
    switch (code)
    {
      case 9010:
        mReportView.submitError(R.string.error_code_9010);
        break;
    }
  }

}
