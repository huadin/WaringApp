package com.huadin.service;

import android.app.IntentService;
import android.content.Intent;

import com.huadin.util.HttpUtil;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

/**
 * Created by Snow on 2017/2/17.
 * 后台服务
 */

public class HttpIntentService extends IntentService
{

  private static final String TAG = "HttpIntentService";

  public HttpIntentService()
  {
    super("");
  }

  public HttpIntentService(String name)
  {
    super(name);
  }

  //子线程中
  @Override
  protected void onHandleIntent(Intent intent)
  {
    //获取数据
    HttpUtil.INSTANCE.addUrl(getString(R.string.http_url)).requestHttp("2017-2-17","2017-2-24");
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    LogUtil.i(TAG, "--- onDestroy ---");
  }
}
