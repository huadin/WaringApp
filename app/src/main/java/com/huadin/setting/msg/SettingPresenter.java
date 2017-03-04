package com.huadin.setting.msg;

import com.huadin.userinfo.UpdateContract;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by NCL on 2017/3/4.
 * 设置 - 检查更新
 */

public class SettingPresenter implements UpdateContract.Presenter
{
  private static final String TAG = "SettingPresenter";
  private UpdateContract.View<SettingPresenter> mView;

  public SettingPresenter(UpdateContract.View<SettingPresenter> view)
  {
    mView = view;
    mView = checkNotNull(view, "UpdateContract.View cannot be null");
    mView.setPresenter(this);
  }

  @Override
  public void start()
  {
    int errorId = 0;
    boolean network = mView.networkState();
    if (!network)
    {
      errorId = R.string.no_network;
    }

    if (errorId != 0)
    {
      mView.updateError(errorId);
      return;
    }

    AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
    JSONObject params = new JSONObject();
    try
    {
      params.put("name", "bmob");
    } catch (JSONException e)
    {
      e.printStackTrace();
    }
    mView.showLoading();
    ace.callEndpoint("getUpdateFile", params, new CloudCodeListener()
    {
      @Override
      public void done(Object object, BmobException e)
      {
        mView.hindLoading();
        LogUtil.i(TAG, "result = " + object.toString());
        mView.updateSuccess();
      }
    });

  }
}
