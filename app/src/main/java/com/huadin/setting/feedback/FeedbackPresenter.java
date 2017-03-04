package com.huadin.setting.feedback;

import android.text.TextUtils;

import com.huadin.bean.FeedbackBean;
import com.huadin.bean.Person;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/2/13.
 * 建议
 */

public class FeedbackPresenter implements FeedbackContract.Presenter
{
  private static final String TAG = "FeedbackPresenter";
  private FeedbackContract.View mFeedbackView;

  public FeedbackPresenter(FeedbackContract.View feedbackView)
  {
    mFeedbackView = feedbackView;
    mFeedbackView = checkNotNull(feedbackView, "FeedbackContract.View cannot be null");
    mFeedbackView.setPresenter(this);
  }

  @Override
  public void start()
  {
    int errorId = 0;
    String feedbackContent = mFeedbackView.getFeedbackContent();
//    boolean networkState = mFeedbackView.networkState();
    if (TextUtils.isEmpty(feedbackContent))
    {
      errorId = R.string.input_cannot_be_null;
    }
    if (errorId != 0)
    {
      mFeedbackView.updateError(errorId);
      return;
    }

    String feedbackUser = "未登录";
    Person person = BmobUser.getCurrentUser(Person.class);
    if (person != null)
    {
      feedbackUser = person.getUsername();
    }

    FeedbackBean bean = new FeedbackBean();
    bean.setFeedbackUser(feedbackUser);
    bean.setFeedbackContent(feedbackContent);

    mFeedbackView.showLoading();
    bean.save(new SaveListener<String>()
    {
      @Override
      public void done(String s, BmobException e)
      {
        mFeedbackView.hindLoading();
        if (e == null)
        {
          mFeedbackView.updateSuccess();
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
      case 9010:
        mFeedbackView.updateError(R.string.error_code_9010);
        break;
      case 100:
        mFeedbackView.updateError(R.string.error_code_100);
        break;
    }
  }
}
