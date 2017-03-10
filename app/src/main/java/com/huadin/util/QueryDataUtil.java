package com.huadin.util;

import com.huadin.interf.OnQueryDataListener;
import com.huadin.waringapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by 潇湘 on 2017/2/9.
 * 查询数据封装
 */

public class QueryDataUtil<T extends BmobObject>
{
  private static final String TAG = "QueryDataUtil";
  private static final int STATE_REFRESH = 0;// 下拉刷新
  private static final int STATE_MORE = 1;// 加载更多
  private int mCurrentPage = 0;//当前页编号
  private int mType = 0;//查询类型(下拉，加载)
  private List<T> mBeanList;//数据集合
  private String mLastTime = "";//查询这个时间之前的数据
  private OnQueryDataListener<T> mListener;

  public QueryDataUtil(OnQueryDataListener<T> listener)
  {
    mBeanList = new ArrayList<>();
    this.mListener = listener;
    mListener = checkNotNull(listener, "OnQueryDataListener cannot be null");
  }

  public void firstQuery(List<T> list, BmobException e)
  {
    if (e == null)
    {
      if (list.size() > 0)
      {
        mLastTime = list.get(list.size() - 1).getCreatedAt();
        mBeanList.clear();
        mBeanList.addAll(list);
        mListener.queryDataSuccess(list);
      } else
      {
        mListener.queryDataSuccessNotData();
      }
    } else
    {
      int code = e.getErrorCode();
      LogUtil.i(TAG, "code = " + code + " / message = " + e.getMessage());
      showCode(code);
    }
  }

  /**
   * 刷新
   */
  public void refreshData()
  {
    mType = STATE_REFRESH;
  }

  /**
   * 加载
   */
  public void loadMoreData()
  {
    mType = STATE_MORE;
  }

  /**
   * 分页查询数据
   *
   * @param list 集合
   * @param e    异常
   */
  public void findPageData(List<T> list, BmobException e)
  {
    if (e == null)
    {
      LogUtil.i(TAG, "list size = " + list.size());
      if (list.size() > 0)
      {
        if (mType == STATE_REFRESH)
        {
          //下拉刷新
          mCurrentPage = -1;
          mLastTime = list.get(list.size() - 1).getCreatedAt();
          mBeanList.clear();
        }
        mBeanList.addAll(list);
        // 这里在每次加载完数据后，将当前页码+1,
        // 这样在上拉刷新的方法中就不需要操作curPage了
        mCurrentPage++;
        //数据回调
        mListener.queryDataSuccess(mBeanList);
      } else if (mType == STATE_REFRESH)
      {
        //刷新没有数据,即服务器无数据
        mListener.queryDataSuccessNotData();
      } else if (mType == STATE_MORE)
      {
        //加载更多没有数据
        mListener.queryDataSuccess(mBeanList);
      }
    } else
    {
      int code = e.getErrorCode();
      LogUtil.i(TAG, "code = " + code + " / message = " + e.getMessage());
      showCode(code);
    }
  }

  public BmobQuery<T> getBmobQuery(BmobQuery<T> query)
  {
    int limit = 15;//每页查询的数据个数
    switch (mType)
    {
      case STATE_REFRESH:
        mCurrentPage = 0;
        query.setSkip(mCurrentPage);
        break;
      case STATE_MORE:
        Date date;
        try
        {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
          date = sdf.parse(mLastTime);

          // 只查询小于等于最后一个item发表时间的数据
          query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
          // 跳过之前页数并去掉重复数据
          query.setSkip(mCurrentPage * limit);
        } catch (Exception e)
        {
          e.printStackTrace();
        }
        break;
    }
    return query;
  }


  private void showCode(int code)
  {
    int errorResId;
    switch (code)
    {
      case 9010:
        errorResId = R.string.error_code_9010;
        break;
      case 9016:
        errorResId = R.string.error_code_9016;
        break;
      default:
        errorResId = -1;
        break;
    }
    mListener.queryDataError(errorResId);
  }

}
