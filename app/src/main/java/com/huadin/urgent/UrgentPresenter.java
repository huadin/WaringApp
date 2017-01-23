package com.huadin.urgent;

import com.huadin.bean.Person;
import com.huadin.bean.ReleaseBean;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
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
  private int mLimit = 15;
  private int mCurrentPage = 0;//当前页编号
  private String mLastTime = "";
  private List<ReleaseBean> mBeanList;
  private UrgentContract.View mRepairView;
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
            mRepairView.success(mBeanList);
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

  private void queryData(int page, int type)
  {

  }

  private BmobQuery<ReleaseBean> getQuery()
  {
    BmobQuery<ReleaseBean> query = new BmobQuery<>();
    String areaId = mPerson != null ? mPerson.getAreaId() : "0";
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
