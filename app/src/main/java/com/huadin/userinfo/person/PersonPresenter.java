package com.huadin.userinfo.person;

import com.huadin.bean.Person;
import com.huadin.bean.ReportBean;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.amap.api.col.t.a.m;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/2/9.
 * 用户管理
 */

public class PersonPresenter implements PersonContract.Presenter
{
  private static final String TAG = "PersonPresenter";
  private static final int STATE_REFRESH = 0;// 下拉刷新
  private static final int STATE_MORE = 1;// 加载更多
  private PersonContract.View mPersonView;
  private List<Person> mPersonList;
  private int mLimit = 15;
  private int mCurrentPage = 0;
  private String mLastTime;

  public PersonPresenter(PersonContract.View personView)
  {
    mPersonView = personView;
    mPersonView = checkNotNull(personView, "personView cannot be null");
    mPersonView.setPresenter(this);
    mPersonList = new ArrayList<>();
  }

  @Override
  public void start()
  {
    mPersonView.showLoading();
    BmobQuery<Person> query = getQuery();
    query.findObjects(new FindListener<Person>()
    {
      @Override
      public void done(List<Person> list, BmobException e)
      {
        mPersonView.hindLoading();
        if (e == null)
        {
          if (list.size() > 0)
          {
            mLastTime = list.get(list.size() - 1).getCreatedAt();
            mPersonList.clear();
            mPersonList.addAll(list);
            mPersonView.querySuccess(mPersonList);
          } else
          {
            mPersonView.updateSuccess();
          }
        } else
        {
          int code = e.getErrorCode();
          LogUtil.i(TAG, "code = " + code + " / message = " + e.getMessage());
          showCode(code);
        }
      }
    });
  }

  @Override
  public void refresh()
  {
    if (getNetworkStatue()) return;
    queryData(mCurrentPage, STATE_REFRESH);

  }

  @Override
  public void loadMore()
  {
    if (getNetworkStatue()) return;
    queryData(mCurrentPage, STATE_MORE);
  }

  /**
   * 查询数据
   *
   * @param page 页数
   * @param type recyclerView 操作类型
   */
  private void queryData(int page, final int type)
  {
    LogUtil.i(TAG, "page = " + page + " / type = " + type);
    BmobQuery<Person> query = getQuery();
    switch (type)
    {
      case STATE_REFRESH:
        page = 0;
        query.setSkip(page);
        break;
      case STATE_MORE:
        Date date;
        try
        {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          date = sdf.parse(mLastTime);

          // 只查询小于等于最后一个item发表时间的数据
          query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
          // 跳过之前页数并去掉重复数据
          query.setSkip(page * mLimit);
        } catch (Exception e)
        {
          e.printStackTrace();
        }
        break;
    }

    query.findObjects(new FindListener<Person>()
    {
      @Override
      public void done(List<Person> list, BmobException e)
      {
        if (e == null)
        {
          LogUtil.i(TAG, "list size = " + list.size());

          if (list.size() > 0)
          {
            if (type == STATE_REFRESH)
            {
              //下拉刷新
              mCurrentPage = -1;
              mLastTime = list.get(list.size() - 1).getCreatedAt();
              mPersonList.clear();
            }

            mPersonList.addAll(list);

            // 这里在每次加载完数据后，将当前页码+1,
            // 这样在上拉刷新的方法中就不需要操作curPage了
            mCurrentPage++;

            //数据回调
            mPersonView.querySuccess(mPersonList);

          } else if (type == STATE_REFRESH)
          {
            //刷新没有数据,即服务器无数据
            mPersonView.updateSuccess();//暂时这么写
          } else if (type == STATE_MORE)
          {
            //加载更多没有数据
            mPersonView.querySuccess(list);
          }

        } else
        {
          int code = e.getErrorCode();
          LogUtil.i(TAG, "code = " + code + " / message = " + e.getMessage());
          showCode(code);
        }
      }
    });
  }

  private BmobQuery<Person> getQuery()
  {
    BmobQuery<Person> query = new BmobQuery<>();
    String areaName = mPersonView.getArea();
    query.addWhereEqualTo("areaName", areaName);
    query.order("-createdAt");
    query.setLimit(mLimit);
    return query;
  }

  private boolean getNetworkStatue()
  {
    if (!mPersonView.networkState())
    {
      mPersonView.updateError(R.string.no_network);
      return true;
    }
    return false;
  }

  private void showCode(int code)
  {
    mPersonView.hindLoading();
    switch (code)
    {
      case 9010:
        mPersonView.updateError(R.string.error_code_9010);
        break;
      case 9016:
        mPersonView.updateError(R.string.error_code_9016);
        break;
    }
  }

}
