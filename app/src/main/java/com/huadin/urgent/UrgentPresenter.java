package com.huadin.urgent;

import com.huadin.bean.Person;
import com.huadin.bean.ReleaseBean;
import com.huadin.interf.OnQueryDataListener;
import com.huadin.util.QueryDataUtil;
import com.huadin.waringapp.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 请求数据
 */

public class UrgentPresenter implements UrgentContract.Presenter, OnQueryDataListener<ReleaseBean>
{

  private QueryDataUtil<ReleaseBean> mQueryDataUtil;
  private UrgentContract.View mRepairView;
  private Person mPerson;

  public UrgentPresenter(UrgentContract.View repairView)
  {
    this.mRepairView = repairView;
    mRepairView = checkNotNull(repairView, "repairView cannot be null");
    mRepairView.setPresenter(this);
    mPerson = Person.getCurrentUser(Person.class);
    OnQueryDataListener<ReleaseBean> listener = this;
    mQueryDataUtil = new QueryDataUtil<>(listener);
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
        mQueryDataUtil.firstQuery(list, e);
      }
    });
  }

  @Override
  public void refresh()
  {
    if (getNetworkStatue()) return;
    mQueryDataUtil.refreshData();
    queryData();
  }

  @Override
  public void loadMore()
  {
    if (getNetworkStatue())
    {
      mRepairView.loadMoreFailed();
      return;
    }
    mQueryDataUtil.loadMoreData();
    queryData();
  }

  private void queryData()
  {
    BmobQuery<ReleaseBean> query = getQuery();
    BmobQuery<ReleaseBean> queryData = mQueryDataUtil.getBmobQuery(query);
    queryData.findObjects(new FindListener<ReleaseBean>()
    {
      @Override
      public void done(List<ReleaseBean> list, BmobException e)
      {
        mQueryDataUtil.findPageData(list, e);
      }
    });
  }

  private BmobQuery<ReleaseBean> getQuery()
  {
    int limit = 15;
    BmobQuery<ReleaseBean> query = new BmobQuery<>();
    String areaId = mPerson != null ? mPerson.getAreaId() : "11405";
    query.addWhereEqualTo("releaseAreaId", areaId);
    query.order("-createdAt");
    query.setLimit(limit);
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


  @Override
  public void queryDataSuccess(List<ReleaseBean> list)
  {
    mRepairView.querySuccess(list);
  }

  @Override
  public void queryDataSuccessNotData()
  {
    mRepairView.updateSuccess();
  }

  @Override
  public void queryDataError(int errorResId)
  {
    mRepairView.error(errorResId);
    mRepairView.loadMoreFailed();
  }
}
