package com.huadin.userinfo.release;


import com.huadin.bean.PushInstallation;
import com.huadin.util.AMUtils;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/10.
 * 信息发布
 */

public class ReleasePresenter implements ReleaseContract.Presenter
{
  private static final String TAG = "ReleasePresenter";
  private ReleaseContract.View mReleaseView;
  private BmobPushManager<PushInstallation> mPushManager;

  public ReleasePresenter(ReleaseContract.View releaseView)
  {
    mReleaseView = releaseView;
    mReleaseView = checkNotNull(releaseView, "releaseView cannot be null");
    mReleaseView.setPresenter(this);
    mPushManager = new BmobPushManager<>();
  }

  @Override
  public void start()
  {
    String title = mReleaseView.releaseTitle();
    final String content = mReleaseView.releaseContent();

    int errorId = 0;
    if (AMUtils.isEmpty(title))
    {
      errorId = R.string.release_msg_title_empty;
    } else if (AMUtils.isEmpty(content))
    {
      errorId = R.string.release_fragment_msg_content;
    }

    if (errorId != 0)
    {
      mReleaseView.updateError(errorId);
      return;
    }

    mReleaseView.showLoading();

    mPushManager.pushMessage(title + content, new PushListener()
    {
      @Override
      public void done(BmobException e)
      {
        mReleaseView.hindLoading();
        if (e == null)
        {
          mReleaseView.updateSuccess();
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
      case 201:
        break;
    }
  }
}
