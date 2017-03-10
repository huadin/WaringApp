package com.huadin.userinfo.fault;

import android.support.annotation.NonNull;

import com.huadin.bean.ReportBean;
import com.huadin.database.WaringAddress;
import com.huadin.interf.OnQueryDataListener;
import com.huadin.util.QueryDataUtil;
import com.huadin.waringapp.R;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/17.
 * 获取用户提交的报修信息
 */

public class FaultPresenter implements FaultContract.Presenter, OnQueryDataListener<ReportBean>
{
  private FaultContract.View mFaultView;
  private QueryDataUtil<ReportBean> mQueryDataUtil;

  public FaultPresenter(FaultContract.View faultView)
  {
    mFaultView = faultView;
    mFaultView = checkNotNull(faultView, "faultView cannot be null");
    mFaultView.setPresenter(this);
    OnQueryDataListener<ReportBean> mListener = this;
    mQueryDataUtil = new QueryDataUtil<>(mListener);
  }

  /**
   * 进入页面初始查询
   */
  @Override
  public void start()
  {
    //如在这检查网络,如果没有网络,生命周期没有执行到 onResume,无法 toast
//    if (getNetworkStatue()) return;

    BmobQuery<ReportBean> query = getQuery();
    mFaultView.showLoading();
    query.findObjects(new FindListener<ReportBean>()
    {
      @Override
      public void done(List<ReportBean> list, BmobException e)
      {
        mFaultView.hindLoading();
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
      mFaultView.loadMoreFailed();
      return;
    }
    mQueryDataUtil.loadMoreData();
    queryData();
  }

  /**
   * 设置 and 查询条件
   *
   * @return BmobQuery<ReportBean> 集合
   */
  @NonNull
  private List<BmobQuery<ReportBean>> setQueryCondition()
  {
    WaringAddress waringAddress = DataSupport.findFirst(WaringAddress.class);
    String areaId = waringAddress.getWaringAreaId();

    BmobQuery<ReportBean> bQ1 = new BmobQuery<>();
    bQ1.addWhereEqualTo("areaId", areaId);

    BmobQuery<ReportBean> bQ2 = new BmobQuery<>();
    bQ2.addWhereEqualTo("isHandle", false);

    List<BmobQuery<ReportBean>> listBQ = new ArrayList<>();
    listBQ.add(bQ1);
    listBQ.add(bQ2);
    return listBQ;
  }

  /**
   * 获取 BmobQuery<T> 实例
   *
   * @return BmobQuery<ReportBean>
   */
  private BmobQuery<ReportBean> getQuery()
  {
    int limit = 15;//每页查询数据的个数
    List<BmobQuery<ReportBean>> listBQ = setQueryCondition();

    BmobQuery<ReportBean> query = new BmobQuery<>();
    query.and(listBQ);
    query.order("-createdAt");
    query.setLimit(limit);
    return query;
  }


  /**
   * 查询数据
   */
  private void queryData()
  {
    BmobQuery<ReportBean> query = getQuery();
    BmobQuery<ReportBean> queryDate = mQueryDataUtil.getBmobQuery(query);
    queryDate.findObjects(new FindListener<ReportBean>()
    {
      @Override
      public void done(List<ReportBean> list, BmobException e)
      {
        mQueryDataUtil.findPageData(list, e);
      }
    });
  }

  /**
   * 检查网络状态
   *
   * @return 有网络 - false;
   */
  private boolean getNetworkStatue()
  {
    if (!mFaultView.networkState())
    {
      mFaultView.updateError(R.string.no_network);
      return true;
    }
    return false;
  }


  @Override
  public void queryDataSuccess(List<ReportBean> list)
  {
    mFaultView.querySuccess(list);
  }

  @Override
  public void queryDataSuccessNotData()
  {
    mFaultView.updateSuccess();
  }

  @Override
  public void queryDataError(int errorResId)
  {
    mFaultView.updateError(errorResId);
  }
}
