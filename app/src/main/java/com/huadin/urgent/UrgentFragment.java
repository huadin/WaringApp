package com.huadin.urgent;

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
import com.huadin.adapter.UrgentAdapter_1;
import com.huadin.base.BaseFragment;
import com.huadin.bean.ReleaseBean;
import com.huadin.userinfo.LoadMoreOnScrollListener;
import com.huadin.waringapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 故障信息，查看管理人员发布的紧急信息
 */
public class UrgentFragment extends BaseFragment implements UrgentContract.View, SwipeRefreshLayout.OnRefreshListener, LoadMoreOnScrollListener.onLoadMore, BaseAdapter.onItemClickListener
{

  private UrgentAdapter_1 mAdapter;
  private List<ReleaseBean> mBeanList;
  private UrgentContract.Presenter mPresenter;

  @BindView(R.id.fragment_refresh)
  SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.fragment_recycler_view)
  RecyclerView mRecyclerView;
  @BindView(R.id.fragment_empty)
  TextView mEmpty;

  @BindView(R.id.top_toolbar)
  Toolbar mToolbar;

  public static UrgentFragment newInstance()
  {
    return new UrgentFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    mBeanList = new ArrayList<>();
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    View view = getViewResId(inflater, container, R.layout.urgemt_fragment_layout);
    ButterKnife.bind(this, view);
    initToolbarHome(mToolbar, R.string.fault_info, getActivity());

    initAdapter();
    return view;
  }

  private void initAdapter()
  {
    mAdapter = new UrgentAdapter_1(mContext,mBeanList,R.layout.urgent_item_adapter);
    mAdapter.setOnItemClickListener(this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);

    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mPresenter.start();

    LoadMoreOnScrollListener listener = new LoadMoreOnScrollListener(layoutManager);
    listener.setOnLoadMore(this);
    mRecyclerView.addOnScrollListener(listener);

    mRefreshLayout.setOnRefreshListener(this);
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
  public void querySuccess(List<ReleaseBean> beanList)
  {
    mRefreshLayout.setRefreshing(false);

    if (beanList.size() == 0)
    {
      //下拉加载没有数据
      mAdapter.setLoadMoreStatus(FaultAdapter.STATUS_READY);
      showMessage(R.string.fault_load_more_null);
      return;
    }

    mBeanList.clear();
    mBeanList.addAll(beanList);
    mAdapter.updateAdapter(beanList);

  }

  @Override
  public void error(int errorId)
  {
    showMessage(errorId);
  }

  @Override
  public boolean networkStatus()
  {
    return isNetwork();
  }

  @Override
  public void updateSuccess()
  {
    //下拉刷新时,数据库没有数据时回调
    mRefreshLayout.setRefreshing(false);
    mAdapter.clearAdapterList();
    mEmpty.setVisibility(View.VISIBLE);
  }

  @Override
  public void setPresenter(UrgentContract.Presenter presenter)
  {
    this.mPresenter = presenter;
    mPresenter = checkNotNull(presenter, "presenter cannot be null");
  }

  @Override
  public void onRefresh()
  {
    mPresenter.refresh();
  }

  @Override
  public void loadMore()
  {
    mPresenter.loadMore();
  }

  @Override
  public void onItemClick(int position)
  {
    ReleaseBean releaseBean = mBeanList.get(position);
    DetailedUrgentFragment fragment = DetailedUrgentFragment.newInstance(releaseBean);
    start(fragment, SINGLETASK);
  }
}
