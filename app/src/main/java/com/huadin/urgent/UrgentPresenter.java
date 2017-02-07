package com.huadin.urgent;

import com.huadin.bean.Person;
import com.huadin.bean.ReleaseBean;
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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 请求数据
 */

public class UrgentPresenter implements UrgentContract.Presenter
{
  private static final String TAG = "UrgentPresenter";
  private static final int STATE_REFRESH = 0;// 下拉刷新
  private static final int STATE_MORE = 1;// 加载更多
  private UrgentContract.View mRepairView;
  private List<ReleaseBean> mBeanList;
  private String mLastTime = "";
  private int mCurrentPage = 0;//当前页编号
  private int mLimit = 15;
  private Person mPerson;

  public UrgentPresenter(UrgentContract.View repairView)
  {
    this.mRepairView = repairView;
    mRepairView = checkNotNull(repairView, "repairView cannot be null");
    mRepairView.setPresenter(this);
    mPerson = Person.getCurrentUser(Person.class);
    mBeanList = new ArrayList<>();
  }

  @Override
  public void start()
  {
    mRepairView.showLoading();
    BmobQuery<ReleaseBean> query = getQuery();
    query.findObjects(new FindListener<ReleaseBean>()
    {
      @Override
      public void done(List<ReleaseBean> list, BmobException e)
      {
        mRepairView.hindLoading();
        if (e == null)
        {
          if (list.size() > 0)
          {
            mLastTime = list.get(list.size() - 1).getCreatedAt();
            mBeanList.addAll(list);
            mRepairView.querySuccess(mBeanList);
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
    queryData(0, STATE_REFRESH);
  }

  @Override
  public void loadMore()
  {
    if (getNetworkStatue()) return;
    queryData(mCurrentPage, STATE_MORE);
  }

  private void queryData(int page, final int type)
  {
    LogUtil.i(TAG, "UrgentPresenter page = " + page + " / type = " + type);

    BmobQuery<ReleaseBean> query = getQuery();
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

    query.findObjects(new FindListener<ReleaseBean>()
    {
      @Override
      public void done(List<ReleaseBean> list, BmobException e)
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
              mBeanList.clear();
            }

            mBeanList.addAll(list);

            // 这里在每次加载完数据后，将当前页码+1,
            // 这样在上拉刷新的方法中就不需要操作curPage了
            mCurrentPage++;

            //数据回调
            mRepairView.querySuccess(mBeanList);

          } else if (type == STATE_REFRESH)
          {
            //刷新没有数据,即服务器无数据
            mRepairView.updateSuccess();//暂时这么写
          } else if (type == STATE_MORE)
          {
            //加载更多没有数据
            mRepairView.querySuccess(list);
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

  private BmobQuery<ReleaseBean> getQuery()
  {
    BmobQuery<ReleaseBean> query = new BmobQuery<>();
    String areaId = mPerson != null ? mPerson.getAreaId() : "11405";
    query.addWhereEqualTo("releaseAreaId", areaId);
    query.order("-createdAt");
    query.setLimit(mLimit);
    return query;
  }

  private boolean getNetworkStatue()
  {
    if (!mRepairView.networkStatus())
    {
      mRepairView.error(R.string.no_network);
      return true;
    }
    return false;
  }

  private void showCode(int code)
  {
    mRepairView.hindLoading();
    switch (code)
    {
      case 9010:
        mRepairView.error(R.string.error_code_9010);
        break;
      case 9016:
        mRepairView.error(R.string.error_code_9016);
        break;
    }
  }

}
