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

  //子线程中
  @Override
  protected void onHandleIntent(Intent intent)
  {

    String startTime = intent.getStringExtra(getString(R.string.key_start_time));
    String endTime = intent.getStringExtra(getString(R.string.key_end_time));
    String orgCode = intent.getStringExtra(getString(R.string.key_org_code));

    //获取数据
    HttpUtil.INSTANCE
            .addUrl(getString(R.string.http_url))
            .setStartTime("2017-02-20", endTime)
            .setOrgCode(orgCode)
            .request();
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    LogUtil.i(TAG, "--- Intent Service onDestroy ---");
  }
}
