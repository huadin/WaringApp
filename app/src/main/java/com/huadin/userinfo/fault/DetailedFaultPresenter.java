package com.huadin.userinfo.fault;

import android.text.TextUtils;

import com.huadin.bean.ReportBean;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/20.
 * 详细停电报修信息
 */

public class DetailedFaultPresenter implements DetailedFaultContract.Presenter
{
  private static final String TAG = "DetailedFaultPresenter";
  private DetailedFaultContract.View mDetailedView;

  public DetailedFaultPresenter(DetailedFaultContract.View detailedView)
  {
    mDetailedView = detailedView;
    mDetailedView = checkNotNull(detailedView, "detailedView cannot be null");
    mDetailedView.setPresenter(this);
  }

  @Override
  public void start()
  {
    int errorId = 0;
    boolean isNetwork = mDetailedView.networkState();
    String objectId = mDetailedView.getObjectId();

    if (!isNetwork)
    {
      errorId = R.string.no_network;
    } else if (TextUtils.isEmpty(objectId))
    {
      errorId = R.string.fault_data_error;
    }

    if (errorId != 0)
    {
      mDetailedView.updateError(errorId);
      return;
    }

    ReportBean bean = mDetailedView.getReportBean();

    bean.setHandle(!bean.isHandle());
    bean.setRead(!bean.isRead());

    mDetailedView.showLoading();
    bean.update(objectId, new UpdateListener()
    {
      @Override
      public void done(BmobException e)
      {
        mDetailedView.hindLoading();
        if (e == null)
        {
          mDetailedView.updateSuccess();
        } else
        {
          int code = e.getErrorCode();
          LogUtil.i(TAG, "code = " + code + " / message = " + e.getMessage());
          showCode(code);
        }
      }
    });
  }

  private void showCode(int code)
  {
    switch (code)
    {
      case 9016:
        mDetailedView.updateError(R.string.error_code_9016);
        break;
      case 9010:
        mDetailedView.updateError(R.string.error_code_9010);
        break;
    }
  }

}
