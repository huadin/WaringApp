package com.huadin.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.huadin.MyApplication;
import com.huadin.database.StopPowerBean;
import com.huadin.eventbus.EventCenter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
  private String mUrl;
  private String mNewUrl;
  private String mOrgCode;
  private String mEndTime;
  private String mStartTime;
  private String mScope;
  private String mType;
  private static final long mEndLong = 7 * 24 * 60 * 60 * 1000;
  private static final String TAG = "HttpUtil";
  private List<JSONArray> mJSONArrays = new ArrayList<>();

  public HttpUtil addUrl(String url)
  {
    this.mUrl = url;
    this.mNewUrl = url;
    return this;
  }

  /**
   * 设置请求数据时间段  yyyy-MM-dd
   *
   * @param startTime 开始时间
   * @param endTime   结束时间
   */
  public HttpUtil setStartTime(@NonNull String startTime, String endTime)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    if (TextUtils.isEmpty(startTime))
    {
      mStartTime = sdf.format(new Date(System.currentTimeMillis()));
    } else
    {
      this.mStartTime = startTime;
    }

    if (TextUtils.isEmpty(endTime))
    {
      //开始时间延长7天
      try
      {
        Date date = sdf.parse(mStartTime);
        long endLong = date.getTime() + mEndLong;
        mEndTime = sdf.format(new Date(endLong));
      } catch (ParseException e)
      {
        e.printStackTrace();
      }
    } else
    {
      this.mEndTime = endTime;
    }
    return this;
  }

  /**
   * 设置地区编码
   *
   * @param orgCode 默认城区 11401
   */
  public HttpUtil setOrgCode(String orgCode)
  {

    LogUtil.i(TAG, "orgCode = " + orgCode);
    if (TextUtils.isEmpty(orgCode))
    {
      mOrgCode = "11401";
    } else
    {
      this.mOrgCode = orgCode;
    }

    return this;
  }

  /**
   * 设置停电类型
   *
   * @param type 01 02 07
   */
  public HttpUtil setType(String type)
  {
    if (TextUtils.isEmpty(type))
    {
      mType = "";
    } else
    {
      mType = type;
    }
    return this;
  }

  /**
   * 设置停电范围
   *
   * @param scope 范围
   */
  public HttpUtil setScope(String scope)
  {

    if (TextUtils.isEmpty(scope))
    {
      mScope = "";
    } else
    {
      mScope = scope;
    }
    return this;
  }


  private FormBody getFormBody()
  {
    return new FormBody.Builder()
            .add("orgNo", mOrgCode)
            .add("outageStartTime", mStartTime)   //开始日期
            .add("outageEndTime", mEndTime)       //结束日期
            .add("scope", mScope)                     //范围
            .add("provinceNo", "11102")
            .add("typeCode", mType)                  //类型
            .add("lineName", "")                  //线路
            .build();
  }


  /**
   * 请求数据
   */
  public HttpUtil request()
  {
    OkHttpClient mOkHttpClient = new OkHttpClient();

    Request request = new Request.Builder()
            .url(mNewUrl)
            .post(getFormBody())
            .build();

    mOkHttpClient.newCall(request).enqueue(new Callback()
    {
      @Override
      public void onFailure(Call call, IOException e)
      {
        LogUtil.i(TAG, "error message = " + e.getMessage());
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException
      {
        String body = response.body().string();
        parseBody(body);
      }
    });

    return this;
  }

  //解析第一页请求的数据
  private void parseBody(String body)
  {

    try
    {
      JSONObject obj = new JSONObject(body);

      //判断  是否获取其余数据
      JSONObject pObj = obj.getJSONObject("pageModel");
      int totalPage = pObj.getInt("totalPage");
      int totalCount = pObj.getInt("totalCount");

      LogUtil.i(TAG, "totalPage = " + totalPage);
      LogUtil.i(TAG, "totalCount = " + totalCount);

      //获取第一页的数据
      JSONArray firstPageJsonArray = obj.getJSONArray("seleList");//前十个数据
      mJSONArrays.add(firstPageJsonArray);

      //判断是否还有数据
      if (totalCount > 10)
      {
        LogUtil.i(TAG, "mJSONArrays size = " + mJSONArrays.size());

        if (mJSONArrays.size() >= totalPage)
        {
          //全部请求完毕,解析JsonArray数据中数据
          parseToBean(mJSONArrays);
        } else
        {
          //继续发送请求
          mNewUrl = mUrl + "?pageNow=" + (mJSONArrays.size() + 1) + "&pageCount=" + totalCount;
          request();
        }
      } else if (totalCount > 0 && totalCount <= 10)
      {
        //只有一页数据
        parseToBean(mJSONArrays);
      } else
      {
        /* 没有数据包括 更换区域时 和 搜索数据时没有数据
        *  1.更换区域时，没有数据会执行 Map.clear();清空地图
        *  2.搜索时，MapFragment 没有被销毁,所以区分两者
        * */

        if (TextUtils.isEmpty(mType))
        {
          EventBus.getDefault().post(new EventCenter(EventCenter.EVENT_CODE_NOT_HTTP_DATA));
        } else
        {
          EventBus.getDefault().post(new EventCenter(EventCenter.EVENT_CODE_SEARCH_NOT_HTTP_DATA));
        }
      }

    } catch (JSONException e)
    {
      e.printStackTrace();
    }
  }

  //解析为实体
  private void parseToBean(List<JSONArray> jsonArrays)
  {
    List<StopPowerBean> beanList = ParseUtil.pareJson(jsonArrays, mOrgCode);
    LogUtil.i(TAG, "beanList = " + beanList.toString());
    mJSONArrays.clear();

    /*
    * 搜索时 TextUtils.isEmpty(mType) 为 false;
    * 根据此可区分是否是搜索
    * */

    if (TextUtils.isEmpty(mType))
    {
      normalGetHttpData(beanList);
    } else
    {
      searchGetHttpData(beanList);
    }

  }

  /**
   * 搜索获取网络数据
   *
   * @param beanList 数据集
   */
  private void searchGetHttpData(List<StopPowerBean> beanList)
  {
    EventCenter<List<StopPowerBean>> eventCenter = new EventCenter<>(
            EventCenter.EVENT_CODE_SEARCH_HTTP_DATA, beanList);
    EventBus.getDefault().post(eventCenter);
  }

  /**
   * 正常获取网络数据
   *
   * @param beanList 数据集
   */
  private void normalGetHttpData(List<StopPowerBean> beanList)
  {
    //删除旧数据
    DataSupport.deleteAll(StopPowerBean.class);
    //保存新数据
    DataSupport.saveAll(beanList);

    //TODO 开始编码解析,MapFragment 还没有创建出来,无发接受到
    while (!MyApplication.mMapFragmentCreate)
    {
//      LogUtil.i(TAG, "MapFragment not create");
    }

    EventBus.getDefault().post(new EventCenter(EventCenter.EVENT_CODE_GEO_CODE_START));
  }

}
