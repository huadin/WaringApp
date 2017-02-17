package com.huadin.util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Snow on 2017/2/17.
 * 网络
 */

public enum HttpUtil
{

  INSTANCE;
  private OkHttpClient mOkHttpClient;
  private FormBody mFormBody;
  private String mUrl;

  public HttpUtil addUrl(String url)
  {
    this.mUrl = url;
    return this;
  }

  /**
   * 请求数据
   *
   * @param startDate 开始日期
   * @param endDate   结束日期
   */
  public HttpUtil requestHttp(String startDate, String endDate)
  {
    mOkHttpClient = new OkHttpClient();

    mFormBody = new FormBody.Builder().add("orgNo", "11403")                //北京
            .add("outageStartTime", startDate)   //开始日期
            .add("outageEndTime", endDate)       //结束日期
            .add("scope", "")                 //范围
            .add("provinceNo", "11102")           //怀柔
            .add("typeCode", "")           //类型
            .add("lineName", "")           //线路
            .build();

    Request request = new Request.Builder()
            .url(mUrl)
            .post(mFormBody)
            .build();

    mOkHttpClient.newCall(request).enqueue(new Callback()
    {
      @Override
      public void onFailure(Call call, IOException e)
      {

      }

      @Override
      public void onResponse(Call call, Response response) throws IOException
      {
        String body = response.body().string();
        LogUtil.i("HttpUtil = ",body);
      }
    });

    return this;
  }

}
