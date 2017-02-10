package com.huadin.userinfo.person;

import com.huadin.bean.Person;
import com.huadin.interf.OnQueryDataListener;
import com.huadin.util.QueryDataUtil;
import com.huadin.waringapp.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/2/9.
 * 用户管理
 */

public class PersonPresenter implements PersonContract.Presenter, OnQueryDataListener<Person>
{
  private QueryDataUtil<Person> mQueryDataUtil;
  private PersonContract.View mPersonView;

  public PersonPresenter(PersonContract.View personView)
  {
    mPersonView = personView;
    mPersonView = checkNotNull(personView, "personView cannot be null");
    mPersonView.setPresenter(this);
    OnQueryDataListener<Person> mListener = this;
    mQueryDataUtil = new QueryDataUtil<>(mListener);
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
    if (getNetworkStatue()) return;
    mQueryDataUtil.loadMoreData();
    queryData();
  }

  /**
   * 查询数据
   */
  private void queryData()
  {

    BmobQuery<Person> query = getQuery();
    BmobQuery<Person> queryData = mQueryDataUtil.getBmobQuery(query);
    queryData.findObjects(new FindListener<Person>()
    {
      @Override
      public void done(List<Person> list, BmobException e)
      {
        mQueryDataUtil.findPageData(list, e);
      }
    });
  }

  private BmobQuery<Person> getQuery()
  {
    int limit = 15;
    BmobQuery<Person> query = new BmobQuery<>();
    String areaName = mPersonView.getArea();
    query.addWhereEqualTo("areaName", areaName);
    query.order("-createdAt");
    query.setLimit(limit);
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

  @Override
  public void queryDataSuccess(List<Person> list)
  {
    mPersonView.querySuccess(list);
  }

  @Override
  public void queryDataSuccessNotData()
  {
    mPersonView.updateSuccess();
  }

  @Override
  public void queryDataError(int errorResId)
  {
    mPersonView.updateError(errorResId);
  }
}
