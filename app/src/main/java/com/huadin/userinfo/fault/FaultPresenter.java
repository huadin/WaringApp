package com.huadin.userinfo.fault;

import android.support.annotation.NonNull;

import com.huadin.bean.ReportBean;
import com.huadin.database.WaringAddress;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import org.litepal.crud.DataSupport;

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
 * Created by 潇湘 on 2017/1/17.
 * 获取用户提交的报修信息
 */

public class FaultPresenter implements FaultContract.Presenter
{

  private static final String TAG = "FaultPresenter";
  private FaultContract.View mFaultView;
  private static final int STATE_REFRESH = 0;// 下拉刷新
  private static final int STATE_MORE = 1;// 加载更多
  private int mCurrentPage = 0;//当前页编号
  private int mLimit = 3;//每页的数据
  private String lastTime = "";
  private List<ReportBean> mBeanList;

  public FaultPresenter(FaultContract.View faultView)
  {
    mFaultView = faultView;
    mFaultView = checkNotNull(faultView, "faultView cannot be null");
    mFaultView.setPresenter(this);
    mBeanList = new ArrayList<>();
  }

  /**
   * 进入页面初始查询
   */
  @Override
  public void start()
  {
    //如在这检查网络,如果没有网络,会应生命周期没有执行到 onResume,无法 toast
//    if (getNetworkStatue()) return;

    BmobQuery<ReportBean> query = getQuery();
    mFaultView.showLoading();
    query.findObjects(new FindListener<ReportBean>()
    {
      @Override
      public void done(List<ReportBean> list, BmobException e)
      {
        mFaultView.hindLoading();
        if (e == null)
        {
          LogUtil.i(TAG, "list size = " + list.size());

          if (list.size() > 0)
          {
            lastTime = list.get(list.size() - 1).getCreatedAt();
            //防止在加载更多时,adapter中 list 集合丢失第一页数据
            mBeanList.addAll(list);
            mFaultView.querySuccess(list);
          } else
          {
            mFaultView.updateSuccess();
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
    List<BmobQuery<ReportBean>> listBQ = setQueryCondition();

    BmobQuery<ReportBean> query = new BmobQuery<>();
    query.and(listBQ);
    query.order("-createdAt");
    query.setLimit(mLimit);
    return query;
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
    BmobQuery<ReportBean> query = getQuery();
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
          date = sdf.parse(lastTime);

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

    query.findObjects(new FindListener<ReportBean>()
    {
      @Override
      public void done(List<ReportBean> list, BmobException e)
      {
        if (e == null)
        {
          LogUtil.i(TAG, "list size = " + list.size());

          if (list.size() > 0)
          {
            if (type == STATE_REFRESH)
            {
              //下拉刷新
              mCurrentPage = 0;
              lastTime = list.get(list.size() - 1).getCreatedAt();
              mBeanList.clear();
            }

            mBeanList.addAll(list);

            // 这里在每次加载完数据后，将当前页码+1,
            // 这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
            mCurrentPage++;

            //数据回调
            mFaultView.querySuccess(mBeanList);

          } else if (type == STATE_REFRESH)
          {
            //刷新没有数据,即服务器无数据
            mFaultView.updateSuccess();//暂时这么写
          } else if (type == STATE_MORE)
          {
            //加载更多没有数据
            mFaultView.querySuccess(list);
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

  private void showCode(int code)
  {
    switch (code)
    {
      case 9010:
        mFaultView.updateError(R.string.error_code_9010);
        break;
      case 9016:
        mFaultView.updateError(R.string.error_code_9016);
        break;
    }
  }
}
