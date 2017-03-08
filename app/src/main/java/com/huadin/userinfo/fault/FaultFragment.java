package com.huadin.userinfo.fault;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huadin.adapter.BaseAdapter;
import com.huadin.adapter.FaultAdapter;
import com.huadin.adapter.FaultAdapter_1;
import com.huadin.base.BaseFragment;
import com.huadin.bean.ReportBean;
import com.huadin.eventbus.EventCenter;
import com.huadin.userinfo.LoadMoreOnScrollListener;
import com.huadin.util.LinearDecoration;
import com.huadin.util.LogUtil;
import com.huadin.waringapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 潇湘 on 2017/1/17.
 * 查看用户提交的报修信息
 */

public class FaultFragment extends BaseFragment implements FaultContract.View,
        SwipeRefreshLayout.OnRefreshListener, LoadMoreOnScrollListener.onLoadMore, BaseAdapter.onItemClickListener
{
  @BindView(R.id.fragment_refresh)
  SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.fragment_recycler_view)
  RecyclerView mRecyclerView;
  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;
  @BindView(R.id.fragment_empty)
  TextView mEmpty;

  private FaultContract.Presenter mPresenter;
  private FaultAdapter_1 mFaultAdapter;
  private List<ReportBean> mBeanList;

  public static FaultFragment newInstance()
  {
    return new FaultFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    _mActivity.setFragmentAnimator(new DefaultHorizontalAnimator());
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.fault_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbar(mToolbar, R.string.fault_info, true);
    initAdapter();
    return view;
  }

  private void initAdapter()
  {

    mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.refresh_1),
            getResources().getColor(R.color.refresh_2),
            getResources().getColor(R.color.refresh_3),
            getResources().getColor(R.color.refresh_4));
    mRefreshLayout.setOnRefreshListener(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.addItemDecoration(new LinearDecoration(mContext, LinearDecoration.VERTICAL_LIST));

    LoadMoreOnScrollListener listener = new LoadMoreOnScrollListener(layoutManager);
    listener.setOnLoadMore(this);

    mRecyclerView.addOnScrollListener(listener);
    mBeanList = new ArrayList<>();

    mFaultAdapter = new FaultAdapter_1(mContext, mBeanList, R.layout.fault_adapter_item);

    mFaultAdapter.setOnItemClickListener(this);
    mRecyclerView.setAdapter(mFaultAdapter);
    mPresenter.start();//开始获取数据
  }

  @Override
  public void showLoading()
  {
    showLoading(R.string.fault_date_get_in);
  }

  @Override
  public void hindLoading()
  {
    dismissLoading();
  }

  @Override
  public void updateSuccess()
  {
    //下拉刷新时,数据库没有数据时回调
    mRefreshLayout.setRefreshing(false);
    mFaultAdapter.clearAdapterList();
    mEmpty.setVisibility(View.VISIBLE);
  }

  @Override
  public void updateError(int errorId)
  {
    //无网络时执行
    mRefreshLayout.setRefreshing(false);
    // TODO: 2017/1/18 加载更多时,无网络时关闭加载更多的View

    //获取失败
    showMessage(errorId);
  }

  @Override
  public boolean networkState()
  {
    return isNetwork();
  }

  @Override
  public void setPresenter(FaultContract.Presenter presenter)
  {
    mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }

  @Override
  public void querySuccess(List<ReportBean> beanList)
  {
    mRefreshLayout.setRefreshing(false);
    mEmpty.setVisibility(View.GONE);
    //获取数据成功,关联到 Adapter
    if (beanList.size() == 0)
    {
      //下拉加载没有数据
      mFaultAdapter.setLoadMoreStatus(FaultAdapter.STATUS_READY);
      showMessage(R.string.fault_load_more_null);
      return;
    }
    mBeanList.clear();
    mBeanList.addAll(beanList);
    mFaultAdapter.updateAdapter(beanList);
  }

  @Override
  public void onRefresh()
  {
    //下拉刷新
    mPresenter.refresh();
  }

  /*点击事件*/
  @Override
  public void onItemClick(int position)
  {
    LogUtil.i(LOG_TAG, "position = " + position);
    ReportBean bean = mBeanList.get(position);

    DetailedFaultFragment fragment = DetailedFaultFragment.newInstance(bean, position);
    start(fragment, SINGLETASK);
    new DetailedFaultPresenter(fragment);

  }

  @Override
  protected void fragmentOnEvent(EventCenter eventCenter)
  {
    switch (eventCenter.getEventCode())
    {
      case EventCenter.EVENT_CODE_UPDATE_SUCCESS:
        int position = (int) eventCenter.getData();
        mFaultAdapter.notifyItemRemoved(position);
        break;
    }
  }

  @Override
  public void loadMore()
  {
    mPresenter.loadMore();
  }
}
