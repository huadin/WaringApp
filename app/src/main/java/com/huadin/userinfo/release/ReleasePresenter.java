package com.huadin.userinfo.release;


import android.content.Context;

import com.huadin.bean.Person;
import com.huadin.bean.PushInstallation;
import com.huadin.bean.ReleaseBean;
import com.huadin.util.AMUtils;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;

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
  private Context mContext;

  public ReleasePresenter(Context context, ReleaseContract.View releaseView)
  {
    mContext = context;
    mContext = checkNotNull(context, "context cannot be null");
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
    boolean isNetwork = mReleaseView.networkState();
    int errorId = 0;
    if (AMUtils.isEmpty(title))
    {
      errorId = R.string.release_msg_title_empty;
    } else if (AMUtils.isEmpty(content))
    {
      errorId = R.string.release_fragment_msg_content;
    } else if (!isNetwork)
    {
      errorId = R.string.no_network;
    }

    if (errorId != 0)
    {
      mReleaseView.updateError(errorId);
      return;
    }

    mReleaseView.showLoading();

    ReleaseBean bean = new ReleaseBean();
    bean.setReleaseTitle(title);
    bean.setReleaseContent(content);
    bean.save(new SaveListener<String>()
    {
      @Override
      public void done(String objectId, BmobException e)
      {
        if (e == null)
        {
          startPushMessage();
        } else
        {
          mReleaseView.hindLoading();
          int code = e.getErrorCode();
          LogUtil.i(TAG, "code = " + code + " / message = " + e.getMessage());
          showCode(code);
        }
      }
    });

  }

  /**
   * 开始推送:
   * <p>接收到消息后根据areaId区分地区<p/>
   */
  private void startPushMessage()
  {
    String areaId = mReleaseView.areaId();

    JSONObject jsonObject = new JSONObject();
    try
    {
      if (areaId.equals("0"))
      {
        Person person = Person.getCurrentUser(Person.class);
        areaId = person.getAreaId();
      }
      jsonObject.put(mContext.getString(R.string.push_type), mContext.getString(R.string.push_release_type))
              .put(mContext.getString(R.string.push_result), mContext.getString(R.string.push_result_key))
              .put(mContext.getString(R.string.push_title), mContext.getString(R.string.push_release_title))
              .put(mContext.getString(R.string.push_content), mContext.getString(R.string.push_release_content))
              .put(mContext.getString(R.string.push_area_id), areaId);
    } catch (JSONException e)
    {
      e.printStackTrace();
    }


    mPushManager.pushMessage(jsonObject, new PushListener()
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
